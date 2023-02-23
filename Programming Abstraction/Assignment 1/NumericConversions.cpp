/*
 * File: NumericConversions.cpp
 * ---------------------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Numeric Conversions problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include <string>
#include "console.h"
#include "simpio.h"
using namespace std;

/* Function prototypes */

string intToString(int n);
int stringToInt(string str);
string conversion1(int n);
int conversion2(string str);

// this method converts string into integer
int stringToInt(string str) {
	// special case
	if(str == "0") return 0;
	int res = 1;
	// case when str represents negative integer
	if (str[0] == '-') {
		res = -1;
		str = str.substr(1);
	}
	res = res * conversion2(str);
	return res;
}

// conversion string into integer recursively
int conversion2(string str) {
	if (str == "") 
		return 0;
	else return 10 * conversion2(str.substr(0, str.length() - 1)) + str[str.length() - 1] - '0';
}


// this method converts integer into string
string intToString(int n) {
	// special case
	if(n == 0) return "0";

	string res;
	// case when n is negative
	if (n < 0) {
		n = -n;
		res += '-';
	}
	res += conversion1(n);
	return res;
}
// conversion integer into string recursively
string conversion1(int n) {
	// base case
	if(n < 10) 
		return (string() + char('0' + n));
	// recursive case
	return conversion1(n / 10) + (string() + char('0' + n % 10));
}

/* Main program */

int main() {
	while(true) {
		string str = getLine("enter number as a string: ");
		cout  << stringToInt(str) << endl;
		int n = getInteger("enter number as an integer: ");
		cout << intToString(n) << endl;
	}
	
    return 0;
}
