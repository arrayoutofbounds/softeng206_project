package vamix;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import mediacomponent.VideoPlayer;
import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;


@SuppressWarnings("serial")
public class Gif extends JFrame implements ActionListener {

	private JPanel forInputVideoButton;
	private JPanel forInputVideoLabel;
	private JPanel forOutputButton;
	private JPanel forOutputLabel;
	private JPanel forOutputName;
	private JPanel forGifButton;
	
	
	private JButton chooseVideo;
	private JLabel showVideo;
	private JButton chooseOutput;
	private JLabel showOutput;
	private JTextField chooseName;
	private JButton makeGif;
	private File selectedFile;
	private File outputDirectory;
	
	
	private GifWorker worker;
	private File toOverride;
	
	public Gif(){
		super("Making a GIF image");
		setLayout(new GridLayout(6,1));
		
		
		forInputVideoButton = new JPanel(new FlowLayout());
		forInputVideoButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		forInputVideoLabel = new JPanel(new BorderLayout());
		forInputVideoLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		forOutputButton = new JPanel(new FlowLayout());
		forOutputButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		forOutputLabel = new JPanel(new BorderLayout());
		forOutputLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		forOutputName = new JPanel(new FlowLayout());
		forOutputName.setBorder(new EmptyBorder(10, 10, 10, 10));
		forGifButton = new JPanel(new FlowLayout());
		forGifButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		
		chooseVideo = new JButton("Choose Video");
		chooseVideo.addActionListener(this);
		showVideo = new JLabel("Video Chosen: ");
		chooseOutput = new JButton("Choose output directory");
		chooseOutput.addActionListener(this);
		showOutput = new JLabel("Output destination: ");
		chooseName = new JTextField(5000);
		chooseName.setText("");
		chooseName.setColumns(40);
		makeGif = new JButton("Make the GIF");
		makeGif.setEnabled(false);
		makeGif.addActionListener(this);
		
		
		forInputVideoButton.add(chooseVideo);
		forInputVideoLabel.add(showVideo,BorderLayout.WEST);
		forOutputButton.add(chooseOutput);
		forOutputLabel.add(showOutput,BorderLayout.WEST);
		forOutputName.add(chooseName);
		forGifButton.add(makeGif);
		
		add(forInputVideoButton);
		add(forInputVideoLabel);
		add(forOutputButton);
		add(forOutputLabel);
		add(forOutputName);
		add(forGifButton);
		
		
		
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == chooseVideo){
			JFileChooser fileChooser = new JFileChooser();
			
			fileChooser.setCurrentDirectory(new java.io.File("."));

			fileChooser.setDialogTitle("Choose Video File");

			fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());

			// Allows files to be chosen only. Make sure they are video files in the extract part
			// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			fileChooser.setFileFilter(SwingFileFilterFactory.newVideoFileFilter());
			fileChooser.setAcceptAllFileFilterUsed(false);
			int returnValue = fileChooser.showOpenDialog(Gif.this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				showVideo.setText("Video chosen: " + selectedFile.getName());
				InvalidCheck i = new InvalidCheck();
				boolean isValidMedia = i.invalidCheck(fileChooser.getSelectedFile().getAbsolutePath());
				
				if (!isValidMedia) {
					JOptionPane.showMessageDialog(Gif.this, "You have specified an invalid file.", "Error", JOptionPane.ERROR_MESSAGE);
					makeGif.setEnabled(false);
					return;
				}else{
					makeGif.setEnabled(true);
				}
				
			}
		}
		
		if(e.getSource() == chooseOutput){

			JFileChooser outputChooser = new JFileChooser();
			outputChooser.setCurrentDirectory(new java.io.File("."));
			outputChooser.setDialogTitle("Choose a directory to output to");

			outputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int returnValue = outputChooser.showOpenDialog(Gif.this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				outputDirectory = outputChooser.getSelectedFile().getAbsoluteFile();
				//JOptionPane.showMessageDialog(ReplaceAudio.this, "Your file will be extracted to " + outputDirectory);
				showOutput.setText("Output Destination: " + outputDirectory);
			}
		}
		
		if(e.getSource() == makeGif){
			
			// check that that everything has been filled
			boolean carryOn = true;
			
			if((chooseName.getText().equals(""))||(selectedFile == null)||(outputDirectory ==null)){
				JOptionPane.showMessageDialog(Gif.this, "Sorry you must fill all fields before carrying on!");
				carryOn = false;
			}
			
			if(carryOn){
				JOptionPane.showMessageDialog(Gif.this,"WARNING! If the video is too big, there may be problems in making the gif. Extract a part of the video by features given and then make the gif.");
				// call the swing worker and make the gif
				
				
				

				boolean override = false;
				
				File propFile = new File(outputDirectory,chooseName.getText());
				if(propFile.exists()){
					toOverride = propFile;
					// ask the user if they want to overrride or not. If not then they must change the name of their file
					String[] options = {"Yes,Override!","No! Do not override!"};
					int code = JOptionPane.showOptionDialog(Gif.this, 
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
						worker = new GifWorker();
						worker.execute();
					}else{
						JOptionPane.showMessageDialog(Gif.this, "Please choose another name to carry on extracting!");
					}
				}else{

					worker = new GifWorker();
					makeGif.setEnabled(false);
					worker.execute();
				}
				
				
				
				
				
				
				
				//worker = new GifWorker();
				//worker.execute();
				
				
			}
			
			
			
		}
		
		
	}
	
	private class GifWorker extends SwingWorker<Integer, Void>{

		@Override
		protected Integer doInBackground() throws Exception {
			
			
			String name = chooseName.getText();
			
			if(!name.contains(".gif")){
				name = name + ".gif";
			}
			
			int exitValue = 1;
			
			
			String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -pix_fmt rgb24 " + outputDirectory.getAbsolutePath() + File.separator + name;
			
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			
			
			
			Process process = builder.start();
			process.waitFor();
			
			
			exitValue = process.exitValue();
	
			return exitValue;
		}

		@Override
		protected void done() {
			makeGif.setEnabled(true);
			try {
				int i = get();
				
				if(i==0){
					JOptionPane.showMessageDialog(Gif.this, "The GIF has successfully been made!");
				}else{
					JOptionPane.showMessageDialog(Gif.this, "The making of the GIF was unsucessful!");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
}
