package vamix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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

public class Library extends JPanel implements ActionListener, ListSelectionListener, MouseListener{



	private JList allMedia;
	private DefaultListModel<Object> l;

	private JTextArea info;
	private JButton button1;
	private JButton button2;
	private File selectedFile;

	private List s = new ArrayList<>();

	private HashMap paths = new HashMap<>();
	private HashMap sizes = new HashMap<>();


	
	public Library(){

		setLayout(new GridBagLayout());

		l = new DefaultListModel<>();

		allMedia = new JList(l);
		allMedia.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scroll = new JScrollPane(allMedia);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx=1;
		gbc.weighty=1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		
		gbc.fill = GridBagConstraints.BOTH;
		add(scroll,gbc);


		info = new JTextArea();
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 1;
		gbc1.gridy = 0;
		gbc1.weighty=1;
		gbc1.weightx = 1;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.gridwidth = 1;
		gbc1.gridheight = 1;
		add(info,gbc1);


		button1 = new JButton("Add Video/Audio");
		GridBagConstraints g = new GridBagConstraints();
		g.gridx=0;
		g.gridy=1;
		g.weighty=1;
		g.weightx=1;
		g.gridheight = 1;
		g.gridwidth =1;
		add(button1,g);

		button2 = new JButton("Remove Video/Audio");
		GridBagConstraints g1 = new GridBagConstraints();
		g1.gridx = 1;
		g1.gridy = 1;
		g1.weightx = 1;
		g1.weighty = 1;
		g1.gridheight = 1;
		g1.gridwidth = 1;
		add(button2,g1);

		button1.addActionListener(this);
		button2.addActionListener(this);


		info.append("Name of File:");
		info.append("\n\n\n\n\n\nPath to File:");
		info.append("\n\n\n\n\n\n\nFile Size:");

		allMedia.addListSelectionListener(this);
		allMedia.addMouseListener(this);
		
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
					paths.put(selectedFile.getName(), selectedFile.getAbsolutePath());
					sizes.put(selectedFile.getName(),selectedFile.length());
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
		// see if any value is selected

		// if a value is selected then make change to the text area

		if(allMedia.getLastVisibleIndex() == -1){
			info.setText("");
			info.append("Name of File:");
			info.append("\n\n\n\n\n\nPath to File:");
			info.append("\n\n\n\n\n\n\nFile Size:");
		}
		
		if(!e.getValueIsAdjusting()){
			

			if(allMedia.getSelectedValue() != null){

				String name = allMedia.getSelectedValue().toString();


				// get the value of the selected value and then
				Object selected = allMedia.getSelectedValue();

				String pathOfFile = paths.get(selected).toString();
				String size = sizes.get(selected).toString();
				int megabytes =(int)((Double.parseDouble(size))/1024)/1024;
				
				info.setText("");
				
				
				info.append("Name of File: " + name);
				info.append("\n\n\n\n\n\nPath to File: " + pathOfFile);
				info.append("\n\n\n\n\n\n\nFile Size: " + megabytes + " megabytes");

				

				

			}
		}

	}


	@Override
	public void mouseClicked(MouseEvent e) {

		
		
		if(e.getClickCount() == 2){
			//ExtendedFrame.tabsPane.setSelectedIndex(0);
			// then load the video in the videoplayers filepath;
			
			if(!allMedia.getValueIsAdjusting()){
				String apath = paths.get(allMedia.getSelectedValue()).toString();
				VideoPlayer.filePath = apath;
				VideoPlayer.startPlaying();
				ExtendedFrame.tabsPane.setSelectedIndex(0);
			}
		}	
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}

