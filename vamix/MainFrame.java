package vamix;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;

import com.sun.jna.Native;


public class MainFrame {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				/**
				 try {
					UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				**/
				
				try {
					setupLibVLC();
				} catch (LibraryNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
				
				ExtendedFrame frame = new ExtendedFrame();
				frame.setResizable(true);
				frame.setSize(800, 600);
				//frame.setSize(600, 600);
				frame.setMinimumSize(new Dimension(500, 400));
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				//int height = screenSize.height;
				//int width = screenSize.width;
				//frame.setSize(width/2, height/2);

				frame.setLocationRelativeTo(null);

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				
				
				
				
				 
			}
		});
	}

	
	private static void setupLibVLC() throws LibraryNotFoundException {

	    new NativeDiscovery().discover();

	    // discovery()'s method return value is WRONG on Linux
	    try {
	        LibVlcVersion.getVersion();
	    } catch (Exception e) {
	        throw new LibraryNotFoundException();
	    }
	}
	
}
