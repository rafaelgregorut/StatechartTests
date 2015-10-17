import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xml.statechart.OutgoingTransitions;
import xml.statechart.Regions;
import xml.statechart.Statechart;
import xml.statechart.Vertices;


public class TestGenerator {

	Statechart sc;
	
	Hashtable<Vertices,String> setC;
	
	Hashtable<String,Vertices>  reverseMapSetC;
	
	Hashtable<Vertices,Set<String>> hashFilhos;
	
	//Set<String> testPaths;
	
	/*PRIMEIRO VOU PENSAR EM UM STATECHART SIMPLES DE TUDO*/
	
	public TestGenerator(Statechart sc) {
		setC = new Hashtable<Vertices,String>();
		hashFilhos = new Hashtable<Vertices,Set<String>>();
		//testPaths = new TreeSet<String>();
		reverseMapSetC = new Hashtable<String,Vertices>();
		this.sc = sc;
	}
	
	public Set<String> expandPath(String original, Set<String> pathsFilhosSet, Vertices vPai) {
		Set<String> expandedSet = new TreeSet<String>();
		
		for (String filhoPath : pathsFilhosSet) {
			String expandedPath = original.replace("@"+vPai.getId()+"@", filhoPath);
			expandedSet.add(expandedPath);
		}
		
		return expandedSet;
	}
	
	public boolean ehPai(Vertices v) {
		return (v.getListRegions().size() > 0);
	}
	
	/*Elimina recursivamente os caminhos de cobertura dos estados pai*/
	public void expandPaiPath(Hashtable<Vertices,String> mapSetC) {
		/*NAO MUDEI AQUI PARA A CONCORRENCIA 
		 * Faco a expansao dos pais do 1o nivel do statechart
		 */
		for (Vertices v : sc.getListRegions().get(0).getListVertices()) {
			if (ehPai(v)) {
				String paiPath = mapSetC.get(v);
				expandPaiPathRec(mapSetC,v,paiPath);
			}
		}
	}
	
	
	private Set<Vertices> achaFilhoCorrespondente(String caminho, Vertices pai, Hashtable<Vertices,String> mapSetC) {
		Set<Vertices> filhosCorrespondentes = new TreeSet<Vertices>();
		
		for (Vertices v : mapSetC.keySet()) {
			/*MUDEI AQUI PARA A CONCORRENCIA*/
			//System.out.println("to vendo "+v.getName()+" *"+mapSetC.get(v)+"*"+" %"+caminho+"%");
			/*Posso ter 2 filhos com o mesmo caminho no caso de sub-regioes concorrentes!!!!!*/
			for (int j = 0; j < pai.getListRegions().size(); j++) {
				//System.out.println(mapSetC.get(v) == caminho);
				if (mapSetC.get(v).equals(caminho) && pai.getListRegions().get(j).getListVertices().contains(v)) {
					filhosCorrespondentes.add(v);
				}
			}
		}
		return filhosCorrespondentes;
	}
	
