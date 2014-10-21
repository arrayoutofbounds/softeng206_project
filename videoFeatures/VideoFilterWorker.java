package videoFeatures;

import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class VideoFilterWorker extends SwingWorker<Integer,Void>{
	
	private JTextField field;
	private JButton start;
	private JProgressBar progress;
	private JComboBox selectFilter;
	private File selectedFile;
	private File outputDirectory;
	
	public VideoFilterWorker(JTextField field, JButton start, JProgressBar progress,JComboBox selectFilter, File selectedFile, File outputDirectory ){
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
				JOptionPane.showMessageDialog(null, "The filter was added successfully!");
			}else{
				JOptionPane.showMessageDialog(null, "The adding of filter failed!");
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
