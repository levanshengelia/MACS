/*
 * File: StoneMasonKarel.java
 * --------------------------
 * The StoneMasonKarel subclass as it appears here does nothing.
 * When you finish writing it, it should solve the "repair the quad"
 * problem from Assignment 1.  In addition to editing the program,
 * you should be sure to edit this comment so that it no longer
 * indicates that the program does nothing.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {
	public void run() {
		repairPillars();
	}

	private void repairPillars() {
		while(frontIsClear()) {
			repairLine();
			moveToTheNextPillar();
		}
		repairLine();
	}
	
	// this method repairs one pillar
	// pre-condition: Karel is standing on the first line, facing east
	// post-condition: same as pre-condition
	private void repairLine() {
		turnLeft();
		while(frontIsClear()) {
			if(noBeepersPresent()) {
				putBeeper();
			}
			move();
		}
		if(noBeepersPresent()) {
			putBeeper();
		}
		moveBack();
	}
	
	// this method returns Karel in initial position after repairing pillar
	private void moveBack() {
		turnAround();
		while(frontIsClear()) {
			move();
		}
		turnLeft();
	}

	// in this method Karel jumps on the next pillar
	private void moveToTheNextPillar() {
		move();
		move();
		move();
		move();
	}
	
}
