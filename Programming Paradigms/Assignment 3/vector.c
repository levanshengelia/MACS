#include "vector.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

void reallocation(vector* v);

void VectorNew(vector *v, int elemSize, VectorFreeFunction freeFn, int initialAllocation) {
    if(initialAllocation == 0) initialAllocation = 4;
    assert(elemSize > 0);
    v->elem_size = elemSize;
    v->size = 0;
    v->alloc_length = initialAllocation;
    v->free_function = freeFn;
    v->base = malloc(initialAllocation * elemSize);
    assert(v->base);
}

void VectorDispose(vector *v) {
    for(int i = 0; i < v->size; i++) {
        void* elem = (char*)v->base + i * v->elem_size;
        if(v->free_function) v->free_function(elem);
    }
    free(v->base);
}

int VectorLength(const vector *v) { 
    return v->size;
}

void *VectorNth(const vector *v, int position) { 
    assert(position >= 0 && position < v->size);
    return (char*)v->base + position * v->elem_size;
}

void VectorReplace(vector *v, const void *elemAddr, int position) {
    assert(position >= 0 && position < v->size);
    void* temp = VectorNth(v, position);
    if(v->free_function) v->free_function(temp);
    memcpy(temp, elemAddr, v->elem_size);
}

void VectorInsert(vector *v, const void *elemAddr, int position) {
    assert(position >= 0 && position <= v->size);
    if(v->size == v->alloc_length) {
        reallocation(v);
    }
    if(position == v->size) {
        VectorAppend(v, elemAddr);
        return;
    }
    memmove((char*)VectorNth(v, position) + v->elem_size, VectorNth(v, position), (v->size - position) * v->elem_size);
    memcpy(VectorNth(v, position), elemAddr, v->elem_size);
    v->size++;
}

void VectorAppend(vector *v, const void *elemAddr) {
    if(v->size == v->alloc_length) {
        reallocation(v);
    }
    memcpy((char*)v->base + v->size * v->elem_size, elemAddr, v->elem_size);
    v->size++;
}

void reallocation(vector* v) {
    v->alloc_length *= 2;
    v->base = realloc(v->base, v->alloc_length * v->elem_size);
    assert(v->base);
}

void VectorDelete(vector *v, int position) {
    assert(position >= 0 && position < v->size);
    if(v->free_function) v->free_function((char*)v->base + position * v->elem_size);
    if(position != v->size - 1) memmove(VectorNth(v, position), VectorNth(v, position + 1), (v->size - 1 - position) * v->elem_size);
    v->size--;
}

void VectorSort(vector *v, VectorCompareFunction compare) {
    assert(compare);
    qsort(v->base, v->size, v->elem_size, compare);
}

void VectorMap(vector *v, VectorMapFunction mapFn, void *auxData) {
    assert(mapFn);
    for(int i = 0; i < v->size; i++) {
        mapFn(VectorNth(v, i), auxData);
    }
}

static const int kNotFound = -1;

int binary_search(const vector* v, const void* key, VectorCompareFunction searchFn, int start) {
    void* base = (char*)v->base + start * v->elem_size;
    void* elem_addr = bsearch(key, base, v->size - start, v->elem_size, searchFn);
    if(!elem_addr) return kNotFound;
    int diff = (char*)elem_addr - (char*)v->base;
    return diff / v->elem_size - 1;
}

int linear_search(const vector* v, const void* key, VectorCompareFunction searchFn, int start) {
    for(int i = start; i < v->size; i++) {
        void* curr_elem = (char*)v->base + i * v->elem_size;
        if(!searchFn(curr_elem, key)) return i;
    }
    return kNotFound;
}

int VectorSearch(const vector *v, const void *key, VectorCompareFunction searchFn, int startIndex, bool isSorted) { 
    assert(startIndex >= 0 && startIndex <= v->size && key && searchFn);
    if(isSorted) return binary_search(v, key, searchFn, startIndex);
    else return linear_search(v, key, searchFn, startIndex);
}