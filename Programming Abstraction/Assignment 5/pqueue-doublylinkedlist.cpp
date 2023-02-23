/*************************************************************
 * File: pqueue-doublylinkedlist.cpp
 *
 * Implementation file for the DoublyLinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-doublylinkedlist.h"
#include "error.h"

DoublyLinkedListPriorityQueue::DoublyLinkedListPriorityQueue() {
	head = new ListNode;
	tail = new ListNode;
	head->next = tail;
	head->prev = NULL;
	tail->next = NULL;
	tail->prev = head;
	length = 0;
}

DoublyLinkedListPriorityQueue::~DoublyLinkedListPriorityQueue() {
	ListNode* temp = head;
	while(temp != NULL) {
		ListNode* nextNode = temp->next;
		delete temp;
		temp = nextNode;
	}
}

int DoublyLinkedListPriorityQueue::size() {
	return length;
}

bool DoublyLinkedListPriorityQueue::isEmpty() {
	return size() == 0;
}

// Complexity: O(1)
void DoublyLinkedListPriorityQueue::enqueue(string value) {
	ListNode* nodeToBeAdded = new ListNode;
	nodeToBeAdded->element = value;
	ListNode* nextNode = head->next;
	head->next = nodeToBeAdded;
	nodeToBeAdded->next = nextNode;
	nextNode->prev = nodeToBeAdded;
	nodeToBeAdded->prev = head;
	length++;
}

// Complexity: O(n)
string DoublyLinkedListPriorityQueue::peek() {
	if(isEmpty()) error("call on empty list");
	ListNode* min;
	get_min_node(min);
	return min->element;
}

// Complexity: O(n)
string DoublyLinkedListPriorityQueue::dequeueMin() {
	if(isEmpty()) error("call on empty list");
	ListNode* min;
	get_min_node(min);
	ListNode* nextNode = min->next;
	ListNode* prevNode = min->prev;
	prevNode->next = nextNode;
	nextNode->prev = prevNode;
	string res = min->element;
	delete min;
	length--;
	return res;
}

// This function finds the node that has lexicographically smallest string.
void DoublyLinkedListPriorityQueue::get_min_node(ListNode* &min) {
	min = head->next;
	ListNode* temp = min;
	while(temp != NULL) {
		if(temp->element < min->element && temp->element != "") {
			min = temp;
		}
		temp = temp->next;
	}
}