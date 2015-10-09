import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import xml.statechart.Regions;
import xml.statechart.Statechart;
import xml.statechart.Vertices;


public class TestGenerator {

	Statechart sc;
	
	Hashtable<Vertices,String> setC;
	
	Set<String> setCReal;
	
	Hashtable<Vertices,String> exitTransitions;
	
	/*PRIMEIRO VOU PENSAR EM UM STATECHART SIMPLES DE TUDO*/
	
	public TestGenerator(Statechart sc) {
		setC = new Hashtable<Vertices,String>();
		exitTransitions = new Hashtable<Vertices,String>();
		this.sc = sc;
	}
	
	public void constructSetC() {
		Regions mainRegion = sc.getListRegions().get(0);
		
		setCReal = mainRegion.constructSetC(sc.statesId,setC);
		
		/*Enumeration<Vertices> keys = setC.keys();
		
		while(keys.hasMoreElements()) {
			Vertices v = keys.nextElement();
			System.out.println(v.getType()+" "+v.getName()+" "+setC.get(v));
		}*/
		
		for (String str : setCReal) {
			System.out.println(str);
		}
	}
	
}
