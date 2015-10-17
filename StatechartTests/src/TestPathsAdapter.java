import java.util.Set;


public class TestPathsAdapter {

	public TestPathsAdapter() {
		
	}
	
	public void adaptToSMPF(Set<String> testSet) {
		for (String str : testSet) {
			str = str.replaceFirst("^\\s+", "");
			str = str.replaceAll("\\s+", " -1 ");
			str = str.replaceAll("$", " -1 -2");
			System.out.println(str);
		}
	}
	
}
