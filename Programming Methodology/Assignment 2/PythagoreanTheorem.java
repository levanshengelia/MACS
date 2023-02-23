
/*
 * File: PythagoreanTheorem.java
 * Name: Levan Shengelia
 * Section Leader: Nino Gogoberishvili
 * -----------------------------
 * This file is the starter file for the PythagoreanTheorem problem.
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	public void run() {
		int cathetus1 = getCathetus();
		int cathetus2 = getCathetus();

		println("hypotenuse: " + evaluateHypotenuse(cathetus1, cathetus2));
	}

	// this method evaluates hypotenuse
	private double evaluateHypotenuse(int a, int b) {
		double hypotenuse = Math.sqrt(a * a + b * b);
		return hypotenuse;
	}

	// this method returns valid length of the cathetus
	private int getCathetus() {
		int cathetus;
		while (true) {
			cathetus = readInt("enter the length of the 1st cathetus: ");
			if (cathetus <= 0) {
				println("illegal length of the cathetus");
			} else {
				break;
			}
		}
		return cathetus;
	}
}
