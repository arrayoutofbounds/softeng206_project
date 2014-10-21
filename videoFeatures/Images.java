package videoFeatures;
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
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import vamix.InvalidCheck;


@SuppressWarnings("serial")
public class Images extends JFrame implements ActionListener{

	private JPanel forInputVideoButton;
	private JPanel forInputVideoLabel;
	private JPanel forOutputButton;
	private JPanel forOutputLabel;
	private JPanel forImagesButton;
	
	private JButton chooseVideo;
	private JLabel showVideo;
	private JButton chooseOutput;
	private JLabel showOutput;
	private JButton makeImages;
	private File selectedFile;
	private File outputDirectory;
	
	private ImagesWorker worker;
	
	private JProgressBar progress;
	private JPanel forBar;
	
	public Images(){
		
		super("Get Images from a video file");
		
		setLayout(new GridLayout(6,1));
		
		forInputVideoButton = new JPanel(new FlowLayout());
		forInputVideoButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		forInputVideoLabel = new JPanel(new BorderLayout());
		forInputVideoLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		forOutputButton = new JPanel(new FlowLayout());
		forOutputButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		forOutputLabel = new JPanel(new BorderLayout());
		forOutputLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		forImagesButton = new JPanel(new FlowLayout());
		forImagesButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		forBar = new JPanel(new FlowLayout());
		forBar.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		
		chooseVideo = new JButton("Choose Video");
		chooseVideo.addActionListener(this);
		showVideo = new JLabel("Video Chosen: ");
		chooseOutput = new JButton("Choose output directory");
		chooseOutput.addActionListener(this);
		showOutput = new JLabel("Output destination: ");
		makeImages = new JButton("Get the images");
		makeImages.addActionListener(this);
		makeImages.setEnabled(false);
		progress = new JProgressBar();
		
		
		forInputVideoButton.add(chooseVideo);
		forInputVideoLabel.add(showVideo,BorderLayout.WEST);
		forOutputButton.add(chooseOutput);
		forOutputLabel.add(showOutput,BorderLayout.WEST);
		forImagesButton.add(makeImages);
		forBar.add(progress);
		
		add(forInputVideoButton);
		add(forInputVideoLabel);
		add(forOutputButton);
		add(forOutputLabel);
		add(forImagesButton);
		add(forBar);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == chooseVideo){
			chooseVideoPressed();
		}
		
		if(e.getSource() == chooseOutput){
			chooseOutputPressed();
		}
		
		if(e.getSource() == makeImages){
			makeImagesPressed();
		}
	}
	
	private void makeImagesPressed() {
		boolean carryOn = true;
		
		if((selectedFile == null)||(outputDirectory ==null)){
			JOptionPane.showMessageDialog(Images.this, "Sorry you must fill all fields before carrying on!");
			carryOn = false;
		}
		
		if(carryOn){
			JOptionPane.showMessageDialog(Images.this,"WARNING! If the video is too big, there will be a LOT if images!");
			// call the swing worker and make the images
			
			worker = new ImagesWorker();
			makeImages.setEnabled(false);
			worker.execute();
		}
	}

	private void chooseOutputPressed() {

		JFileChooser outputChooser = new JFileChooser();
		outputChooser.setCurrentDirectory(new java.io.File("."));
		outputChooser.setDialogTitle("Choose a directory to output to");

		outputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnValue = outputChooser.showOpenDialog(Images.this);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			outputDirectory = outputChooser.getSelectedFile().getAbsoluteFile();
			//JOptionPane.showMessageDialog(ReplaceAudio.this, "Your file will be extracted to " + outputDirectory);
			showOutput.setText("Output Destination: " + outputDirectory);
		}
	}

	private void chooseVideoPressed() {

		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setCurrentDirectory(new java.io.File("."));

		fileChooser.setDialogTitle("Choose Video File");

		fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());

		// Allows files to be chosen only. Make sure they are video files in the extract part
		// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		fileChooser.setFileFilter(SwingFileFilterFactory.newVideoFileFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);
		int returnValue = fileChooser.showOpenDialog(Images.this);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
			showVideo.setText("Video chosen: " + selectedFile.getName());
			InvalidCheck i = new InvalidCheck();
			boolean isValidMedia = i.invalidCheck(fileChooser.getSelectedFile().getAbsolutePath());
			
			if (!isValidMedia) {
				JOptionPane.showMessageDialog(Images.this, "You have specified an invalid file.", "Error", JOptionPane.ERROR_MESSAGE);
				makeImages.setEnabled(false);
				return;
			}else{
				makeImages.setEnabled(true);
			}
		}
	}

	private class ImagesWorker extends SwingWorker<Integer,Void>{

		@Override
		protected Integer doInBackground() throws Exception {
			
			String name = "image-%3d.jpeg";
			int exitValue = 1;
			
			
			String cmd = "/usr/bin/avconv -i " + selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -r 1 " + outputDirectory.getAbsolutePath().replaceAll(" ", "\\\\ ") + File.separator +name;
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			
			Process process = builder.start();
			process.waitFor();
			
			exitValue = process.exitValue();
	
			return exitValue;

		}

		@Override
		protected void done() {
			makeImages.setEnabled(true);
			try {
				int i = get();
				if(i == 0){
					JOptionPane.showMessageDialog(Images.this, "Images created!");
				}else{
					JOptionPane.showMessageDialog(Images.this, "Sorry! Failed to create images");
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
