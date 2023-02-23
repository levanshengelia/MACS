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
#include "vector.h"
#include "queue.h"
#include <cmath>
using namespace std;

const int ANGLE = 60;

// Function prototypes:
void draw_Sierpinski_triangle(int length, int order, GWindow & gw, 
							GPoint & p1, GPoint & p2, GPoint & p3);
GPoint midPoint(GPoint & p1, GPoint & p2);
void draw_order0_triangle(GPoint & startingPoint, int length, GWindow & gw,  
						GPoint & p1, GPoint & p2, GPoint & p3);
void draw_line_between_two_points(GPoint & p1, GPoint & p2, GWindow & gw);

struct triangle {
	GPoint p1;
	GPoint p2;
	GPoint p3;
	int edgeSize;
};

void draw_all_triangles(Vector<triangle> triangles, GWindow & gw);


/*
* The main function asks user to input the length and the order of the Sierpinski triangle and 
* calls functions to draw it in the center of the canvas
*/
int main1() {
	int length = getInteger("Enter the length of the edge of the order 0 Sierpinski triangle: ");
	int order = getInteger("Enter the order of the Sierpinski triangle: ");
	GWindow gw;
	GPoint startingPoint(gw.getWidth() / 2 - length / 2, gw.getHeight() / 2 + length  / 2);
	GPoint p1,p2,p3;
	draw_order0_triangle(startingPoint, length, gw, p1, p2, p3);
	draw_Sierpinski_triangle(length, order, gw, p1, p2, p3);
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
void draw_Sierpinski_triangle(int length, int order, GWindow & gw,
							GPoint & p1, GPoint & p2, GPoint & p3) {
	if(order == 0) return;
	order--;
	Vector<triangle> allTriangles;
	Queue<triangle> triangles;
	triangle initial;
	initial.p1 = p1;
	initial.p2 = p2;
	initial.p3 = p3;
	initial.edgeSize = length;
	allTriangles += initial;
	triangles.enqueue(initial);
	while(true) {
		triangle currentTriangle = triangles.dequeue();
		if(currentTriangle.edgeSize == length / pow(2.0, order)) {
			break;
		}
		triangle triangle1;
		triangle1.p1 = currentTriangle.p1;
		triangle1.p2 = midPoint(currentTriangle.p1, currentTriangle.p2);
		triangle1.p3 = midPoint(currentTriangle.p1, currentTriangle.p3);
		triangle1.edgeSize = currentTriangle.edgeSize / 2;
		triangle triangle2;
		triangle2.p1 = midPoint(currentTriangle.p1, currentTriangle.p2);
		triangle2.p2 = currentTriangle.p2;
		triangle2.p3 = midPoint(currentTriangle.p2, currentTriangle.p3);
		triangle1.edgeSize = currentTriangle.edgeSize / 2;
		triangle triangle3;
		triangle3.p1 = midPoint(currentTriangle.p1, currentTriangle.p3);
		triangle3.p2 = midPoint(currentTriangle.p2, currentTriangle.p3);
		triangle3.p3 = currentTriangle.p3;
		triangle1.edgeSize = currentTriangle.edgeSize / 2;
		allTriangles += triangle1, triangle2, triangle3;
		triangles.enqueue(triangle1);
		triangles.enqueue(triangle2);
		triangles.enqueue(triangle3);
	}
	draw_all_triangles(allTriangles, gw);
}

void draw_all_triangles(Vector<triangle> triangles, GWindow & gw) {
	foreach(triangle curTriangle in triangles) {
		GPoint p1 = midPoint(curTriangle.p1, curTriangle.p2);
		GPoint p2 = midPoint(curTriangle.p2, curTriangle.p3);
		GPoint p3 = midPoint(curTriangle.p1, curTriangle.p3);
		draw_line_between_two_points(p1, p2, gw);
		draw_line_between_two_points(p2, p3, gw);
		draw_line_between_two_points(p1, p3, gw);
	}
}

/* 
* This function takes 2 points as arguments and returns the midpoint between those 2 points
*/
GPoint midPoint(GPoint & p1, GPoint & p2) {
	GPoint midpoint((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
	return midpoint;
}

/*
* This function draws order 0 Sierpinski Triangle, which is just a equilateral triangle with size 
* 'length' which it takes as a parameter. It also takes starting point which is bottom left
* vertex of the triangle. p1, p2 and p3 represent all three vertices of the triangle.
*/

void draw_order0_triangle(GPoint & startingPoint, int length, GWindow & gw,
						GPoint & p1, GPoint & p2, GPoint & p3) {
	p2 = gw.drawPolarLine(startingPoint, length, ANGLE);
	p3 = gw.drawPolarLine(p2, length, 360 - ANGLE);
	p1 = gw.drawPolarLine(p3, length, 360 - 3 * ANGLE);
}

// This function draws a line on canvas between two points: p1 and p2 which are passed parameters 
void draw_line_between_two_points(GPoint & p1, GPoint & p2, GWindow & gw) {
	gw.add(new GLine(p1.getX(), p1.getY(), p2.getX(), p2.getY()));
}