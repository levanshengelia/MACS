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
#include "simpio.h"
#include "strlib.h"
using namespace std;

const int SIMULATION_NUM = 1000; 

// function prototypes:
bool isValid(string str);
string getSequance();
double simulation(string sequance);
void flipCoin(int & flips, string sequance);


int main() {
	while(true) {
		string sequance = getSequance();
		double averageFlips = simulation(sequance);
		cout << "It takes " << averageFlips << " flips on average to get this exact sequance" << endl;
	}
    return 0;
}

// this method simulates coin flip and count on average how many flips are needed to get certain sequance
double simulation(string sequance) {
	int flips = 0;
	for (int i = 0; i < SIMULATION_NUM; i++) {
		flipCoin(flips, sequance);
	}
	return double(flips) / SIMULATION_NUM;
}

// flipping the coin until we get sequance
void flipCoin(int & flips, string sequance) {
	string flipped = "";
	while(true) {
		flips++;
		if(randomChance(0.5)) {
			flipped += 'H';
		} else {
			flipped += 'T';
		}
		if (flipped.length() < sequance.length()) {
			continue;
		} else {
			if(flipped.substr(flipped.length() - sequance.length()) == sequance) {
				return;
			}
		}
	}
}

// asks user to input some sequance of heads(H) and tails(T)
string getSequance() {
	while(true) {
		string sequance = getLine("enter sequance of H and T: ");
		if(isValid(sequance)) return toUpperCase(sequance);
		cout << "invalid sequance, try again." << endl;
	}
}
// check if sequance is valid
bool isValid(string str) {
	str = toUpperCase(str);
	for(int i = 0; i < str.length(); i++) {
		if (str[i] != 'H' && str[i] != 'T') return false;
	}
	return true;
}

