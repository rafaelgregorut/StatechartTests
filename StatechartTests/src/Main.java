import xml.handler.*;

public class Main {

	public static void main(String[] args) throws Exception{
		
		String filePath = "/Users/rafaelgregorut/Documents/workspace/YakinduTest/test2.sct";
		
		XMLReader xml = new XMLReader();
		
		xml.readXML(filePath);
		xml.printElements();
		
	}

}
