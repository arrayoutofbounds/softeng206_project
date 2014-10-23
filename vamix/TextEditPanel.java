package vamix;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * This class makes the text edit panel. It designs
 * the GUI and makes the logic for editing text panel. This is the component put underneath
 * the text area where the text added is made input.
 * @author anmol
 *
 */
@SuppressWarnings("serial")
public class TextEditPanel extends JPanel {
	
	private JPanel subPanel; // Might be able to remove this
	private JTextArea textArea;
	private JScrollPane scrollPane;
	protected JTextField timeField;
	private JTextField fontSizeField;
	private JComboBox<String> fontSelectionComboBox;
	protected Font selectedFont;
	protected String selectedFontPath;
	private JComboBox<String> colorComboBox;
	private String[] colorStringArray = {"Black", "Green", "Red", "Blue", "Yellow"};
	private Color[] colorArray = {Color.BLACK, Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW};
	private JCheckBox enableCheckBox;
	private boolean isEnabled = false;
	
	TextEditPanel (String title, Vector<String> fontNames, final ArrayList<Font> fontList) {
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		textArea = new JTextArea(10, 26);
		textArea.setDocument(new JTextAreaLimitedSize(100));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(new JLabel(title));
		scrollPane = new JScrollPane(textArea);
		this.add(scrollPane);
		
		subPanel = new JPanel(new BorderLayout());
		
		timeField = new JTextField(2);
		timeField.setMaximumSize(new Dimension(50, 30));
		timeField.setDocument(new IntegerDocument());
		timeField.setText("10");
		fontSizeField = new JTextField(2);
		fontSizeField.setMaximumSize(new Dimension(50, 30));
		fontSizeField.setDocument(new IntegerDocument());
		fontSizeField.setText("12");
		
		fontSelectionComboBox = new JComboBox<String>(fontNames);
		
		fontSelectionComboBox.addItemListener(new ItemListener() {
			int index = 0;
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				int newIndex = fontSelectionComboBox.getSelectedIndex();
	
				if (newIndex != index) {
					index = newIndex;
					selectedFont = fontList.get(index);
					
					if (fontSizeField.getText().isEmpty()) {
						fontSizeField.setText("12");
					}
					selectedFont = selectedFont.deriveFont(Float.parseFloat(fontSizeField.getText()));
					textArea.setFont(selectedFont);
				}
			}
			
		});
		
		fontSelectionComboBox.setPreferredSize(new Dimension(170, 20));
		
		fontSizeField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedFont = fontList.get(fontSelectionComboBox.getSelectedIndex());
				if (fontSizeField.getText().isEmpty()) {
					fontSizeField.setText("12");
				}
				selectedFont = selectedFont.deriveFont(Float.parseFloat(fontSizeField.getText()));
				textArea.setFont(selectedFont);
				
			}
		});
		
		JPanel p1 = new JPanel();
		enableCheckBox = new JCheckBox("Enable");
		p1.add(enableCheckBox);
		p1.add(new JLabel("Time to show text: "));
		p1.add(timeField);
	
		subPanel.add(p1, BorderLayout.NORTH);
		JPanel p2 = new JPanel();
		p2.add(new JLabel("Font size: "));
		p2.add(fontSizeField);
		colorComboBox = new JComboBox<String>(colorStringArray);
		p2.add(colorComboBox);
		subPanel.add(p2, BorderLayout.CENTER);
	
		JPanel p3 = new JPanel();
		p3.add(new JLabel("Font: "));
		p3.add(fontSelectionComboBox);
		subPanel.add(p3, BorderLayout.SOUTH);
		subPanel.setMaximumSize(new Dimension(2000, 200));
		
		this.add(subPanel);
		this.setBorder(blackline);
		
		colorComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedColorIndex = colorComboBox.getSelectedIndex();
				textArea.setForeground(colorArray[selectedColorIndex]);
				
			}
		});
		
		enableCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isEnabled) {
					isEnabled = false;
				} else {
					isEnabled = true;
				}
			}
		});

	}
	
	public boolean hasText() {
		if (textArea.getText().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	public String getText() {
		return textArea.getText();
	}
	
	public int getFontIndex() {
		return fontSelectionComboBox.getSelectedIndex();
	}
	
	public int getTimeValue() {
		try {
			return Integer.parseInt(timeField.getText());
		} catch (NumberFormatException e) {
			return 10;
		}
	}
	
	public String getColor() {
		return (String)colorComboBox.getSelectedItem();
	}
	
	public int getFontSize() {
		try {
			return Integer.parseInt(fontSizeField.getText());
		} catch (NumberFormatException e) {
			return 12;
		}
	}
	
	public boolean shouldProcess() {
		return isEnabled;
	}
	
	public String[] getSettingsArray() {
		String enabled;
		if (isEnabled) {
			enabled = "1";
		} else {
			enabled = "0";
		}
		String[] settings = {enabled, timeField.getText(), fontSizeField.getText(), colorComboBox.getSelectedIndex() + "", fontSelectionComboBox.getSelectedIndex() + "", "{", textArea.getText(), "}"};
		
		return settings;
	}
	
	public void setSettings(String[] settings) {
		
		if (settings[0].equals("0")) {
			enableCheckBox.setSelected(false);
		} else if (settings[0].equals("1")) {
			enableCheckBox.setSelected(true);
		} else {
			// something went wrong warn user and move on to next setting
		}
		
		timeField.setText(settings[1]);
		fontSizeField.setText(settings[2]);
		try {
			colorComboBox.setSelectedIndex(Integer.parseInt(settings[3]));
		} catch (IllegalArgumentException e) {
			// warn user and move on
		}
		try {
			fontSelectionComboBox.setSelectedIndex(Integer.parseInt(settings[4]));
		} catch (IllegalArgumentException e) {
			// warn user and move on
		}
		
		textArea.setText(settings[5]);
		
		
	}
	
}
