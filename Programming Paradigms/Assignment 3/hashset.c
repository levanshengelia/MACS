#include "hashset.h"
#include <assert.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

void HashSetNew(hashset *h, int elemSize, int numBuckets,
		HashSetHashFunction hashfn, HashSetCompareFunction comparefn, HashSetFreeFunction freefn) {
	assert(elemSize > 0 && numBuckets > 0 && comparefn && hashfn);
	h->buckets = (vector**)malloc(numBuckets * sizeof(vector*));
	h->elem_size = elemSize;
	h->num_buckets = numBuckets;
	h->size = 0;
	h->freeFn = freefn;
	h->compFn = comparefn;
	h->hashFn = hashfn;
	for(int i = 0; i < numBuckets; i++) {
		vector* v = malloc(sizeof(vector));
		VectorNew(v, elemSize, h->freeFn, 4);
		h->buckets[i] = v;
	}
}

void HashSetDispose(hashset *h) {
	for(int i = 0; i < h->num_buckets; i++) {
		VectorDispose(h->buckets[i]);
		free(h->buckets[i]);
	}
	free(h->buckets);
}

int HashSetCount(const hashset *h) { 
	return h->size;
}

void HashSetMap(hashset *h, HashSetMapFunction mapfn, void *auxData) {
	assert(mapfn);
	for(int i = 0; i < h->num_buckets; i++) {
		VectorMap(h->buckets[i], mapfn, auxData);
	}
}

void HashSetEnter(hashset *h, const void *elemAddr) {
	assert(elemAddr);
	int index = h->hashFn(elemAddr, h->num_buckets);
	assert(index >= 0 && index < h->num_buckets);
	vector* v = h->buckets[index];
	int search_index = VectorSearch(v, elemAddr, h->compFn, 0, false);
	if(search_index == -1) {
		VectorAppend(v, elemAddr);
		h->size++;
	} else {
		VectorReplace(v, elemAddr, search_index);
	}
}

void *HashSetLookup(const hashset *h, const void *elemAddr) { 
	assert(elemAddr);
	int index = h->hashFn(elemAddr, h->num_buckets);
	assert(index >= 0 && index < h->num_buckets);
	vector* v = h->buckets[index];
	int search_index = VectorSearch(v, elemAddr, h->compFn, 0, false);
	if(search_index == -1) return NULL;
	return VectorNth(v, search_index);
	return NULL;
}