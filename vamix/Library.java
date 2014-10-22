package vamix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mediacomponent.LogFile;
import mediacomponent.VideoPlayer;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;
import videoFeatures.ExtractPart;

public class Library extends JPanel implements ActionListener, ListSelectionListener, MouseListener{

	private JButton cancelPlayingList;
	private JSplitPane splitPane;
	
	private JList allMedia;
	private DefaultListModel<Object> l;

	private JTextArea info;
	private JButton button1;
	private JButton button2;
	private File selectedFile;

	private List s = new ArrayList<>();

	private HashMap paths = new HashMap<>();
	private HashMap sizes = new HashMap<>();

	private JPanel container;

	private JButton makePlayList;

	List playlist = new ArrayList<>();
	List allPathPlaylist = new ArrayList<>();

	GetPlayList playlistName = new GetPlayList();
	private JButton loadPlaylist;
	private File playlistDirectory;
	private JButton playTheList;
	private JButton loadFolder;
	private JPanel arrangeButtons1;
	private JPanel arrangeButtons2;
	private List<File> listFolder = new ArrayList<>();
	
	public Library(){

		setLayout(new BorderLayout());

		l = new DefaultListModel<>();
		container = new JPanel(new BorderLayout());
		
		arrangeButtons1 = new JPanel(new FlowLayout());
		arrangeButtons2 = new JPanel(new FlowLayout());
		
		allMedia = new JList(l);
		allMedia.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scroll = new JScrollPane(allMedia);
		info = new JTextArea();
		button1 = new JButton("Add Video/Audio");
		arrangeButtons1.add(button1);
	
		button2 = new JButton("Remove Video/Audio");
		arrangeButtons1.add(button2);

		makePlayList = new JButton("Make a playlist");
		arrangeButtons1.add(makePlayList);
		makePlayList.addActionListener(this);


		loadPlaylist = new JButton("Load a playlist");
		arrangeButtons2.add(loadPlaylist);
		loadPlaylist.addActionListener(this);

		playTheList = new JButton("Play the list");
		arrangeButtons2.add(playTheList);
		playTheList.addActionListener(this);
		
		loadFolder = new JButton("Load a folder");
		arrangeButtons2.add(loadFolder);
		loadFolder.addActionListener(this);
		
		cancelPlayingList = new JButton("Cancel playing the list");
		arrangeButtons2.add(cancelPlayingList);
		cancelPlayingList.addActionListener(this);
		cancelPlayingList.setEnabled(false);
		
		container.add(arrangeButtons1,BorderLayout.NORTH);
		container.add(arrangeButtons2,BorderLayout.CENTER);

		playlistName = new GetPlayList();
		playlistName.setResizable(false);
		playlistName.setSize(700, 500);
		playlistName.setLocationRelativeTo(null);
		playlistName.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		button1.addActionListener(this);
		button2.addActionListener(this);

		info.append("Name of File:");
		info.append("\n\n\n\n\n\nPath to File:");
		info.append("\n\n\n\n\n\n\nFile Size:");
		allMedia.addListSelectionListener(this);
		allMedia.addMouseListener(this);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,allMedia,info);
		add(splitPane,BorderLayout.CENTER);
		add(container,BorderLayout.SOUTH);
		splitPane.setResizeWeight(0.5d);
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		// if the button1 is pressed then open a jfilechooser and add a video or audio to the list;
		// if the button2 is chosen then remove it from the list;
		// put a listener on the items in the jlist so when they are clicked their information shows up on the text area.

		if(e.getSource() == button1){
			button1Pressed();
		}

		if(e.getSource() == button2){
			button2Pressed();
		}

		if(e.getSource() == makePlayList){
			makeAPlayListPressed();
		}

		if(e.getSource() == loadPlaylist){
			loadAPlayListPressed();
		}

