import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import xml.statechart.OutgoingTransitions;
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
	
	public Set<String> createTestPaths() {
		Regions mainRegion = sc.getListRegions().get(0);
		Hashtable<String, Vertices> caminhosVerts = new Hashtable<String,Vertices>();
		
		Set<String> set = mainRegion.constructSetC(sc.statesId,caminhosVerts,hashFilhos);
		
		System.out.println("Set C:");
		for (String str : set) {
			System.out.println(str);
		}
		/*for (String path : caminhosVerts.keySet()) {
			System.out.println(caminhosVerts.get(path).getName()+": "+path);
		}*/
		
		System.out.println("Test cases:");
		Hashtable<Vertices,Set<String>> mapVertPaths = reverseHash(caminhosVerts);
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
