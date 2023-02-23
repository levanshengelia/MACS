/*
 * File: ConsecutiveHeads.cpp
 * --------------------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Consecutive Heads problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "random.h"
using namespace std;

// main function simulates fair coin flips and prints how many flips are needed
// to get 3 heads in a row
int main() {
	int counter = 0;
	int heads = 0;
	while (true) {
		counter++;
		if(randomChance(0.5)) { 
			heads++;
			cout << "heads" << endl;
			if(heads == 3)
				break;
		}
		else {
			heads = 0;
			cout << "tails" << endl;
		}
	}
	cout << "It took " << counter << " flips to get 3 consecutive heads." << endl;
    return 0;
}
