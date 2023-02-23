
/*
 * File: CheckerboardKarel.java
 * ----------------------------
 * When you finish writing it, the CheckerboardKarel class should draw
 * a checkerboard using beepers, as described in Assignment 1.  You
 * should make sure that your program works for all of the sample
 * worlds supplied in the starter folder.
 */

import stanford.karel.*;

public class midp extends SuperKarel {
	public void run() {
		// for 1 x n worlds
		if (frontIsBlocked()) {
			turnLeft();
			fillLine();
		// for every other world
		} else {
			while (leftIsClear()) {
				fillLine();
				moveBack();
				goUp();
			}
			fillLine();
		}
	}

	// pre-condition: first square of the street, facing east
	// post-condition: first or second square of the street which is one square up,
	// facing east
	// in this method Karel walks to one line up
	private void goUp() {
		turnLeft();
		if (beepersPresent()) {
			move();
			turnRight();
			move();
		} else {
			move();
			turnRight();
		}
	}

	// pre-condition: first square of the street, facing east
	// post-condition: last square of the street, facing east
	// this method fills every other square in the horizontal line with beepers
	private void fillLine() {
		if (frontIsBlocked()) {
			putBeeper();
		}
		while (frontIsClear()) {
			putBeeper();
			move();
			if (frontIsClear()) {
				move();
				if (frontIsBlocked()) {
					putBeeper();
				}
			}

		}
	}

	// pre-condition: last square of the street, facing east
	// post-condition: first square of the street, facing east
	// this method returns Karel in its initial position after filling horizontal
	// line
	private void moveBack() {
		turnAround();
		while (frontIsClear()) {
			move();
		}
		turnAround();
	}
}
