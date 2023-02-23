/*************************************************************
 * File: pqueue-linkedlist.cpp
 *
 * Implementation file for the LinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-linkedlist.h"
#include "error.h"
#include <iostream>
LinkedListPriorityQueue::LinkedListPriorityQueue() {
	length = 0;
	head = new ListNode;
	head->next = NULL;
}

LinkedListPriorityQueue::~LinkedListPriorityQueue() {
	ListNode* temp = head;
	while(temp != NULL) {
		ListNode* nextNode = temp->next;
		delete temp;
		temp = nextNode;
	}
}

int LinkedListPriorityQueue::size() {
	return length;
}

bool LinkedListPriorityQueue::isEmpty() {
	return size() == 0;
}

// Complexity: O(n)
void LinkedListPriorityQueue::enqueue(string value) {
	length++;
	ListNode* nodeToBeAdded = new ListNode;
	nodeToBeAdded->element = value;
	ListNode* temp = head;
	while(temp != NULL) {
		ListNode* nextNode = temp->next;
		if(nextNode == NULL || nextNode->element >= value) {
			temp->next = nodeToBeAdded;
			nodeToBeAdded->next = nextNode;
			return;
		}
		temp = temp->next;
	}
}

// Complexity: O(1)
string LinkedListPriorityQueue::peek() {
	if(isEmpty()) error("call on empty list");
	return head->next->element;
}

// Complexity: O(1)
string LinkedListPriorityQueue::dequeueMin() {
	if(isEmpty()) error("call on empty list");
	string res = peek();
	ListNode* nodeToBeDeleted = head->next;
	head->next = head->next->next;
	delete nodeToBeDeleted;
	length--;
	return res;
}

