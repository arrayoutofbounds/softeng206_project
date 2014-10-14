package videoFeatures;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import vamix.InvalidCheck;

public class VideoFilter extends JFrame implements ActionListener {


	private JPanel chooseInput;
	private JPanel showInput;
	private JPanel chooseOutput;
	private JPanel showOutput;
	private JPanel nameOutput;
	private JPanel startProcess;
	private JPanel showProgress;
	private JPanel chooseFilters;



	private JButton chooseInputButton;
	private JLabel showingInput;
	private JButton chooseOutputButton;
	private JLabel showingOutput;
	private JTextField field;
	private JButton start;
	private JProgressBar progress;
	private JLabel selectFilterlabel;
	private JComboBox selectFilter;
	private JLabel nameOutputLabel;

	private String[] inComboBox = {"Rotate 90 degrees","Rotate 270 degrees","Negate and vertical flip","Negate","Vertical flip","Blur (barely)","Blur (noticable)","Blur (A lot!)","Scale to 320x240 (240p)","Scale to 480x360 (360p)","Scale to 640x480 (480p)","Scale to 1280x720 (720p)","Scale to 1920x1080 (1080p)"};
	private File selectedFile;
	private File outputDirectory;
	private File toOverride;
	private FilterWorker worker;


	public VideoFilter(){
		super("Add Video Filter");
		setLayout(new GridLayout(8,1));

		chooseInput = new JPanel(new FlowLayout());
		chooseInput.setBorder(new EmptyBorder(10, 10, 10, 10));

		showInput = new JPanel(new BorderLayout());
		showInput.setBorder(new EmptyBorder(10, 10, 10, 10));

		chooseOutput  = new JPanel(new FlowLayout());
		chooseOutput.setBorder(new EmptyBorder(10, 10, 10, 10));

		showOutput = new JPanel(new BorderLayout());
		showOutput.setBorder(new EmptyBorder(10, 10, 10, 10));

		nameOutput = new JPanel(new BorderLayout());
		nameOutput.setBorder(new EmptyBorder(10, 10, 10, 10));

		startProcess = new JPanel(new FlowLayout());
		startProcess.setBorder(new EmptyBorder(10, 10, 10, 10));

		showProgress = new JPanel(new FlowLayout());
		showProgress.setBorder(new EmptyBorder(10, 10, 10, 10));

		chooseFilters = new JPanel(new BorderLayout());
		chooseFilters.setBorder(new EmptyBorder(10, 10, 10, 10));


		chooseInputButton = new JButton("Choose Input Video File");
		showingInput = new JLabel("Input File:");
		chooseOutputButton = new JButton("Choose Destination of Output");
		showingOutput = new JLabel("Output destination:");
		field = new JTextField(50);
		field.setColumns(50);
		start = new JButton("Add Filter");
		progress = new JProgressBar();
		selectFilterlabel = new JLabel("Select Filter ");
		nameOutputLabel = new JLabel("Name Output:");



		chooseInputButton.addActionListener(this);
		chooseOutputButton.addActionListener(this);
		start.addActionListener(this);



		selectFilter = new JComboBox(inComboBox);
		selectFilter.setEditable(false);


		chooseInput.add(chooseInputButton);
		showInput.add(showingInput,BorderLayout.WEST);
		chooseOutput.add(chooseOutputButton);
		showOutput.add(showingOutput,BorderLayout.WEST);
		nameOutput.add(nameOutputLabel,BorderLayout.WEST);
		nameOutput.add(field);
		startProcess.add(start);
		showProgress.add(progress);
		chooseFilters.add(selectFilterlabel,BorderLayout.WEST);
		chooseFilters.add(selectFilter,BorderLayout.CENTER);




		add(chooseInput);
		add(showInput);
		add(chooseOutput);
		add(showOutput);
		add(nameOutput);
		add(chooseFilters);
		add(startProcess);
		add(showProgress);



	}


	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == chooseInputButton){
			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setCurrentDirectory(new java.io.File("."));

			fileChooser.setDialogTitle("Choose Video File");

			fileChooser.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());

			// Allows files to be chosen only. Make sure they are video files in the extract part
			// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			fileChooser.setFileFilter(SwingFileFilterFactory.newVideoFileFilter());
			fileChooser.setAcceptAllFileFilterUsed(false);
			int returnValue = fileChooser.showOpenDialog(VideoFilter.this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				showingInput.setText("Input File: " + selectedFile.getName());
				InvalidCheck i = new InvalidCheck();
				boolean isValidMedia = i.invalidCheck(fileChooser.getSelectedFile().getAbsolutePath());

				if (!isValidMedia) {
					JOptionPane.showMessageDialog(VideoFilter.this, "You have specified an invalid file.", "Error", JOptionPane.ERROR_MESSAGE);
					start.setEnabled(false);
					return;
				}else{
					start.setEnabled(true);
				}

			}
		}

		if(e.getSource() == chooseOutputButton){

			JFileChooser outputChooser = new JFileChooser();
			outputChooser.setCurrentDirectory(new java.io.File("."));
			outputChooser.setDialogTitle("Choose a directory to output to");

			outputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int returnValue = outputChooser.showOpenDialog(VideoFilter.this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				outputDirectory = outputChooser.getSelectedFile().getAbsoluteFile();
				//JOptionPane.showMessageDialog(ReplaceAudio.this, "Your file will be extracted to " + outputDirectory);
				showingOutput.setText("Output Destination: " + outputDirectory);
			}
		}


		if(e.getSource() == start){

			// make a check to ensure that all the fields are filled and valid. If not then dont allow the filter application to happen.

			boolean carryOn = true;

			if((field.getText().equals(""))||(selectedFile == null)||(outputDirectory ==null)){
				JOptionPane.showMessageDialog(VideoFilter.this, "Sorry you must fill all fields before carrying on!");
				carryOn = false;
			}

			if(carryOn){

				//carry on with the process


				JOptionPane.showMessageDialog(VideoFilter.this,"WARNING! Large files can take a long time!");




				boolean override = false;

				File propFile = new File(outputDirectory,field.getText() + ".mp4");
				if(propFile.exists()){
					toOverride = propFile;
					// ask the user if they want to overrride or not. If not then they must change the name of their file
					String[] options = {"Yes,Override!","No! Do not override!"};
					int code = JOptionPane.showOptionDialog(VideoFilter.this, 
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
						worker = new FilterWorker();
						worker.execute();
						progress.setIndeterminate(true);
					}else{
						JOptionPane.showMessageDialog(VideoFilter.this, "Please choose another name to continue and add the filter!");
					}
				}else{
					worker = new FilterWorker();
					start.setEnabled(false);
					worker.execute();
					progress.setIndeterminate(true);
				}	
			}
		}





	}




	private class FilterWorker extends SwingWorker<Integer,Void>{

		@Override
		protected Integer doInBackground() throws Exception {

			// based on what item is selected, do the respective adding of filter
			String name = field.getText();

			if(!name.contains(".mp4")){
				name = name + ".mp4";
			}

			int exitValue = 1;

			if(selectFilter.getSelectedIndex() == 0){
				// it is the flip 90 degress so do the avconv related to that
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "transpose=1 " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}

			if(selectFilter.getSelectedIndex() == 1){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "transpose=0 " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}
			if(selectFilter.getSelectedIndex() == 2){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "negate,vflip " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}


			if(selectFilter.getSelectedIndex() == 3){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "negate " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}

			if(selectFilter.getSelectedIndex() == 4){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "vflip " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}

			if(selectFilter.getSelectedIndex() == 5){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "boxblur=2:1 " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}

			if(selectFilter.getSelectedIndex() == 6){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "boxblur=5:1 " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}

			if(selectFilter.getSelectedIndex() == 7){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "boxblur=10:1 " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}

			if(selectFilter.getSelectedIndex() == 8){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "scale=320:240 " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}

			if(selectFilter.getSelectedIndex() == 9){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "scale=480:360 " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}

			if(selectFilter.getSelectedIndex() == 10){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "scale=640:480 " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}


			if(selectFilter.getSelectedIndex() == 11){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "scale=1280:720 " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}

			if(selectFilter.getSelectedIndex() == 12){
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -vf " + "scale=1920:1080 " + "-strict experimental " + outputDirectory.getAbsolutePath() + File.separator + name;

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = builder.start();
				process.waitFor();

				exitValue = process.exitValue();
			}


			return exitValue;
		}

		@Override
		protected void done() {
			start.setEnabled(true);
			progress.setIndeterminate(false);
			try {
				int i = get();

				if(i == 0){
					JOptionPane.showMessageDialog(VideoFilter.this, "The filter was added successfully!");
				}else{
					JOptionPane.showMessageDialog(VideoFilter.this, "The adding of filter failed!");
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
