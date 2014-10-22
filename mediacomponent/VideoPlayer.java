package mediacomponent;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.TrackDescription;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import vamix.ExtendedFrame;
import vamix.InvalidCheck;

@SuppressWarnings("serial")
public class VideoPlayer extends JPanel  implements ActionListener, ChangeListener, MouseListener{

	public static EmbeddedMediaPlayer mediaPlayer;

	private int clicked = 0;

	private JButton toggleExtraPanel;

	// panels in the extra panel
	private JPanel extra;

	// inside the extra panel
	private JPanel area;
	private JPanel other;

	// inside the "other" panel, put 2 more panels
	private JPanel chooseFilePanel;
	private JPanel skipButtonsPanel;

	// in the area panel
	private JLabel showHistoryTitle = new JLabel("History");
	public static JTextArea history;
	private JPopupMenu popup;
	private JMenuItem normalPlay;
	private JMenuItem onepointfivePlay;
	private JMenuItem twoPlay;
	private JMenuItem twopointfivePlay;
	private JMenuItem threePlay;
	private JMenuItem slow;

	private JButton hide;
	private JButton load;

	private JButton snapShotButton;
	private JButton rewindBack;
	private JButton fastForwardButton;
	private static JButton playButton; 
	private JButton muteButton;
	private JButton forwardButton;
	private JButton backButton;
	private JLabel timeLabel;
	public static JSlider timeSlider;
	private JSlider volumeSlider;

	private JPanel everythingElse = new JPanel(new FlowLayout());
	private JLabel volumeLabel;


	public static String filePath;
	private boolean hasPlayed;

	//references to the images for the icons
	//https://www.iconfinder.com/icons/216309/media_pause_icon#size=16
	//https://www.iconfinder.com/icons/211876/play_icon#size=16
	private ImageIcon play = null;
	private ImageIcon pause = null;
	private ImageIcon skipback = null;
	private ImageIcon skipforward = null;
	private ImageIcon mute = null;
	private ImageIcon unmuted = null;
	private ImageIcon stop = null;
	private ImageIcon rewind = null;
	private ImageIcon fastForward = null;
	private ImageIcon collapse = null;
	private ImageIcon show = null;
	private ImageIcon snapshot = null;
	private int volumeBeforeMuted;
	private JButton stopVideo;

	private JButton chooseFileToPlay;

	private boolean loadedPlayIcon = true;
	private boolean loadedPauseIcon = true;
	private boolean loadedMuteIcon = true;
	private boolean loadedUnmuteIcon = true;

	private boolean isFastForwarding = false;
	private boolean isRewinding = false;

	rewindWorker worker;
	VideoExtracter vx;
	String startTime = "";
	String lengthTime = "";

	private static long start;
	private static long end;
	private static long length;

