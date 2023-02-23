
/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */


import acm.program.*;
import acm.util.*;

public class Hangman extends ConsoleProgram {
	
	private static final int LIVES = 8;

	private RandomGenerator rgen;
	private int NTURNS;
	private HangmanLexicon lexicon;
	private String word;
	private String wordRem;
	private String currentLabel;
	private HangmanCanvas canvas;

	public void init() {
		// add canvas
		canvas = new HangmanCanvas();
		add(canvas);
		// create HangmanLexicon object
		lexicon = new HangmanLexicon();
		rgen = RandomGenerator.getInstance();
	}

	public void run() {
		play();
	}

	// playing process of the game
	private void play() {
		NTURNS = LIVES;
		canvas.reset();
		writeWelcomeMessage();
		word = chooseRandomWord();
		wordRem = word;
		//println(word);
		currentLabel = getStartingLabel();
		while (true) {
			writeWordCondition();
			if (!currentLabel.contains("-")) {
				break;
			}
			writeNumberOfGuesses();
			guessingChar();
			if (NTURNS == 0) {
				writeWordCondition();
				break;
			}
		}
		askToReset();
	}

	// this method restarts the game if player enters '1'
	private void askToReset() {
		int restart = readInt("Enter 1 to play new game: ");
		while (true) {
			if (restart == 1) {
				play();
			} else {
				restart = readInt("Enter '1' to play new game: ");
			}
		}
	}

	// the process of the guessing the character
	private void guessingChar() {
		char ch = readChar();
		if (word.contains(ch + "")) {
			println("That guess is correct.");
			reformWord(ch);
		} else {
			if (currentLabel.contains(ch + "")) {
				return;
			} else {
				canvas.noteIncorrectGuess(ch);
				println("There are no " + ch + "'s in the word.");
				NTURNS--;
			}
		}
	}

	// this method changes hidden word condition
	private void reformWord(char ch) {
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == ch) {
				word = word.substring(0, i) + '*' + word.substring(i + 1);
				currentLabel = currentLabel.substring(0, i) + ch + currentLabel.substring(i + 1);
			}
		}
	}

	// this method reads player input
	private char readChar() {
		char result;
		String s = readLine("Your guess: ");
		while (true) {
			if (s.length() != 0) {
				result = s.charAt(0);

				if (s.length() == 1 && Character.isAlphabetic(result)) {
					break;
				} else {
					println("illegal input");
					s = readLine("Your guess: ");
				}
			} else {
				println("illegal input");
				s = readLine("Your guess: ");
			}

		}
		return Character.toUpperCase(result);
	}

	// this method informs player how many guesses are left
	private void writeNumberOfGuesses() {
		println("You have " + NTURNS + " guesses left.");

	}

	// this method gives starting label of chosen word
	private String getStartingLabel() {
		String result = "";
		for (int i = 0; i < word.length(); i++) {
			result += "-";
		}
		return result;
	}

	// the method writes current condition of hidden word
	private void writeWordCondition() {
		canvas.displayWord(currentLabel);
		if (!currentLabel.contains("-")) {
			println("You guessed the word: " + currentLabel);
			println("You win.");
		} else if (NTURNS == 0) {
			println("You are completely hung.");
			println("The word was: " + wordRem);
			println("You lose");
		} else {
			println("The word now looks like this: " + currentLabel);
		}
	}

	// this method randomly chooses word from lexicon class
	private String chooseRandomWord() {
		int randomIndex = rgen.nextInt(0, lexicon.getWordCount() - 1);
		String result = lexicon.getWord(randomIndex);
		return result;
	}

	// this method adds welcome message label
	private void writeWelcomeMessage() {
		println("Welcome To Hangman!");
	}

}
