
/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the interactors in the
	 * application, and taking care of any other initialization that needs to be
	 * performed.
	 */
	public void init() {
		canvas = new FacePamphletCanvas();
		add(canvas);

		addNorthInteractors();
		addWestInteractors();
		addActionListeners();
	}

	private void addNorthInteractors() {
		JLabel name = new JLabel("Name");
		add(name, NORTH);
		NAMETextField = new JTextField(TEXT_FIELD_SIZE);
		add(NAMETextField, NORTH);
		ADDButton = new JButton("Add");
		add(ADDButton, NORTH);
		DELETEButton = new JButton("Delete");
		add(DELETEButton, NORTH);
		LOOKUPButton = new JButton("Lookup");
		add(LOOKUPButton, NORTH);
	}

	private void addWestInteractors() {
		STATUSTextField = new JTextField(TEXT_FIELD_SIZE);
		STATUSTextField.addActionListener(this);
		add(STATUSTextField, WEST);
		STATUSButton = new JButton("Change Status");
		add(STATUSButton, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		PICTURETextField = new JTextField(TEXT_FIELD_SIZE);
		PICTURETextField.addActionListener(this);
		add(PICTURETextField, WEST);
		PICTUREButton = new JButton("Change Picture");
		add(PICTUREButton, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		FRIENDTextField = new JTextField(TEXT_FIELD_SIZE);
		FRIENDTextField.addActionListener(this);
		add(FRIENDTextField, WEST);
		FRIENDButton = new JButton("Add Friend");
		add(FRIENDButton, WEST);
	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ADDButton) {
			addProfile();
		} else if (e.getSource() == DELETEButton) {
			deleteProfile();
		} else if (e.getSource() == LOOKUPButton) {
			lookUpProfile();
		} else if (e.getSource() == STATUSTextField || e.getSource() == STATUSButton) {
			changeStatus();
		} else if (e.getSource() == PICTURETextField || e.getSource() == PICTUREButton) {
			changeProfilePicture();
		} else if (e.getSource() == FRIENDTextField || e.getSource() == FRIENDButton) {
			addFriend();
		}
	}

	private void changeProfilePicture() {
		if (currentProfile != null) {
			GImage image = null;
			try {
				image = new GImage(PICTURETextField.getText());
				currentProfile.setImage(image);
			} catch (ErrorException ex) {
				canvas.displayProfile(currentProfile);
				canvas.showMessage("Unable to open image file: \"" + PICTURETextField.getText() + "\"");
			}
			if (image != null) {
				canvas.displayProfile(currentProfile);
				canvas.showMessage("Picture updated");
			}
		} else {
			canvas.removeAll();
			canvas.showMessage("Please select a profile to change picture");
		}
		PICTURETextField.setText("");
	}

	// this method changes the status of current profile
	private void changeStatus() {
		if (currentProfile != null) {
			currentProfile.setStatus(STATUSTextField.getText());
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Status updated to \"" + STATUSTextField.getText() + "\"");
		} else {
			canvas.removeAll();
			canvas.showMessage("Please select a profile to change status");
		}
		STATUSTextField.setText("");

	}

	// this method deletes profile and removes everything on canvas
	private void deleteProfile() {
		if (Database.containsProfile(NAMETextField.getText())) {
			Database.deleteProfile(NAMETextField.getText());
			canvas.removeAll();
			canvas.showMessage("Profile of \"" + NAMETextField.getText() + "\" deleted");
			currentProfile = null;
		} else {
			canvas.removeAll();
			canvas.showMessage("A profile with the name \"" + NAMETextField.getText() + "\" does not exist");
		}
		NAMETextField.setText("");
	}

	// this method looks up current profile and displays it on canvas
	private void lookUpProfile() {
		if (Database.containsProfile(NAMETextField.getText())) {
			currentProfile = Database.getProfile(NAMETextField.getText());
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Displaying \"" + NAMETextField.getText() + "\"");
		} else {
			canvas.removeAll();
			canvas.showMessage("A profile with the name \"" + NAMETextField.getText() + "\" does not exist");
			currentProfile = null;
		}

		NAMETextField.setText("");
	}

	// this method adds a person in another's friend list and vice versa
	private void addFriend() {
		if (currentProfile != null) {
			if (!currentProfile.getName().equals(FRIENDTextField.getText())) {
				if (Database.containsProfile(FRIENDTextField.getText())) {
					if (Database.getProfile(FRIENDTextField.getText()).addFriend(currentProfile.getName())) {
						currentProfile.addFriend(FRIENDTextField.getText());
						canvas.displayProfile(currentProfile);
						canvas.showMessage("\"" + FRIENDTextField.getText() + "\" added as a friend");
					} else {
						canvas.displayProfile(currentProfile);
						canvas.showMessage("\"" + currentProfile.getName() + "\" already has " + "\"" + Database.getProfile(FRIENDTextField.getText()).getName() + "\" as a friend");
					}
				} else {
					canvas.displayProfile(currentProfile);
					canvas.showMessage("\"" + FRIENDTextField.getText() + "\" does not exist");
				}
			}
		} else {
			canvas.removeAll();
			canvas.showMessage("Please select a profile to add friend");
		}
		FRIENDTextField.setText("");
	}

	// this method adds new profile if it does not already exist
	private void addProfile() {
		if (!NAMETextField.getText().equals("")) {
			if (!Database.containsProfile(NAMETextField.getText())) {
				Profile = new FacePamphletProfile(NAMETextField.getText());
				currentProfile = Profile;
				Database.addProfile(Profile);
				canvas.displayProfile(currentProfile);
				canvas.showMessage("New Profile created");
				NAMETextField.setText("");
			} else {
				currentProfile = Database.getProfile(NAMETextField.getText());
				canvas.displayProfile(currentProfile);
				canvas.showMessage("A profile with the name \"" + currentProfile.getName() + "\" already exists");
			}
		}
	}

	// instance variables:
	private JTextField NAMETextField;
	private JButton ADDButton;
	private JButton DELETEButton;
	private JButton LOOKUPButton;
	private JTextField STATUSTextField;
	private JButton STATUSButton;
	private JTextField PICTURETextField;
	private JButton PICTUREButton;
	private JTextField FRIENDTextField;
	private JButton FRIENDButton;
	private FacePamphletProfile Profile;
	private FacePamphletDatabase Database = new FacePamphletDatabase();
	private FacePamphletProfile currentProfile;
	private FacePamphletCanvas canvas;
}
