/*
 * File: InverseGenetics.cpp
 * --------------------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: Ruska Keldishvili]
 * This file is the starter project for the Inverse Genetics problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <string>
#include <fstream>
#include "set.h"
#include "map.h"
#include "console.h"
#include "simpio.h"
#include "strlib.h"
using namespace std;

/* Function: listAllRNAStrandsFor(string protein,
 *                                Map<char, Set<string> >& codons);
 * Usage: listAllRNAStrandsFor("PARTY", codons);
 * ==================================================================
 * Given a protein and a map from amino acid codes to the codons for
 * that code, lists all possible RNA strands that could generate
 * that protein
 */
void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons);

/* 
* This is a wrapper of the listAllRNAStrandsFor function. The only difference is that this function 
* returns Set<string> object, in which all the RNA strands will be stored.
*/
Set<string> listAllRNAStrandsForRec(string protein, Map<char, Set<string> >& codons);

 
// This Function takes Set<string> object as a parameter and prints all the elements of it
void print_RNAStrands(Set<string> RNAStrands);

/* Function: loadCodonMap();
 * Usage: Map<char, Lexicon> codonMap = loadCodonMap();
 * ==================================================================
 * Loads the codon mapping table from a file.
 */
Map<char, Set<string> > loadCodonMap();

int main() {
    /* Load the codon map. */
    Map<char, Set<string> > codons = loadCodonMap();
	while(true) {
		string protein = toUpperCase(getLine("Enter the protein: "));
		listAllRNAStrandsFor(protein, codons);
	}
    return 0;
}

/* You do not need to change this function. */
Map<char, Set<string> > loadCodonMap() {
    ifstream input("codons.txt");
    Map<char, Set<string> > result;

    /* The current codon / protein combination. */
    string codon;
    char protein;

    /* Continuously pull data from the file until all data has been
     * read.
     */
    while (input >> codon >> protein) {
        result[protein] += codon;
    }

    return result;
}

void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons) {
	Set<string> RNAStrands = listAllRNAStrandsForRec(protein, codons);
	print_RNAStrands(RNAStrands);
}

Set<string> listAllRNAStrandsForRec(string protein, Map<char, Set<string> >& codons) {
	Set<string> res;
	if(protein == "") {
		res += "";
	} else {
		char c = protein[0];
		foreach(string str in codons[c]) {
			foreach(string codon in listAllRNAStrandsForRec(protein.substr(1), codons)) {
				res += str + codon;
			}
		}
	}
	return res;
}

void print_RNAStrands(Set<string> RNAStrands) {
	int counter = 1;
	foreach(string strand in RNAStrands) {
		cout << counter << ". " << strand << endl;
		counter++;
	}
}