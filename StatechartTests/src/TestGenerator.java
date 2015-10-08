import java.util.Hashtable;

import mef.basics.EventList;
import xml.statechart.Regions;
import xml.statechart.Statechart;
import xml.statechart.Vertices;


public class TestGenerator {

	Statechart sc;
	
	Hashtable<Vertices,String> setC;
	
	Hashtable<Vertices,String> exitTransitions;
	
	/*PRIMEIRO VOU PENSAR EM UM STATECHART SIMPLES DE TUDO*/
	
	public TestGenerator(Statechart sc) {
		setC = new Hashtable<Vertices,String>();
		exitTransitions = new Hashtable<Vertices,String>();
		this.sc = sc;
	}
	
	public void constructSetC() {
		Regions mainRegion = sc.getListRegions().get(0);
		
		mainRegion.constructSetC(sc.statesId,setC);
	}
	
}
