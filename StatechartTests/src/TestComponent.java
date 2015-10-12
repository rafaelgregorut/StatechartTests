import xml.statechart.Vertices;


public class TestComponent implements Comparable<TestComponent> {

	String sequenciaCobertura;
	Vertices atingido;
	
	public TestComponent(String s, Vertices v) {
		this.sequenciaCobertura = s;
		this.atingido = v;
	}
	
	public int compareTo(TestComponent o) {
        return sequenciaCobertura.compareTo(o.sequenciaCobertura);
    }
}
