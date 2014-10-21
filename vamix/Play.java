package vamix;
import java.awt.GridLayout;


import javax.swing.JPanel;

import mediacomponent.VideoPlayer;


@SuppressWarnings("serial")
public class Play extends JPanel {
	
	public Play() {
		
		setLayout(new GridLayout());
		
		VideoPlayer videoPlayer = new VideoPlayer();
		this.add(videoPlayer);
	}
}
