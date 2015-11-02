import java.util.Set;


public class TestPathsAdapter {

	Output out;
	
	public TestPathsAdapter() {
		out = Main.out;
	}
	
	public void adaptToSMPF(Set<String> testSet) {
		for (String str : testSet) {
			//out.println(str);
			str = str.replaceFirst("^\\s+", "");
			str = str.replaceFirst("\\s+$", "");
			str = str.replaceAll("\\s+", " -1 ");
			if (!str.equals("")) {
				str = str.replaceAll("$", " -1 -2");
				out.println(str);
			}
		}
	}
	
}
