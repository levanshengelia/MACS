/******************************************************************************
 * File: Trailblazer.cpp
 *
 * Implementation of the graph algorithms that comprise the Trailblazer
 * assignment.
 */

#include "Trailblazer.h"
#include "TrailblazerGraphics.h"
#include "TrailblazerTypes.h"
#include "TrailblazerPQueue.h"
#include "random.h"
#include "hashmap.h"
using namespace std;

Vector<Loc> getPath(Map<Loc, Loc> &parents, Loc start, Loc end, Map<Loc, double>& costs) {
	Vector<Loc> vec;
	vec += end;
	while(true) {
		if(end == start) break;
		vec += parents[end];
		end = parents[end];
	}
	Vector<Loc> res;
	for(int i = vec.size() - 1; i >= 0; i--) {
		res += vec[i];
	}
	return res;
}

/* Function: shortestPath
 * 
 * Finds the shortest path between the locations given by start and end in the
 * specified world.	 The cost of moving from one edge to the next is specified
 * by the given cost function.	The resulting path is then returned as a
 * Vector<Loc> containing the locations to visit in the order in which they
 * would be visited.	If no path is found, this function should report an
 * error.
 *
 * In Part Two of this assignment, you will need to add an additional parameter
 * to this function that represents the heuristic to use while performing the
 * search.  Make sure to update both this implementation prototype and the
 * function prototype in Trailblazer.h.
 */
Vector<Loc>
shortestPath(Loc start, Loc end, Grid<double>& world,
             double costFn(Loc from, Loc to, Grid<double>& world), 
			 double heuristic(Loc start, Loc end, Grid<double>& world)) {
	Grid<bool> greenVertices(world.nRows, world.nCols);
	Grid<bool> yellowVertices(world.nRows, world.nCols);
	TrailblazerPQueue<Loc> q;
	Map<Loc, double> costs;
	costs[start] = 0;
	Map<Loc, Loc> parents;
	colorCell(world, start, YELLOW);
	q.enqueue(start, heuristic(start, end, world));
	int counter = 0;
	while(!q.isEmpty()) {
		counter++;
		Loc curr = q.dequeueMin();
		colorCell(world, curr, GREEN);
		greenVertices[curr.row][curr.col] = true;
		if(curr == end) break;
		for(int dRow = -1; dRow <= 1; dRow++) {
			for(int dCol = -1; dCol <= 1; dCol++) {
				int curRow = curr.row + dRow;
				int curCol = curr.col + dCol;
				if(!world.inBounds(curRow, curCol)) continue;
				if(greenVertices[curRow][curCol]) continue;
				Loc newLoc = makeLoc(curRow, curCol);
				double cost = costs[curr] + costFn(curr, newLoc, world);
				if(!yellowVertices[curRow][curCol]) {
					yellowVertices[curRow][curCol] = true;
					colorCell(world, newLoc, YELLOW);
					q.enqueue(newLoc, cost + heuristic(newLoc, end, world));
					parents[newLoc] = curr;
					costs[newLoc] = cost;
				} else if(cost < costs[newLoc]){
					costs[newLoc] = cost;
					q.decreaseKey(newLoc, cost + heuristic(newLoc, end, world));
					parents[newLoc] = curr;
				}
			}
		}
	}
    return getPath(parents, start, end, costs);
}

bool inBounds(int numRows, int numCols, int row, int col) {
	return row < numRows && col < numCols;
}

void addAllEdges(int numRows, int numCols, TrailblazerPQueue<Edge> &edges) {
	for(int i = 0 ; i < numRows; i++) {
		for(int j = 0; j < numCols; j++) {
			if(inBounds(numRows, numCols, i + 1, j)) 
				edges.enqueue(makeEdge(makeLoc(i, j), makeLoc(i + 1, j)), randomReal(0.0, 1.0));
			
			if(inBounds(numRows, numCols, i, j + 1)) 
				edges.enqueue(makeEdge(makeLoc(i, j), makeLoc(i, j + 1)), randomReal(0.0, 1.0));
		}
	}
}

bool areConnected(HashMap<Loc, Loc> &parents, Loc start, Loc end) {
	bool b1, b2;
	while(true) {
		b1 = true;
		b2 = true;
		if(parents.containsKey(end)) {
			end = parents[end];
			b1 = false;
		}
		if(parents.containsKey(start)) {
			start = parents[start];
			b2 = false;
		}
		if(b1 && b2) break;
	}
	if(start != end) {
		parents[end] = start;
	}
	return start == end;
}

Set<Edge> createMaze(int numRows, int numCols) {
	Set<Edge> result;
	TrailblazerPQueue<Edge> edges;
	addAllEdges(numRows, numCols, edges);
	HashMap<Loc, Loc> parents;
	Edge curr;
	while(!edges.isEmpty()) {
		curr = edges.dequeueMin();
		
		if(areConnected(parents, curr.start, curr.end)) continue;
		result += curr;
	}
    return result;
}