	public VideoPlayer()  {
		super(new BorderLayout(10,10));

		// Initialize Video surface
		Canvas canvas = new Canvas();
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setVideoSurface(videoSurface);

		// Add the media player to the center of the video player panel
		add(canvas,BorderLayout.CENTER);

		this.add(canvas);
		videoSurface.canvas().setSize(800, 600);
		videoSurface.canvas().setBackground(Color.BLACK);

		canvas.addMouseListener(this);
		
		popup = new JPopupMenu("Popup");
		slow = new JMenuItem("0.5x");
		normalPlay = new JMenuItem("1x");
		onepointfivePlay = new JMenuItem("1.5x");
		twoPlay = new JMenuItem("2x");
		twopointfivePlay = new JMenuItem("2.5x");
		threePlay = new JMenuItem("3x");
		
		popup.add(slow);
		popup.add(normalPlay);
		popup.add(onepointfivePlay);
		popup.add(twoPlay);
		popup.add(twopointfivePlay);
		popup.add(threePlay);
		
		slow.addActionListener(this);
		normalPlay.addActionListener(this);
		onepointfivePlay.addActionListener(this);
		twoPlay.addActionListener(this);
		twopointfivePlay.addActionListener(this);
		threePlay.addActionListener(this);
	
		// declare all the panels in the extra
		extra = new JPanel(new BorderLayout());

		area = new JPanel(new BorderLayout());
		other = new JPanel(new BorderLayout());

		chooseFilePanel = new JPanel(new BorderLayout());
		skipButtonsPanel = new JPanel(new BorderLayout(5, 5));

		history = new JTextArea();	
		// history.setPreferredSize(new Dimension(50,50));;
		history.setEditable(false);

		//scroll = new JScrollPane(history);
		//scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		//scroll.setPreferredSize(new Dimension(0, 0));
		//area.add(scroll);
		// history.setEditable(false); 
		
		// Read log file into log panel
		try {
			File logFile = LogFile.getLogFile();
			BufferedReader br = new BufferedReader(new FileReader(logFile));
			String line = br.readLine();
			while (line != null) {
				history.append(line);
				history.append(System.lineSeparator());
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// Could not read log file, display error message
			JOptionPane.showMessageDialog(null, "Could not open log file: No log available", "ERROR", JOptionPane.ERROR_MESSAGE);
		}

		try {
			snapshot = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/snapshot.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Unable to load mute icon, revert to using text
			loadedMuteIcon = false;
			//muteButton.setText("Mute");
		}

		snapShotButton = new JButton(snapshot);
		snapShotButton.setToolTipText("Take Screenshot");
		snapShotButton.addActionListener(this);
		
		// Everything is in a grid bag layout for the panel. now add stuff to the panel and
		// put it in a grid bag layout
		everythingElse.setLayout(new GridBagLayout());

		volumeLabel = new JLabel("Volume");
		GridBagConstraints gb0 = new GridBagConstraints();
		gb0.gridx = 0;
		gb0.gridy = 1;
		gb0.gridwidth = 1;
		gb0.weightx = 0;
		gb0.weighty = 0;
		everythingElse.add(volumeLabel,gb0);

		// Add the volume slider
		volumeSlider = new JSlider(SwingConstants.HORIZONTAL);
		volumeSlider.setToolTipText("" + volumeSlider.getValue());
		GridBagConstraints gb = new GridBagConstraints();
		gb.gridx = 1;
		gb.gridy = 1;
		gb.gridwidth = 1;
		gb.weightx = 0.5;
		gb.weighty = 0;
		gb.fill = GridBagConstraints.HORIZONTAL;
		everythingElse.add(volumeSlider,gb);

		// Add mute/unmute buttons
		try {
			mute = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/mute.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Unable to load mute icon, revert to using text
			loadedMuteIcon = false;
			//muteButton.setText("Mute");
		}

		try {
			unmuted = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/unmuted.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Unable to load unmute icon, revert to using text
			loadedUnmuteIcon = false;
		}

		if (loadedUnmuteIcon) {
			muteButton = new JButton(unmuted);
		} else {
			muteButton = new JButton("Unmuted");
		}
		GridBagConstraints gb1 = new GridBagConstraints();
		gb1.gridx = 2;
		gb1.gridy = 1;
		gb1.gridwidth = 1;
		gb1.gridheight = 1;
		gb1.weightx = 0;
		gb1.weighty = 0;
		gb1.insets = new Insets(0,5,0,5);
		everythingElse.add(muteButton,gb1);
		muteButton.setToolTipText("Mute/Unmute");


		boolean iconLoaded = true;

		// Add back button
		try {
			skipback = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/skipback.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Couldn't load icon
			iconLoaded = false;
		}

		if (iconLoaded) {
			backButton = new JButton(skipback);
		} else {
			backButton = new JButton("Back");
			iconLoaded = true;
		}

		backButton.setToolTipText("Skip Back");

		// add rewind button

		try {
			rewind = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/rewind.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Couldn't load icon
			iconLoaded = false;
		}

		if (iconLoaded) {
			rewindBack= new JButton(rewind);
		} else {
			rewindBack = new JButton("Rewind");
			iconLoaded = true;
		}

		rewindBack.setToolTipText("Rewind");

		GridBagConstraints gb2 = new GridBagConstraints();
		gb2.gridx = 3;
		gb2.gridy = 1;
		gb2.gridwidth = 1;
		gb2.gridheight = 1;
		gb2.weightx = 0;
		gb2.weighty = 0;
		gb2.insets = new Insets(0,5,0,5);
		rewindBack.addActionListener(this);
		everythingElse.add(rewindBack,gb2);

		// Add play button
		try {
			play = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/play.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Couldn't load play icon
			loadedPlayIcon = false;
		}

		try {
			pause = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/pause.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Couldn't load pause icon
			loadedPauseIcon = false;
		}

		if (loadedPlayIcon) {
			playButton = new JButton(play);
		} else {
			playButton = new JButton("Play");
		}
		GridBagConstraints gb3 = new GridBagConstraints();
		gb3.gridx = 4;
		gb3.gridy = 1;
		gb3.gridwidth = 1;
		gb3.gridheight = 1;
		gb3.weightx = 0;
		gb3.weighty = 0;
		gb3.insets = new Insets(0,5,0,5);
		playButton.setToolTipText("Play");
		everythingElse.add(playButton,gb3);

		// Add forward button
		try {
			skipforward = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/skipforward.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Couldn't load forward icon
			iconLoaded = false;
		}


		if (iconLoaded) {
			forwardButton = new JButton(skipforward);
		} else {
			forwardButton = new JButton("Forward");
			iconLoaded = true;
		}
		forwardButton.setToolTipText("Skip Forward");



		/// add the fast forward button

		try {
			fastForward = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/fastforward.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Couldn't load forward icon
			iconLoaded = false;
		}


		if (iconLoaded) {
			fastForwardButton = new JButton(fastForward);
		} else {
			forwardButton = new JButton("Fast Forward");
			iconLoaded = true;
		}
		fastForwardButton.setToolTipText("Fast Forward");


		GridBagConstraints gb4 = new GridBagConstraints();
		gb4.gridx = 5;
		gb4.gridy = 1;
		gb4.gridwidth = 1;
		gb4.gridheight = 1;
		gb4.weightx = 0;
		gb4.weighty = 0;
		gb4.insets = new Insets(0,5,0,5);
		fastForwardButton.addActionListener(this);
		everythingElse.add(fastForwardButton,gb4);

		chooseFileToPlay = new JButton(" Choose file to play");
		chooseFileToPlay.setToolTipText("Select a file to play in the video player");
		chooseFileToPlay.addActionListener(this);



		// Add time label
		timeLabel = new JLabel("00:00:00");
		GridBagConstraints gb5 = new GridBagConstraints();
		gb5.gridx = 0;
		gb5.gridy = 0;
		gb5.gridheight = 1;
		gb5.gridwidth = 1;
		gb5.weightx = 0;
		gb5.weighty = 0;
		gb5.insets = new Insets(5,10,20,10);
		timeLabel.setToolTipText("Time Elapsed");
		everythingElse.add(timeLabel,gb5);

		// Add time slider
		timeSlider = new JSlider();
		GridBagConstraints gb6 = new GridBagConstraints();
		gb6.gridx = 1;
		gb6.gridy =0;
		gb6.gridwidth = 7;
		gb6.gridheight = 1;
		gb6.fill = GridBagConstraints.HORIZONTAL;
		gb6.weightx = 1;
		gb6.weighty = 0;
		gb6.insets = new Insets(5,10,20,10);
		timeSlider.setToolTipText("Time Slider");
		everythingElse.add(timeSlider,gb6);


		try {
			stop = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/stop.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Couldn't load stop icon
			iconLoaded = false;
		}

		if (iconLoaded) {
			stopVideo = new JButton(stop);
		} else {
			stopVideo = new JButton("Stop");
		}
		GridBagConstraints gb7 = new GridBagConstraints();
		gb7.gridx = 6;
		gb7.gridy = 1;
		gb7.gridwidth = 1;
		gb7.weightx = 0;
		gb7.weighty = 0;
		everythingElse.add(stopVideo,gb7);
		stopVideo.setToolTipText("Stop");
		stopVideo.addActionListener(this);


		try {
			show = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/toggleon.png")));
			collapse = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("resources/toggleoff.png")));
		} catch (IllegalArgumentException | IOException e) {
			// Couldn't load forward icon
			iconLoaded = false;
		}
		toggleExtraPanel = new JButton(collapse);
		GridBagConstraints gb12 = new GridBagConstraints();
		gb12.gridx = 7;
		gb12.gridy = 1;
		gb12.gridwidth = 1;
		gb12.weightx = 0;
		gb12.weighty = 0;
		gb12.insets = new Insets(0,5,0,5);

