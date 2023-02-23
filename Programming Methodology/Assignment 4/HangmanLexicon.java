
/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class HangmanLexicon {

	private ArrayList<String> arr;

	// this constructor reads HangmanLexicon file and saves words in a list
	public HangmanLexicon() {
		arr = new ArrayList<>();
		try {
			BufferedReader rd = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			while (true) {
				String word = rd.readLine();
				if (word == null) {
					break;
				}
				arr.add(word);
			}
			rd.close();
		} catch (Exception e) {
			System.out.println("es programa ar mushaobs mgoni brakia...");
		}
	}

	/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return arr.size();
	}

	/** Returns the word at the specified index. */
	public String getWord(int index) {
		return arr.get(index);
	}
}
