
/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 50;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 10;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/** Slowness of the game */
	// the lower the speed is the harder the game gets
	private static final int DELAY = 8;

	/** Instant variables: */
	int bricks;
	int tries;
	GRect paddle;
	GOval ball;
	double Vx; // horizontal speed
	double Vy; // vertical speed
	private RandomGenerator rgen = RandomGenerator.getInstance();

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		bricks = NBRICK_ROWS * NBRICKS_PER_ROW;
		tries = NTURNS;
		initialization();
		play();
	}

	// this method initialises static part of the game
	private void initialization() {
		drawBricks();
		drawPaddle();
		addBall();
	}

	// this method draws NBRICK_ROWS brick rows
	private void drawBricks() {
		int rowCounter = 0;
		int y = BRICK_Y_OFFSET;
		Color color = Color.RED;
		for (int i = 0; i < NBRICK_ROWS; i++) {
			rowCounter++;
			drawBrickRow(y, color);
			y += BRICK_HEIGHT + BRICK_SEP;
			color = setProperColor(rowCounter);
		}
	}

	// this method sets colour of the brick row respectively to its number of row
	// RED, ORANGE, YELLOW, GREEN, CYAN, (random colour)
	private Color setProperColor(int rowCounter) {
		if (rowCounter >= 2 && rowCounter < 4) {
			return Color.ORANGE;
		} else if (rowCounter >= 4 && rowCounter < 6) {
			return Color.YELLOW;
		} else if (rowCounter >= 6 && rowCounter < 8) {
			return Color.GREEN;
		} else if (rowCounter >= 8 && rowCounter < 10) {
			return Color.CYAN;
		} else if (rowCounter < 2) {
			return Color.RED;
		}
		return rgen.nextColor();
	}

	// this method draws one brick row with certain colour
	private void drawBrickRow(int y, Color color) {
		int x = BRICK_SEP;
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			GRect brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
			brick.setFilled(true);
			brick.setFillColor(color);
			add(brick);
			x += BRICK_WIDTH + BRICK_SEP;
		}
	}

	// this method draws paddle
	private void drawPaddle() {
		paddle = new GRect(WIDTH / 2 - PADDLE_WIDTH / 2, HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT, PADDLE_WIDTH,
				PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.BLACK);
		add(paddle);
		addMouseListeners(this);
	}

	// this method is in charge of moving paddle with mouse cursor
	public void mouseMoved(MouseEvent e) {
		double x;
		if (e.getX() > WIDTH - PADDLE_WIDTH / 2) {
			x = WIDTH - PADDLE_WIDTH;
		} else if (e.getX() < PADDLE_WIDTH / 2) {
			x = 0;
		} else {
			x = e.getX() - PADDLE_WIDTH / 2;
		}
		paddle.setLocation(x, HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
	}

	// this method adds ball
	private void addBall() {
		ball = new GOval(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.BLACK);
		add(ball);
	}

	// this method runs the dynamic part of the game
	private void play() {
		waitForClick();
		Vx = rgen.nextDouble(1, 3);
		if (rgen.nextBoolean(0.5)) {
			Vx = -Vx;
		}
		Vy = 3;
		// this is the process of the game
		while (tries > 0 && bricks > 0) {
			ball.move(Vx, Vy);
			pause(DELAY);
			checkCollision(ball.getX(), ball.getY());
		}
		removeAll();
		if (tries == 0) {
			giveLosingMessage();
		} else {
			giveWinningMessage();
		}
	}

	// this method finds out whether the ball collided with some object or not
	private void checkCollision(double x, double y) {
		double x1 = x;
		double x2 = x + 2 * BALL_RADIUS;
		double y1 = y;
		double y2 = y + 2 * BALL_RADIUS;
		checkForWalls(x1, x2, y1, y2);
		checkForObjects(x1, x2, y1, y2);

	}

	// this method checks if the ball collided with wall
	private void checkForWalls(double x1, double x2, double y1, double y2) {
		if (x1 < 0) {
			Vx = -Vx;
		} else if (y1 < 0) {
			Vy = -Vy;
		} else if (x2 > WIDTH) {
			Vx = -Vx;
		} else if (y2 > HEIGHT) {
			tries--;
			if(tries == 0) {
				return;
			}
			remove(ball);
			addBall();
			play();
		}
	}

	// this method draw winning label
	private void giveWinningMessage() {
		GLabel label = new GLabel("YOU WON :)");
		label.setFont(new Font("Verdana", Font.PLAIN, 50));
		label.setColor(Color.RED);
		add(label, WIDTH / 2 - label.getWidth() / 2, HEIGHT / 2 + label.getHeight() / 2);
	}
	
	// this method draws losing label
	private void giveLosingMessage() {
		GLabel label = new GLabel("YOU LOST :(");
		label.setFont(new Font("Verdana", Font.PLAIN, 50));
		label.setColor(Color.RED);
		add(label, WIDTH / 2 - label.getWidth() / 2, HEIGHT / 2 + label.getHeight() / 2);
	}

	// this method checks if the ball collided with some object other than wall
	private void checkForObjects(double x1, double x2, double y1, double y2) {
		GObject collider = getCollidingObject(x1, x2, y1, y2);
		if (collider != null) {
			if (collider == paddle) {
				if (ball.getY() >= HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2
						&& ball.getY() <= HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2 + 3) {
					Vy = -Vy;
				}
			} else {
				remove(collider);
				bricks--;
				Vy = -Vy;
			}
		}
	}

	// this method returns colliding object if there is any
	private GObject getCollidingObject(double x1, double x2, double y1, double y2) {
		if (isObject(x1, y1)) {
			return getObject(x1, y1);
		} else if (isObject(x2, y1)) {
			return getObject(x2, y1);
		} else if (isObject(x1, y2)) {
			return getObject(x1, y2);
		} else if (isObject(x2, y2)) {
			return getObject(x2, y2);
		}
		return null;
	}

	// this method returns the object ball collided or null if it did not
	private GObject getObject(double x1, double y1) {
		return getElementAt(x1, y1);
	}

	// this method checks if ball collided with some object
	private boolean isObject(double x1, double y1) {
		if (getElementAt(x1, y1) != null) {
			return true;
		}
		return false;
	}
}
