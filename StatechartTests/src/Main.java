import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;

import java.util.Set;

import xml.handler.XMLProcessor;
import xml.statechart.Statechart;
import javax.swing.JCheckBox;

public class Main {

	private JFrame frame;
	private JTextField textField;
	
	JTextArea textArea;
	
	JButton btnNewButton;
	
	public static Output out;
	private JCheckBox chckbxSpmf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
					
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 649, 479);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		btnNewButton = new JButton("Create test cases");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.clear();
				
				String filePath_1 = textField.getText();
				
				XMLProcessor xml = new XMLProcessor();
				xml.createXmlFromYakindu(filePath_1);
				
				try {
					Statechart statechart = xml.createStatechartFromXml("temp.xml");
				
					statechart.constructStateIdHash();
					TestGenerator tg = new TestGenerator(statechart);
					Set<String> testPaths = tg.createTestPaths();
				
					if (chckbxSpmf.isSelected()) {
						TestPathsAdapter adapter = new TestPathsAdapter();
						out.println("==========================================================");
						out.println("Test paths for SPMF");
						//textArea.append("####################");
						adapter.adaptToSMPF(testPaths);
					}
					/*System.out.println("Caminhos computados no all-transitions:");
					for (String path : testPaths) {
						System.out.println(path);
					}*/
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBounds(495, 39, 134, 29);
		frame.getContentPane().add(btnNewButton);
		
		textField = new JTextField();
		textField.setBounds(16, 38, 403, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 73, 613, 378);
		frame.getContentPane().add(scrollPane);
		
		//
		textArea = new JTextArea();
		textArea.setEditable(false);
		out = new Output(textArea);

		
//		textArea.setText("blaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\nbla\nbla\nbla\n");
		scrollPane.setViewportView(textArea);
		
		JLabel lblNewLabel = new JLabel("Path to your Yakindu Statechart (.sct):");
		lblNewLabel.setBounds(16, 0, 258, 37);
		frame.getContentPane().add(lblNewLabel);
		
		chckbxSpmf = new JCheckBox("SPMF");
		chckbxSpmf.setBounds(423, 31, 79, 42);
		frame.getContentPane().add(chckbxSpmf);
	}
}