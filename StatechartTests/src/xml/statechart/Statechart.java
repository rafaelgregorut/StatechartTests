package xml.statechart;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Statechart")
public class Statechart {

	private ArrayList<Regions> listRegions;
	
	public Hashtable<String,Vertices> statesId;
	
	public Statechart() {
		listRegions = new ArrayList<Regions>();
		statesId = new Hashtable<String,Vertices>();
	}

	@XmlElement(name="regions")
	public ArrayList<Regions> getListRegions() {
		return listRegions;
	}

	public void setListRegions(ArrayList<Regions> listRegions) {
		this.listRegions = listRegions;
	}
	
	public void printStatechart() {
		for (Regions r : listRegions) {
			r.printRegions();
		}
	}
	
	public void constructStateIdHash() {
		for (Regions r : listRegions) {
			r.addToStatesIdHash(statesId);
		}
	}
}
