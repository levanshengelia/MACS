/*
 * File: UniversalHealthCoverageExtension.cpp
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

/*
* This function takes parameters: map of the cities and their populations,
* list of what cities various hospitals can cover and maximum number of hospitals that
* can be built.
* The function return the maximum number of people that can be covered. 
*/
int maximum_covered_people(Map<string, int> & cityPopulations,  
						   Vector< Set<string> > & locations, int numHospitals,
						   Set<string> & coveredCities);

/*
* This function takes set of cities as a parameter and return the total population of
* these cities
*/
int total_population(Set<string> & cities, Map<string, int> & cityPopulations, 
					 Set<string> & coveredCities);

int main1() {
	// TEST CODE
	Map<string, int> cityPopulations;
	cityPopulations["A"] = 500;
	cityPopulations["B"] = 400;
	cityPopulations["C"] = 1000;
	cityPopulations["D"] = 550;
	cityPopulations["E"] = 650;
	cityPopulations["F"] = 700;
    Vector< Set<string> > locations;
    Set<string> s1; 
    Set<string> s2; 
    Set<string> s3;
    Set<string> s4;
	Set<string> s5;
	Set<string> s6;
    s1 += "A", "C";
    s2 += "B";
    s3 += "C";
    s4 += "D";
	s5 += "E";
	s6 += "F";
    locations += s1, s2, s3, s4, s5, s6; 
    int numHospitals = 2;
	Set<string> coveredCities;
	cout << maximum_covered_people(cityPopulations, locations, numHospitals,
		coveredCities) << endl;
    return 0;
}

int maximum_covered_people(Map<string, int> & cityPopulations,  
						   Vector< Set<string> > & locations, int numHospitals, 
						   Set<string> & coveredCities) {
							   if(numHospitals == 0 || locations.isEmpty()) {
									return 0;
							   } else {
									Set<string> temp = coveredCities;
									Set<string> first = locations[0];
									locations.remove(0);
									int maxWithFirst = total_population(first, cityPopulations, coveredCities) +
										maximum_covered_people(cityPopulations,
														locations, numHospitals - 1, coveredCities);
									coveredCities = temp;
									int maxWithoutFirst = maximum_covered_people(cityPopulations,
														locations, numHospitals, coveredCities);
									locations.insert(0, first);
									return max(maxWithFirst, maxWithoutFirst);
							   }
}

int total_population(Set<string> & cities, Map<string, int> & cityPopulations, 
					 Set<string> & coveredCities) {
						 int res = 0;
						 foreach(string city in cities) {
							 if(!coveredCities.contains(city)) {
								res += cityPopulations[city];
							 }
						 }
						 coveredCities += cities;
						 return res;
}