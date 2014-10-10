package vamix;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class VideoFilter extends JFrame implements ItemListener {
	

	private JPanel chooseInput;
	private JPanel showInput;
	private JPanel chooseOutput;
	private JPanel showOutput;
	private JPanel nameOutput;
	private JPanel startProcess;
	private JPanel showProgress;
	private JPanel chooseFilters;
	
	
	
	private JButton chooseInputButton;
	private JLabel showingInput;
	private JButton chooseOutputButton;
	private JLabel showingOutput;
	private JTextField field;
	private JButton start;
	private JProgressBar progress;
	private JLabel selectFilterlabel;
	private JComboBox selectFilter;
	
	private String[] inComboBox = {"Rotate 90 degrees","Rotate 270 degrees","Negate and flip","Select 1/10 frames"};
	
	
	public VideoFilter(){
		setLayout(new GridLayout(8,1));
		
		chooseInput = new JPanel(new FlowLayout());
		chooseInput.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		showInput = new JPanel(new BorderLayout());
		showInput.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		chooseOutput  = new JPanel(new FlowLayout());
		chooseOutput.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		showOutput = new JPanel(new BorderLayout());
		showOutput.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		nameOutput = new JPanel(new FlowLayout());
		nameOutput.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		startProcess = new JPanel(new FlowLayout());
		startProcess.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		showProgress = new JPanel(new FlowLayout());
		showProgress.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		chooseFilters = new JPanel(new BorderLayout());
		chooseFilters.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		
		chooseInputButton = new JButton("Choose Input Video File");
		showingInput = new JLabel("Input File");
		chooseOutputButton = new JButton("Choose Destination of Output");
		showingOutput = new JLabel("Output destination:");
		field = new JTextField(50);
		field.setColumns(50);
		start = new JButton("Add Filter");
		progress = new JProgressBar();
		selectFilterlabel = new JLabel("Select Filter ");
		
		
		selectFilter = new JComboBox(inComboBox);
		selectFilter.setEditable(false);
		
		selectFilter.addItemListener(this);
		
		chooseInput.add(chooseInputButton);
		showInput.add(showingInput,BorderLayout.WEST);
		chooseOutput.add(chooseOutputButton);
		showOutput.add(showingOutput,BorderLayout.WEST);
		nameOutput.add(field);
		startProcess.add(start);
		showProgress.add(progress);
		chooseFilters.add(selectFilterlabel,BorderLayout.WEST);
		chooseFilters.add(selectFilter,BorderLayout.CENTER);
		
		
		add(chooseInput);
		add(showInput);
		add(chooseOutput);
		add(showOutput);
		add(nameOutput);
		add(chooseFilters);
		add(startProcess);
		add(showProgress);
		
		
		
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}

}
