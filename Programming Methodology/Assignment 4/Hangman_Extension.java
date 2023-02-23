import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.MediaTools;
import acm.util.RandomGenerator;

public class Hangman_Extension extends GraphicsProgram {

	/*
	 * კლავიატურითაც შესაძლებელია თამაში, თუმცა ჯერ კანვასზე ერთხელ კლიკია საჭირო
	 * რომ keyListener-ი გააქტიურდეს. დამატებულია HINT ოფცია, თუ მოთამაშემ 3-ჯერ
	 * ზედიზედ ვერ გამოიცნო ეძლევა მინიშნების გამოყენების შესაძლებლობა. დანარჩენი
	 * ისედაც ჩანს
	 */

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 700;
	private static final int OVAL_SIZE = 50;
	private static final int CHAR_SEP = 10;
	private static final int CHAR_Y = 500;
	private static final int LIVES = 6;
	private static final int HEART_SIZE = 30;
	private static final int HEART_SEP = 10;

	private int life;
	private ArrayList<Character> arr;
	private ArrayList<GOval> ovalArr;
	private ArrayList<GImage> lives;
	private String word;
	private String encryptedWord;
	private GLabel encryptedLabel;
	private char clickedChar;
	private double x;
	private double y;
	private GImage head;
	private GImage body;
	private GImage leftHand;
	private GImage rightHand;
	private GRect newGame;
	private int counter;
	private GRect hint;

	public void init() {
		counter = 0;
		life = LIVES;
		initialiseLivesArray();
		word = chooseWord();
		makeItEmpty();
		arr = new ArrayList<>();
		ovalArr = new ArrayList<>();
		setBackground(Color.CYAN);
		addMouseListeners();
		addScaffold();
		addKeyListeners();
	}

	// this method chooses word randomly from text file
	private String chooseWord() {
		HangmanLexicon lexicon = new HangmanLexicon();
		RandomGenerator rgen = RandomGenerator.getInstance();
		String result = lexicon.getWord(rgen.nextInt(lexicon.getWordCount() - 1));
		return result;
	}

	// this method draws 8 hearts on canvas, as an indicator of number of tries a
	// player has
	private void initialiseLivesArray() {
		lives = new ArrayList<>();
		for (int i = 0; i < LIVES; i++) {
			GImage img = new GImage("heart.png");
			img.setSize(HEART_SIZE, HEART_SIZE);
			lives.add(img);
		}
		for (int i = 0; i < lives.size(); i++) {
			add(lives.get(i), WIDTH - 2 * HEART_SEP - (i + 1) * (lives.get(i).getWidth() + HEART_SEP), 10);
		}
	}

	public void run() {
		setSize(WIDTH, HEIGHT);
		drawLetters();
	}

	// this method adds scaffold
	private void addScaffold() {
		GImage bar1 = new GImage("bar1.png");
		GImage bar2 = new GImage("bar2.png");
		bar2.setSize(150, 20);
		add(bar1, WIDTH / 2 - bar2.getWidth() + 20, CHAR_Y - 100 - bar1.getHeight());
		add(bar2, WIDTH / 2 - bar2.getWidth() + 20, CHAR_Y - 100 - bar1.getHeight());
		GImage rope = new GImage("rope.png");
		rope.setSize(100, 100);
		add(rope, bar2.getX() + bar2.getWidth() - rope.getWidth() / 2 - 20, bar2.getY() + bar2.getHeight() - 10);
		bar2.sendForward();
		x = rope.getX() + rope.getWidth();
		y = rope.getY() + rope.getHeight();
	}

	// this method resets the game
	private void reset() {
		removeAll();
		head = null;
		body = null;
		leftHand = null;
		rightHand = null;
		newGame = null;
		init();
		run();
	}

	// this method initialises ecryptedWord
	private void makeItEmpty() {
		encryptedWord = "";
		for (int i = 0; i < word.length(); i++) {
			encryptedWord += '-';
		}
		encryptedLabel = new GLabel(encryptedWord);
		encryptedLabel.setFont("Rockwell-25");
		encryptedLabel.setColor(Color.RED);
		add(encryptedLabel, WIDTH / 2 - encryptedLabel.getWidth() / 2, CHAR_Y - 50);
	}

	// this method draws letter circles
	private void drawLetters() {
		double y = CHAR_Y;
		int counter = 0;
		for (int i = 0; i < 14; i++) {
			drawChar((char) ('A' + counter), getWidth() / 2 - 7 * OVAL_SIZE - 7 * CHAR_SEP + i * (OVAL_SIZE + CHAR_SEP),
					y);
			counter++;
		}
		y += OVAL_SIZE + CHAR_SEP;
		for (int i = 0; i < 12; i++) {
			drawChar((char) ('A' + counter), getWidth() / 2 - 6 * OVAL_SIZE - 6 * CHAR_SEP + i * (OVAL_SIZE + CHAR_SEP),
					y);
			counter++;
		}
	}

