/*
 * File: FindRange.java
 * Name: Levan Shengelia
 * Section Leader: Nino Gogoberishvili
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	
	private static final int SENTINEL = 0;
	
	// this method prints minimum and maximum numbers among entered numbers
	public void run() {
		boolean instantBreak = false;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		while (true) {
			int num = readInt("enter number: ");
			if (num == SENTINEL) {
				// instantBreak boolean is indicator whether user entered SENTINEL as a first number 
				if (min == Integer.MAX_VALUE) {
					instantBreak = true;
				}
				break;
			}
			if (min > num) {
				min = num;
			}
			if (max < num) {
				max = num;
			}
		}
		if (!instantBreak) {
			println("smallest: " + min + "   largest: " + max);
		} else {
			println("egreve rato daabreake dsma?");
		}
	}
}

