package xml.statechart;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
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
	
	public Set<String> constructSetC(Hashtable<String,Vertices> hash,Hashtable<String,Vertices> hashCaminhoVert, 
			Hashtable<Vertices,Set<String>> hashFilhos) {
		Vertices initial = getInitialState();
		
		ArrayList<Vertices> visit = new ArrayList<Vertices>();
		Set<String> setC = new TreeSet<String>();
		
		constructSetCRec(0,initial,hash,visit,"",setC,hashCaminhoVert,hashFilhos);
		
		System.out.println("Hash de filhos:");
		for (Vertices vPai2 : hashFilhos.keySet()) {
			for (String str2 : hashFilhos.get(vPai2))
				System.out.println(vPai2.getName()+" -> "+str2);
		}
		
		return setC;
	}
	
	private void printRec(int i) {
		for (int j = 0; j < i; j++)
			System.out.print("\t");
	}
	
	Set<String> filhosPaths = null;

	public Set<String> constructSetCRec(int i,Vertices v, Hashtable<String, Vertices> hashId, ArrayList<Vertices> visitados,String p, 
			Set<String> setC, Hashtable<String,Vertices> hashCaminhoVert, Hashtable<Vertices,Set<String>> hashFilhos) {
		System.out.println("Regiao: "+this.name+" v: "+v.getName());
		visitados.add(v);
		setC.add(p);
		hashCaminhoVert.put(p, v);
		
		String newT = "";
		boolean vEhPai = (v.getListRegions().size() != 0);
		
		if(vEhPai) {
			printRec(i);System.out.println("vEhPai aqui:"+v.getName());
						
			//filhosPaths = v.getListRegions().get(0).constructSetC(hashId,hashCaminhoVert,hashFilhos);
			Vertices initFilho = v.getListRegions().get(0).getInitialState();
			System.out.println("Recursao na regiao "+v.getListRegions().get(0).getName());
			//filhosPaths = v.getListRegions().get(0).constructSetCRec(i+1,initFilho, hashId, visitados, p, setC, hashCaminhoVert);
			filhosPaths = v.getListRegions().get(0).constructSetC(hashId,hashCaminhoVert,hashFilhos);
			
			setC.remove(p);
			setC.add(p+" @_"+v.getName());

			//hashFilhos eh usado no TestGenerator
			hashFilhos.put(v, filhosPaths);			
			
			for (String str : filhosPaths) {
				System.out.println(str+" eh filho de "+v.getName());
			}
		}
			
		for (OutgoingTransitions out : v.getListTransitions()) {
			Vertices proxV = hashId.get(out.getTarget());
			
			if (!visitados.contains(proxV)) {
				newT = out.getSpecification();
				if (newT == null)                                                                                          
					newT = "";
				if(vEhPai) {
					constructSetCRec(i+1,proxV,hashId,visitados,p+" "+"@_"+v.getName()+" "+newT,setC,hashCaminhoVert,hashFilhos);
				}
				else 
					constructSetCRec(i+1,proxV,hashId,visitados,p+" "+newT,setC,hashCaminhoVert,hashFilhos);
			}
		}
		
		if (vEhPai) {
			System.out.println("ultimo "+v.getName());
			for (String str : filhosPaths) {
				System.out.println(str+" eh filho de "+v.getName());
			}
		}
		
		return setC;
	}
	
}
