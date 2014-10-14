package audioFeatures;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import vamix.InvalidCheck;


public class AudioFilter extends JFrame implements ActionListener{

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

	private String[] inComboBox = {"Remove Audio"};
	private File selectedFile;
	private File outputDirectory;
	private File toOverride;
	private FilterWorker worker;



	public AudioFilter(){
		super("Add Audio Filter");
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
			int returnValue = fileChooser.showOpenDialog(AudioFilter.this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				showingInput.setText("Input File: " + selectedFile.getName());
				InvalidCheck i = new InvalidCheck();
				boolean isValidMedia = i.invalidCheck(fileChooser.getSelectedFile().getAbsolutePath());

				if (!isValidMedia) {
					JOptionPane.showMessageDialog(AudioFilter.this, "You have specified an invalid file.", "Error", JOptionPane.ERROR_MESSAGE);
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

			int returnValue = outputChooser.showOpenDialog(AudioFilter.this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				outputDirectory = outputChooser.getSelectedFile().getAbsoluteFile();
				//JOptionPane.showMessageDialog(ReplaceAudio.this, "Your file will be extracted to " + outputDirectory);
				showingOutput.setText("Output Destination: " + outputDirectory);
			}
		}


		if(e.getSource() == start){

			boolean carryOn = true;

			if((field.getText().equals(""))||(selectedFile == null)||(outputDirectory ==null)){
				JOptionPane.showMessageDialog(AudioFilter.this, "Sorry you must fill all fields before carrying on!");
				carryOn = false;
			}

			if(carryOn){

				//carry on with the process


				JOptionPane.showMessageDialog(AudioFilter.this,"WARNING! Large files can take a long time!");




				boolean override = false;

				File propFile = new File(outputDirectory,field.getText() + ".mp4");
				if(propFile.exists()){
					toOverride = propFile;
					// ask the user if they want to overrride or not. If not then they must change the name of their file
					String[] options = {"Yes,Override!","No! Do not override!"};
					int code = JOptionPane.showOptionDialog(AudioFilter.this, 
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
						JOptionPane.showMessageDialog(AudioFilter.this, "Please choose another name to continue and add the filter!");
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
				String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -an "  + outputDirectory.getAbsolutePath() + File.separator + name;

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
					JOptionPane.showMessageDialog(AudioFilter.this, "The filter was added successfully!");
				}else{
					JOptionPane.showMessageDialog(AudioFilter.this, "The adding of filter failed!");
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
