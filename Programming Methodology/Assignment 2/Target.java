/*
 * File: Target.java
 * Name: Levan Shengelia
 * Section Leader: Nino Gogoberishvili
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {	
	
	private static final double BIG_RADIUS = 72;
	private static final double MID_RADIUS = BIG_RADIUS * 1.65 / 2.54;
	private static final double SMALL_RADIUS = BIG_RADIUS * 0.76 / 2.54;
	
	public void run() {
		drawTarget();
	}

	// this method draws target
	private void drawTarget() {
		
		add(getCircle(BIG_RADIUS, Color.RED), getWidth() / 2 - BIG_RADIUS, getHeight() / 2 - BIG_RADIUS);
		add(getCircle(MID_RADIUS, Color.WHITE), getWidth() / 2 - MID_RADIUS, getHeight() / 2 - MID_RADIUS);
		add(getCircle(SMALL_RADIUS, Color.RED), getWidth() / 2 - SMALL_RADIUS, getHeight() / 2 - SMALL_RADIUS);
	}

	// this method draws circle with certain radius and colour
	private GObject getCircle(double radius, Color color) {
		GOval oval = new GOval(2 * radius, 2 * radius);
		oval.setFilled(true);
		oval.setColor(color);
		return oval;
	}
}
