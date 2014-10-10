package vamix;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import mediacomponent.LogFile;
import mediacomponent.VideoPlayer;


@SuppressWarnings("serial")
public class ExtendedFrame extends JFrame implements ActionListener, MenuListener {
	
	private JTabbedPane tabsPane;
	private Download downloadTab;
	private Play playTab;
	private ExtractFrame extractFrame;
	private EditTextFrame editTextFrame;
	private JMenuBar menuBar;
    private JMenu audioFeatures;
    private JMenuItem extractMenuItem;
    private JMenuItem editTextMenuItem;
    private JMenuItem replaceAudio;
    private ReplaceAudio replaceAudioFrame;
    private JMenuItem makeGif;
    private JMenuItem extractImagesFromVideo;
    private JMenuItem delete;
    private JMenu help;
    private JMenuItem hide;
    
    private HelpFrame helpFrame;;
    
    private JMenu videoFeatures;
    private JMenu other;
    private JMenuItem download;
    
    private Gif gifFrame;
	private Images i;
	
	private DownloadFrame downloadFrame;
    
	
	private JMenu speed;
	private static JRadioButtonMenuItem playingSpeed[];
	private ButtonGroup group;
	
	private JMenuItem videoFilters;
	private VideoFilter v;
	
	public ExtendedFrame() {
		super("Vamix");
		
		//Container mainContainer = this.getContentPane(); 
		//mainContainer.add(new JLabel(new ImageIcon("/home/anmol/Downloads/light_background-wallpaper-1280x800.jpg")));
		
		tabsPane = new JTabbedPane();
		downloadTab = new Download();
		playTab  = new Play();
		
		
	    menuBar = new JMenuBar();
        add(menuBar);
        
       
        

        audioFeatures = new JMenu("Audio Features");
        audioFeatures.setMnemonic('a');
        menuBar.add(audioFeatures);
        
        speed = new JMenu("Playback speed");
        audioFeatures.add(speed);
        
        videoFeatures = new JMenu("Video Features");
        videoFeatures.setMnemonic('v');
        menuBar.add(videoFeatures);
        
        other = new JMenu("Tools");
        other.setMnemonic('t');
        menuBar.add(other);
        
        help = new JMenu("Help");
        help.setMnemonic('h');
        menuBar.add(help);
        help.addMenuListener(this);
        
        download = new JMenuItem("Download audio/video");
        download.setMnemonic('o');
        other.add(download);
        download.addActionListener(this);

        extractMenuItem = new JMenuItem("Extract Audio");
        extractMenuItem.setMnemonic('e');
        audioFeatures.add(extractMenuItem);
        
        editTextMenuItem = new JMenuItem("Add text to video");
        editTextMenuItem.setMnemonic('t');
        videoFeatures.add(editTextMenuItem);
        
        replaceAudio = new JMenuItem("Replace/Overlay Audio of a Video");
        replaceAudio.setMnemonic('r');
        audioFeatures.add(replaceAudio);
        
        makeGif = new JMenuItem("Make a GIF Image");
        makeGif.setMnemonic('g');
        videoFeatures.add(makeGif);
        
        extractImagesFromVideo = new JMenuItem("Extract Images from video");
        extractImagesFromVideo.setMnemonic('i');
        videoFeatures.add(extractImagesFromVideo);
        
        videoFilters = new JMenuItem("Add Video Filters");
        videoFilters.setMnemonic('f');
        videoFeatures.add(videoFilters);
        
        delete = new JMenuItem("Delete history");
        delete.setMnemonic('d');
        other.add(delete);
        
        hide = new JMenuItem("Hide/Load History");
        other.add(hide);
       
        setJMenuBar(menuBar);
        
        extractMenuItem.addActionListener(this);
        editTextMenuItem.addActionListener(this);
        replaceAudio.addActionListener(this);
        makeGif.addActionListener(this);
		extractImagesFromVideo.addActionListener(this);
		delete.addActionListener(this);
		hide.addActionListener(this);
		videoFilters.addActionListener(this);

        tabsPane.add("Download",downloadTab);
        tabsPane.add("Play",playTab);
        
        tabsPane.setSelectedComponent(playTab);
		
		add(tabsPane);
		
		// Create single extract frame, so that the user can close it, and reopen it
		// and maintain extraction progress
		extractFrame = new ExtractFrame();
		extractFrame.setResizable(false);
		extractFrame.setSize(600, 400);
		extractFrame.setLocationRelativeTo(null);
		extractFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Create single edit text frame
		editTextFrame = new EditTextFrame();
		editTextFrame.setResizable(false);
		editTextFrame.setSize(750, 700);
		editTextFrame.setLocationRelativeTo(null);
		editTextFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		replaceAudioFrame = new ReplaceAudio();
		replaceAudioFrame.setResizable(false);
		replaceAudioFrame.setSize(700, 600);
		replaceAudioFrame.setLocationRelativeTo(null);
		replaceAudioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		gifFrame = new Gif();
		gifFrame.setResizable(false);
		gifFrame.setSize(600, 500);
		gifFrame.setLocationRelativeTo(null);
		gifFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		i = new Images();
		i.setResizable(false);
		i.setSize(500, 400);
		i.setLocationRelativeTo(null);
		i.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		helpFrame = new HelpFrame();
		helpFrame.setResizable(false);
		helpFrame.setSize(1000, 700);
		helpFrame.setLocationRelativeTo(null);
		helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		downloadFrame = new DownloadFrame();
		downloadFrame.setResizable(false);
		downloadFrame.setSize(700, 500);
		downloadFrame.setLocationRelativeTo(null);
		downloadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		v = new VideoFilter();
		v.setResizable(false);
		v.setSize(700, 500);
		v.setLocationRelativeTo(null);
		v.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		
		// initialise all the speed radio buttons and set the 1x speed to the inital speed
		String speeds[] = {"0.5x","1.0x","1.5x","2.0x","2.5x","3.0x"};
		playingSpeed = new JRadioButtonMenuItem[speeds.length];
		
		group = new ButtonGroup();
		
		
		for(int count = 0;count<playingSpeed.length;count++){
			playingSpeed[count] = new JRadioButtonMenuItem(speeds[count]);
			speed.add(playingSpeed[count]);
			group.add(playingSpeed[count]);
			playingSpeed[count].addActionListener(this);
		}
		playingSpeed[1].setSelected(true);
		
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == extractMenuItem) {
			extractFrame.setVisible(true);
		} else if (e.getSource() == editTextMenuItem) {
			editTextFrame.setVisible(true);
		}else if(e.getSource() == replaceAudio){
			replaceAudioFrame.setVisible(true);
		}else if(e.getSource() == makeGif){
			gifFrame.setVisible(true);
		}else if(e.getSource() == extractImagesFromVideo){
			i.setVisible(true);
		}else if(e.getSource() == delete){
			LogFile.delete();
		}else if(e.getSource() == hide){
			LogFile.hide();
		}else if(e.getSource() == download){
			//get the download frame and do the download.
			downloadFrame.setVisible(true);
		}else if(e.getSource() == playingSpeed[0]){
			VideoPlayer.setCurrentRate((float) 0.5);
		}else if(e.getSource() == playingSpeed[1]){
			VideoPlayer.setCurrentRate(1);
		}else if(e.getSource() == playingSpeed[2]){
			VideoPlayer.setCurrentRate((float) 1.5);
		}else if(e.getSource() == playingSpeed[3]){
			VideoPlayer.setCurrentRate(2);
		}else if(e.getSource() == playingSpeed[4]){
			VideoPlayer.setCurrentRate((float) 2.5);
		}else if(e.getSource() == playingSpeed[5]){
			VideoPlayer.setCurrentRate(3);
		}else if(e.getSource() == videoFilters){
			v.setVisible(true);
		}
	}


	@Override
	public void menuCanceled(MenuEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void menuDeselected(MenuEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void menuSelected(MenuEvent arg0) {
		helpFrame.setVisible(true);
		helpFrame.appendReadmeFile();
		
		
	}


	public static void setRadioButton(int j) {
		playingSpeed[j].setSelected(true);
		
	}

	
}
