/*
 * File: Combinations.cpp
 * ----------------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Combinations problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "simpio.h"
using namespace std;

// calculating c(n, k) recursively using Pascal's Triangle
int c(int n, int k) {
	if (n == k || k == 0) 
		return 1;
	else return c(n - 1, k - 1) + c(n - 1, k);
}

// main function calculates c(n, k) combination for every n and k
int main() {
	while(true) {
		int n = getInteger("Enter n: ");
		int k = getInteger("Enter k: ");
		cout << "c(n, k) = " << c(n, k) << endl;
	}
    return 0;
}
