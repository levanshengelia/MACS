/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * At present, the CollectNewspaperKarel subclass does nothing.
 * Your job in the assignment is to add the necessary code to
 * instruct Karel to walk to the door of its house, pick up the
 * newspaper (represented by a beeper, of course), and then return
 * to its initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {
	public void run() {
		getToTheNewspaper();
		pickNewspaper();
		moveBack();
	}
	
	// this method allows Karel to get to the newspaper
	// pre-condition: 3x4, facing east
	// post-condition: 6x3, facing east
	private void getToTheNewspaper() {
		turnRight();
		move();
		turnLeft();
		move();
		move();
		move();	
	}
	
	// in this method Karel picks up the newspaper
	// pre-condition: 6x3, facing east
	// post-condition: 6x3, facing west
	private void pickNewspaper() {
		pickBeeper();
		turnAround();
	}
	
	// this method returns Karel in its initial position
	// pre-condition: 6x3, facing west
	// post-condition: 1x1, facing east
	private void moveBack() {
		move();
		move();
		move();
		turnRight();
		move();
		turnRight();
	}
}
