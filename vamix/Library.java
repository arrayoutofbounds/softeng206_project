package vamix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mediacomponent.VideoPlayer;
import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;

public class Library extends JPanel implements ActionListener, ListSelectionListener {
	

	
	private JList allMedia;
	private DefaultListModel<Object> l;
	
	private JTextArea info;
	private JButton button1;
	private JButton button2;
	private File selectedFile;

	private List s = new ArrayList<>();
	
	public Library(){
		
		setLayout(new GridBagLayout());

		l = new DefaultListModel<>();
		
		allMedia = new JList(l);
		allMedia.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scroll = new JScrollPane(allMedia);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx=0.8;
		gbc.weighty=1;
		gbc.fill = GridBagConstraints.BOTH;
		add(scroll,gbc);
		
		
		info = new JTextArea();
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 1;
		gbc1.gridy = 0;
		gbc1.weighty=1;
		gbc1.weightx = 0.5;
		gbc1.fill = GridBagConstraints.BOTH;
		add(info,gbc1);
		
		
		button1 = new JButton("Add Video/Audio");
		GridBagConstraints g = new GridBagConstraints();
		g.gridx=0;
		g.gridy=1;
		g.weighty=0.2;
		g.weightx=0;
		add(button1,g);
		
		button2 = new JButton("Remove Video/Audio");
		GridBagConstraints g1 = new GridBagConstraints();
		g1.gridx = 1;
		g1.gridy = 1;
		g1.weightx = 0.7;
		g1.weighty = 0;
		add(button2,g1);
		
		button1.addActionListener(this);
		button2.addActionListener(this);
		
		info.append("Name of File:");
		info.append("\n\n\n\n\n\nPath to File:");
		info.append("\n\n\n\n\n\n\nFile Size:");
		

		allMedia.addListSelectionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		// if the button1 is pressed then open a jfilechooser and add a video or audio to the list;
		// if the button2 is chosen then remove it from the list;
		// put a listener on the items in the jlist so when they are clicked their information shows up on the text area.
		
		if(e.getSource() == button1){
			
			
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(SwingFileFilterFactory.newMediaFileFilter());
			int returnVal = fc.showOpenDialog(Library.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				

				String newFile = fc.getSelectedFile().getAbsolutePath();
				
				// Check that file is a video or audio file.
				InvalidCheck i = new InvalidCheck();
				boolean isValidMedia = i.invalidCheck(newFile);
				
				

				if (!isValidMedia) {
					JOptionPane.showMessageDialog(Library.this, "You have specified an invalid file.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}else{
					selectedFile = fc.getSelectedFile();
					l.addElement(selectedFile.getName());
					allMedia.setModel(l);
				}
			}
		}
		
		
		
		
		if(e.getSource() == button2){
			//System.out.println(allMedia.getSelectedValue());
			
			s = allMedia.getSelectedValuesList();
			
			// now remove all the value that are in the list from the default list model
			
			for(Object o : s){
				if(l.contains(o)){
					l.removeElement(o);
				}
			}
			
		}
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