	// this method draws one particular letter circle
	private void drawChar(char letter, double x, double y) {
		arr.add(letter);
		GOval oval = new GOval(x, y, OVAL_SIZE, OVAL_SIZE);
		oval.setFilled(true);
		oval.setColor(Color.YELLOW);
		add(oval);
		ovalArr.add(oval);
		GLabel label = new GLabel(letter + "");
		label.setFont("Rockwell-25");
		label.setColor(Color.RED);
		add(label, oval.getX() + oval.getWidth() / 2 - label.getWidth() / 2,
				oval.getY() + oval.getHeight() / 2 + label.getAscent() / 2 - 3);
	}

	// this method makes visible on which letter the mouse cursor is
	public void mouseMoved(MouseEvent e) {
		checkObject(newGame, e.getX(), e.getY());
		checkObject(hint, e.getX(), e.getY());
		GObject obj = getElementAt(e.getX(), e.getY());
		if (obj == null) {
			resetOval();
		}
		if (ovalArr.contains(obj) && obj.getColor() == Color.YELLOW) {
			obj.setColor(Color.ORANGE);
			obj.move(-5, -5);
			((GOval) obj).setSize(OVAL_SIZE + 10, OVAL_SIZE + 10);
		}
	}

	// this method makes object change colour when it has mouse cursor on it
	private void checkObject(GRect obj, double x, double y) {
		if (obj != null && obj.contains(x, y)) {
			obj.setColor(Color.RED);
		} else if (obj != null) {
			obj.setColor(Color.YELLOW);
		}

	}

	// this method resets colour and dimension of the letter circle after moving
	// mouse cursor away
	private void resetOval() {
		for (int i = 0; i < ovalArr.size(); i++) {
			if (ovalArr.get(i).getColor() == Color.ORANGE) {
				ovalArr.get(i).setColor(Color.YELLOW);
				ovalArr.get(i).setSize(OVAL_SIZE, OVAL_SIZE);
				ovalArr.get(i).move(5, 5);
			}
		}
	}

	// this method makes letter circle gray after clicking on it
	public void mouseClicked(MouseEvent e) {
		GObject obj = getObject(e.getX(), e.getY());
		if (newGame != null && newGame.contains(e.getX(), e.getY())) {
			reset();
		}
		if (hint != null && hint.contains(e.getX(), e.getY())) {
			makeHint();
		}
		if (ovalArr.contains(obj) && obj.getColor() != Color.lightGray) {
			chooseChar(obj);
		}
	}

	// this method makes it visible which characters are already guessed
	private void chooseChar(GObject obj) {
		clickedChar = ((GLabel) getElementAt(obj.getX() + obj.getWidth() / 2, obj.getY() + obj.getHeight() / 2))
				.getLabel().charAt(0);
		resetOval();
		obj.setColor(Color.lightGray);
		checkGuess();
	}

	// this method opens one character
	private void makeHint() {
		for (int i = 0; i < word.length(); i++) {
			if (encryptedWord.charAt(i) == '-') {
				remove(hint);
				hint = null;
				clickedChar = word.charAt(i);
				reformEncryptedWord();
				encryptedLabel.setLabel(encryptedWord);
				return;
			}
		}
	}

	// this method checks if player guessed the letter
	private void checkGuess() {
		if (word.contains(clickedChar + "")) {
			guess();
		} else {
			wrongGuess();
		}
	}

	// case when player guessed the letter correctly
	private void guess() {
		counter = 0;
		reformEncryptedWord();
		encryptedLabel.setLabel(encryptedWord);
		encryptedLabel.setLocation(WIDTH / 2 - encryptedLabel.getWidth() / 2, CHAR_Y - 50);
		if (!encryptedWord.contains("-")) {
			addNewGame();
		}
	}

	// case when a player did not guess the letter correctly
	private void wrongGuess() {
		counter++;
		if (counter == 3) {
			hint();
			counter = 0;
		}
		life--;
		if (life != 0) {
			MediaTools.loadAudioClip("wrongGuess.au").play();
		}
		remove(lives.get(lives.size() - 1));
		lives.remove(lives.size() - 1);
		if (life == LIVES - 1) {
			addHead();
		} else if (life == LIVES - 2) {
			addBody();
		} else if (life == LIVES - 3) {
			addLeftHand();
		} else if (life == LIVES - 4) {
			addRightHand();
		} else if (life == LIVES - 5) {
			addLeftLeg();
		} else if (life == LIVES - 6) {
			addRightLeg();
		}
		if (life == 0) {
			MediaTools.loadAudioClip("losingSound.au").play();
			addNewGame();
		}
	}

