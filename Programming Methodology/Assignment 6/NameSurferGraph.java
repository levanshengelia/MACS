
/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.*;

public class NameSurferGraph extends GCanvas implements NameSurferConstants, ComponentListener {

	private List<NameSurferEntry> entries;
	private int counter;

	/**
	 * Creates a new NameSurferGraph object that displays the data.
	 */
	public NameSurferGraph() {
		entries = new ArrayList<>();
		counter = 0;
		addComponentListener(this);
	}

	// this method draws decade sections
	private void drawDecadeSections() {
		double x = 0;
		for (int i = 0; i <= NDECADES; i++) {
			x = i * getWidth() / NDECADES;
			GLine line = new GLine(x, 0, x, getHeight());
			add(line);
			GLabel decade = new GLabel(Integer.toString(START_DECADE + i * 10));
			add(decade, line.getX() + 5, getHeight() - GRAPH_MARGIN_SIZE / 2 + decade.getHeight() / 2);
		}
		GLine line1 = new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		add(line1);
		GLine line2 = new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, getWidth(), getHeight() - GRAPH_MARGIN_SIZE);
		add(line2);
	}

	/**
	 * Clears the list of name surfer entries stored inside this class.
	 */
	public void clear() {
		entries = new ArrayList<>();
		update();
	}

	/* Method: addEntry(entry) */
	/**
	 * Adds a new NameSurferEntry to the list of entries on the display. Note that
	 * this method does not actually draw the graph, but simply stores the entry;
	 * the graph is drawn by calling update.
	 */
	public void addEntry(NameSurferEntry entry) {
		counter++;
		if (entry == null) {
			return;
		}
		entries.add(entry);
		addNameGraph(entry);
	}

	/**
	 * Updates the display image by deleting all the graphical objects from the
	 * canvas and then reassembling the display according to the list of entries.
	 * Your application must call update after calling either clear or addEntry;
	 * update is also called whenever the size of the canvas changes.
	 */
	public void update() {
		counter = 0;
		removeAll();
		drawDecadeSections();
		drawGraph();
	}

	// this method draws graph
	private void drawGraph() {
		for (NameSurferEntry entry : entries) {
			counter++;
			addNameGraph(entry);
		}
	}

	// this method draws graph for a single name
	private void addNameGraph(NameSurferEntry entry) {
		for (int i = 0; i < NDECADES - 1; i++) {
			GLine line = new GLine(i * getWidth() / NDECADES, yCoordinate(entry.getRank(i)),
					(i + 1) * getWidth() / NDECADES, yCoordinate(entry.getRank(i + 1)));
			add(line);
			line.setColor(getColor());
			addLabel(entry.getName(), entry.getRank(i), line.getStartPoint(), line.getColor());
			if (i == NDECADES - 2) {
				addLabel(entry.getName(), entry.getRank(i + 1), line.getEndPoint(), line.getColor());
			}
		}
	}

	// this method sets colour for lines
	private Color getColor() {
		if (counter % 4 == 1) {
			return Color.BLACK;
		} else if (counter % 4 == 2) {
			return Color.RED;
		} else if (counter % 4 == 3) {
			return Color.BLUE;
		} else {
			return Color.YELLOW;
		}
	}

	// this method checks if rank is 0 or not
	private double yCoordinate(int rank) {
		if (rank != 0) {
			return GRAPH_MARGIN_SIZE + (getHeight() - 2 * GRAPH_MARGIN_SIZE) * (double) rank / MAX_RANK;
		}
		return getHeight() - GRAPH_MARGIN_SIZE;
	}

	// this method adds name and rank for every decade
	private void addLabel(String name, int rank, GPoint point, Color color) {
		GLabel label = new GLabel(name + " " + rank);
		if (point.getY() == getHeight() - GRAPH_MARGIN_SIZE) {
			label = new GLabel(name + " *");
		}
		label.setColor(color);
		add(label, point.getX() + 5, point.getY() - 5);
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
