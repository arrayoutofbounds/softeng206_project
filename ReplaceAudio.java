import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
	
	
	@SuppressWarnings("serial")
	public class ReplaceAudio extends JFrame implements ActionListener {
	
		private JPanel forInputButtons;
		private JPanel forInputValues1;
		private JPanel forInputValues2;
		private JPanel forProgressbar;
		private JPanel forChoosingDirectory;
		private JPanel forDisplayingOutputDirectory;
		private JPanel forOutputName;
		private JPanel forReplaceButton;
	
		private JButton inputVideo;
		private JButton inputAudio;
	
		private JLabel showInputVideo;
		private JLabel showInputAudio;
	
		private JButton chooseDirectory;
	
		private JLabel showOutputDirectory;
		private JButton replace;
		private File selectedFile = null;
		private File selectedFile2 = null;
		private File outputDirectory = null;
		private JButton overlay;
		private JProgressBar progressBar;
	
	
		private JLabel output;
		private JTextField outputName;
	
		private ReplaceWorker worker;
		private OverlayWorker worker2;
		private File toOverride;
	
	
		public ReplaceAudio(){
	
			super("Replace/Overlay audio of a video");
	
			setLayout(new GridLayout(8,1));
	
			forInputButtons = new JPanel(new FlowLayout());
			forInputButtons.setBorder(new EmptyBorder(10, 10, 10, 10));
	
			forInputValues1 = new JPanel(new BorderLayout());
			forInputValues1.setBorder(new EmptyBorder(10, 10, 10, 10));
			forInputValues2 = new JPanel(new BorderLayout());
			forInputValues2.setBorder(new EmptyBorder(10, 10, 10, 10));
	
			forOutputName = new JPanel(new FlowLayout());
			
			forProgressbar = new JPanel(new FlowLayout());
			forProgressbar.setBorder(new EmptyBorder(10, 10, 10, 10));
	
			forChoosingDirectory = new JPanel(new FlowLayout());
			forChoosingDirectory.setBorder(new EmptyBorder(10, 10, 10, 10));
	
			forDisplayingOutputDirectory = new JPanel(new BorderLayout());
			forDisplayingOutputDirectory.setBorder(new EmptyBorder(10, 10, 10, 10));
	
			forReplaceButton = new JPanel(new FlowLayout());
			forReplaceButton.setBorder(new EmptyBorder(10, 10, 10, 10));
	
			inputVideo = new JButton("Choose input Video");
			inputVideo.addActionListener(this);
			inputAudio = new JButton("Choose input Audio");
			inputAudio.addActionListener(this);
	
			showInputVideo = new JLabel("Input Video: ");
			showInputAudio = new JLabel("Input Audio: ");
	
			chooseDirectory = new JButton("Choose Output Destination");
			chooseDirectory.addActionListener(this);
	
			showOutputDirectory = new JLabel("Output Destination: ");
	
			replace = new JButton("Replace Audio");
			overlay = new JButton("Overlay Audio");
			replace.addActionListener(this);
			overlay.addActionListener(this);
	
			output = new JLabel("Enter name for output file: ");
			outputName = new JTextField(5000);
			outputName.setText("");
			outputName.setColumns(40);
	
			forInputButtons.add(inputVideo);
			forInputButtons.add(inputAudio);
	
			forInputValues1.add(showInputVideo,BorderLayout.WEST);
			forInputValues2.add(showInputAudio,BorderLayout.WEST);
	
			forChoosingDirectory.add(chooseDirectory);
	
			forDisplayingOutputDirectory.add(showOutputDirectory,BorderLayout.WEST);
	
			forReplaceButton.add(replace);
			forReplaceButton.add(overlay);
	
			forOutputName.add(output);
			forOutputName.add(outputName);
			
			progressBar = new JProgressBar();
			progressBar.setStringPainted(true);
			forProgressbar.add(progressBar);
	
			add(forInputButtons);
			add(forInputValues1);
			add(forInputValues2);
			add(forChoosingDirectory);
			add(forDisplayingOutputDirectory);
			add(forOutputName);
			add(forReplaceButton);
			add(forProgressbar);
	
		}
	
	
		@Override
		public void actionPerformed(ActionEvent e) {
	
	
			// open jfilechooser for video
	
			if(e.getSource() == inputVideo){
				JFileChooser fileChooser = new JFileChooser();
	
				fileChooser.setCurrentDirectory(new java.io.File("."));
	
				fileChooser.setDialogTitle("Choose Video File");
	
				fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());
	
				// Allows files to be chosen only. Make sure they are video files in the extract part
				// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	
				fileChooser.setFileFilter(SwingFileFilterFactory.newVideoFileFilter());
				fileChooser.setAcceptAllFileFilterUsed(false);
				int returnValue = fileChooser.showOpenDialog(ReplaceAudio.this);
	
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChooser.getSelectedFile();
					showInputVideo.setText("Input Video: " + selectedFile.getName());
				}
	
			}
	
			if(e.getSource() == inputAudio){
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new java.io.File("."));
	
				fileChooser.setDialogTitle("Choose Audio File");
	
				fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newAudioFileFilter());
	
				// Allows files to be chosen only. Make sure they are video files in the extract part
				// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	
				fileChooser.setFileFilter(SwingFileFilterFactory.newAudioFileFilter());
				fileChooser.setAcceptAllFileFilterUsed(false);
				int returnValue = fileChooser.showOpenDialog(ReplaceAudio.this);
	
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile2 = fileChooser.getSelectedFile();
					showInputAudio.setText("Input Audio: " + selectedFile2.getName());
				}
			}
	
	
			if(e.getSource() == chooseDirectory){
	
				JFileChooser outputChooser = new JFileChooser();
				outputChooser.setCurrentDirectory(new java.io.File("."));
				outputChooser.setDialogTitle("Choose a directory to output to");
	
				outputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	
				int returnValue = outputChooser.showOpenDialog(ReplaceAudio.this);
	
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					outputDirectory = outputChooser.getSelectedFile().getAbsoluteFile();
					//JOptionPane.showMessageDialog(ReplaceAudio.this, "Your file will be extracted to " + outputDirectory);
					showOutputDirectory.setText("Output Directory: " + outputDirectory);
				}
			}
	
			if(e.getSource() == replace) {
				//do the whole swingworker replacement thing
				boolean carryOn = true;
				// do checks that everything has been entered
				if((selectedFile2 == null)||((selectedFile == null)||(outputDirectory == null)||(outputName.getText().equals("")))){
					JOptionPane.showMessageDialog(ReplaceAudio.this,"Some fields are empty! Please fill all the fields to procced!");
					carryOn = false;
				}
				
				if(carryOn){
					String a = outputName.getText();
					boolean override = false;
					if(!a.contains(".mp4")){
						a = a + ".mp4";
					}
					File propFile = new File(outputDirectory,a);
					
					if(propFile.exists()){
						toOverride = propFile;
						String[] options = {"Yes,Override!","No! Do not override!"};
						int code = JOptionPane.showOptionDialog(ReplaceAudio.this, 
								"This file already exists! Would you like to override it?", 
								"Option Dialog Box", 0, JOptionPane.QUESTION_MESSAGE, 
								null, options, "Yes,Override!");
						if (code == 0) {
							// Allow override
							override = true;
						} else if(code == 1) {
							override = false;
						}

						if(override){
							toOverride.delete();
							worker = new ReplaceWorker();
							replace.setEnabled(false);
							overlay.setEnabled(false);
							worker.execute();
						}else{
							JOptionPane.showMessageDialog(ReplaceAudio.this, "Please choose another name to carry on extracting!");
						}
					}else{
						worker = new ReplaceWorker();
						replace.setEnabled(false);
						overlay.setEnabled(false);
						worker.execute();
					}
					
				}
		}
			
			if(e.getSource() == overlay){
				boolean carryOn = true;
				// do checks that everything has be entered
				if((selectedFile2 == null)||((selectedFile == null)||(outputDirectory == null)||(outputName.getText().equals("")))){
					JOptionPane.showMessageDialog(ReplaceAudio.this,"Some fields are empty! Please fill all the fields to procced!");
					carryOn = false;
				}
				
				if(carryOn){
					
					String a = outputName.getText();
					boolean override = false;
					if(!a.contains(".mp4")){
						a = a + ".mp4";
					}
					File propFile = new File(outputDirectory,a);
					
					if(propFile.exists()){
						toOverride = propFile;
						String[] options = {"Yes,Override!","No! Do not override!"};
						int code = JOptionPane.showOptionDialog(ReplaceAudio.this, 
								"This file already exists! Would you like to override it?", 
								"Option Dialog Box", 0, JOptionPane.QUESTION_MESSAGE, 
								null, options, "Yes,Override!");
						if (code == 0) {
							// Allow override
							override = true;
						} else if(code == 1) {
							override = false;
						}

						if(override){
							toOverride.delete();
							worker2 = new OverlayWorker();
							replace.setEnabled(false);
							overlay.setEnabled(false);
							worker2.execute();
						}else{
							JOptionPane.showMessageDialog(ReplaceAudio.this, "Please choose another name to carry on extracting!");
						}
					}else{
						worker2 = new OverlayWorker();
						replace.setEnabled(false);
						overlay.setEnabled(false);
						replace.setEnabled(false);
						overlay.setEnabled(false);
						worker2.execute();
					}

					//worker2 = new OverlayWorker();
					//worker2.execute();
				}
			}
	
	}
	
	
	private class ReplaceWorker extends SwingWorker<Integer, Integer> {
		
		
		@Override
		protected Integer doInBackground() throws Exception {

			String name = outputName.getText();
			
			if(!name.contains(".mp4")){
				name = name + ".mp4";
			}
			
			// Get length of the video and audio and find which is the smallest
			MediaLengthWorker LengthWorker = new MediaLengthWorker(selectedFile.getAbsolutePath());
			LengthWorker.execute();
			int length1 = LengthWorker.get();
			
			LengthWorker = new MediaLengthWorker(selectedFile2.getAbsolutePath());
			LengthWorker.execute();
			int length2 = LengthWorker.get();
			boolean haveLengths = true;
			if (length1 == -1 || length2 == -1) {
				// Couldn't get length for at least one of the files
				// We will use an indeterminate progress bar
				haveLengths = false;
				progressBar.setStringPainted(false);
				progressBar.setIndeterminate(true);
			}
			int minLength = 0;
			if (length1 <= length2) {
				minLength = length1;
			} else if (length2 <= length1) {
				minLength = length2;
			}
			
			
			int exitValue = 1;
			String cmd = "/usr/bin/avconv -i " + "" +selectedFile2.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -i " + "" + selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -map 0:0 -map 1:0 -acodec copy -vcodec copy -shortest " + outputDirectory + File.separator + name;
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd).redirectErrorStream(true);
			
			Process process = builder.start();
			
			InputStream out = process.getInputStream();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
			stdout = new BufferedReader(new InputStreamReader(out));
			
			String line = null;
			Pattern p = Pattern.compile("(.*time=)(\\d+\\.\\d+)(.*)");

			while ((line = stdout.readLine()) != null) {
				Matcher m = p.matcher(line);
				if (m.matches()) {
					if (haveLengths) {
						publish((int)(Float.parseFloat(m.group(2)) / minLength * 100));
					}
				}
			}
			
			process.waitFor();
			exitValue = process.exitValue();
	
			return exitValue;
		}

		@Override
		protected void done() {
			progressBar.setIndeterminate(false);
			replace.setEnabled(true);
			overlay.setEnabled(true);
			try {
				int i = get();
				
				if(i == 0){
					progressBar.setValue(100);
					JOptionPane.showMessageDialog(ReplaceAudio.this,"The replacement was successful!");
				}else{
					JOptionPane.showMessageDialog(ReplaceAudio.this,"Sorry! The replacement was unsuccessful!");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		protected void process(List<Integer> list) {
			for (Integer i : list) {
				progressBar.setValue(i);
			}
		}

	
	}
	
	
private class OverlayWorker extends SwingWorker<Integer, Integer>{
		
		
		@Override
		protected Integer doInBackground() throws Exception {

			String name = outputName.getText();
			
			if(!name.contains(".mp4")){
				name = name + ".mp4";
			}
			
			// Get length of the video and audio and find which is the largest
						MediaLengthWorker LengthWorker = new MediaLengthWorker(selectedFile.getAbsolutePath());
						LengthWorker.execute();
						int length1 = LengthWorker.get();
						
						LengthWorker = new MediaLengthWorker(selectedFile2.getAbsolutePath());
						LengthWorker.execute();
						int length2 = LengthWorker.get();
						boolean haveLengths = true;
						if (length1 == -1 || length2 == -1) {
							// Couldn't get length for at least one of the files
							// We will use an indeterminate progress bar
							haveLengths = false;
							progressBar.setStringPainted(false);
							progressBar.setIndeterminate(true);
						}
						int maxLength = 0;
						if (length1 >= length2) {
							maxLength = length1;
						} else if (length2 >= length1) {
							maxLength = length2;
						}
			
			
			int exitValue = 1;
			
			String cmd = "/usr/bin/avconv -i " + selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -i " + selectedFile2.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -filter_complex amix=inputs=2 -strict experimental " + outputDirectory + File.separator + name;
			
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd).redirectErrorStream(true);
			
			
			Process process = builder.start();
			
			InputStream out = process.getInputStream();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
			stdout = new BufferedReader(new InputStreamReader(out));
			
			String line = null;
			Pattern p = Pattern.compile("(.*time=)(\\d+\\.\\d+)(.*)");

			while ((line = stdout.readLine()) != null) {
				Matcher m = p.matcher(line);
				if (m.matches()) {
					if (haveLengths) {
						publish((int)(Float.parseFloat(m.group(2)) / maxLength * 100));
					}
				}
			}
			
			process.waitFor();
			
			exitValue = process.exitValue();
	
			return exitValue;
		}

		@Override
		protected void done() {
			replace.setEnabled(true);
			overlay.setEnabled(true);
			try {
				int i = get();
				
				if(i == 0){
					progressBar.setValue(100);
					JOptionPane.showMessageDialog(ReplaceAudio.this,"The overlaying was successful!");
				}else{
					JOptionPane.showMessageDialog(ReplaceAudio.this,"Sorry! The overlaying was unsuccessful!");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		@Override
		protected void process(List<Integer> list) {
			for (Integer i : list) {
				progressBar.setValue(i);
			}
		}

	
	}
	
	
	
	}
	

	