	/*Usado para eliminar o caminho de cobertura de um estado pai (Recursivo)*/
	public void expandPaiPathRec(Hashtable<Vertices,String> mapSetC, Vertices vPai, String caminhoPai) {
		/*Pegos os filhos do pai*/
		Set<String> filhosPaths= hashFilhos.get(vPai);
			
		/*Para cada caminho filho*/
		for (String caminhoFilho : filhosPaths) {
			/*Pego o filho de destino*/
			Set<Vertices> filhoDests = achaFilhoCorrespondente(caminhoFilho,vPai,mapSetC);
			
			for (Vertices filhoDest : filhoDests) {
				//System.out.println(filhoDest.getName()+" eh filho do "+vPai.getName());

				if (filhoDest != null && filhoDest.getType() != "Entry") {
					//System.out.println("Caminho: "+caminhoFilho);
					//System.out.println(filhoDest.getName()+" eh filho do "+vPai.getName());
					String prefixPai = caminhoPai.replace("@"+vPai.getId()+"@", caminhoFilho);
					mapSetC.put(filhoDest,prefixPai);
					filhoDest.getListTransitions().addAll(vPai.getListTransitions());
					//mapSetC.put(vPai, prefixPai);
					if (ehPai(filhoDest))
						expandPaiPathRec(mapSetC,filhoDest,prefixPai);
						//expandPaiPathRec(mapSetC,vPai, prefixPai);
				}
			}
		}
		//String novoCaminhoPai = caminhoPai.replaceAll("@"+vPai.getId()+"@", "");
		mapSetC.remove(vPai);
		//mapSetC.put(vPai, novoCaminhoPai);
	}
	
	
	public Set<String> createTestPaths() {
		Regions mainRegion = sc.getListRegions().get(0);
		//Hashtable<String, Vertices> caminhosVerts = new Hashtable<String,Vertices>();
		
		/*Gero o setC, ainda nao expandido*/
		Hashtable<Vertices,String> mapSetC = mainRegion.constructSetC(sc.statesId,hashFilhos);
		
		//System.out.println("Set C:");
		/*for (Vertices v : mapSetC.keySet()) {
			System.out.println(v.getName()+"("+v.getType()+")"+" coberto por *"+mapSetC.get(v)+"*");
		}*/
		
		//System.out.println("Semi-Expanded set C:");
		/*Preciso expandir o Set C*/
		//reverseMapSetC = reverseHash(mapSetC);
		expandPaiPath(mapSetC);
		/*for (Vertices v : mapSetC.keySet()) {
			System.out.println(v.getName()+"("+v.getType()+")"+" coberto por *"+mapSetC.get(v)+"*");
		}*/
		
		//System.out.println("Componentes de teste simples:");
		Set<TestComponent> tcSet = geraComponentesDeTestSimples(mapSetC);
		/*for (TestComponent tc : tcSet) {
			System.out.println(tc.sequenciaCobertura+" atinge "+tc.atingido.getName()+" #"+tc.atingido.getType());
		}*/
		
		//System.out.println("mapSetC que sobrou:");
		/*for (Vertices v : mapSetC.keySet()) {
			System.out.println(v.getName()+"("+v.getType()+")"+" coberto por "+mapSetC.get(v));
		}*/
		
		//System.out.println("Componentes de teste complexos:");
		Set<TestComponent> tcSet2 = geraComponentesDeTestHierarquia(mapSetC);
		/*for (TestComponent tc : tcSet2) {
			System.out.println(tc.sequenciaCobertura+" atinge "+tc.atingido.getName()+" #"+tc.atingido.getType());
		}*/
		
		
		//System.out.println("Printando tcSet2 antes da uniao");
		//printTestSet(tcSet2);
		tcSet2.addAll(tcSet);
		
		//System.out.println("Printando tcSet2 apos uniao");
		//printTestSet(tcSet2);
		
		Set<String> testPaths = generateTestCases(tcSet2);
		
		return testPaths;
		//return generateTestPaths(mapVertPaths);
	}
	
	 public  List<String> getAllMatches(String text, String regex) {
	        List<String> matches = new ArrayList<String>();
	        Matcher m = Pattern.compile(regex).matcher(text);
	        while(m.find()) {
	            matches.add(m.group(1));
	        }
	        return matches;
	 }
	
	 public String aindaPrecisaExpandir(Set<String> strSet) {

		 for (String str : strSet) {
		     if (str.contains("@")) {
		    	 return str;
		     }
		 }
		 
		 return null;
	 }
	 
