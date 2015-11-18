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

	//The statechart to which we generate the tests
	Statechart sc;
	
	//State coverage set
	Hashtable<Vertices,String> setC;
		
	//Hash de estados filhos
	Hashtable<Vertices,Set<String>> hashFilhos;
	
	//Output to where we print results
	Output out;
	
	//Lines written to the csv file
	public Set<String> csvLines;
		
	public TestGenerator(Statechart sc) {
		setC = new Hashtable<Vertices,String>();
		hashFilhos = new Hashtable<Vertices,Set<String>>();
		this.sc = sc;
		out = Main.out;
		csvLines = new TreeSet<String>();
	}
	
	//Used to expand coverage paths with superstates
	public Set<String> expandPath(String original, Set<String> pathsFilhosSet, Vertices vPai) {
		Set<String> expandedSet = new TreeSet<String>();
		
		for (String filhoPath : pathsFilhosSet) {
			String expandedPath = original.replace("@"+vPai.getId()+"@", filhoPath);
			expandedSet.add(expandedPath);
		}
		
		return expandedSet;
	}
	
	//Returns if a state contains substates
	public boolean ehPai(Vertices v) {
		return (v.getListRegions().size() > 0);
	}
	
	/*
	 * Recursively eliminates the coverage paths of superstates
	 * */
	public void expandPaiPath(Hashtable<Vertices,String> mapSetC) {
		/*
		 * Expand the superstates in the 1st level
		 */
		for (Vertices v : sc.getListRegions().get(0).getListVertices()) {
			if (ehPai(v)) {
				String paiPath = mapSetC.get(v);
				expandPaiPathRec(mapSetC,v,paiPath);
			}
		}
	}
	
	//Finds the substate, given its coverage path and its superstate
	private Set<Vertices> achaFilhoCorrespondente(String caminho, Vertices pai, Hashtable<Vertices,String> mapSetC) {
		Set<Vertices> filhosCorrespondentes = new TreeSet<Vertices>();
		
		for (Vertices v : mapSetC.keySet()) {
			for (int j = 0; j < pai.getListRegions().size(); j++) {
				if (mapSetC.get(v).equals(caminho) && pai.getListRegions().get(j).getListVertices().contains(v)) {
					filhosCorrespondentes.add(v);
				}
			}
		}
		return filhosCorrespondentes;
	}
	
	/*Used to eliminate the coverage path of a superstate (Recursive)*/
	public void expandPaiPathRec(Hashtable<Vertices,String> mapSetC, Vertices vPai, String caminhoPai) {
		/*Get the substates*/
		Set<String> filhosPaths= hashFilhos.get(vPai);
			
		/*To each substate path*/
		for (String caminhoFilho : filhosPaths) {
			/*Get the corresponding substate*/
			Set<Vertices> filhoDests = achaFilhoCorrespondente(caminhoFilho,vPai,mapSetC);
			
			for (Vertices filhoDest : filhoDests) {

				if (filhoDest != null && filhoDest.getType() != "Entry") {
					String prefixPai = caminhoPai.replace("@"+vPai.getId()+"@", caminhoFilho);
					mapSetC.put(filhoDest,prefixPai);
					filhoDest.getListTransitions().addAll(vPai.getListTransitions());
					if (ehPai(filhoDest))
						expandPaiPathRec(mapSetC,filhoDest,prefixPai);
				}
			}
		}
		mapSetC.remove(vPai);
	}
	
	//Create the test paths used in the state cases
	public Set<String> createTestPaths() {
		//Get the main region of the statechart
		Regions mainRegion = sc.getListRegions().get(0);
		
		/*Gero o setC, ainda nao expandido*/
		Hashtable<Vertices,String> mapSetC = mainRegion.constructSetC(sc.statesId,hashFilhos);
		
		/*Preciso expandir o Set C*/
		expandPaiPath(mapSetC);
		
		//Create the test components for simple paths
		Set<TestComponent> tcSet = geraComponentesDeTestSimples(mapSetC);
		
		//Create the test components for cases with hierarchy
		Set<TestComponent> tcSet2 = geraComponentesDeTestHierarquia(mapSetC);
		
		//Union of both
		tcSet2.addAll(tcSet);
		
		//Get the test paths
		Set<String> testPaths = generateTestCases(tcSet2);
		
		return testPaths;
	}
	
	 public  List<String> getAllMatches(String text, String regex) {
	        List<String> matches = new ArrayList<String>();
	        Matcher m = Pattern.compile(regex).matcher(text);
	        while(m.find()) {
	            matches.add(m.group(1));
	        }
	        return matches;
	 }
	
	 //Checks if a path still needs expension
	 public String aindaPrecisaExpandir(Set<String> strSet) {

		 for (String str : strSet) {
		     if (str.contains("@")) {
		    	 return str;
		     }
		 }
		 
		 return null;
	 }
	 
	 //change the path to a flat version
	 public Set<String> geraFlatParaCaminho(String caminho) {
		 
		 Set<String> novosCaminhos = new TreeSet<String>();
		 novosCaminhos.add(caminho);
		 while (true) {
			String strMuda = aindaPrecisaExpandir(novosCaminhos);
		 	if(strMuda == null)
		 		break;
		 	List<String> matches = getAllMatches(strMuda,"@(.*?)@");
		 	String id = matches.get(0);
		 	
		 	Set<String> subCaminhos = hashFilhos.get(sc.statesId.get(id));
		 	for (String caminhoSub : subCaminhos) {
		 		String novoCaminho = strMuda.replace("@"+id+"@", caminhoSub);
		 		novosCaminhos.add(novoCaminho);
		 	}
	 		novosCaminhos.remove(strMuda);
		 }
		 
		 return novosCaminhos;
	 }
	 
	//Create the test cases
	public Set<String> generateTestCases(Set<TestComponent> tcSet) {
		Set<String> testPaths = new TreeSet<String>();
		
		for (TestComponent tc : tcSet) {
			Vertices state = tc.atingido;
			for (OutgoingTransitions outTrans : state.getListTransitions()) {
				if (outTrans.getSpecification() != null) {
					String testPath = tc.sequenciaCobertura+" "+outTrans.getSpecification();
					testPaths.add(testPath);
					out.printRow(state.getName(),outTrans.getSpecification(),testPath,sc.statesId.get(outTrans.getTarget()).getName());
					csvLines.add(state.getName()+","+outTrans.getSpecification()+","+testPath+","+sc.statesId.get(outTrans.getTarget()).getName());
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
	
	//Create the test components for cases with hierarchy.
	public Set<TestComponent> geraComponentesDeTestHierarquia(Hashtable<Vertices,String> mapSetC) {

		Set<TestComponent> tcSet = new TreeSet<TestComponent>();
		for (Vertices v : mapSetC.keySet()) {
			String str = mapSetC.get(v);
			Set<String> flats = geraFlatParaCaminho(str);
			for (String flat : flats) {
				TestComponent tc = new TestComponent(flat,v);
				tcSet.add(tc);
			}
		}
        
        return tcSet;
	}
	
	//Create test components for simple cases
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
	
		
}
