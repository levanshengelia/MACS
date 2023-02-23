/*
 * File: RandomWriter.cpp
 * ----------------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: Ruska Keldishvili]
 * This file is the starter project for the Random Writer problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include <fstream>
#include <string>
#include "vector.h"
#include "map.h"
#include "simpio.h"
#include "random.h"
using namespace std;

// function prototypes: 
void askUserInput(string & filename, int & k);
void generateText(string filename, int k);
void writeText(Map<string, Vector<char> > & data, int k);
void getData(Map<string, Vector<char> > & data, string filename, int k);
string getStartingWord(Map<string, Vector<char> > & data, int k);

int main() {
    string filename;
	int k;
	askUserInput(filename, k);
	generateText(filename, k);
    return 0;
}

// this function writes new text containing 2000 characters
void writeText(Map<string, Vector<char> > & data, int k) {
	string startingWord = getStartingWord(data, k);
	string currentWord = startingWord;
	cout << startingWord;
	for(int i = 0; i < 2000 - startingWord.length(); i++) {
		if(data[currentWord].isEmpty()) break;
		int randomIndex = randomInteger(0, data[currentWord].size() - 1);
		char c = data[currentWord][randomIndex];
		cout << c;
		currentWord = currentWord.substr(1) + c;
	}
}

// this function returns the word which occures the most of the time in the text file
string getStartingWord(Map<string, Vector<char> > & data, int k) {
	int size = 0;
	string res;
	foreach(string key in data) {
		if(key.length() == k && data[key].size() > size) {
			size = data[key].size();
			res = key;
		}
	}
	return res;
}

// this function generates text based on the statistics of the text characters
void generateText(string filename, int k) {
	Map<string, Vector<char> > data;
	getData(data, filename, k);
	writeText(data, k);
}

// this method collects data in map
void getData(Map<string, Vector<char> > & data, string filename, int k) {
	ifstream infile;
	infile.open(filename.c_str());
	char c;
	int size = 0;
	string sequance = "";
	for (int i = 0; i < k - 1; i++) {
		infile.get(c);
		sequance += c;
	}
	while(infile.get(c)) {
		sequance += c;
		if(!infile.get(c)) break;
		data[sequance] += c;
		sequance = sequance.substr(1);
		infile.unget();
	}
	infile.close();
}

// this function asks user for valid file name and integer k
void askUserInput(string & filename, int & k) {
	ifstream infile;
	while(true) {
		filename = getLine("Enter the source text: ");
		infile.open(filename.c_str());
		if(!infile.fail()) break;
		infile.clear();
		cout << "Illegal file name, try again: " << endl;
	}
	while(true) {
		k = getInteger("Enter the Markov order [1-10]: ");
		if(k >= 1 && k <= 10) break;
		cout << "Illegal order, try again: " << endl;
	}
	cout << "Processing file..." << endl;
}
