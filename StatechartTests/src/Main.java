import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import xml.handler.XMLProcessor;
import xml.statechart.Statechart;

public class Main {

	private JFrame frame;
	private JTextField textField;
	
	JButton btnNewButton;
	
	public static Output out;
	public  JTable table;

	private Set<String> testPaths;
	private Set<String> csvLines;
	
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
		frame.setBounds(100, 100, 984, 596);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 80, 962, 450);
		frame.getContentPane().add(scrollPane);
		

		String[] columnNames = {"State",
	            "Transition",
	            "Test Path",
	            "Expected State"};
		DefaultTableModel model = new DefaultTableModel(null,columnNames);
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
		
		table.setBounds(688, 100, 1, 1);
		
		out = new Output(table);
		
		btnNewButton = new JButton("Create test cases");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				//out.clear();
				
				XMLProcessor xml = new XMLProcessor();
				String filePath = textField.getText();
				xml.createXmlFromYakindu(filePath);
				
				try {
					Statechart statechart = xml.createStatechartFromXml("temp.xml");
						
					statechart.constructStateIdHash();
					TestGenerator tg = new TestGenerator(statechart);
					testPaths = tg.createTestPaths();
					csvLines = tg.csvLines;
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBounds(844, 39, 134, 29);
		frame.getContentPane().add(btnNewButton);
		
		textField = new JTextField();
		textField.setBounds(264, 4, 579, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		
		
		JLabel lblNewLabel = new JLabel("Path to your Yakindu Statechart (.sct):");
		lblNewLabel.setBounds(16, 0, 258, 37);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnOpenStatechart = new JButton("Open Statechart");
		btnOpenStatechart.setBounds(844, 5, 134, 29);
		btnOpenStatechart.addActionListener(new ActionListener() {
			String filePath;
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    filePath = selectedFile.getAbsolutePath();
				}
				textField.setText(filePath);
			}
		});
		frame.getContentPane().add(btnOpenStatechart);
		
		JButton btnExportInSpmf = new JButton("Export in SPMF format");
		btnExportInSpmf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				FileDialog fDialog = new FileDialog(frame, "Save", FileDialog.SAVE);
		        fDialog.setVisible(true);
		        String spmfPath = fDialog.getDirectory() + fDialog.getFile();
				TestPathsAdapter adapter = new TestPathsAdapter();
		        Set<String> adaptedPaths = adapter.adaptToSMPF(testPaths);
		        out.writeSPMFToFile(spmfPath, adaptedPaths);
		        
			}
		});
		btnExportInSpmf.setBounds(660, 542, 183, 29);
		frame.getContentPane().add(btnExportInSpmf);
		
		JButton btnExportTocsv = new JButton("Export to .csv");
		btnExportTocsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				FileDialog fDialog = new FileDialog(frame, "Save", FileDialog.SAVE);
		        fDialog.setVisible(true);
		        String csvPath = fDialog.getDirectory() + fDialog.getFile();
		        out.writeCsvToFile(csvPath, csvLines);
			}
		});
		btnExportTocsv.setBounds(844, 542, 134, 29);
		frame.getContentPane().add(btnExportTocsv);
		
		
	}
}