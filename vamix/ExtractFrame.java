package vamix;
import java.awt.BorderLayout;
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
public class ExtractFrame extends JFrame implements ActionListener {

	private JPanel fileSelectionPanel;
	private JButton chooseInputFileButton;
	private JPanel DisplayChosenFilePanel;
	private JPanel getOutputNamePanel;
	private JPanel allowExtractPanel;
	private JButton chooseOutputDirectoryButton;
	private JLabel showInputFileLabel;
	private JLabel outputFilenameLabel;
	private JTextField selectOutputNameField;
	private JButton extractButton;
	private JProgressBar extractProgressBar;
	private File selectedFile;
	private File outputDirectory;
	private JPanel showprogressbar;
	private JPanel showOutputDirectoryButton;
	File toOverride;
	
	
	private JPanel showDestination;
	private JLabel showing;
	
	public ExtractFrame(){
		super("Extract Audio From Video");

		setLayout(new GridLayout(7,1));
		
		fileSelectionPanel = new JPanel();
		chooseInputFileButton = new JButton("Choose File");
		DisplayChosenFilePanel = new JPanel();
		getOutputNamePanel = new JPanel();
		allowExtractPanel = new JPanel();
		showprogressbar = new JPanel();
		showOutputDirectoryButton = new JPanel();
		showDestination = new JPanel();
		
		chooseOutputDirectoryButton = new JButton("Choose output directory");
		showInputFileLabel  = new JLabel("Input file: ");
		outputFilenameLabel = new JLabel("Output filename: ");
		selectOutputNameField = new JTextField(1000);
		selectOutputNameField.setText("");
		selectOutputNameField.setColumns(30);
		extractButton = new JButton("Extract");
		extractProgressBar = new JProgressBar();
		extractProgressBar.setStringPainted(true);
		showing = new JLabel("Output Destination: ");
		
		fileSelectionPanel.setLayout(new FlowLayout());
		DisplayChosenFilePanel.setLayout(new BorderLayout());
		getOutputNamePanel.setLayout(new FlowLayout());
		allowExtractPanel.setLayout(new FlowLayout());
		showDestination.setLayout(new BorderLayout());
		
		
		
		fileSelectionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		DisplayChosenFilePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		getOutputNamePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		allowExtractPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		showprogressbar.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		showOutputDirectoryButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		showDestination.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		fileSelectionPanel.add(chooseInputFileButton);
		DisplayChosenFilePanel.add(showInputFileLabel,BorderLayout.WEST);
		getOutputNamePanel.add(outputFilenameLabel);
		getOutputNamePanel.add(selectOutputNameField);
		allowExtractPanel.add(extractButton);
		showprogressbar.add(extractProgressBar);
		showOutputDirectoryButton.add(chooseOutputDirectoryButton);
		showDestination.add(showing,BorderLayout.WEST);
		
		add(fileSelectionPanel);
		add(DisplayChosenFilePanel);
		add(getOutputNamePanel);
		add(showOutputDirectoryButton);
		add(showDestination);
		add(allowExtractPanel);
		add(showprogressbar);
		
		chooseInputFileButton.addActionListener(this);
		chooseOutputDirectoryButton.addActionListener(this);
		extractButton.addActionListener(this);
		extractButton.setEnabled(false);
		
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == chooseInputFileButton) {
			// Open a file chooser and only allow a video file to be chosen
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new java.io.File("."));
			fileChooser.setDialogTitle("Choose Video File");
			fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());
			// Allows files to be chosen only. Make sure they are video files in the extract part
			// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setFileFilter(SwingFileFilterFactory.newVideoFileFilter());
			fileChooser.setAcceptAllFileFilterUsed(false);
	        int returnValue = fileChooser.showOpenDialog(ExtractFrame.this);
	        
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	          selectedFile = fileChooser.getSelectedFile();
	          showInputFileLabel.setText("Input file: " + selectedFile.getName());
	          InvalidCheck i = new InvalidCheck();
			  boolean isValidMedia = i.invalidCheck(selectedFile.getAbsolutePath());
			  
			  if (!isValidMedia) {
					JOptionPane.showMessageDialog(ExtractFrame.this, "You have specified an invalid file.", "Error", JOptionPane.ERROR_MESSAGE);
					extractButton.setEnabled(false);
					return;
				}else{
					extractButton.setEnabled(true);
				}
	        }
	        
		}
		
		
		if (e.getSource() == chooseOutputDirectoryButton) {
			JFileChooser outputChooser = new JFileChooser();
			outputChooser.setCurrentDirectory(new java.io.File("."));
			outputChooser.setDialogTitle("Choose a directory to output to");
			
			outputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int returnValue = outputChooser.showOpenDialog(ExtractFrame.this);
			
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				outputDirectory = outputChooser.getSelectedFile().getAbsoluteFile();
				showing.setText("Output Destination: " + outputDirectory);
				//JOptionPane.showMessageDialog(ExtractFrame.this, "Your file will be extracted to " + outputDirectory);
			}
		}
		
		
		if (e.getSource() == extractButton) {
			// TODO Get the file and pass it through bash and make sure it is a video file
			
			
			// first check that a file is chosen
			// then check that a destination was chosen
			// then check that the user entered a name that was .mp3
			// then start the download
		
			if ((selectedFile == null) && (selectOutputNameField.getText().equals(""))) {
				JOptionPane.showMessageDialog(ExtractFrame.this, "Please choose a file and then an output destination!", "Warning", JOptionPane.WARNING_MESSAGE);
				
			} else if(selectedFile == null){
				JOptionPane.showMessageDialog(ExtractFrame.this, "Please choose a file to extract from!", "Warning", JOptionPane.WARNING_MESSAGE);
				
			} else if(selectOutputNameField.getText().equals("")) {
				JOptionPane.showMessageDialog(ExtractFrame.this, "Please choose a file to output extracted file to!", "Warning", JOptionPane.WARNING_MESSAGE);
				
			} else {
				// Allow output to happen
				String a = selectOutputNameField.getText();
				
				// Disable start button
				extractButton.setEnabled(false);
				
				// TODO allow files without .mp3 but automatically append it
				if(!a.toLowerCase().contains(".mp3")) {
					selectOutputNameField.setText( a + ".mp3");
				}
				
			// Do the extraction
			SwingWorker<Integer, Integer > worker = new SwingWorker<Integer, Integer>() {
				int exitValue = 1;
				@Override
				protected Integer doInBackground() throws Exception {
					try {
						
						// Process to get length of input file
						ProcessBuilder timeBuilder = new ProcessBuilder("/usr/bin/avconv", "-i", "" + selectedFile + "", "| grep Duration").redirectErrorStream(true);
						Process timeProcess = timeBuilder.start();
						InputStream out = timeProcess.getInputStream();
						BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
						
						Pattern p = Pattern.compile("(.*Duration: )(\\d{2}?):(\\d{2}?):(\\d{2}?)(.*)");
						
						String line = null;
						String hoursString = null;
						String minutesString = null;
						String secondsString = null;

						while ((line = stdout.readLine()) != null ) {
							Matcher m = p.matcher(line);
							if (m.matches()) {
								hoursString = m.group(2);
								minutesString = m.group(3);
								secondsString = m.group(4);
							}
						}
						
						boolean gotTime = false;
						int totalSeconds = 0;
						
						if (hoursString != null) {
							gotTime = true;
							totalSeconds = (Integer.parseInt(hoursString) * 3600) + (Integer.parseInt(minutesString) * 60) + Integer.parseInt(secondsString);
						} else {
							extractProgressBar.setStringPainted(false);
							extractProgressBar.setIndeterminate(true);
						}
						
						
						// Process to extract audio
						ProcessBuilder builder = new ProcessBuilder("/usr/bin/avconv", "-i","" + selectedFile, "-vn", "" + selectOutputNameField.getText()).redirectErrorStream(true);
						builder.directory(outputDirectory);
						
						
						Process process = builder.start();
						
						out = process.getInputStream();
						stdout = new BufferedReader(new InputStreamReader(out));
						line = null;
						
						p = Pattern.compile("(.*time=)(\\d+\\.\\d+)(.*)");

						while ((line = stdout.readLine()) != null) {
							Matcher m = p.matcher(line);
							if (m.matches()) {
								if (gotTime) {
									publish((int)(Float.parseFloat(m.group(2)) / totalSeconds * 100));
								}
							}
						}
						
						process.waitFor();
						exitValue = process.exitValue();
						
					} catch(Exception e1) {
						
					}
					return exitValue;
				}
				@Override
				protected void done() {
					extractProgressBar.setIndeterminate(false);
					extractButton.setEnabled(true);
						try {
							int i = get();
							if (i == 0) {
								extractProgressBar.setValue(100);
								JOptionPane.showMessageDialog(ExtractFrame.this,"Extraction complete!");

							} else {
								JOptionPane.showMessageDialog(ExtractFrame.this,"Extraction failed!", "ERROR", JOptionPane.ERROR_MESSAGE);
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
						extractProgressBar.setValue(i);
					}
				}
			};
			
			
			// check if the input name of the file exists or not in the chosen directory
			// if it does then we have a clash and ask user if they want to override or not
			// if they want to override then delete the existing file with the same name 
			// and make then carry out the swingworker, else do NOT carry out the swingworker.
			
			boolean override = false;
			
			File propFile = new File(outputDirectory,a);
			if(propFile.exists()) {
				toOverride = propFile;
				// ask the user if they want to overrride or not. If not then they must change the name of their file
				String[] options = {"Yes,Override!","No! Do not override!"};
				int code = JOptionPane.showOptionDialog(ExtractFrame.this, 
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
					worker.execute();
				}else{
					JOptionPane.showMessageDialog(ExtractFrame.this, "Please choose another name to carry on extracting!");
					extractButton.setEnabled(true);
				}
			} else {
				worker.execute();
			}
			
			}
		}
	}

	
}
