package xml.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import xml.statechart.Statechart;

public class XMLProcessor {
		
	public void createXmlFromYakindu(String filePath) {
		boolean done = false;
		try {
			PrintWriter writer = new PrintWriter("temp.xml", "UTF-8");
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = reader.readLine()) != null && !done) {
				if (!line.contains("xmi:XMI")) {
					if(line.contains("</sgraph:Statechart>"))
						done = true;
					line = line.replaceAll("sgraph:", "");
					line = line.replaceAll("xmi:id", "id");
					line = line.replaceAll("xsi:type", "type");
					writer.println(line);
				}
			}
			reader.close();
			writer.close();
		} catch (Exception e) {
		    System.err.format("Exception occurred trying to read '%s'.", filePath);
		    e.printStackTrace();
		}
	}

	public Statechart createStatechartFromXml(String filePath) throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(xml.statechart.Statechart.class);  
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
		File XMLfile = new File(filePath);
		return (Statechart) jaxbUnmarshaller.unmarshal(XMLfile);
	}
	
	public void writeStatechartToXml(Statechart statechart, String filePath) throws Exception {
		File file = new File(filePath);
		JAXBContext jaxbContext2 = JAXBContext.newInstance(Statechart.class);
		Marshaller jaxbMarshaller = jaxbContext2.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(statechart, file);
	}
}
