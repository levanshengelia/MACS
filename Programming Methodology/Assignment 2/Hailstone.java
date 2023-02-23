
/*
 * File: Hailstone.java
 * Name: Levan Shengelia
 * Section Leader: Nino Gogoberishvili
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	
	public void run() {
		int n;
		while (true) {
			n = readInt("enter number positive integer: ");
			if (n > 0) {
				break;
			} else {
				println("illegal input");
			}
		}

		int counter = countProcessLength(n);
		println("the process took " + counter + " to reach 1");
	}

	// this method evaluates how many steps does some n need to reach 1
	private int countProcessLength(int n) {
		int result = 0;
		if (n != 1) {

			while (n != 1) {
				// when n is even
				if (n % 2 == 0) {
					println(n + " is even, so I make half: " + n / 2);
					n /= 2;
					// when n is odd
				} else {
					println(n + " is odd, so I make 3n + 1: " + (n * 3 + 1));
					n = n * 3 + 1;
				}
				result++;
			}
		}
		return result;
	}
}