		toggleExtraPanel.addActionListener(this);
		everythingElse.add(toggleExtraPanel,gb12);
		toggleExtraPanel.setToolTipText("Toggle extra panel");

		// add the choose file button to the panel
		chooseFilePanel.add(chooseFileToPlay, BorderLayout.CENTER);
		chooseFilePanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );

		// add the buttons to the panel
		skipButtonsPanel.add(backButton,BorderLayout.WEST);
		skipButtonsPanel.add(snapShotButton,BorderLayout.CENTER);
		skipButtonsPanel.add(forwardButton,BorderLayout.EAST);
		skipButtonsPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );

		// add the two panel above to the other panel
		other.add(chooseFilePanel,BorderLayout.NORTH);
		other.add(skipButtonsPanel,BorderLayout.SOUTH);
		other.setBorder( new EmptyBorder( 0, 0, 10, 0 ) );



		// add area to history panel
		area.add(showHistoryTitle,BorderLayout.NORTH);
		area.add(history,BorderLayout.CENTER);
		area.setBorder( new EmptyBorder( 0,0, 0,0 ) );

		// add both the other and history panel to the extra panel
		extra.add(other,BorderLayout.NORTH);
		extra.add(area,BorderLayout.CENTER);

		add(extra,BorderLayout.EAST);
		add(everythingElse,BorderLayout.SOUTH);
		
		setupListeners();
	}



	private void setupListeners() {

		playButton.addActionListener(this);
		muteButton.addActionListener(this);
		forwardButton.addActionListener(this);
		backButton.addActionListener(this);

		timeSlider.addChangeListener(this);
		volumeSlider.addChangeListener(this);


		ActionListener updater = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long milliSeconds = mediaPlayer.getTime();
				long hours = TimeUnit.MILLISECONDS.toHours(milliSeconds);
				long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds));
				long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds));

				String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
				timeLabel.setText(time);
				timeSlider.setValue((int)mediaPlayer.getTime());
				timeSlider.setToolTipText(time);
			}
		};

		Timer timer = new Timer(100, updater);
		timer.start();

	}


	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == toggleExtraPanel){
			togglePanelPressed();
		}

		if (e.getSource() == playButton) {
			playButtonPress();
		} 

		if (e.getSource() == muteButton) {
			muteButtonPress();
		}		
		if (e.getSource() == forwardButton) {
			
			if (mediaPlayer.getTime() + 10000 <= mediaPlayer.getLength()) { // Prevent skipping past end of file
				mediaPlayer.skip(10000);
			}
		}

		if (e.getSource() == backButton) {
			mediaPlayer.skip(-10000);	
		}

		if (e.getSource() == chooseFileToPlay) {
			//check if a file is already playing
			boolean a = (mediaPlayer.isPlaying())||(mediaPlayer.isPlayable());
			
			SwingWorker worker = new SwingWorker<Void,Void>(){

				@Override
				protected Void doInBackground() throws Exception {
					
					JFileChooser fc = new JFileChooser();
					fc.setFileFilter(SwingFileFilterFactory.newMediaFileFilter());
					int returnVal = fc.showOpenDialog(VideoPlayer.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						
						String newFile = fc.getSelectedFile().getAbsolutePath();
						// Check that file is a video or audio file.
						InvalidCheck i = new InvalidCheck();
						boolean isValidMedia = i.invalidCheck(newFile);
						/**
						String command = "file " + "-ib " + "\"" + newFile + "\"" + " | grep \"video\\|audio\"";
						ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
						boolean isValidMedia = false;

						try {
							Process process = builder.start();
							process.waitFor();
							if (process.exitValue() == 0) {
								isValidMedia = true;
							}

						} catch (IOException | InterruptedException e1) {
							// Couldn't determine file type. Warn user
							JOptionPane.showMessageDialog(VideoPlayer.this, "Unable to determine file type. Cannot load file.", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}

						 **/

						if (!isValidMedia) {
							JOptionPane.showMessageDialog(VideoPlayer.this, "You have specified an invalid file.", "Error", JOptionPane.ERROR_MESSAGE);
							return null;
						} else if (!newFile.equals(filePath)) {
							
							
							VideoPlayer.this.filePath = newFile;


							// before starting the video add it to the log

							LogFile.writeToLog(VideoPlayer.this.filePath.substring(VideoPlayer.this.filePath.lastIndexOf(File.separator)+1));
							VideoPlayer.this.hasPlayed = false;


							mediaPlayer.startMedia(filePath);

						}
					}
					return null;
				}

				@Override
				protected void done() {
					playButton.setIcon(pause);
					VideoPlayer.this.hasPlayed = true;
					while (mediaPlayer.getVolume() != volumeSlider.getValue() || mediaPlayer.getLength() != timeSlider.getMaximum()) {
						mediaPlayer.setVolume(volumeSlider.getValue());
						timeSlider.setMaximum((int)mediaPlayer.getLength());
					}
				}
				
				
			};
			
			worker.execute();
			
			
			// Get selection from user
			
			
		}

		if(e.getSource() == stopVideo){
			mediaPlayer.stop();
			mediaPlayer.setRate(1.0f);
			playButton.setIcon(play);
		}

		if(e.getSource() == snapShotButton){
			snapShotPressed();
		}

		if(e.getSource() == fastForwardButton){
			boolean isr = isRewinding;
			
			fastForwardPressed(isr);
		}
		if (e.getSource() == rewindBack) {
			
			boolean isf = isFastForwarding;
			
			rewindPressed(isf);
		}
		
		if(e.getSource() == slow){
			mediaPlayer.setRate(0.5f);
			ExtendedFrame.setRadioButton(0);
		}
		
		if(e.getSource() == normalPlay){
			mediaPlayer.setRate(1.0f);
			ExtendedFrame.setRadioButton(1);
		}
		
		if(e.getSource() == onepointfivePlay){
			mediaPlayer.setRate(1.5f);
			ExtendedFrame.setRadioButton(2);
		}
		
		if(e.getSource() == twoPlay){
			mediaPlayer.setRate(2.0f);
			ExtendedFrame.setRadioButton(3);
		}
		
		if(e.getSource() == twopointfivePlay){
			mediaPlayer.setRate(2.5f);
			ExtendedFrame.setRadioButton(4);
		}
		
		if(e.getSource() == threePlay){
			mediaPlayer.setRate(3.0f);
			ExtendedFrame.setRadioButton(5);
		}
	}
	
	private void fastForwardPressed(boolean isr){
		
		if(isr){
			worker.cancel(true);
			rewindBack.setBackground(null);
			isRewinding = false;
		}
		
		if (isFastForwarding) {
			mediaPlayer.setRate(1.0f);
			fastForwardButton.setBackground(null);
			isFastForwarding = false;
		} else {
			mediaPlayer.setRate(3.0f);
			fastForwardButton.setBackground(Color.darkGray);
			isFastForwarding = true;
		}
		
	}
	
	private void rewindPressed(boolean isf){
		
		if(isf){
			mediaPlayer.setRate(1.0f);
			fastForwardButton.setBackground(null);
			isFastForwarding = false;
		}
		
		if (isRewinding) {
			worker.cancel(true);
			rewindBack.setBackground(null);
			isRewinding = false;
		} else {
			
			worker = new rewindWorker();
			rewindBack.setBackground(Color.darkGray);
			worker.execute();
			isRewinding = true;
		}
		
		
	}

	private void togglePanelPressed() {
		boolean visible = extra.isVisible();
		boolean change = true;
		if(change){
			if(!visible){

				change = false;
				toggleExtraPanel.setIcon(collapse);
				extra.setVisible(true);
			}
		}
		if(change){
			if(visible){
				change = false;
				toggleExtraPanel.setIcon(show);
				extra.setVisible(false);
			}
		}
	}
	
	private void snapShotPressed(){
		BufferedImage image = mediaPlayer.getSnapshot();
		boolean ifNull = (image == null);

		if(!ifNull){
			File outputImage = new File(System.getProperty("user.home") + File.separator +  +mediaPlayer.getTime() + ".png");
			try {
				ImageIO.write(image, "png", outputImage);
				JOptionPane.showMessageDialog(VideoPlayer.this,"The snapshot was saved to your home folder with name " + outputImage.getName());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(VideoPlayer.this,"Sorry, snapshot failed!");
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == volumeSlider) {
			if (e.getSource() instanceof JSlider) {
				JSlider slider = (JSlider)e.getSource();
				if (slider.getValueIsAdjusting()) {
					if (mediaPlayer.isPlayable()) {
						// Checks the volume and changes it to mute icon if 0 or 1.
						if ((mediaPlayer.getVolume() <= 1)) {
							muteButton.setIcon(mute);
						} else {
							muteButton.setIcon(unmuted);
							mediaPlayer.mute(false);
						}
					}
					mediaPlayer.setVolume(slider.getValue());
					volumeSlider.setToolTipText("" + volumeSlider.getValue());
				}
			}
		}

		if (e.getSource() == timeSlider) {
			JSlider slider = (JSlider)e.getSource();

			// If the slider has reached the end then make it go back to start and then make the icon  switch to play
			if(timeSlider.getValue() == mediaPlayer.getLength()){
				playButton.setIcon(play);
				timeSlider.setValue(0);
				mediaPlayer.stop();
				
				if(ExtendedFrame.getReplay()){
					//mediaPlayer.start();
					playButton.doClick();
				}
			
			}


			if (slider.getValueIsAdjusting()) {
				mediaPlayer.setTime(slider.getValue());
			}
		}
	}
	
	private class rewindWorker extends SwingWorker<Void, Void>{

		@Override
		protected Void doInBackground() throws Exception {
			while (true) {
				if (isCancelled()) {
					break;
				}
				Thread.sleep(100);
				process(null);
			}
			return null;
		}

		@Override
		protected void done() {
			// TODO Auto-generated method stub
			super.done();
		}

		@Override
		protected void process(List<Void> chunks) {
			mediaPlayer.skip(-1000);
		}

	}


	@Override
	public void mouseClicked(MouseEvent arg0) {


		if(arg0.getModifiers() == MouseEvent.BUTTON3_MASK){
			popup.show(arg0.getComponent(), arg0.getX(), arg0.getY());
			
		}else if(arg0.getModifiers() == MouseEvent.BUTTON1_MASK){
			clicked++;
			if(clicked == 2){
				start = mediaPlayer.getTime();
				startTime = convertTime(mediaPlayer.getTime());

				JOptionPane.showMessageDialog(VideoPlayer.this,"Started video recording");
			}else if(clicked == 4){
				end = mediaPlayer.getTime();
				length = end-start;
				lengthTime = convertTime(length);

				if(length<8000){
					JOptionPane.showMessageDialog(VideoPlayer.this, "WARNING! Extraction less than 8 seconds may give orthodox results!");
				}

				vx = new VideoExtracter(startTime,lengthTime);
				vx.execute();
				startTime = "";
				lengthTime = "";
				start = 0;
				end = 0;
				length = 0;

				JOptionPane.showMessageDialog(VideoPlayer.this,"Ended Recording");
				clicked = 0;
			}
		}
	}


	private String convertTime(long milliSeconds) {
		//long milliSeconds = mediaPlayer.getTime();
		long hours = TimeUnit.MILLISECONDS.toHours(milliSeconds);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds));

		String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		return time;
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent ev) {
	}
	@Override
	public void mouseReleased(MouseEvent ev) {
	}
	
	public static void setCurrentRate(float i){
		mediaPlayer.setRate(i);
	}
	
	public static void startPlaying() {
		mediaPlayer.stop();
		playButton.doClick();
		AddToFile a = new AddToFile();
		a.add();
	}
	
	private void playButtonPress(){

		if (isRewinding) {
			worker.cancel(true);
			isRewinding = false;
			rewindBack.setBackground(null);
		}
		if (isFastForwarding) {
			mediaPlayer.setRate(1.0f);
			fastForwardButton.setBackground(null);
			isFastForwarding = false;
		}
		if (mediaPlayer.isPlaying()) {

			if (loadedPlayIcon) {
				playButton.setIcon(play);
			} else {
				playButton.setText("Play");
			}
		} else if (filePath != null) {
			if (loadedPauseIcon) {
				playButton.setIcon(pause);
			} else {
				playButton.setText("Pause");
			}
		}
		mediaPlayer.pause();

		if (!mediaPlayer.isPlayable()) {
			if (filePath != null) {
				mediaPlayer.startMedia(filePath);
				this.hasPlayed = true;
				// Continually try to set correct volume on player, and get total length.
				// This is necessary as, it takes a while for the audio output to be created
				// and the volume won't get set until then
				while (mediaPlayer.getVolume() != volumeSlider.getValue() || mediaPlayer.getLength() != timeSlider.getMaximum()) {
					if (volumeSlider.getValue() > 1) {
						mediaPlayer.mute(false);
					}
					mediaPlayer.setVolume(volumeSlider.getValue());
					timeSlider.setMaximum((int)mediaPlayer.getLength());
				}
			}
		} else if (!this.hasPlayed) {
		
			mediaPlayer.startMedia(filePath);
			this.hasPlayed = true;

			// check if the file path is the same, if it is then don't add it to the history

			// if file path is not the same then add it to the file path


			while (mediaPlayer.getVolume() != volumeSlider.getValue() || mediaPlayer.getLength() != timeSlider.getMaximum()) {
				mediaPlayer.setVolume(volumeSlider.getValue());
				timeSlider.setMaximum((int)mediaPlayer.getLength());
			}
		}
	}
	
	private void muteButtonPress(){
		if (volumeSlider.getValue() <= 1) {
			// The volume is mute so now the user presses unmute so change the icon.
			// Restore the volume of the volume slider to one before mute
			if (loadedUnmuteIcon) {
				muteButton.setIcon(unmuted);
			} else {
				muteButton.setText("Unmuted");
			}
			volumeSlider.setValue(volumeBeforeMuted);
			mediaPlayer.setVolume(volumeBeforeMuted);
		} else {
			volumeBeforeMuted = volumeSlider.getValue();
			if (loadedMuteIcon) {
				muteButton.setIcon(mute);
			} else {
				muteButton.setText("Mute");
			}
			volumeSlider.setValue(0);
		}
		mediaPlayer.mute();
		volumeSlider.setToolTipText("" + volumeSlider.getValue());
	}

	private void chooseFilePressed() {
		
	}

}
