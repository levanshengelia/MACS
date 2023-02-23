
/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

	private JButton graph;
	private JButton clear;
	private JTextField textField;
	private NameSurferDataBase data;
	private NameSurferGraph canvas;

	/* Method: init() */
	/**
	 * This method has the responsibility for reading in the data base and
	 * initializing the interactors at the bottom of the window.
	 */
	public void init() {
		data = new NameSurferDataBase(NAMES_DATA_FILE);
		canvas = new NameSurferGraph(); 
		add(canvas);
		canvas.clear();
		JLabel label = new JLabel("Name");
		add(label, SOUTH);
		graph = new JButton("Graph");
		clear = new JButton("Clear");
		textField = new JTextField(20);
		textField.addActionListener(this);
		add(textField, SOUTH);
		add(graph, SOUTH);
		add(clear, SOUTH);
		addActionListeners();
	}

	/* Method: actionPerformed(e) */
	/**
	 * This class is responsible for detecting when the buttons are clicked, so you
	 * will have to define a method to respond to button actions.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == graph || e.getSource() == textField) {
			canvas.addEntry(data.findEntry(textField.getText()));
			textField.setText("");
		} else if (e.getSource() == clear) {
			canvas.clear();
		}
	}
}
