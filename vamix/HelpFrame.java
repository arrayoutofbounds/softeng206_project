package vamix;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

import mediacomponent.LogFile;


public class HelpFrame extends JFrame{
	
	private JPanel p;
	private JTextArea area; 
	private JScrollPane scroll;
	
	
	File readme;
	
	
	public HelpFrame(){
		super("Help");
		setLayout(new BorderLayout());
		
		p = new JPanel(new BorderLayout());
		
		area = new JTextArea();
		area.setLineWrap(true);
		area.setEditable(false);
		
		scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		p.add(scroll,BorderLayout.CENTER);
		add(p,BorderLayout.CENTER);
		readme = new File(System.getProperty("user.dir") +  File.separator + "src" + File.separator + "README.md");
		
		 
	}
	
	
	public void appendReadmeFile(){
		
		try {
 			
 			BufferedReader br = new BufferedReader(new FileReader(readme));
 			String line = br.readLine();
 	        while (line != null) {
 	            area.append(line);
 	            area.append(System.lineSeparator());
 	            line = br.readLine();
 	        }
 	        br.close();
 		} catch (IOException e) {
 			// Could not read log file, display error message
 			JOptionPane.showMessageDialog(null, "Could not open log file: No log available", "ERROR", JOptionPane.ERROR_MESSAGE);
 		}
		area.setCaretPosition(0);
	}
	

}
