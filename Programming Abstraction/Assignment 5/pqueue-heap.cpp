/*************************************************************
 * File: pqueue-heap.cpp
 *
 * Implementation file for the HeapPriorityQueue
 * class.
 */
 
#include "pqueue-heap.h"
#include "error.h"
#include <iostream>

int const INITIAL_SIZE = 5;

HeapPriorityQueue::HeapPriorityQueue() {
	capacity = INITIAL_SIZE;
	elements = new string[capacity];
	length = 0;
}

HeapPriorityQueue::~HeapPriorityQueue() {
	delete[] elements;
}

int HeapPriorityQueue::size() {
	return length;
}

bool HeapPriorityQueue::isEmpty() {
	return size() == 0;
}

void HeapPriorityQueue::enqueue(string value) {
	elements[length] = value;
	length++;
	bubble_up();
	if(capacity == length) grow();
}

string HeapPriorityQueue::peek() {
	if(isEmpty()) error("call on empty list");
	return elements[0];
}

string HeapPriorityQueue::dequeueMin() {
	string res = peek();
	elements[0] = elements[size() - 1];
	length--;
	bubble_down();
	return res;
}

void HeapPriorityQueue::grow() {
	capacity *= 2;
	string *temp = new string[capacity];
	for(int i = 0; i < size(); i++) {
		temp[i] = elements[i];
	}
	delete[] elements;
	elements = temp;
}

void HeapPriorityQueue::bubble_up() {
	int index = size() - 1;
	while(true) {
		if(index == 0) return;
		if(elements[index] < elements[(index - 1) / 2]) {
			swap(elements[index], elements[(index - 1) / 2]);
			index = (index - 1) / 2;
		} else {
			break;
		}
	}
}

void HeapPriorityQueue::bubble_down(int index) {
	if(2 * index + 1 >= size()) {
		return;
	} else if(2 * index + 2 >= size()) {
		string child1 = elements[2 * index + 1];
		if(elements[2 * index + 1] < elements[index]) {
			swap(elements[index], elements[2 * index + 1]);
			index = 2 * index + 1;
		} else return;
	} else {
		string parent = elements[index];
		string child1 = elements[2 * index + 1];
		string child2 = elements[2 * index + 2];
		if(parent > child1 || parent > child2) {
			if(child1 < child2) {
				swap(elements[index], elements[2 * index + 1]);
				index = 2 * index + 1;
			} else {
				swap(elements[index], elements[2 * index + 2]);
				index = 2 * index + 2;
			}
			bubble_down(index);
		} else {
			return;
		}
	}
}