		if(e.getSource() == playTheList){
			playTheListPressed();
		}
		if(e.getSource()== loadFolder){
			loadFolderPressed();
		}
		if(e.getSource()==cancelPlayingList){
			cancelPressed();
		}
	}

	private void cancelPressed() {
		VideoPlayer.mediaPlayer.stop();
		l.clear();
		paths.clear();
		sizes.clear();
		cancelPlayingList.setEnabled(false);
		playTheList.setEnabled(true);
	}


	private void loadFolderPressed() {
		
		// open a jfile chooser and then go through the directory chosen
		// then load all the video and audio files in that directory
		
		File chosenDirectory =null;
		
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Choose Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int response = chooser.showOpenDialog(Library.this);

		if (response == JFileChooser.APPROVE_OPTION) {
			// Chose the directory to save to
			chosenDirectory = chooser.getSelectedFile().getAbsoluteFile();
		}
		
		if(chosenDirectory != null){
			// go through the chosen directory
			  l.clear();
			  File[] directoryListing = chosenDirectory.listFiles();
			  if (directoryListing != null) {
				  InvalidCheck ic = new InvalidCheck();
					
					for(File f : directoryListing){
						boolean isValid = ic.invalidCheck(f.getAbsolutePath());
						
						if(isValid){
							listFolder.add(f);
						}
					}
					
					for(File f2 : listFolder){
						System.out.println(f2.getName());
					}

			    for(File f : listFolder){
			    	l.addElement(f.getName());
			    	paths.put(f.getName(),f.getAbsoluteFile());
			    	sizes.put(f.getName(), f.length());
			    }
			  }
			
		}
	}


	private void playTheListPressed() {
		
		if(l.size() != 0){
			cancelPlayingList.setEnabled(true);
			ExtendedFrame.tabsPane.setSelectedIndex(0);
			JOptionPane.showMessageDialog(null,"Once the current item has finished playing, click the play button to play the next item. Repeat it.");
			@SuppressWarnings("unchecked")
			SwingWorker<Void,Void> w = new SwingWorker(){
				
				@Override
				protected Void doInBackground() throws Exception {
					
					for(int index = 0;index<l.size();index++){
						String apath = "" + paths.get(l.get(index));
						System.out.println(apath);
						VideoPlayer.filePath = apath;
						VideoPlayer.startPlaying();
						while((VideoPlayer.mediaPlayer.isPlaying())||(VideoPlayer.timeSlider.getValue() != VideoPlayer.mediaPlayer.getLength())){
							// do nothing
							// once it stops loop again
						}
						//if(VideoPlayer.mediaPlayer.getTime() == VideoPlayer.timeSlider.getValue()){
							VideoPlayer.mediaPlayer.stop();
						//}
					}
					return null;
				}
				@Override
				protected void done() {
				}
			};
			w.execute();
		}
	}

	private void loadAPlayListPressed() {

		// go to the selected folder and look for a file called list.txt and then load all the names of the files onto the jlist

		JFileChooser outputChooser = new JFileChooser();
		outputChooser.setCurrentDirectory(new java.io.File("."));
		outputChooser.setDialogTitle("Choose a directory to output to");

		outputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnValue = outputChooser.showOpenDialog(Library.this);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			playlistDirectory = outputChooser.getSelectedFile().getAbsoluteFile();
			//JOptionPane.showMessageDialog(ReplaceAudio.this, "Your file will be extracted to " + outputDirectory);
		}


		// now look in the playlist directory for the list.txt file. If there is one then go to all the paths and then load the file 
		// names onto the jlist
		File[] files;
		if(playlistDirectory !=null){
			files = playlistDirectory.listFiles();

			boolean textFileExists = false;
			File a = null;
			for(File f : files ){
				if((f.getName().toLowerCase().endsWith(".txt"))){
					textFileExists = true;
					a = f;
				}

			}


			if(textFileExists){

				try {
					l.clear();
					BufferedReader br = new BufferedReader(new FileReader(a));
					String line = br.readLine();
					while (line != null) {


						String[] split = line.split(File.separator);
						int last = split.length;
						String lastString = split[last-1];
						paths.put(lastString, line);
						File mock = new File(line);
						sizes.put(lastString,mock.length());
						l.addElement(lastString);
						line = br.readLine();
					}
					br.close();
				} catch (IOException e3) {
					// Could not read log file, display error message
					JOptionPane.showMessageDialog(null, "Could not open file", "ERROR", JOptionPane.ERROR_MESSAGE);
				}


			}else{
				JOptionPane.showMessageDialog(Library.this, "Sorry, no plyalist exists in this folder!");
			}
		}
		
	}


	private void makeAPlayListPressed() {
		// get all the selected options and their file paths and then store them in the file with a each file and its path 
		// seperated from the next one by a a newline character so they can be split at that point.

		playlist = allMedia.getSelectedValuesList();

		// get all the paths of the selected items and put them in a list and make sure that they are not zero

		if(playlist.size() != 0){

			// now open up a jfile chooser and make the user pick a directory and make a folder
			// with the name of the playlist.. The name of the text file is the location chosen and 
			// the name of the playlist


			// open up a option pane and get the name of a playlist.

			playlistName.setVisible(true);


			for(Object o : playlist){
				allPathPlaylist.add(paths.get(o.toString()));
			}

			writeToFile();
		}

	}


	private void button2Pressed() {

		s = allMedia.getSelectedValuesList();

		// now remove all the value that are in the list from the default list model

		for(Object o : s){
			if(l.contains(o)){
				l.removeElement(o);
			}
		}
	}


	private void button1Pressed() {

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


	private void createDirectoryIfNeeded(String directoryName)
	{
		File theDir = new File(directoryName);

		// if the directory does not exist, create it
		if (!theDir.exists())
		{
			//System.out.println("creating directory: " + directoryName);
			theDir.mkdir();
		}
	}

	private void writeToFile() {

		String nameOfPlaylist = GetPlayList.selectedplaylistname;
		//System.out.println(GetPlayList.outputDirectory.getAbsolutePath());
		createDirectoryIfNeeded(GetPlayList.outputDirectory.getAbsolutePath() + File.separator + nameOfPlaylist);

		File file = new File(GetPlayList.outputDirectory.getAbsoluteFile() + File.separator  + nameOfPlaylist + File.separator + "list.txt" );
		try{

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// loop through the list of playlist and their paths and add to file

			for(int i = 0;i<playlist.size();i++){
				//String content = playlist.get(i) + " " + allPathPlaylist.get(i) + "\n";
				String content = allPathPlaylist.get(i) + "\n";
				bw.write(content);
			}
			bw.close();
		}catch(Exception e){

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

			// to ensure that the item is not clicked twice ensure that the value is not adjusting
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

			// make sure that the value is not adjusting so that you dont get the click twice
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
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}



