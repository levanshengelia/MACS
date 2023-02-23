/*************************************************************
 * File: pqueue-vector.cpp
 *
 * Implementation file for the VectorPriorityQueue
 * class.
 */
 
#include "pqueue-vector.h"
#include "error.h"

VectorPriorityQueue::VectorPriorityQueue() {
}

VectorPriorityQueue::~VectorPriorityQueue() {
}

int VectorPriorityQueue::size() {
	return vec.size();
}

bool VectorPriorityQueue::isEmpty() {
	return size() == 0;
}

void VectorPriorityQueue::enqueue(string value) {
	vec += value;
}

string VectorPriorityQueue::peek() {
	if(isEmpty()) error("call on ampty list!");
	int index = linear_search();
	string res = vec[index];
	return res;
}

string VectorPriorityQueue::dequeueMin() {
	if(isEmpty()) error("call on ampty list!");
	int index = linear_search();
	string res = vec[index];
	vec.remove(index);
	return res;
}

int VectorPriorityQueue::linear_search() {
	int index = 0;
	string element = vec[0];
	for(int i = 0; i < vec.size(); i++) {
		if(vec[i] < element) {
			element = vec[i];
			index = i;
		}
	}
	return index;
}

