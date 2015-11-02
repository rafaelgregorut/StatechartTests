import javax.swing.JTextArea;


public class Output {

	private JTextArea textArea;

	Output(JTextArea ta) {
		textArea = ta;
	}
	
	public void println(String str) {
		textArea.append(str+"\n");
	}
	
	public void clear() {
		textArea.setText(null);
	}
}
