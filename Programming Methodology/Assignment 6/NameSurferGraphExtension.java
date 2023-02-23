
/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import acm.util.RandomGenerator;

import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.*;

public class NameSurferGraphExtension extends GCanvas implements NameSurferConstantsExtension, ComponentListener {

	private NameSurferEntry currentEntry;
	private List<Color> colorArr;

	/**
	 * Creates a new NameSurferGraph object that displays the data.
	 */
	public NameSurferGraphExtension() {
		addComponentListener(this);
		colorArr = new ArrayList<>();
		colorArr.add(Color.PINK);
		colorArr.add(Color.YELLOW);
		colorArr.add(Color.ORANGE);
		colorArr.add(Color.RED);
		colorArr.add(Color.CYAN);
		colorArr.add(Color.BLUE);
		colorArr.add(Color.GREEN);
		colorArr.add(Color.GRAY);
		colorArr.add(Color.BLACK);
		colorArr.add(Color.DARK_GRAY);
		colorArr.add(Color.magenta);
	}

	// this method draws graph
	public void addGraph() {
		GLine line1 = new GLine(OFFSET, OFFSET, OFFSET, getHeight() - OFFSET);
		add(line1);
		GLine line2 = new GLine(OFFSET, getHeight() - OFFSET, getWidth() - OFFSET, getHeight() - OFFSET);
		add(line2);
		addScale();
	}

	// this method adds graph's scale
	private void addScale() {
		GLabel scale = new GLabel("1");
		scale.setFont("RockWell-15");
		add(scale, OFFSET / 2 - scale.getWidth() / 2, OFFSET + scale.getHeight() / 2);
		GLine line = new GLine(OFFSET - 5, OFFSET, OFFSET + 5, OFFSET);
		add(line);
		for (int i = 1; i <= SCALE_NUM; i++) {
			scale = new GLabel(Integer.toString(i * MAX_RANK / SCALE_NUM));
			scale.setFont("RockWell-15");
			add(scale, OFFSET / 2 - scale.getWidth() / 2,
					OFFSET + (double) i / SCALE_NUM * (getHeight() - 2 * OFFSET) + scale.getHeight() / 2);
			line = new GLine(OFFSET - 5, OFFSET + (double) i / SCALE_NUM * (getHeight() - 2 * OFFSET), OFFSET + 5,
					OFFSET + (double) i / SCALE_NUM * (getHeight() - 2 * OFFSET));
			add(line);
		}
	}

	// this method adds entry and corresponding graph
	public void addEntry(NameSurferEntry entry) {
		currentEntry = entry;
		update();
	}

	private void addDiagram(NameSurferEntry entry) {
		if (entry == null) {
			return;
		}
		int rectOffset = (getWidth() - 2 * OFFSET) / (3 * NDECADES);
		int rectWidth = ((getWidth() - 2 * OFFSET) - 11 * rectOffset) / NDECADES;
		List<GRect> diagrams = new ArrayList<>();
		for (int i = 0; i < NDECADES; i++) {
			diagrams.add(new GRect(rectWidth,
					(getHeight() - 2 * OFFSET) * (double) (MAX_RANK - entry.getRank(i)) / MAX_RANK));
		}
		double x = OFFSET + rectOffset;
		int counter = 0;
		for (GRect rect : diagrams) {
			if (entry.getRank(counter) == 0) {
				rect.setSize(rect.getWidth(), 0);
			}
			add(rect, x, getHeight() - OFFSET - rect.getHeight());
			rect.setFilled(true);
			rect.setFillColor(colorArr.get(counter));
			x += rectOffset + rectWidth;
			GLabel label = new GLabel(Integer.toString(entry.getRank(counter)));
			label.setFont("RockWell-15");
			if (entry.getRank(counter) == 0) {
				label.setColor(Color.RED);
				label.setLabel("*");
			}
			add(label, rect.getX() + rect.getWidth() / 2 - label.getWidth() / 2, rect.getY() - 5);
			GLabel decade = new GLabel(Integer.toString(START_DECADE + counter * 10));
			decade.setFont("RockWell-15");
			add(decade, rect.getX() + rect.getWidth() / 2 - decade.getWidth() / 2,
					getHeight() - OFFSET + decade.getHeight() + 3);
			counter++;
		}
	}

	/**
	 * Updates the display image by deleting all the graphical objects from the
	 * canvas and then reassembling the display according to the list of entries.
	 * Your application must call update after calling either clear or addEntry;
	 * update is also called whenever the size of the canvas changes.
	 */
	public void update() {
		removeAll();
		addGraph();
		if (currentEntry != null) {
			addDiagram(currentEntry);
		}
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		update();
	}

	public void componentShown(ComponentEvent e) {
	}
}
