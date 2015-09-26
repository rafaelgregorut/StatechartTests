package xml.handler;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import xml.statechart.Statechart;

public class XMLProcessor {

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
