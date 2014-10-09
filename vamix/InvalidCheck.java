package vamix;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import mediacomponent.VideoPlayer;


public class InvalidCheck {


	public static boolean invalidCheck(String newFile){
		String command = "file " + "-ib " + "\"" + newFile + "\"" + " | grep \"video\\|audio\"";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
		boolean isValidMedia = false;

		try {
			Process process = builder.start();
			process.waitFor();
			if (process.exitValue() == 0) {
				isValidMedia = true;
			}
			return isValidMedia;

		} catch (IOException | InterruptedException e1) {
			// Couldn't determine file type. Warn user
			JOptionPane.showMessageDialog(null, "Unable to determine file type. Cannot load file.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}

