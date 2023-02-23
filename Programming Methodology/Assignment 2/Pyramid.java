
/*
 * File: Pyramid.java
 * Name: Levan Shengelia
 * Section Leader: Nino Gogoberishvili
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;

import java.awt.*;

public class Pyramid extends GraphicsProgram {

	/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

	/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

	/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;
	
	private RandomGenerator rgen;

	public void run() {
		rgen = RandomGenerator.getInstance();
		drawPyramid();
	}

	// this method draws pyramid
	private void drawPyramid() {
		GRect rect;
		int x = BRICKS_IN_BASE;
		int y = x;
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				rect = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				rect.setFilled(true);
				rect.setFillColor(rgen.nextColor());
				add(rect, getWidth() / 2 - x * BRICK_WIDTH / 2 + j * BRICK_WIDTH, getHeight() - i * BRICK_HEIGHT);
			}
			x--;
		}

	}
}
