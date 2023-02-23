
/*
 * File: MidpointFindingKarel.java
 * -------------------------------
 * When you finish writing it, the MidpointFindingKarel class should
 * leave a beeper on the corner closest to the center of 1st Street
 * (or either of the two central corners if 1st Street has an even
 * number of corners).  Karel can put down additional beepers as it
 * looks for the midpoint, but must pick them up again before it
 * stops.  The world may be of any size, but you are allowed to
 * assume that it is at least as tall as it is wide.
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {
	public void run() {
		findMiddlePoint();
		putBeeper();
	}

	// Karel gets on the middle square in this method
	// pre-condition: first square of the first street, facing east
	// post-condition: (one of) the middle square of the first street 
	private void findMiddlePoint() {
		fillStreetWithBeepers();
		pickFirstAndLast();
		if (frontIsClear() ) {
			move();
		}
		while (beepersPresent())
			walkOnBeepers();
	}

	// after filling the first street, Karel pick up first and last beepers in this method
	// pre-condition: first square of the first street, facing east
	// post-condition: first square of the first street, facing east
	private void pickFirstAndLast() {
		pickBeeper();
		while (frontIsClear()) {
			move();
		}
		if (beepersPresent()) {
			pickBeeper();
		}
		moveBack();

	}

	// this method allows Karel to walk on beepers and once he walks on empty square,
	// turns around and stands on last beeper
	private void walkOnBeepers() {
		pickBeeper();
		if (frontIsClear()) {
			move();
		}
		while (beepersPresent()) {
			move();
		}
		turnAround();
		if (frontIsClear()) {
			move();
		}
	}

	// this method fills first street with beepers
	// pre-condition: first square of the first street, facing east
	// post-condition: first square of the first street, facing east
	private void fillStreetWithBeepers() {
		while (frontIsClear()) {
			putBeeper();
			move();
		}
		putBeeper();
		moveBack();

	}
	
	// this method returns Karel on the first square from the last square 
	// pre-condition: last square of the first street, facing east
	// post-condition: first square of the first street, facing east
	private void moveBack() {
		turnAround();
		while (frontIsClear()) {
			move();
		}
		turnAround();

	}
}