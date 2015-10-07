package xml.statechart;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Statechart")
public class Statechart {

	private ArrayList<Regions> listRegions;
	
	public Statechart() {
		listRegions = new ArrayList<Regions>();
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
	
}
