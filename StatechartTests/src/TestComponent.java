import xml.statechart.Vertices;


public class TestComponent implements Comparable<TestComponent> {

	//Coverage path
	String sequenciaCobertura;
	//Destiny vertex
	Vertices atingido;
	
	public TestComponent(String s, Vertices v) {
		this.sequenciaCobertura = s;
		this.atingido = v;
	}
	
	public int compareTo(TestComponent o) {
		String myId = atingido.getId()+sequenciaCobertura;
		String otherId = o.atingido.getId()+o.sequenciaCobertura;
        return myId.compareTo(otherId);
    }
}
