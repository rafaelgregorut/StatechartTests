import xml.handler.XMLProcessor;
import xml.statechart.Statechart;

public class Main {

	public static void main(String[] args) throws Exception{
		
		/*String filePath = "/Users/rafaelgregorut/Documents/workspace/YakinduTest/test2.sct";
		
		XMLReader xml = new XMLReader();
		
		xml.readXML(filePath);
		xml.printElements();
		*
		String file2 = "/Users/rafaelgregorut/Documents/workspace/YakinduTest/test2.xml";
		
		JAXBContext jaxbContext = JAXBContext.newInstance(xml.statechart.Statechart.class);  
		
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
		
		File XMLfile = new File(file2);
		
		Statechart statechart = (Statechart) jaxbUnmarshaller.unmarshal(XMLfile);  
		
		System.out.println("done");
		
		File file = new File("/Users/rafaelgregorut/Documents/workspace/YakinduTest/file2.xml");
		JAXBContext jaxbContext2 = JAXBContext.newInstance(Statechart.class);
		Marshaller jaxbMarshaller = jaxbContext2.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(statechart, file);
		//jaxbMarshaller.marshal(statechart, System.out);
	
		System.out.println("done");*/
		
		String filePath_1 = "/Users/rafaelgregorut/Documents/workspace/YakinduTest/test2.xml";
		
		XMLProcessor xml = new XMLProcessor();
		
		Statechart statechart = xml.createStatechartFromXml(filePath_1);
		
		String filePath_2 = "/Users/rafaelgregorut/Documents/workspace/YakinduTest/file2.xml";
	
		xml.writeStatechartToXml(statechart, filePath_2);
		
		statechart.printStatechart();
	}

}
