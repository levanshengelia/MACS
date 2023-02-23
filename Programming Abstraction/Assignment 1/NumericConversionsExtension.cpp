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
string decimalToHexadecimal(int n); 
int hexadecimalToDecimal(string str);
int hexToDigit(char c);
string intToHex(int n);

/* Main program */
// main function converts hexadecimal number to decimal number and vice versa
int main() {
	while(true) {
		int n = getInteger("enter decimal number: ");
		cout << "decimal > hexadecimal: " << decimalToHexadecimal(n) << endl;
		string str = getLine("enter hexadecimal number: ");
		cout << "hexadecimal > decimal: " << hexadecimalToDecimal(str) << endl;
	}
	return 0;
}

// method converts decimal number to hexadecimal number
string decimalToHexadecimal(int n) {
	if(n / 16 == 0) {
		return intToHex(n % 16);
	} else {
		return decimalToHexadecimal(n / 16) + intToHex(n % 16);
	}
}

// converting integer to hexadecimal digit
string intToHex(int n) {
	if (n >= 0 && n <= 10) {
		return string() + char(n + '0');
	} else {
		return string() + char(n - 10 + 'A');
	}
}

// method converts hexadecimal number to decimal number
int hexadecimalToDecimal(string str) {
	if (str == "") {
		return 0;
	} else {
		return 16 * hexadecimalToDecimal(str.substr(0, str.length() - 1)) + hexToDigit(str[str.length() - 1]);
	}
}
// converts hex digit to decimal digit
int hexToDigit(char c) {
	if(c >= '0' && c <= '9') return (c - '0');
	return 10 + (c - 'A');
}