	 public Set<String> geraFlatParaCaminho(String caminho) {
		 
		 Set<String> novosCaminhos = new TreeSet<String>();
		 novosCaminhos.add(caminho);
		 while (true) {
		 	//List<String> matches = getAllMatches(caminho,"@(.*?)@");
		 	//System.out.println(caminho+" ~ "+matches);
			String strMuda = aindaPrecisaExpandir(novosCaminhos);
		 	if(strMuda == null)
		 		break;
		 	//System.out.println("muda "+strMuda);
		 	List<String> matches = getAllMatches(strMuda,"@(.*?)@");
		 	String id = matches.get(0);
		 	
		 	Set<String> subCaminhos = hashFilhos.get(sc.statesId.get(id));
		 	for (String caminhoSub : subCaminhos) {
		 		String novoCaminho = strMuda.replace("@"+id+"@", caminhoSub);
		 		novosCaminhos.add(novoCaminho);
		 		//System.out.println("mudei "+novosCaminhos);
		 	}
	 		novosCaminhos.remove(strMuda);
	 		//System.out.println(novosCaminhos);
		 }
		 
		 return novosCaminhos;
	 }
	 
	public Set<String> generateTestCases(Set<TestComponent> tcSet) {
		Set<String> testPaths = new TreeSet<String>();
		
		for (TestComponent tc : tcSet) {
			Vertices state = tc.atingido;
			System.out.println("State: "+state.getName()+" ("+state.getType()+")");
			for (OutgoingTransitions out : state.getListTransitions()) {
				if (out.getSpecification() != null) {
					String testPath = tc.sequenciaCobertura+" "+out.getSpecification();
					testPaths.add(testPath);
					System.out.println("\tTransition to be tested: "+out.getSpecification());
					System.out.println("\tTest path: "+testPath);
					System.out.println("\tExpected state: "+sc.statesId.get(out.getTarget()).getName());
				}
			}
		}
		return testPaths;
	}
	
	public void printTestSet(Set<TestComponent> s) {
		for (TestComponent tc : s) {
			System.out.println("tc: "+tc.sequenciaCobertura+" "+tc.atingido.getName()+" ("+tc.atingido.getType()+")");
		}
	}
	
	public Set<TestComponent> geraComponentesDeTestHierarquia(Hashtable<Vertices,String> mapSetC) {

		//Set<String> caminhosComplexos = new TreeSet<String>(mapSetC.values());
		Set<TestComponent> tcSet = new TreeSet<TestComponent>();
		for (Vertices v : mapSetC.keySet()) {
			//System.out.println(v.getName()+" "+v.getType());
			String str = mapSetC.get(v);
			Set<String> flats = geraFlatParaCaminho(str);
			for (String flat : flats) {
				//System.out.println("add "+flat);
				TestComponent tc = new TestComponent(flat,v);
				tcSet.add(tc);
			}
			//System.out.println("Printando tcSet no fim do processamento do vertice "+v.getName()+"("+v.getType()+")");
			//printTestSet(tcSet);
		}
        
		//System.out.println("Printando tcSet no fim do metodo geraComponentesDeTestHierarquia");
		//printTestSet(tcSet);
        return tcSet;
	}
	
	public Set<TestComponent> geraComponentesDeTestSimples(Hashtable<Vertices,String> mapSetC) {
		Set<TestComponent> conjComponentesTeste= new TreeSet<TestComponent>();
		Set<Vertices> deleteThis = new TreeSet<Vertices>();
		
		for (Vertices v : mapSetC.keySet()) {
			if (!mapSetC.get(v).contains("@")) {
				TestComponent tc = new TestComponent(mapSetC.get(v),v);
				conjComponentesTeste.add(tc);
				deleteThis.add(v);
			}
		}
		
		for (Vertices v : deleteThis)
			mapSetC.remove(v);
		
		return conjComponentesTeste;
	}
	
	public Hashtable<String, Vertices> reverseHash(Hashtable<Vertices,String> hash) {
		Hashtable<String, Vertices> newH = new Hashtable<String,Vertices>();
		
		for (Map.Entry<Vertices,String> entry : hash.entrySet()) {
			Vertices v = entry.getKey();
			String str = entry.getValue();
			newH.put(str, v);
		}
		return newH;
	}	
}
