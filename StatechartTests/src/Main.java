import java.util.Set;

import xml.handler.XMLProcessor;
import xml.statechart.Statechart;

public class Main {

	public static void main(String[] args) throws Exception{
				
		String filePath_1 = "/Users/rafaelgregorut/Documents/workspace/YakinduTest/test4.xml";
		
		XMLProcessor xml = new XMLProcessor();
		
		Statechart statechart = xml.createStatechartFromXml(filePath_1);
		
		//String filePath_2 = "/Users/rafaelgregorut/Documents/workspace/YakinduTest/file2.xml";
	
		//xml.writeStatechartToXml(statechart, filePath_2);
		
		//statechart.printStatechart();
		
		statechart.constructStateIdHash();
		TestGenerator tg = new TestGenerator(statechart);
		Set<String> testPaths = tg.createTestPaths();
		
		/*System.out.println("Caminhos computados no all-transitions:");
		for (String path : testPaths) {
			System.out.println(path);
		}*/
	}

}
