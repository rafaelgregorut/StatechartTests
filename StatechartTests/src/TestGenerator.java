import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xml.statechart.Regions;
import xml.statechart.Statechart;
import xml.statechart.Vertices;


public class TestGenerator {

	Statechart sc;
	
	Hashtable<Vertices,String> setC;
	
	Set<String> setCReal;
	
	Hashtable<Vertices,String> exitTransitions;
	
	Hashtable<Vertices,Set<String>> hashFilhos;
	
	Set<String> testPaths;
	
	/*PRIMEIRO VOU PENSAR EM UM STATECHART SIMPLES DE TUDO*/
	
	public TestGenerator(Statechart sc) {
		setC = new Hashtable<Vertices,String>();
		exitTransitions = new Hashtable<Vertices,String>();
		hashFilhos = new Hashtable<Vertices,Set<String>>();
		testPaths = new TreeSet<String>();
		this.sc = sc;
	}
	
	public Set<String> expandPath(String original, Set<String> pathsFilhosSet, Vertices vPai) {
		Set<String> expandedSet = new TreeSet<String>();
		
		for (String filhoPath : pathsFilhosSet) {
			String expandedPath = original.replace("@"+vPai.getName()+"@", filhoPath);
			expandedSet.add(expandedPath);
		}
		
		return expandedSet;
	}
	
	public boolean ehPai(Vertices v) {
		return (hashFilhos.get(v) != null);
	}
	
	/*Elimina recursivamente os caminhos de cobertura dos estados pai*/
	public void expandPaiPath(Hashtable<String,Vertices> mapSetC, Hashtable<Vertices,Set<String>> reverseMapSetC) {
		for (Vertices v : sc.getListRegions().get(0).getListVertices()) {
			if (ehPai(v)) {
				String[] a = new String[1];
				String paiPath = reverseMapSetC.get(v).toArray(a)[0];
				expandPaiPathRec(mapSetC,v,paiPath);
			}
		}
	}
	
	/*Usado para eliminar o caminho de cobertura de um estado pai (Recursivo)*/
	public void expandPaiPathRec(Hashtable<String,Vertices> mapSetC, Vertices vPai, String caminhoPai) {
		/*Pegos os filhos do pai*/
		Set<String> filhosPaths= hashFilhos.get(vPai);
	
		/*Para cada caminho filho*/
		for (String caminhoFilho : filhosPaths) {
			
			/*Pego o filho de destino*/
			Vertices filhoDest = mapSetC.get(caminhoFilho);
			if (filhoDest != null) {
				//System.out.println("Caminho: "+caminhoFilho);
				String prefixPai = caminhoPai.replace("@"+vPai.getName()+"@", caminhoFilho);
				mapSetC.put(prefixPai, filhoDest);
				if (ehPai(filhoDest))
					expandPaiPathRec(mapSetC,filhoDest,prefixPai);
				
				mapSetC.remove(caminhoFilho);
			}
		}
		mapSetC.remove(caminhoPai);
	}
	
	
	public Set<String> createTestPaths() {
		Regions mainRegion = sc.getListRegions().get(0);
		//Hashtable<String, Vertices> caminhosVerts = new Hashtable<String,Vertices>();
		
		/*Gero o setC, ainda nao expandido*/
		Hashtable<String,Vertices> mapPathCVertice = mainRegion.constructSetC(sc.statesId,hashFilhos);
		
		System.out.println("Set C:");
		for (String str : mapPathCVertice.keySet()) {
			System.out.println(str+" cobre o "+mapPathCVertice.get(str).getName());
		}
		
		Hashtable<Vertices,Set<String>> mapVertPaths = reverseHash(mapPathCVertice);
		System.out.println("Semi-Expanded set C:");
		/*Preciso expandir o Set C*/
		expandPaiPath(mapPathCVertice,mapVertPaths);
		for (String str : mapPathCVertice.keySet()) {
			System.out.println(str+" cobre o "+mapPathCVertice.get(str).getName()+" ("+mapPathCVertice.get(str).getType()+")");
		}
		
		System.out.println("Test cases:");
		
		return null;
		//return generateTestPaths(mapVertPaths);
	}
	
	public Hashtable<Vertices, Set<String>> reverseHash(Hashtable<String,Vertices> hash) {
		Hashtable<Vertices, Set<String>> newH = new Hashtable<Vertices,Set<String>>();
		
		for (Map.Entry<String, Vertices> entry : hash.entrySet()) {
			String str = entry.getKey();
			Vertices v = entry.getValue();
			if (newH.containsKey(v)) {
				Set<String> temp = newH.remove(v);
				temp.add(str);
				newH.put(v,temp);
			} else {
				Set<String> newS = new TreeSet<String>();
				newS.add(str);
				newH.put(v, newS);
			}
		}
		return newH;
	}
	
	public Set<String> generateTestPaths(Hashtable<Vertices,Set<String>> hashVertsCaminhos) {
		
		
		
		
		
		
		
		
		/*for (Vertices state : sc.statesId.values()) {
			System.out.println(state.getType()+": "+state.getName()+" "+state.getId());
			for (OutgoingTransitions out : state.getListTransitions()) {
				System.out.println("Testando transicao "+out.getSpecification());
				Set<String> filhosPaths;
				String testPath = "";
				if ((filhosPaths = hashFilhos.get(state)) != null) {
					for (String filhoPath : filhosPaths) {
						testPath = filhoPath+" "+out.getSpecification();
						System.out.println("\tTest transition: "+out.getSpecification());
						System.out.println("\t"+testPath);
						System.out.println("\tExpected next state:"+sc.statesId.get(out.getTarget()).getName());
						testPaths.add(testPath);
					}
				} else if (hashVertsCaminhos.get(state) != null){
					//primeiro entry nao esta mapeado -> da erro de numpointer
					for(String path : hashVertsCaminhos.get(state)) {
						testPath = path+" "+out.getSpecification();
						System.out.println("\tTest transition: "+out.getSpecification());
						System.out.println("\t"+testPath);
						System.out.println("\tExpected next state:"+sc.statesId.get(out.getTarget()).getName());
						testPaths.add(testPath);
					}
				}
			}
		}*/
		return testPaths;
	}
	
}
