package vamix;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ComboBoxModel;
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
import javax.swing.border.EmptyBorder;

import mediacomponent.VideoPlayer;
import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;

public class ExtractPart extends JFrame implements ActionListener {
	
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

	private JLabel nameOutputLabel;
	private File selectedFile;
	private File outputDirectory;
	private File toOverride;


	private JTextField startTime;
	private JTextField lengthTime;
	private JLabel length;
	private JLabel startLabel;
	private JPanel forTime;
	private boolean carryOn;
	private FilterWorker worker;


	public ExtractPart(){
		super("Extract part of a video");
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

		forTime  = new JPanel(new FlowLayout());
		forTime.setBorder(new EmptyBorder(10,10,10,10));
		

		chooseInputButton = new JButton("Choose Input Video File");
		showingInput = new JLabel("Input File:");
		chooseOutputButton = new JButton("Choose Destination of Output");
		showingOutput = new JLabel("Output destination:");
		field = new JTextField(50);
		field.setColumns(50);
		start = new JButton("Extract");
		start.setEnabled(false);
		progress = new JProgressBar();

		nameOutputLabel = new JLabel("Name Output:");
		
		startTime = new JTextField(10);
		startTime.setColumns(10);

		lengthTime  = new JTextField(10);
		lengthTime.setColumns(10);
		
		startLabel = new JLabel("Start Time (hh:mm:ss)");
		length = new JLabel("Length (hh:mm:ss)");


		chooseInputButton.addActionListener(this);
		chooseOutputButton.addActionListener(this);
		start.addActionListener(this);




		chooseInput.add(chooseInputButton);
		showInput.add(showingInput,BorderLayout.WEST);
		chooseOutput.add(chooseOutputButton);
		showOutput.add(showingOutput,BorderLayout.WEST);
		nameOutput.add(nameOutputLabel,BorderLayout.WEST);
		nameOutput.add(field);
		startProcess.add(start);
		showProgress.add(progress);

		forTime.add(startLabel);
		forTime.add(startTime);
		forTime.add(length);
		forTime.add(lengthTime);



		add(chooseInput);
		add(showInput);
		add(chooseOutput);
		add(showOutput);
		add(nameOutput);
		add(forTime);
		add(startProcess);
		add(showProgress);



	}


	private void checkTime(){
		String inputStartTime = startTime.getText();
		String inputLengthTime = lengthTime.getText();
		String pattern = "^[0-9]{1,}:[0-9]{1,2}:[0-9]{1,2}$";
		Pattern regex = Pattern.compile(pattern);
		Matcher check = regex.matcher(inputStartTime);
		Matcher check2 = regex.matcher(inputLengthTime);


		boolean carryOn2;
		boolean carryOn3;
		if(!check.find()){
			JOptionPane.showMessageDialog(ExtractPart.this, "Sorry the start time is not the correct format!");
			carryOn2 = false;
		}else{
			carryOn2 = true;
		}

		if(!check2.find()){
			JOptionPane.showMessageDialog(ExtractPart.this, "Sorry the length time is not the correct format!");
			carryOn3 = false;
		}else{
			carryOn3 = true;
		}
		
		if(carryOn3 && carryOn2 ){
			carryOn = true;
		}else{
			carryOn = false;
		}
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
			int returnValue = fileChooser.showOpenDialog(ExtractPart.this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				showingInput.setText("Input File: " + selectedFile.getName());
				InvalidCheck i = new InvalidCheck();
				boolean isValidMedia = i.invalidCheck(fileChooser.getSelectedFile().getAbsolutePath());

				if (!isValidMedia) {
					JOptionPane.showMessageDialog(ExtractPart.this, "You have specified an invalid file.", "Error", JOptionPane.ERROR_MESSAGE);
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

			int returnValue = outputChooser.showOpenDialog(ExtractPart.this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				outputDirectory = outputChooser.getSelectedFile().getAbsoluteFile();
				//JOptionPane.showMessageDialog(ReplaceAudio.this, "Your file will be extracted to " + outputDirectory);
				showingOutput.setText("Output Destination: " + outputDirectory);
			}
		}
		
		if(e.getSource() == start){

			carryOn = true;

			if((field.getText().equals(""))||(selectedFile == null)||(outputDirectory ==null)){
				JOptionPane.showMessageDialog(ExtractPart.this, "Sorry you must fill all fields before carrying on!");
				carryOn = false;
			}
			
			checkTime();
			

			if(carryOn){

				//carry on with the process
				
				boolean override = false;

				File propFile = new File(outputDirectory,field.getText() + ".mp4");
				if(propFile.exists()){
					toOverride = propFile;
					// ask the user if they want to overrride or not. If not then they must change the name of their file
					String[] options = {"Yes,Override!","No! Do not override!"};
					int code = JOptionPane.showOptionDialog(ExtractPart.this, 
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
						JOptionPane.showMessageDialog(ExtractPart.this, "Please choose another name to continue!");
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

			int exitValue;
			
			String cmd = "/usr/bin/avconv -i " + selectedFile.getAbsolutePath() + " -ss " + startTime.getText() + " -t " + lengthTime.getText() + " -c:a copy -c:v copy "  + outputDirectory.getAbsolutePath() + File.separator + name; 

			ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",cmd);
			
			Process process = builder.start();
			process.waitFor();


			exitValue = process.exitValue();			
			
			return exitValue;
		}

		@Override
		protected void done() {
			start.setEnabled(true);
			progress.setIndeterminate(false);
			try {
				int i = get();

				if(i == 0){
					JOptionPane.showMessageDialog(ExtractPart.this, "The extraction of the part was successful");
				}else{
					JOptionPane.showMessageDialog(ExtractPart.this, "The extraction of the part failed!");
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
