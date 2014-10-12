package mediacomponent;

import java.io.File;

public class AddToFile {
	
	public AddToFile(){
		
	}
	
	
	public void add(){
		LogFile.writeToLog(VideoPlayer.filePath.substring(VideoPlayer.filePath.lastIndexOf(File.separator)+1));
	}

}
