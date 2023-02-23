/*
 * File: WordLadder.cpp
 * --------------------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: Ruska Keldishvili]
 * This file is the starter project for the Word Ladder problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include <string>
#include "simpio.h"
#include "vector.h"
#include "lexicon.h"
#include "queue.h"
#include "set.h"
#include "strlib.h"
using namespace std;
// Function prototypes:
Vector<string> getLadder(string startingWord, string endingWord);
Set<string> getWordNeighbours(string word, Lexicon & lexicon);
string ladderToString(Vector<string> & ladder);

// main function searches word ladder between to words
int main() {
	while (true) {
		string startingWord = getLine("Enter the starting word(or nothing to quit): ");
		if(startingWord == "") break;
		startingWord = toLowerCase(startingWord);
		string endingWord = getLine("Enter the ending word: ");
		endingWord = toLowerCase(endingWord);
		cout << "Searching..." << endl;
		cout << ladderToString(getLadder(toLowerCase(startingWord), endingWord)) << endl;
	}
    return 0;
}

// this function converts vector that represents ladder to string
string ladderToString(Vector<string> & ladder) {
	if(ladder.isEmpty()) return "No word ladder could be found";
	string res = "Ladder found: ";
	for(int i = 0; i < ladder.size(); i++) {
		res += ladder[i];
		if(i != ladder.size() - 1) res += " -> ";
	}
	return res;
}

// this function return the shortest ladder between starting and ending words if it exists
Vector<string> getLadder(string startingWord, string endingWord) {
	Lexicon lexicon("EnglishWords.dat");
	Set<string> usedWords;
	Queue<Vector<string> > ladders;
	Vector<string> vec;
	if(!lexicon.contains(startingWord) || !lexicon.contains(endingWord)) return vec;
	vec += startingWord;
	ladders.enqueue(vec);
	while (!ladders.isEmpty()) {
		Vector<string> ladder = ladders.dequeue();
		if (ladder[ladder.size() - 1] == endingWord) {
			return ladder;
		}
		string lastWord = ladder[ladder.size() - 1];
		Set<string> neighbours = getWordNeighbours(lastWord, lexicon);
		foreach (string neighbour in neighbours) {
			if (!usedWords.contains(neighbour)) {
				usedWords += neighbour;
				Vector<string> newLadder = ladder;
				newLadder += neighbour;
				ladders.enqueue(newLadder);
			}
		}
	}
	vec.clear();
	return vec;
}


// this function returns set of neighbours of the passed word. Two words are neighbours if
// it is possible to change only one letter in the first word and get second.
Set<string> getWordNeighbours(string word, Lexicon & lexicon) {
	Set<string> res;
	for(int i = 0; i < word.length(); i++) {
		for(int j = 'a'; j <= 'z'; j++) {
			string copy = word;
			copy[i] = char(j);
			if(lexicon.contains(copy)) {
				res += copy;
			}
		}
	}
	res.remove(word);
	return res;
}
