package vamix;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import mediacomponent.LogFile;


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

        tabsPane.add("Download",downloadTab);
        tabsPane.add("Play",playTab);
        
        tabsPane.setSelectedComponent(playTab);
		
		add(tabsPane);
		
		// Create single extract frame, so that the user can close it, and reopen it
		// and maintain extraction progress
		extractFrame = new ExtractFrame();
		extractFrame.setResizable(false);
		extractFrame.setSize(400, 300);
		extractFrame.setLocationRelativeTo(null);
		extractFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Create single edit text frame
		editTextFrame = new EditTextFrame();
		editTextFrame.setResizable(false);
		editTextFrame.setSize(600, 500);
		editTextFrame.setLocationRelativeTo(null);
		editTextFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		replaceAudioFrame = new ReplaceAudio();
		replaceAudioFrame.setResizable(false);
		replaceAudioFrame.setSize(500, 400);
		replaceAudioFrame.setLocationRelativeTo(null);
		replaceAudioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		gifFrame = new Gif();
		gifFrame.setResizable(false);
		gifFrame.setSize(500, 400);
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

	
}
