/*
 * File: InverseGeneticsExtension.cpp
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

/*
*	This function takes 3 parameters: RNA strand, protein which is represented by that RNA strand
*	and map of codons. It evaluates the probability probability that a single point mutation to that 
*	strand will cause the strand to encode a different protein
*/
double probability_of_mutation(string RNAStrand, string protein, 
							   Map<char, Set<string> > & codons);

/*
*	This function takes some RNA strand as a parameter and returns the protein which is encoded by 
*	this RNA strand if it exists
*/
string get_protein(string RNAStrand, Map<char, Set<string> > & codons);

/*
* This function asks user to enter the RNA strand and if user enters incorrect RNA strand
* it asks again until user enters correct strand.
*/
void input_RNAStrand(string & RNAStrand, string & protein, Map<char, Set<string> > & codons);

/* Function: loadCodonMap();
 * Usage: Map<char, Lexicon> codonMap = loadCodonMap();
 * ==================================================================
 * Loads the codon mapping table from a file.
 */
Map<char, Set<string> > loadCodonMap1();

int main() {
    /* Load the codon map. */
    Map<char, Set<string> > codons = loadCodonMap1();
	while(true) {
		string RNAStrand;
		string protein;
		input_RNAStrand(RNAStrand, protein, codons);
		double probability = probability_of_mutation(RNAStrand, protein, codons);
		string chance = integerToString(int(probability * 100)) + "%";
		cout << chance << endl;
	}
    return 0;
}

/* You do not need to change this function. */
Map<char, Set<string> > loadCodonMap1() {
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

void input_RNAStrand(string & RNAStrand, string & protein, Map<char, Set<string> > & codons) {
	while(true) {
		RNAStrand = toUpperCase(getLine("Enter RNA strand: "));
		if(RNAStrand.length() != 0 && RNAStrand.length() % 3 == 0) {
			protein = get_protein(RNAStrand, codons);
			if(protein != "") {
				break;
			}
		}
		cout << "Illegal RNA strand" << endl;
	}
}

string get_protein(string RNAStrand, Map<char, Set<string> > & codons) {
	string protein;
	for(int i = 0; i < RNAStrand.length(); i+=3) {
		string temp = "";
		temp += RNAStrand[i];
		temp += RNAStrand[i + 1];
		temp += RNAStrand[i + 2];
		bool isValidStrand = false;
		foreach(char codon in codons) {
			if(codons[codon].contains(temp)) {
				protein += codon;
				isValidStrand = true;
				break;
			}
		}
		if(!isValidStrand) return "";
	}
	return protein;
}

double probability_of_mutation(string RNAStrand, string protein, 
							   Map<char, Set<string> > & codons) {
								   string nucleotides= "ACUG";
								   int allPossibleMutations = 3 * RNAStrand.length();
								   int validMutations = 0;
								   foreach(char c in nucleotides) {
										for(int i = 0; i < RNAStrand.length(); i++) {
											string mutation = RNAStrand;
											mutation[i] = c;
											string mutationProtein = get_protein(mutation, codons);
											if(mutationProtein != protein) {
												validMutations++;
											}
										}
								   }
								   return double(validMutations)/allPossibleMutations;
}
