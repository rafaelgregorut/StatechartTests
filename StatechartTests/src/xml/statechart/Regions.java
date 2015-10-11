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
	
	//public ArrayList<EventList> setC;
	
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
	
	public Hashtable<String,Vertices> constructSetC(Hashtable<String,Vertices> hash, 
			Hashtable<Vertices,Set<String>> hashFilhos) {
		Vertices initial = getInitialState();
		
		ArrayList<Vertices> visit = new ArrayList<Vertices>();
		Hashtable<String,Vertices> setCHash = new Hashtable<String,Vertices>();
		
		constructSetCRec(0,initial,hash,visit,"",setCHash,hashFilhos);
		
		System.out.println("Hash de filhos:");
		for (Vertices vPai2 : hashFilhos.keySet()) {
			for (String str2 : hashFilhos.get(vPai2))
				System.out.println(vPai2.getName()+" -> "+str2);
		}
		
		return setCHash;
	}
	
	private void printRec(int i) {
		for (int j = 0; j < i; j++)
			System.out.print("\t");
	}
	

	public Hashtable<String,Vertices> constructSetCRec(int i,Vertices v, Hashtable<String, Vertices> hashId, ArrayList<Vertices> visitados,String p, 
			Hashtable<String,Vertices> setCHash, Hashtable<Vertices,Set<String>> hashFilhos) {
		/*Adiciono o vertice na lista de visitados*/
		visitados.add(v);
		
		/*Associo o caminho ate aqui com o vertice*/
		setCHash.put(p,v);
		
		String newT = "";
		
		/*Preparo pra ver se o vertice eh pai de outros*/
		boolean vEhPai = (v.getListRegions().size() != 0);
		Set<String> filhosPathsSet = new TreeSet<String>();
		Hashtable<String,Vertices> filhosPathsHash = null;
		
		/*Se for pai*/
		if(vEhPai) {						
			/*Construo o conjunto de cobertura da regiao dos filhos separadamente*/
			filhosPathsHash = v.getListRegions().get(0).constructSetC(hashId,hashFilhos);
			
			/*Removo a associacao do caminho ate aqui com o vertice pai*/
			setCHash.remove(p);
			
			/*Renovo a associacao do caminho ate aqui com o vertice pai, deixando marcado que preciso expandir o caminho*/
			setCHash.put(p+" @"+v.getName()+"@",v);

			/*Crio o conjunto dos caminhos dos filhos*/
			for (String filhoPath : filhosPathsHash.keySet())
				filhosPathsSet.add(filhoPath);
			
			/*Associo o conjunto dos caminhos dos filhos com o vertice pai. O hashFilhos eh usado no TestGenerator*/
			hashFilhos.put(v, filhosPathsSet);
			
			/*Associo cada caminho filho ao vertice que ele cobriu*/
			setCHash.putAll(filhosPathsHash);
		}
			
		/*Para cada transicao que sai do vertice*/
		for (OutgoingTransitions out : v.getListTransitions()) {
			
			/*Pego o vertice de destino*/
			Vertices proxV = hashId.get(out.getTarget());
			
			/*Se o vertice de destino ainda nao foi visitado
			 * e o vertice de destino está nessa regiao*/
			if (!visitados.contains(proxV) && this.getListVertices().contains(proxV)) {
				
				/*Pego o nome da transicao*/
				newT = out.getSpecification();
				if (newT == null)                                                                                          
					newT = "";
				
				/*Se o estado onde estou eh pai*/
				if(vEhPai) {
					/*chamo a recursao deixando indicado que preciso expandir*/
					constructSetCRec(i+1,proxV,hashId,visitados,p+" "+"@"+v.getName()+"@ "+newT,setCHash,hashFilhos);
				}
				else {
					/*Chamo a recursao sem indicar expansao*/
					constructSetCRec(i+1,proxV,hashId,visitados,p+" "+newT,setCHash,hashFilhos);
				}
			}
		}
		
		/*Retorno a associacao entre caminho e vertice alcançado*/
		return setCHash;
	}
	
}
