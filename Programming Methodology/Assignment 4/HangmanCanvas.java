
/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import java.util.ArrayList;

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

	private ArrayList<Character> guessedLetters;
	private GLabel letterLabel;
	private GLabel wordLabel;
	private int counter;

	/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		counter = 0;
		guessedLetters = new ArrayList<>();
		letterLabel = new GLabel("");
		add(letterLabel, 50, 400);
		wordLabel = new GLabel("");
		wordLabel.setFont("-20");
		add(wordLabel, 50, 375);
		wordLabel.setFont("RockWell-25");
		drawScaffold();
	}

	// this method draws scaffold
	private void drawScaffold() {
		GLine line1 = new GLine(getWidth() / 2 - BEAM_LENGTH, 2 * getHeight() / 3, getWidth() / 2 - BEAM_LENGTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT);
		add(line1);
		GLine line2 = new GLine(getWidth() / 2 - BEAM_LENGTH, 2 * getHeight() / 3 - SCAFFOLD_HEIGHT, getWidth() / 2,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT);
		add(line2);
		GLine line3 = new GLine(getWidth() / 2, 2 * getHeight() / 3 - SCAFFOLD_HEIGHT, getWidth() / 2,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH);
		add(line3);

	}

	/**
	 * Updates the word on the screen to correspond to the current state of the
	 * game. The argument string shows what letters have been guessed so far;
	 * unguessed letters are indicated by hyphens.
	 */
	public void displayWord(String word) {
		wordLabel.setLabel(word);
	}

	/**
	 * Updates the display to correspond to an incorrect guess by the user. Calling
	 * this method causes the next body part to appear on the scaffold and adds the
	 * letter to the list of incorrect guesses that appears at the bottom of the
	 * window.
	 */
	public void noteIncorrectGuess(char letter) {
		if (!guessedLetters.contains(letter)) {
			guessedLetters.add(letter);
			displayLetters();
		}
		counter++;
		if (counter == 1) {
			drawHead();
		} else if (counter == 2) {
			drawBody();
		} else if (counter == 3) {
			drawLeftHand();
		} else if (counter == 4) {
			drawRightHand();
		} else if (counter == 5) {
			drawLeftLeg();
		} else if (counter == 6) {
			drawRightLeg();
		} else if (counter == 7) {
			drawLeftFoot();
		} else {
			drawRightFoot();
		}
	}

	// this method draws incorrectly guessed letters on the canvas
	private void displayLetters() {
		String letters = "";
		for (int i = 0; i < guessedLetters.size(); i++) {
			letters += guessedLetters.get(i);
		}
		letterLabel.setLabel(letters);
	}

	// this method draws right foot after eighth incorrect guess
	private void drawRightFoot() {
		GLine foot = new GLine(getWidth() / 2 + HIP_WIDTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH,
				getWidth() / 2 + HIP_WIDTH + FOOT_LENGTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(foot);
	}

	// this method draws left foot after seventh incorrect guess
	private void drawLeftFoot() {
		GLine foot = new GLine(getWidth() / 2 - HIP_WIDTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH,
				getWidth() / 2 - HIP_WIDTH - FOOT_LENGTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(foot);
	}

	// this method draws right leg after sixth incorrect guess
	private void drawRightLeg() {
		GLine leg1 = new GLine(getWidth() / 2,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH,
				getWidth() / 2 + HIP_WIDTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH);
		add(leg1);
		GLine leg2 = new GLine(getWidth() / 2 + HIP_WIDTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH,
				getWidth() / 2 + HIP_WIDTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(leg2);
	}

	// this method draws left leg after fifth incorrect guess
	private void drawLeftLeg() {
		GLine leg1 = new GLine(getWidth() / 2,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH,
				getWidth() / 2 - HIP_WIDTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH);
		add(leg1);
		GLine leg2 = new GLine(getWidth() / 2 - HIP_WIDTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH,
				getWidth() / 2 - HIP_WIDTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(leg2);
	}

	// this method draws right hand after fourth incorrect guess
	private void drawRightHand() {
		GLine arm1 = new GLine(getWidth() / 2,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				getWidth() / 2 + UPPER_ARM_LENGTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		add(arm1);
		GLine arm2 = new GLine(getWidth() / 2 + UPPER_ARM_LENGTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				getWidth() / 2 + UPPER_ARM_LENGTH, 2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS
						+ ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
		add(arm2);

	}

	// this method draws left hand after third incorrect guess
	private void drawLeftHand() {
		GLine arm1 = new GLine(getWidth() / 2,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				getWidth() / 2 - UPPER_ARM_LENGTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		add(arm1);
		GLine arm2 = new GLine(getWidth() / 2 - UPPER_ARM_LENGTH,
				2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				getWidth() / 2 - UPPER_ARM_LENGTH, 2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS
						+ ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
		add(arm2);
	}

	// this method draws body after second incorrect guess
	private void drawBody() {
		GLine body = new GLine(getWidth() / 2, 2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS,
				getWidth() / 2, 2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH);
		add(body);
	}

	// this method draws head after first incorrect guess
	private void drawHead() {
		GOval head = new GOval(getWidth() / 2 - HEAD_RADIUS, 2 * getHeight() / 3 - SCAFFOLD_HEIGHT + ROPE_LENGTH,
				2 * HEAD_RADIUS, 2 * HEAD_RADIUS);
		add(head);
	}

	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 200;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 18;
	private static final int BODY_LENGTH = 70;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 40;
	private static final int LOWER_ARM_LENGTH = 15;
	private static final int HIP_WIDTH = 20;
	private static final int LEG_LENGTH = 60;
	private static final int FOOT_LENGTH = 14;
}
