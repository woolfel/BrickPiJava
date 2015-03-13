/*
 * ScannedColumn.cpp
 *
 *  Created on: Mar 9, 2015
 *      Author: peter
 */

#include <iostream>
#include <vector>
#include "ScannedColumn.h"

using namespace std;


ScannedColumn::ScannedColumn() {
	columnIndex = 0;
	size = 0;
	results = new int[size];
	centerPoint = new int[size];
}

ScannedColumn::~ScannedColumn()
{
	columnIndex = 0;
	size = 0;
}

void ScannedColumn::addHeight( int height, int center )
{
	int newsize = size + 1;
	int *newresults = new int[newsize];
	int *newcps = new int[newsize];
	for (int i=0; i < newsize; i++) {
		newresults[i] = results[i];
		newcps[i] = centerPoint[i];
	}
	newresults[size] = height;
	newcps[size] = center;
	delete results;
	delete centerPoint;
	this->size = newsize;
	this->results = newresults;
	this->centerPoint = newcps;
	// printString();
}

void ScannedColumn::setColumnIndex( int index )
{
	columnIndex = index;
}

int ScannedColumn::getColumnIndex()
{
	return columnIndex;
}

int ScannedColumn::getHeight(int index)
{
	if (index < this->size) {
		return results[index];
	} else {
		return 0;
	}
}

int ScannedColumn::getCenter(int index)
{
	if (index < this->size) {
		return centerPoint[index];
	} else {
		return 0;
	}
}

int ScannedColumn::getCount()
{
	return this->size;
}

void ScannedColumn::printString(void) {
	cout << this->columnIndex << ":";
	cout <<"[";
	for (int i=0; i < this->size; i++) {
		if (i > 0) { cout << ","; }
		cout << this->results[i];
	}
	cout << "] - ";
	cout <<"[";
	for (int i=0; i < this->size; i++) {
		if (i > 0) { cout << ","; }
		cout << this->centerPoint[i];
	}
	cout << "]";
	cout << endl;
}
