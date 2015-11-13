import java.util.Set;
import java.util.TreeSet;


public class TestPathsAdapter {

	Output out;
	
	public TestPathsAdapter() {
		out = Main.out;
	}
	
	public Set<String> adaptToSMPF(Set<String> testSet) {
		Set<String> results = new TreeSet<String>();
		for (String str : testSet) {
			//out.println(str);
			str = str.replaceFirst("^\\s+", "");
			str = str.replaceFirst("\\s+$", "");
			str = str.replaceAll("\\s+", " -1 ");
			if (!str.equals("")) {
				str = str.replaceAll("$", " -1 -2");
				//out.println(str);
				results.add(str);
			}
		}
		return results;
	}
	
}
