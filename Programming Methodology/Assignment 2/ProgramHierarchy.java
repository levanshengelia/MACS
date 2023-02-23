
/*
 * File: ProgramHierarchy.java
 * Name: Levan Shengelia
 * Section Leader: Nino Gogoberishvili
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class ProgramHierarchy extends GraphicsProgram {

	private static final int RECT_WIDTH = 100;
	private static final int RECT_HEIGHT = 60;
	private static final int Y_OFFSET = 50;
	private static final int X_OFFSET = 50;

	public void run() {
		drawDiagram();
	}

	// this method draws diagram in the centre of the canvas
	private void drawDiagram() {
		drawRectangles();
	}

	// this method draws all 4 rectangles
	private void drawRectangles() {
		drawRect("Program", getWidth() / 2 - RECT_WIDTH / 2, getHeight() / 4);
		drawRect("GraphicsProgram", getWidth() / 2 - 1.5 * RECT_WIDTH - X_OFFSET,
				getHeight() / 4 + RECT_HEIGHT + Y_OFFSET);
		drawRect("ConsoleProgram", getWidth() / 2 - RECT_WIDTH / 2, getHeight() / 4 + RECT_HEIGHT + Y_OFFSET);
		drawRect("DialogProgram", getWidth() / 2 + RECT_WIDTH / 2 + X_OFFSET, getHeight() / 4 + RECT_HEIGHT + Y_OFFSET);
	}

	// this method draws rectangle with some label on a certain location
	private void drawRect(String label, double x, double y) {
		GRect rect = new GRect(RECT_WIDTH, RECT_HEIGHT);
		connectRectangles(x + RECT_WIDTH / 2, y);
		add(rect, x, y);
		setLabel(rect, label);
	}

	// this method connects rectangles with lines
	private void connectRectangles(double x2, double y2) {
		double x1 = getWidth() / 2;
		double y1 = getHeight() / 4 + RECT_HEIGHT;
		if (y2 == getHeight() / 4) {
			return;
		}
		GLine line = new GLine(x1, y1, x2, y2);
		add(line);
	}

	// this method adds certain label in the middle of the rectangle
	private void setLabel(GRect rect, String text) {
		GLabel label = new GLabel(text);
		add(label, rect.getX() + rect.getWidth() / 2 - label.getWidth() / 2,
				rect.getY() + rect.getHeight() / 2 + label.getAscent() / 2);

	}
}
