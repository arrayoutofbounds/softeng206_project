package audioFeatures;

import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class AudioFilterWorker extends SwingWorker<Integer,Void>{
	
	private JTextField field;
	private JButton start;
	private JProgressBar progress;
	private JComboBox selectFilter;
	private File selectedFile;
	private File outputDirectory;
	
	
	public AudioFilterWorker(JTextField field, JButton start, JProgressBar progress,JComboBox selectFilter, File selectedFile, File outputDirectory ){
		this.field = field;
		this.start = start;
		this.progress = progress;
		this.selectedFile = selectedFile;
		this.selectFilter = selectFilter;
		this.outputDirectory = outputDirectory;
	}
	
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
			String cmd = "/usr/bin/avconv -i " + "" +selectedFile.getAbsolutePath().replaceAll(" ", "\\\\ ") + " -an "  + outputDirectory.getAbsolutePath().replaceAll(" ", "\\\\ ") + File.separator + name;

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
				JOptionPane.showMessageDialog(null, "The filter was added successfully!");
			}else{
				JOptionPane.showMessageDialog(null, "The adding of filter failed!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}



}