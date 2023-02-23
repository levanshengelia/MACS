/*
 * File: UniversalHealthCoverage.cpp
 * ----------------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: Ruska Keldishvili]
 * This file is the starter project for the UniversalHealthCoverage problem
 * on Assignment #3.
 * [TODO: extend the documentation]
*/
#include <iostream>
#include <string>
#include "set.h"
#include "vector.h"
#include "console.h"
using namespace std;

// Function prototypes:
bool canOfferUniversalCoverage(Set<string> & cities, Vector< Set<string> > & locations, 
							   int numHospitals, Vector< Set<string> >& result);
bool containsAllCities(Vector< Set<string> >& result, Set<string> cities);

int main() {
	// TEST CODE
	Set<string> cities;
    cities += "A", "B", "C", "D", "E", "F";
    Vector< Set<string> > locations;
    Set<string> s1; 
    Set<string> s2; 
    Set<string> s3;
    Set<string> s4;
    s1 += "A", "B", "C";
    s2 += "A", "C", "D";
    s3 += "B", "F";
    s4 += "C", "E", "F";
    locations += s1, s2, s3, s4;
    int numHospitals = 3;
    Vector<Set<string> > result;
	cout << canOfferUniversalCoverage(cities, locations, numHospitals, result) << endl;
	cout << result << endl;
    return 0;
}

/* Function: canOfferUniversalCoverage(Set<string>& cities,
 *                                     Vector< Set<string> >& locations,
 *                                     int numHospitals,
 *                                     Vector< Set<string> >& result);
 * Usage: if (canOfferUniversalCoverage(cities, locations, 4, result)
 * ==================================================================
 * Given a set of cities, a list of what cities various hospitals can
 * cover, and a number of hospitals, returns whether or not it's
 * possible to provide coverage to all cities with the given number of
 * hospitals.  If so, one specific way to do this is handed back in the
 * result parameter.
 */
bool canOfferUniversalCoverage(Set<string>& cities, Vector< Set<string> >& locations,
                               int numHospitals, Vector< Set<string> >& result) {
	if(numHospitals == 0 || locations.isEmpty()) {
		return false;
	} else {
		Set<string> first = locations[0];
		locations.remove(0);
		result += first;
		canOfferUniversalCoverage(cities, locations, numHospitals - 1, result);
		if(containsAllCities(result, cities)) {
			return true;
		}
		result.remove(result.size() - 1);
		canOfferUniversalCoverage(cities, locations, numHospitals, result);
		if(containsAllCities(result, cities)) {
			return true;
		}
		locations.insert(0, first);
		return false;
	}
}

/*	
 * This function takes two arguments: the vector of hospitals and set of cities.
 * The function returns true if vector of hospitals contains all the cities and false if it does not
*/
bool containsAllCities(Vector< Set<string> >& result, Set<string> cities) {
	foreach(Set<string> set in result) {
		cities -= set;
	}
	return cities.isEmpty();
}