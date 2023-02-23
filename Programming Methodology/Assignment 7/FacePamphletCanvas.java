
/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {

	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas() {
		// You fill this in
	}

	/**
	 * This method displays a message string near the bottom of the canvas. Every
	 * time this method is called, the previously displayed message (if any) is
	 * replaced by the new message text passed in.
	 */
	public void showMessage(String msg) {
		GLabel label = new GLabel(msg);
		label.setFont(MESSAGE_FONT);
		add(label, getWidth() / 2 - label.getWidth() / 2, getHeight() - BOTTOM_MESSAGE_MARGIN);
	}

	/**
	 * This method displays the given profile on the canvas. The canvas is first
	 * cleared of all existing items (including messages displayed near the bottom
	 * of the screen) and then the given profile is displayed. The profile display
	 * includes the name of the user from the profile, the corresponding image (or
	 * an indication that an image does not exist), the status of the user, and a
	 * list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		addName(profile.getName());
		addPicture(profile.getImage());
		addStatus(profile);
		Iterator<String> it = profile.getFriends();
		addFriends(it);
	}

	// this profile adds current profile's friend list on canvas
	private void addFriends(Iterator<String> it) {
		GLabel label = new GLabel("Friends:");
		label.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(label, getWidth() / 2, nameYCoordinate + IMAGE_MARGIN);
		double SPACER = label.getHeight();
		GLabel label2 = new GLabel("");
		while (it.hasNext()) {
			label2 = new GLabel(it.next());
			label2.setFont(PROFILE_FRIEND_FONT);
			add(label2, getWidth() / 2, nameYCoordinate + IMAGE_MARGIN + SPACER);
			SPACER += label2.getHeight();
		}
	}

	// this method adds current profile's status on canvas
	private void addStatus(FacePamphletProfile profile) {
		GLabel label;
		if (profile.getStatus().equals("")) {
			label = new GLabel("No current status");
		} else {
			label = new GLabel(profile.getName() + " is " + profile.getStatus());
		}
		label.setFont(PROFILE_STATUS_FONT);
		add(label, LEFT_MARGIN, nameYCoordinate + IMAGE_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN + label.getAscent());
	}

	// this method adds picture on canvas, if current profile does not have
	// picture, method draws rectangle with "No Image" message in it
	private void addPicture(GImage image) {
		if (image == null) {
			GRect rect = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(rect, LEFT_MARGIN, nameYCoordinate + IMAGE_MARGIN);
			GLabel label = new GLabel("No Image");
			label.setFont(PROFILE_IMAGE_FONT);
			add(label, LEFT_MARGIN + IMAGE_WIDTH / 2 - label.getWidth() / 2,
					rect.getY() + IMAGE_HEIGHT / 2 + label.getAscent() / 2);
		} else {
			image.scale(IMAGE_WIDTH / image.getWidth(), IMAGE_HEIGHT / image.getHeight());
			add(image, LEFT_MARGIN, nameYCoordinate + IMAGE_MARGIN);
		}
	}

	// this method adds current profile's name on canvas
	private void addName(String name) {
		GLabel label = new GLabel(name);
		label.setFont(PROFILE_NAME_FONT);
		label.setColor(Color.BLUE);
		nameYCoordinate = TOP_MARGIN + label.getAscent();
		add(label, LEFT_MARGIN, nameYCoordinate);
	}

	// instance variable:
	private double nameYCoordinate;
}
