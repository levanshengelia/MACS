/*
 * File: Subsequences.cpp
 * ----------------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: Ruska Keldishvili]
 * This file is the starter project for the Subsequences problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <string>
#include "console.h"
#include "simpio.h"
using namespace std;

/* Given two strings, returns whether the second string is a
 * subsequence of the first string.
 */
bool isSubsequence(string text, string subsequence);

/*
* The main function asks user for two strings: text and subsequence. Then it prints out in the console if 
* entered string is a real subsequence of text string or not by calling isSubsequance function.
*/
int main() {
	while(true) {
		string text = getLine("Enter the text: ");
		string subsequence = getLine("Enter the subsequence: ");
		if(isSubsequence(text, subsequence)) {
			cout << "Yes" << endl;
		} else {
			cout << "No" << endl;
		}
	}
    return 0;
}
/*
*	This function takes two string arguments: text, and subseqence. It checks recursively if character order 
*	in the subsequence string is the same as in the text string, if so function returns true, otherwise 
*  it returns false.
*/
bool isSubsequence(string text, string subsequence) {
	if(subsequence == "") {
		return true;
	} else if (text == "") {
		return subsequence == "";
	} else {
		if(text[0] == subsequence[0]) {
			return isSubsequence(text.substr(1), subsequence.substr(1));
		} else {
			return isSubsequence(text.substr(1), subsequence);
		}
	}
}