	// this method allows player to input characters from keyboard
	public void keyTyped(KeyEvent e) {
		if (Character.isAlphabetic(e.getKeyChar())) {
			clickedChar = Character.toUpperCase(e.getKeyChar());
			for (int i = 0; i < ovalArr.size(); i++) {
				if (newGame == null) {
					char ch = ((GLabel) getElementAt(ovalArr.get(i).getX() + ovalArr.get(i).getWidth() / 2,
							ovalArr.get(i).getY() + ovalArr.get(i).getHeight() / 2)).getLabel().charAt(0);
					if (ch == clickedChar && ovalArr.get(i).getColor() != Color.lightGray) {
						ovalArr.get(i).setColor(Color.lightGray);
						resetOval();
						checkGuess();
						break;
					}
				}
			}
		}
	}

	// this method draws hint button on canvas
	private void hint() {
		hint = new GRect(100, 50);
		hint.setFilled(true);
		hint.setColor(Color.YELLOW);
		add(hint, 20, 100);
		GLabel label = new GLabel("HINT");
		label.setFont("RockWell-20");
		label.setColor(Color.CYAN);
		add(label, hint.getX() + hint.getWidth() / 2 - label.getWidth() / 2,
				hint.getY() + hint.getHeight() / 2 + label.getHeight() / 2 - 5);

	}

	// this method adds new game button after winning or losing
	private void addNewGame() {
		removeAll();
		newGame = new GRect(200, 70);
		newGame.setFilled(true);
		newGame.setColor(Color.YELLOW);
		add(newGame, WIDTH / 2 - newGame.getWidth() / 2, getHeight() / 2 - newGame.getHeight() / 2);
		GLabel label = new GLabel("NEW WORD");
		label.setFont("RockWell-30");
		label.setColor(Color.CYAN);
		add(label, newGame.getX() + newGame.getWidth() / 2 - label.getWidth() / 2,
				newGame.getY() + newGame.getHeight() / 2 + label.getHeight() / 2 - 5);
	}

	// this method adds right leg after sixth incorrect guess
	private void addRightLeg() {
		GImage rightLeg = new GImage("rightLeg.png");
		rightLeg.setSize(20, 50);
		add(rightLeg, x + 30, y - 15);
	}

	// this method adds left leg after fifth incorrect guess
	private void addLeftLeg() {
		GImage leftLeg = new GImage("leftLeg.png");
		leftLeg.setSize(20, 50);
		add(leftLeg, x + 10, y - 7);

	}

	// this method adds right hand after fourth incorrect guess
	private void addRightHand() {
		rightHand = new GImage("rightHand.png");
		rightHand.setSize(30, 30);
		add(rightHand, x + body.getWidth() - 20, y - 65);
		rightHand.sendBackward();
		leftHand.sendToFront();
		rightHand.sendToFront();
	}

	// this method adds left hand after third incorrect guess
	private void addLeftHand() {
		leftHand = new GImage("leftHand.png");
		leftHand.setSize(20, 50);
		add(leftHand, x - leftHand.getWidth() + 10, y - 60);
		leftHand.sendBackward();
	}

	// this method adds head after first incorrect guess
	private void addHead() {
		head = new GImage("head.png");
		head.setSize(50, 50);
		add(head, x - head.getWidth() - 20, y - head.getHeight() - 10);
		x = head.getX();
		y = head.getY() + head.getHeight();
	}

	// this method adds body after second incorrect guess
	private void addBody() {
		body = new GImage("body.png");
		body.setSize(50, 70);
		add(body, x + 15, y - 5);
		head.sendForward();
		x = body.getX();
		y = body.getY() + body.getHeight();
	}

	// case when a player guesses the letter
	private void reformEncryptedWord() {
		for (int i = 0; i < word.length(); i++) {
			if (clickedChar == word.charAt(i)) {
				encryptedWord = encryptedWord.substring(0, i) + clickedChar + encryptedWord.substring(i + 1);
			}
		}
		MediaTools.loadAudioClip("guessedCharSound.au").play();
	}

	// this method returns oval object if player clicks on it
	private GObject getObject(int x, int y) {
		GObject obj = getElementAt(x, y);
		if (obj != null && obj != encryptedLabel) {
			if (obj.getColor() == Color.RED) {
				GObject temp;
				obj.sendBackward();
				temp = obj;
				obj = getElementAt(x, y);
				temp.sendForward();
			}
		}
		return obj;
	}
}
