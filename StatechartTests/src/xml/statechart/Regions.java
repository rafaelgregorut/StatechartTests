package xml.statechart;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import mef.basics.EventList;

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
			if(v.getListRegions().size() != 0) {
				for (Regions r : v.getListRegions()) {
					r.addToStatesIdHash(hash);
				}
			}
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
	
	public Set<String> constructSetC(Hashtable<String,Vertices> hash, Hashtable seqCover) {
		Vertices initial = getInitialState();
		
		ArrayList<Vertices> visit = new ArrayList<Vertices>();
		Set<String> setC = new TreeSet<String>();
		
		constructSetCRec(0,initial,hash,visit,"",seqCover,setC);
		
		return setC;
	}
	
	private void printRec(int i) {
		for (int j = 0; j < i; j++)
			System.out.print("\t");
	}
	
	public void constructSetCRec(int i,Vertices v, Hashtable<String, Vertices> hashId, ArrayList<Vertices> visitados,String p, Hashtable<Vertices,String> hashSeqCover, Set<String> setC) {
		visitados.add(v);
		hashSeqCover.put(v,p);
		setC.add(p);
		
		String newT = "";
		boolean vEhPai = (v.getListRegions().size() != 0);
		Set<String> filhosPaths = null;
		
		if(vEhPai) {
			printRec(i);System.out.println("aqui");
			filhosPaths = v.getListRegions().get(0).constructSetC(hashId, hashSeqCover);
		}
			
		for (OutgoingTransitions out : v.getListTransitions()) {
			Vertices proxV = hashId.get(out.getTarget());
			
			if (!visitados.contains(proxV)) {
				
				newT = out.getSpecification();
				if (newT == null)                                                                                          
					newT = "";

				constructSetCRec(i+1,proxV,hashId,visitados,p+" "+newT,hashSeqCover,setC);	
				
				if (vEhPai) {
					Set<String> auxResults = new TreeSet<String>();
					for (String path : setC) {
						if (path.contains(newT)) {
							for (String filhoPath : filhosPaths) {
								String aux = path.replace(newT, filhoPath+" "+newT);
								auxResults.add(aux);
							}
							setC.remove(path);
						}
					}
					setC.addAll(auxResults);
				}
			}
			
		}
	}
	
}
