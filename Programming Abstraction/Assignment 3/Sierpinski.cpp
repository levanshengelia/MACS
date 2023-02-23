/*
 * File: Sierpinski.cpp
 * --------------------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: Ruska Keldishvili]
 * This file is the starter project for the Sierpinski problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include "gwindow.h"
#include "simpio.h"
#include "gobjects.h"
using namespace std;

const int ANGLE = 60;

// Function prototypes:
void drawSierpinskiTriangle(int length, int order, GWindow & gw, 
							GPoint & p1, GPoint & p2, GPoint & p3);
GPoint midPoint(GPoint & p1, GPoint & p2);
void drawOrder0Triangle(GPoint & startingPoint, int length, GWindow & gw,  
						GPoint & p1, GPoint & p2, GPoint & p3);
void drawLineBetweenTwoPoints(GPoint & p1, GPoint & p2, GWindow & gw);

/*
* The main function asks user to input the length and the order of the Sierpinski triangle and 
* calls functions to draw it in the center of the canvas
*/
int main() {
	int length = getInteger("Enter the length of the edge of the order 0 Sierpinski triangle: ");
	int order = getInteger("Enter the order of the Sierpinski triangle: ");
	GWindow gw;
	GPoint startingPoint(gw.getWidth() / 2 - length / 2, gw.getHeight() / 2 + length / 2);
	GPoint p1,p2,p3;
	drawOrder0Triangle(startingPoint, length, gw, p1, p2, p3);
	drawSierpinskiTriangle(length, order, gw, p1, p2, p3);
    return 0;
}
  
/*
*  This function draws Sierpinski Triangle recursively.
*	It takes 6 arguments:  length of the edge of the triangle, order of the triangle, gwindow object and 
*	three GPoint objects which represent all vertices of the triangle. 
*	p1 - bottom left
*  p2 - top
*  p3 - bottom right
*  In each recursive step function finds middle points of the edges and connects them
*/
void drawSierpinskiTriangle(int length, int order, GWindow & gw,
							GPoint & p1, GPoint & p2, GPoint & p3) {
	if(order == 0) {
		return;
	} else {
		GPoint p4 = midPoint(p1, p2);
		GPoint p5 = midPoint(p2, p3);
		GPoint p6 = midPoint(p1, p3);
		drawLineBetweenTwoPoints(p4, p5, gw);
		drawLineBetweenTwoPoints(p5, p6, gw);
		drawLineBetweenTwoPoints(p4, p6, gw);
		drawSierpinskiTriangle(length / 2, order - 1, gw, p1, p4, p6);
		drawSierpinskiTriangle(length / 2, order - 1, gw, p4, p2, p5);
		drawSierpinskiTriangle(length / 2, order - 1, gw, p6, p5, p3);
	}
}

 
// This function takes 2 points as arguments and returns the midpoint between those 2 points
GPoint midPoint(GPoint & p1, GPoint & p2) {
	GPoint midpoint((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
	return midpoint;
}

/*
* This function draws order 0 Sierpinski Triangle, which is just a equilateral triangle with size 
* 'length' which it takes as a parameter. It also takes starting point which is bottom left
* vertex of the triangle. p1, p2 and p3 represent all three vertices of the triangle.
*/
void drawOrder0Triangle(GPoint & startingPoint, int length, GWindow & gw,
						GPoint & p1, GPoint & p2, GPoint & p3) {
	p2 = gw.drawPolarLine(startingPoint, length, ANGLE);
	p3 = gw.drawPolarLine(p2, length, 360 - ANGLE);
	p1 = gw.drawPolarLine(p3, length, 360 - 3 * ANGLE);
}

// This function draws a line on canvas between two points: p1 and p2 which are passed parameters 
void drawLineBetweenTwoPoints(GPoint & p1, GPoint & p2, GWindow & gw) {
	gw.add(new GLine(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
}


