
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
import acmx.export.javax.swing.JButton;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Breakout_Extension extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 800;
	public static final int APPLICATION_HEIGHT = 800;

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
	private static final int DELAY = 5;

	/** Instant variables: */
	private GRect rect2;
	private GLabel loading;
	private boolean start;
	private GLabel PLAY;
	private GRect rect;
	private int delay;
	private boolean isPowerUp;
	private int paddleWidth;
	private boolean reset;
	private GRect tryAgainButton;
	private GLabel score;
	private int scoreCounter;
	private GImage heart;
	private GImage background;
	private GImage event;
	private int bricks;
	private int tries;
	private GRect paddle;
	private GOval ball;
	private double Vx; // horizontal speed
	private double Vy; // vertical speed
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private ArrayList<GImage> arr;
	private GLabel timer;
	private int time;

	// this method initialises instance variables
	public void init() {
		start = false;
		delay = DELAY;
		isPowerUp = false;
		paddleWidth = PADDLE_WIDTH;
		arr = new ArrayList<>();
		scoreCounter = 0;
		bricks = NBRICK_ROWS * NBRICKS_PER_ROW;
		tries = NTURNS;
		addMouseListeners();
	}

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		addMenu();
		while (true) {
			addbackGround();
			addHearts();
			initialization();
			play();
			while (true) {
				if (reset) {
					break;
				}
			}
			reset = false;
		}
	}

	// this method adds starting menu in the game
	private void addMenu() {
		addBreakoutPic();
		addLoadingBar();
		while (!start) {
			// wait player to start
		}
	}

	// this method adds loading bar in menu
	private void addLoadingBar() {
		rect = new GRect(250, 50);
		rect.setFilled(true);
		rect.setColor(Color.WHITE);
		add(rect, WIDTH / 2 - rect.getWidth() / 2, (HEIGHT * 3) / 4);
		addLoadingLabel(rect);
		addFilling();
		addPlayLabel(rect);
	}

	// this method adds play label after loading
	private void addPlayLabel(GRect rect) {
		PLAY = new GLabel("PLAY");
		PLAY.setFont(new Font("Verdana", Font.PLAIN, 25));
		PLAY.setColor(Color.WHITE);
		add(PLAY, WIDTH / 2 - PLAY.getWidth() / 2, (HEIGHT * 3) / 4 + rect.getHeight() / 2 + PLAY.getAscent() / 2);
	}

	// this method adds filling simulation of the loading bar
	private void addFilling() {
		rect2 = new GRect(0, 50);
		rect2.setFilled(true);
		rect2.setColor(Color.GRAY);
		add(rect2, WIDTH / 2 - 125, (HEIGHT * 3) / 4);
		for (int i = 0; i <= 250; i++) {
			if (i == 200) {
				pause(100);
			} else if (i == 230) {
				pause(250);
			}
			rect2.setSize(i, 50);
			pause(20);
		}
		remove(loading);
		loading = null;
	}

	// this method draws LOADING label
	private void addLoadingLabel(GRect rect) {
		loading = new GLabel("LOADING...");
		loading.setFont(new Font("Verdana", Font.PLAIN, 25));
		loading.setColor(Color.WHITE);
		add(loading, WIDTH / 2 - loading.getWidth() / 2 + 15, (HEIGHT * 3) / 4 - 20);

	}

	private void addBreakoutPic() {
		GImage image = new GImage("breakout_pic.jpg");
		image.setSize(WIDTH, HEIGHT);
		add(image);
	}

	// this method draws hearts
	private void addHearts() {
		for (int i = 1; i <= tries; i++) {
			heart = new GImage("heart.png");
			heart.setSize(WIDTH / 10, 50);
			arr.add(heart);
			add(heart, WIDTH - i * heart.getWidth(), 10);
		}
	}

	// this method adds black background
	private void addbackGround() {
		background = new GImage("Black_Background.png");
		background.setSize(WIDTH + 50, HEIGHT + 50);
		add(background);
	}

	// this method initialises static part of the game
	private void initialization() {
		updateScore();
		drawBricks();
		drawPaddle();
		addBall();
	}

	// this method updates score (player receive one point for one brick
	private void updateScore() {
		if (scoreCounter != 0) {
			remove(score);
		}
		score = new GLabel("SCORE:  " + scoreCounter);
		score.setFont(new Font("Verdana", Font.PLAIN, 30));
		score.setColor(Color.GREEN);
		add(score, 10, 40);
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
			pause(5);
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
		paddle.setColor(Color.GREEN);
		add(paddle);
	}

	// this method is in charge of moving paddle with mouse cursor
	public void mouseMoved(MouseEvent e) {
		double x;
		if (paddle != null) {
			if (e.getX() > WIDTH - paddle.getWidth() / 2) {
				x = WIDTH - paddle.getWidth();
			} else if (e.getX() < paddle.getWidth() / 2) {
				x = 0;
			} else {
				x = e.getX() - paddle.getWidth() / 2;
			}
			if (paddle != null) {
				paddle.setLocation(x, HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
			}
		}
		// NEW GAME button
		if (tryAgainButton != null) {
			if (tryAgainButton.contains(e.getX(), e.getY())) {
				tryAgainButton.setColor(Color.WHITE);
			} else {
				tryAgainButton.setColor(Color.DARK_GRAY);
			}
		}

		if (rect2 != null && PLAY != null) {
			if (rect2.contains(e.getX(), e.getY())) {
				rect2.setColor(Color.RED);
			} else {
				rect2.setColor(Color.GRAY);
			}
		}
	}

	// this method catches the moment when player clicks on NEW GAME button and
	// resets the game
	public void mouseClicked(MouseEvent e) {
		if (tryAgainButton != null && tryAgainButton.contains(e.getX(), e.getY())) {
			removeAll();
			tryAgainButton = null;
			init();
			reset = true;
		}
		if (rect != null && PLAY != null) {
			if (rect.contains(e.getX(), e.getY())) {
				removeAll();
				rect2 = null;
				rect = null;
				PLAY = null;
				start = true;
			}
		}
	}

	// this method adds ball
	private void addBall() {
		MediaTools.loadAudioClip("gameStartingSound.au").play();
		pause(100);
		ball = new GOval(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.GREEN);
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
			pause(delay);
			checkCollision(ball.getX(), ball.getY());
			if (time != 0 && time % 500 == 0) {
				timer.setLabel("" + (time / 500));
			}
			if (time != 0) {
				time -= 5;
				if (time == 0) {
					resetEvent();
				}
			}
		}
		removeAll();
		addbackGround();
		addTryAgainButton();
		if (tries == 0) {
			giveMessage("YOU LOST");
			MediaTools.loadAudioClip("losingSound.au").play();
		} else {
			giveMessage("YOU WON");
			MediaTools.loadAudioClip("mama.au").play(); //
		}
	}

	// this method resets events
	private void resetEvent() {
		time = 0;
		if (timer != null) {
			remove(timer);
			timer = null;
		}
		if (paddleWidth != PADDLE_WIDTH) {
			paddleWidth = PADDLE_WIDTH;
		} else if (delay != DELAY) {
			delay = DELAY;
		} else if (isPowerUp) {
			isPowerUp = false;
		}
		paddle.setSize(paddleWidth, PADDLE_HEIGHT);
	}

	// this method adds event timer on canvas
	private void addEventTimer() {
		timer = new GLabel("");
		timer.setFont(new Font("Verdana", Font.PLAIN, 30));
		timer.setColor(Color.RED);
		add(timer, 10 + score.getWidth() + 40, 40);
	}

	// this method draws try again button
	private void addTryAgainButton() {
		tryAgainButton = new GRect(150, 50);
		tryAgainButton.setFilled(true);
		tryAgainButton.setColor(Color.DARK_GRAY);
		add(tryAgainButton, WIDTH / 2 - tryAgainButton.getWidth() / 2, (HEIGHT * 3) / 4);
		GLabel tryAgain = new GLabel("NEW GAME");
		tryAgain.setFont(new Font("Verdana", Font.PLAIN, 20));
		tryAgain.setColor(Color.RED);
		add(tryAgain, WIDTH / 2 - tryAgain.getWidth() / 2,
				(HEIGHT * 3) / 4 + tryAgainButton.getHeight() / 2 + tryAgain.getAscent() / 2);
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
			resetEvent();
			if (tries == 0) {
				return;
			}
			remove(arr.get(arr.size() - 1));
			arr.remove(arr.size() - 1);
			remove(ball);
			addBall();
			play();
		}
	}

	// this method draw winning or losing label
	private void giveMessage(String text) {
		GLabel label = new GLabel(text);
		label.setFont(new Font("Verdana", Font.PLAIN, 20));
		label.setColor(Color.RED);
		add(label, WIDTH / 2 - label.getWidth() / 2, HEIGHT / 2 - label.getHeight() / 2);
		addFinalScore();
	}

	// this method draws final score at the end of the game
	private void addFinalScore() {
		GLabel finalScore = new GLabel("YOUR FINAL SCORE: " + scoreCounter);
		finalScore.setFont(new Font("Verdana", Font.PLAIN, 20));
		finalScore.setColor(Color.RED);
		add(finalScore, WIDTH / 2 - finalScore.getWidth() / 2, HEIGHT / 2 + finalScore.getHeight() / 2);
	}

	// this method checks if the ball collided with some object other than wall
	private void checkForObjects(double x1, double x2, double y1, double y2) {
		GObject collider = getCollidingObject(x1, x2, y1, y2);
		if (collider != null && collider != background && collider != score && !arr.contains(collider)
				&& collider != timer) {
			if (collider == event) {
				randomEvent();
				remove(event);
			} else if (collider == paddle) {
				if (ball.getY() >= HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2
						&& ball.getY() <= HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2 + 3) {
					MediaTools.loadAudioClip("paddleHitSound.au").play();
					Vy = -Vy;
				}
			} else {
				remove(collider);
				MediaTools.loadAudioClip("brickHitSound.au").play();
				bricks--;
				int bonus = getBonus((GRect) collider);
				scoreCounter += bonus;
				if (rgen.nextBoolean(0.1)) {
					addEvent();
				}
				updateScore();
				if (!isPowerUp) {
					Vy = -Vy;
				}
			}
		}
	}

	// this method finds out how many point should player receive depending on the
	// colour of the broken brick
	private int getBonus(GRect collider) {
		Color c = collider.getFillColor();
		if (c == Color.CYAN) {
			return 1;
		} else if (c == Color.GREEN) {
			return 2;
		} else if (c == Color.YELLOW) {
			return 3;
		} else if (c == Color.ORANGE) {
			return 4;
		} else if (c == Color.RED) {
			return 5;
		} else {
			return 1;
		}
	}

	// this method adds event image on canvas
	private void addEvent() {
		if (event != null) {
			remove(event);
		}
		event = new GImage("question_mark.png");
		event.setSize(WIDTH / 15, WIDTH / 15);
		add(event, rgen.nextDouble(0, WIDTH - event.getWidth()), getHeight() / 2 - event.getHeight());
	}

	// this method creates random event after picking the event image
	private void randomEvent() {
		resetEvent();
		int random = rgen.nextInt(0, 5);
		if (random == 0) { // 10 seconds
			time = 5000;
			paddleWidth *= 2;
			paddle.setSize(paddleWidth, PADDLE_HEIGHT);
		} else if (random == 1) { // 10 seconds
			time = 5000;
			paddleWidth /= 2;
			paddle.setSize(paddleWidth, PADDLE_HEIGHT);
		} else if (random == 3) { // 3 seconds
			time = 1500;
			isPowerUp = true;
		} else if (random == 4) { // 20 seconds
			time = 10000;
			delay += 3;
		} else if (random == 5) { // 20 seconds
			time = 10000;
			delay -= 3;
		}
		addEventTimer();
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
		return background;
	}

	// this method returns object on certain coordinates
	private GObject getObject(double x1, double y1) {
		return getElementAt(x1, y1);
	}

	// this method checks if ball collided with some object
	private boolean isObject(double x1, double y1) {
		if (getElementAt(x1, y1) != background) {
			return true;
		}
		return false;
	}
}
