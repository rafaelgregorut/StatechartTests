package xml.statechart;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "xml.statechart.Statechart")
public class Vertices {

	private ArrayList<OutgoingTransitions> listTransitions;
	
	private ArrayList<Regions> listRegions;
	
	private String type;
	
	private String id;
	
	private String name;
	
	private String incomingTransitions;
	
	Vertices() {
		listTransitions = new ArrayList<OutgoingTransitions>();
		listRegions = new ArrayList<Regions>();
		name = "";
	}

	@XmlElement(name="outgoingTransitions")
	public ArrayList<OutgoingTransitions> getListTransitions() {
		return listTransitions;
	}

	public void setListTransitions(ArrayList<OutgoingTransitions> listTransitions) {
		this.listTransitions = listTransitions;
	}

	@XmlElement(name="regions")
	public ArrayList<Regions> getListRegions() {
		return listRegions;
	}

	public void setListRegions(ArrayList<Regions> listRegions) {
		this.listRegions = listRegions;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getIncomingTransitions() {
		return incomingTransitions;
	}

	public void setIncomingTransitions(String incomingTransitions) {
		this.incomingTransitions = incomingTransitions;
	}
	
	public void printVertices() {
		System.out.println("Vertice "+name+":");
		System.out.println("Type: "+type);
		System.out.println("Id: "+id);
		System.out.println("IncomingTransitions: "+incomingTransitions);
		for (OutgoingTransitions out : listTransitions) {
			out.printOutgoingTransitions();
		}
		for (Regions r : listRegions) {
			r.printRegions();
		}
	}
	
}
