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
	
	public Set<String> constructSetCRec(int i,Vertices v, Hashtable<String, Vertices> hashId, ArrayList<Vertices> visitados,String p, 
			Set<String> setC, Hashtable<String,Vertices> hashCaminhoVert, Hashtable<Vertices, Set<String>> hashFilhos) {
		//printRec(i);System.out.println(v.getType()+" "+v.getName());
		visitados.add(v);
		setC.add(p);
		hashCaminhoVert.put(p, v);
		
		String newT = "";
		boolean vEhPai = (v.getListRegions().size() != 0);
		Set<String> filhosPaths = null;
		
		if(vEhPai) {
			//printRec(i);System.out.println("vEhPai aqui:"+v.getName());
			//del = hashCaminhoVert.remove(p);
			
			
			//filhosPaths = v.getListRegions().get(0).constructSetC(hashId,hashCaminhoVert,hashFilhos);
			Vertices initFilho = v.getListRegions().get(0).getInitialState();
			filhosPaths = v.getListRegions().get(0).constructSetCRec(i+1,initFilho, hashId, visitados, p, setC, hashCaminhoVert, hashFilhos);
			
			for (String f : filhosPaths)
				System.out.println(f);
				
			setC.remove(p);

			//hashFilhos eh usado no TestGenerator
			hashFilhos.put(v, filhosPaths);			
			for (Vertices vPai : hashFilhos.keySet()) {
				for (String str : hashFilhos.get(vPai)) {
					System.out.println(vPai.getName()+" -> "+str);
				}
			}
			
			/*for (String filhoPath : filhosPaths) {
				Vertices vDest = hashCaminhoVert.remove(filhoPath);
				hashCaminhoVert.put(p+filhoPath, v);
			}*/
			
			
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
		/*
		if (vEhPai) {
			

			System.out.println("vEhPai aqui tmb tbm:"+v.getName());
			Enumeration<String> keys = hashCaminhoVert.keys();
			while (keys.hasMoreElements()) {
				String incompletePath = keys.nextElement();
				if(incompletePath.contains("@_"+v.getName())) {
					Vertices destState = hashCaminhoVert.remove(incompletePath);
					for (String filhoPath: filhosPaths) {
						 String aux = incompletePath.replace("@_"+v.getName(), filhoPath);
						 hashCaminhoVert.put(aux, destState);
					}
				}
			}
			Set<String> newFilhosPath = new TreeSet<String>();
			
			for (String caminhoFilho : filhosPaths) {
				Vertices f = hashCaminhoVert.remove(caminhoFilho);
				if(f!= null) {
					//printRec(i);System.out.println("removi caminho "+caminhoFilho);
					//printRec(i);System.out.println(" que ia pro "+f.getName());
					hashCaminhoVert.put(p+caminhoFilho,f);
					newFilhosPath.add(p+caminhoFilho);
				} else {
					hashCaminhoVert.put(p+caminhoFilho,del);
					newFilhosPath.add(p+caminhoFilho);
				}
					
			}
			//hashCaminhoVert.remove(p);
			hashFilhos.remove(v);
			hashFilhos.put(v, newFilhosPath);
		}*/
		System.out.println("ultimo "+v.getName());
		for (Vertices vPai : hashFilhos.keySet()) {
			for (String str : hashFilhos.get(vPai)) {
				System.out.println(vPai.getName()+" -> "+str);
			}
		}
		
		return setC;
	}
	
}
