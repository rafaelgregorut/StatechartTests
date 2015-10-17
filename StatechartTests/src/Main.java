import java.util.Set;

import xml.handler.XMLProcessor;
import xml.statechart.Statechart;

public class Main {

	public static void main(String[] args) throws Exception{
				
		String filePath_1 = args[0];
		
		XMLProcessor xml = new XMLProcessor();
		xml.createXmlFromYakindu(filePath_1);
		
		Statechart statechart = xml.createStatechartFromXml("temp.xml");
		
		//String filePath_2 = "/Users/rafaelgregorut/Documents/workspace/YakinduTest/file2.xml";
	
		//xml.writeStatechartToXml(statechart, filePath_2);
		
		//statechart.printStatechart();
		
		statechart.constructStateIdHash();
		TestGenerator tg = new TestGenerator(statechart);
		Set<String> testPaths = tg.createTestPaths();
		
		TestPathsAdapter adapter = new TestPathsAdapter();
		System.out.println("==========================================================");
		adapter.adaptToSMPF(testPaths);
		
		/*System.out.println("Caminhos computados no all-transitions:");
		for (String path : testPaths) {
			System.out.println(path);
		}*/
	}

}
