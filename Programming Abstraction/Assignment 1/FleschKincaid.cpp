/*
 * File: FleschKincaid.cpp
 * ----------------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Flesch-Kincaid problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include <fstream>
#include "simpio.h"
#include "tokenscanner.h"
#include <cctype>
#include "strlib.h"
using namespace std;
// constants
double const C0 = -15.59;
double const C1 = 0.39;
double const C2 = 11.8;

// data of the file text
struct fileTextDataT {
	int words;
	int sentences;
	int syllables;
};

// function prototypes:
bool isSyllables(string token, int index);
bool isVowel(char c);
bool isNumber(string str);
int countSyllables(string token);
string openFile();
fileTextDataT scanText(string filename);
double grade(int words, int sentences, int syllables);
void countData(int & words, int & sentences, int & syllables, string token);

// main function is evaluating grade for some text file
int main() {
	while(true) {
	string filename = openFile();
	fileTextDataT data = scanText(filename);
	cout << "Words: " << data.words << endl;
	cout << "Syllables: " << data.syllables << endl;
	cout << "Sentences: " << data.sentences << endl;
	cout << "Grade Level: " << grade(data.words, data.sentences, data.syllables) << endl;
	}
	return 0;
}

// this method asks user to input correct file name
string openFile() {
	ifstream infile;
	while (true) {
		string filename = getLine("enter file name: ");
		infile.open(filename.c_str());
		if (!infile.fail()) return filename;
		infile.clear();
		cout << "illegal file name! please try again " << endl;
	}
}
// this method scans text and returns information about words, sentences and syllables
fileTextDataT scanText(string filename) {
	fileTextDataT textData;
	ifstream infile;
	infile.open(filename.c_str());
	TokenScanner scanner(infile);
	scanner.addWordCharacters("'"); 
	scanner.ignoreWhitespace();
	int words = 0;
	int syllables = 0;
	int sentences = 0;
	while(scanner.hasMoreTokens()) {
		string str = scanner.nextToken();
		countData(words, sentences, syllables, str);
	}
	if(words == 0) words++;
	if(sentences == 0) sentences++;
	infile.close();
	textData.words = words;
	textData.syllables = syllables;
	textData.sentences = sentences;
	return textData;
}
// this method checks! if passed token is a word or the end of sentence and counts how many syllables are in it
void countData(int & words, int & sentences, int & syllables, string token) {
	if(token[token.length() - 1] == '.' || token[token.length() - 1] == '?' || token[token.length() -1] == '!') {
		sentences++;
	} else if(isalpha(token[0])) {
		words++;
		int tokenSyllables = countSyllables(token);
		syllables += tokenSyllables;
	}
}
// counting syllables in token
int countSyllables(string token) {
	int vowels = 0;
	for (int i = 0; i < token.length(); i++) {
		if(isSyllables(token, i)) vowels++;
	}
	if(vowels == 0) return 1;
	return vowels;
}
// checks if index'th character in token string creates syllable
bool isSyllables(string token, int index) {
	char c = token[index];
	if(!isVowel(c)) return false;
	if(index == 0) return true;
	if(index == token.length() - 1 && (c == 'e' || c == 'E')) return false;
	if(isVowel(token[index - 1])) return false;
	return true;
}
// checks if passed character is vowel
bool isVowel(char c) {
	return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y' 
		|| c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y';
}
// evaluating grade
double grade(int words, int sentences, int syllables) {
	return C0 + C1 * (double(words) / sentences) + C2 * (double(syllables) / words);
}
