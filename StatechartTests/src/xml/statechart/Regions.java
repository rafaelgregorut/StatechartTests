package xml.statechart;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import mef.basics.*;

@XmlRootElement(namespace = "xml.statechart.Statechart")  
public class Regions {

	private ArrayList<Vertices> listVertices;

	private String name;
	
	public ArrayList<EventList> setC;
	
	public Regions() {
		listVertices = new ArrayList<Vertices>();
	}
	
	@XmlElement(name="vertices")
	public ArrayList<Vertices> getListVertices() {
		return listVertices;
	}

	public void setListVertices(ArrayList<Vertices> listVertices) {
		this.listVertices = listVertices;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void printRegions() {
		System.out.println("Region "+name+":");
		for (Vertices v : listVertices) {
			v.printVertices();
		}
	}
	
	public void addToStatesIdHash(Hashtable<String,Vertices> hash) {
		for (Vertices v : listVertices) {
			hash.put(v.getId(),v);
		}
	}
	
	public Vertices getInitialState() {
		for (Vertices v : listVertices) {
			//System.out.println(v.getType());
			if (v.getType().equals("Entry")) {
				return v;
			}
		}
		return null;
	}
	
	public void constructSetC(Hashtable<String,Vertices> hash, Hashtable hashSetC) {
		Vertices initial = getInitialState();
		
		System.out.print(initial.getType());
		System.out.println(" "+initial.getName());
		ArrayList<Vertices> visit = new ArrayList<Vertices>();
		constructSetCRec(initial,hash,visit,"",hashSetC);
	}
	
	public void constructSetCRec(Vertices v, Hashtable<String, Vertices> hashId, ArrayList<Vertices> visitados,String p, Hashtable<Vertices,String> hashSeqCover) {
		visitados.add(v);
		//System.out.print(v.getType());
		//System.out.println(" "+v.getName());
		//boolean achou = false;
		//para cada transicao que sai de v
		//System.out.println(v.getType()+" "+v.getName());
		//System.out.println(p);
		hashSeqCover.put(v,p);
		for (OutgoingTransitions out : v.getListTransitions()) {
			//System.out.println(out.getSpecification());
			//se a transicao alcanca alguem nao visto
			if (!visitados.contains(hashId.get(out.getTarget()))) {
				//achou = true;
				String newT = out.getSpecification();
				if (newT == null)                                                                                          
					newT = "";
				//for(int j = 0; j < i; j++) {System.out.print("\t");}
				//System.out.print(p);
				//System.out.println(" "+newT);
				constructSetCRec(hashId.get(out.getTarget()),hashId,visitados,p+" "+newT,hashSeqCover);
			}
		}
		//System.out.println();
		//if (!achou)
			//System.out.println(p);
	}
	
}
