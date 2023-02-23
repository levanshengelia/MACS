
/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.ArrayList;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		while (true) {
			if (nPlayers <= MAX_PLAYERS) {
				break;
			}
			nPlayers = dialog.readInt("The maximum number of players is 4");
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	// the process of the game
	private void playGame() {
		int[][] totalScore = new int[nPlayers][3];
		ArrayList<ArrayList<Integer>> chosenCategories = initializeChosenCategories();
		for (int j = 0; j < N_SCORING_CATEGORIES; j++) {
			for (int i = 1; i <= nPlayers; i++) {
				display.printMessage(playerNames[i - 1] + "'s turn! Click 'Roll Dice' button to roll the dice.");
				display.waitForPlayerToClickRoll(i);
				dices = randomizeDices();
				display.displayDice(dices);
				for (int k = 0; k < SELECT_CHANCE; k++) {
					rollAgain();
				}
				display.printMessage("Select a category for this roll");
				int category = display.waitForPlayerToSelectCategory();
				while (true) {
					if (chosenCategories.get(i - 1).contains(category)) {
						display.printMessage("You have already chosen this category, choose a different one");
						category = display.waitForPlayerToSelectCategory();
					} else {
						chosenCategories.get(i - 1).add(category);
						break;
					}
				}
				int score = checkCategory(category);
				if (category >= ONES && category <= SIXES) {
					totalScore[i - 1][0] += score;
				} else {
					totalScore[i - 1][1] += score;
				}
				display.updateScorecard(category, i, score);
				totalScore[i - 1][2] += score;
				display.updateScorecard(TOTAL, i, totalScore[i - 1][2]);
			}
		}
		endGame(totalScore);
	}

	// Initialising array in which chosen categories will be stored
	private ArrayList<ArrayList<Integer>> initializeChosenCategories() {
		ArrayList<ArrayList<Integer>> arr = new ArrayList<>();
		for (int i = 0; i < nPlayers; i++) {
			arr.add(new ArrayList<Integer>());
		}
		return arr;
	}

	// ending the game
	private void endGame(int[][] totalScore) {
		int maxScore = 0;
		String winner = "";
		for (int i = 0; i < totalScore.length; i++) {
			display.updateScorecard(UPPER_SCORE, i + 1, totalScore[i][0]);
			if (totalScore[i][0] >= 63) {
				display.updateScorecard(UPPER_BONUS, i + 1, 35);
				totalScore[i][2] += 35;
			}
			display.updateScorecard(LOWER_SCORE, i + 1, totalScore[i][1]);
			display.updateScorecard(TOTAL, i + 1, totalScore[i][2]);
			if (totalScore[i][2] > maxScore) {
				maxScore = totalScore[i][2];
				winner = playerNames[i];
			}
		}
		display.printMessage(
				"Congratulations, " + winner + ", you're the winner with a total score of " + maxScore + "!");
	}

	// rolling again
	private void rollAgain() {
		display.printMessage("Select the dice you wish to re-roll and click 'Roll Again'");
		display.waitForPlayerToSelectDice();
		changeSelectedDices();
		display.displayDice(dices);
	}

	// this method checks how many points should a player receive for a certain
	// category
	private int checkCategory(int category) {
		int[] frequency = frequency();
		if (category >= ONES && category <= SIXES) {
			return category * frequency[category - 1];
		} else if (category == THREE_OF_A_KIND) {
			if (checkKind(3, frequency)) {
				return sum();
			}
			return 0;
		} else if (category == FOUR_OF_A_KIND) {
			if (checkKind(4, frequency)) {
				return sum();
			}
			return 0;
		} else if (category == FULL_HOUSE) {
			if (checkFullHouse(frequency)) {
				return 25;
			}
			return 0;
		} else if (category == SMALL_STRAIGHT) {
			if (checkStraight(4, frequency)) {
				return 30;
			}
			return 0;
		} else if (category == LARGE_STRAIGHT) {
			if (checkStraight(5, frequency)) {
				return 40;
			}
			return 0;
		} else if (category == YAHTZEE) {
			if (checkKind(5, frequency)) {
				return 50;
			}
			return 0;
		} else if (category == CHANCE) {
			return sum();
		}
		return 0;
	}

	// checking if player rolled Full House
	private boolean checkFullHouse(int[] arr) {
		boolean bool1 = false;
		boolean bool2 = false;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == 2) {
				bool1 = true;
			} else if (arr[i] == 3) {
				bool2 = true;
			}
		}
		return bool1 && bool2;
	}

	// checking if player rolled small or large straight
	private boolean checkStraight(int straightSize, int[] arr) {
		int counter = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] >= 1) {
				counter++;
				if (counter == straightSize) {
					return true;
				}
			} else {
				counter = 0;
			}
		}
		return false;
	}

	// evaluating the sum of rolled dices
	private int sum() {
		int sum = 0;
		for (int i = 0; i < dices.length; i++) {
			sum += dices[i];
		}
		return sum;
	}

	// this method checks for THREE_OF_A_KIND and FOUR_OF_A_KIND
	private boolean checkKind(int kind, int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == kind) {
				return true;
			}
		}
		return false;
	}

	// this method returns frequency array of the elements of "dices" array
	private int[] frequency() {
		int[] frequency = new int[6];
		for (int i = 0; i < dices.length; i++) {
			frequency[dices[i] - 1]++;
		}
		return frequency;
	}

	// changing selected dices
	private void changeSelectedDices() {
		for (int i = 0; i < N_DICE; i++) {
			if (display.isDieSelected(i)) {
				dices[i] = rgen.nextInt(1, 6);
			}
		}
	}

	// dice rolling simulation
	private int[] randomizeDices() {
		dices = new int[N_DICE];
		for (int i = 0; i < dices.length; i++) {
			dices[i] = rgen.nextInt(1, 6);
		}
		return dices;
	}

	/* Private instance variables */
	private int[] dices;
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
}
