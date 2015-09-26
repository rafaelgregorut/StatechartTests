package xml.handler;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {

	Document xmlDoc;
	
	public XMLReader() {
		xmlDoc = null;
	}
	
	public Document getXmlDoc() {
		return xmlDoc;
	}
	
	public void readXML(String filePath) throws Exception {
		 File inputFile = new File(filePath);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         
         xmlDoc = doc;
  	}
	
	public void processaRegions(Node region, int rec) {
		for (int r = 0; r < rec; r++) System.out.print("\t");
		
		System.out.println(region.getNodeName());
		
		NodeList nodes = region.getChildNodes();
		//TODO
		//FIXME
		
		
		/*CONTINUAR AQUI!!!!!
		 * estou processando uma regiao
		 * dentro de uma regiao tem varios vertices
		 * */
	}
	
	public void printElements() {
		Document doc = xmlDoc;
		 System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         
		 Node statechartNode = doc.getElementsByTagName("sgraph:Statechart").item(0);
		 
		 
		 System.out.println(statechartNode.getNodeName());
		 
		 NodeList nodes = statechartNode.getChildNodes();
		 
		 for (int i = 0; i < nodes.getLength(); i++) {
			 if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
				 if(nodes.item(i).getNodeName() == "regions")
					 processaRegions(nodes.item(i),0);
		 }
		 
		 /*NodeList nList = doc.getElementsByTagName("vertices");
         System.out.println("----------------------------");
         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               System.out.println("State name: " 
                  + eElement.getAttribute("name"));
               /*System.out.println("First Name : " 
                  + eElement
                  .getElementsByTagName("firstname")
                  .item(0)
                  .getTextContent());
               System.out.println("Last Name : " 
               + eElement
                  .getElementsByTagName("lastname")
                  .item(0)
                  .getTextContent());
               System.out.println("Nick Name : " 
               + eElement
                  .getElementsByTagName("nickname")
                  .item(0)
                  .getTextContent());
               System.out.println("Marks : " 
               + eElement
                  .getElementsByTagName("marks")
                  .item(0)
                  .getTextContent());*
            }
         }*/
	}
	
}
