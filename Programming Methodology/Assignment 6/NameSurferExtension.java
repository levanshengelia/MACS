
/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GRect;
import acm.program.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class NameSurferExtension extends Program implements NameSurferConstantsExtension {

	private JButton graph;
	private JTextField textField;
	private NameSurferDataBase data;
	private NameSurferGraphExtension canvas;
	private JComboBox<String> comboBox;
	private List<String> list;

	/* Method: init() */
	/**
	 * This method has the responsibility for reading in the data base and
	 * initializing the interactors at the bottom of the window.
	 */
	public void init() {
		canvas = new NameSurferGraphExtension();
		add(canvas);
		data = new NameSurferDataBase(NAMES_DATA_FILE);
		JLabel label = new JLabel("Name");
		add(label, SOUTH);
		graph = new JButton("Graph");
		textField = new JTextField(20);
		textField.addActionListener(this);
		add(textField, SOUTH);
		add(graph, SOUTH);
		comboBox = new JComboBox<>();
		comboBox.addActionListener(this);
		add(comboBox, SOUTH);
		list = new ArrayList<>();
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
			if (data.findEntry(textField.getText()) != null) {
				if(!list.contains(textField.getText())) {
				list.add(textField.getText());
				comboBox.addItem(textField.getText());
				comboBox.setSelectedIndex(comboBox.getItemCount() - 1);
				} else {
					comboBox.setSelectedIndex(list.indexOf(textField.getText()));
				}
			}
			textField.setText("");
		} else if (e.getSource() == comboBox) {
			canvas.addEntry(data.findEntry(comboBox.getSelectedItem().toString()));
		}
	}
}