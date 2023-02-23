/*
 * File: SubsequencesExtension.cpp
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
#include "set.h"
using namespace std;


//Function prototypes:
void inputWords(Vector<string> & words);
string getSmallestSuperSequence(Vector<string> & words);
string LCS(string s1, string s2);
string SCS(string s1, string s2, string lcs);


int main() {
	Vector<string> words;
	inputWords(words);
	cout << getSmallestSuperSequence(words) << endl;
    return 0;
}

/*
* This function asks user to enter words and stores them in the Vector until user enteres 
* an empty string 
*/
void inputWords(Vector<string> & words) {
	while(true) {
		string word = getLine("Enter the word or nothing to quit: ");
		if(word == "") break;
		words += word;
	}
}

/*
* This function takes Vector as parameter which represents collection of words. Given this collection 
* of words, function recursively finds smallest string possible such that every word in the collection is 
* a subsequence of that string.
*/
string getSmallestSuperSequence(Vector<string> & words) {
	if(words.size() == 0) {
		return "";
	}

	if(words.size() == 1) {
		return words[0];
	} else {
		string s1 = words[0];
		string s2 = words[1];
		words.remove(0);
		words.remove(0);
		words.insert(0, SCS(s1, s2, LCS(s1, s2)));
		return getSmallestSuperSequence(words);
	}
}


/* 
* This function recursively finds smallest super sequence of two concrete strings.
* The function uses the following approach: It takes  2 strings and longest common sequence of 
* those strings as parameters. It saves each character of the LCS in the final answer exactly 
* once and also unique characters of the s1 and s2, maintaining correct order from left to right.
*/
string SCS(string s1, string s2, string lcs) {
	if(lcs == "") {
		return s1 + s2;
	} else {
		if(s1[0] == lcs[0]) {
			if(s2[0] == lcs[0]) {
				return lcs[0] + SCS(s1.substr(1), s2.substr(1), lcs.substr(1));
			} else {
				return s2[0] + SCS(s1, s2.substr(1), lcs);
			}
		} else {
			return s1[0] + SCS(s1.substr(1), s2, lcs);
		}
	}
}

// This function recursively finds longest common sequence of two concrete strings
string LCS(string s1, string s2) {
	if(s1 == "" || s2 == "") {
		return "";
	} else {
		if(s1[0] == s2[0]) {
			return string(s1[0] + LCS(s1.substr(1), s2.substr(1)));
		} else {
			string changeFirst = LCS(s1.substr(1), s2);
			string changeSecond = LCS(s1, s2.substr(1));
			if(changeFirst.length() > changeSecond.length()) {
				return changeFirst;
			} else {
				return changeSecond;
			}
		}
	}
}
