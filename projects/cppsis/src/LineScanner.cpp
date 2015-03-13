/*
 * LineScanner.cpp
 *
 *  Created on: Mar 9, 2015
 *      Author: peter
 */

#include <iostream>
#include <sys/time.h>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <vector>
#include "LineScanner.h"

using namespace cv;
using namespace std;

LineScanner::LineScanner(void) {
	blackThreshold = 60;
	scanWidth = 10;
	rows = 480;
	size = 0;
}

void LineScanner::setBlackThreshold(int value) {
	blackThreshold = value;
}

int LineScanner::getBlackThreshold(void) {
	return blackThreshold;
}

void LineScanner::setScanWidth(int width) {
	scanWidth = width;
}

int LineScanner::getScanWidth(void) {
	return scanWidth;
}

void LineScanner::printResults(void) {
	for (int i=0; i < size; i++) {
		ScannedColumn sc = this->result[i];
		sc.printString();
	}
}

long LineScanner::scanImage(Mat imageMatrix) {
	size = imageMatrix.cols/scanWidth;
	this->result = new ScannedColumn[size];
	rows = imageMatrix.rows;
	timeval startTime;
	timeval endTime;
	gettimeofday(&startTime, NULL);
	int itr = 0;
	for (int i=0; i < imageMatrix.cols; i+=scanWidth) {
		ScannedColumn scol = ScannedColumn();
		scol.setColumnIndex(i);
		// the function passes the object back to make sure we have the updated reference
		scol = scanColumn(scol, imageMatrix, i, rows);
		this->result[itr] = scol;
		this->result[itr].printString();
		itr++;
	}
	gettimeofday(&endTime, NULL);
	long msstart = (startTime.tv_sec * 1000) + (startTime.tv_usec / 1000);
	long msend = (endTime.tv_sec * 1000) + (endTime.tv_usec / 1000);
	return msend - msstart;
}

ScannedColumn LineScanner::scanColumn(ScannedColumn col, Mat image, int column, int row) {
	int counter = 0;
	for (int i=0; i < row; i++) {
		Vec3b px = image.at<Vec3b>(i,column);
		if (px[0] < blackThreshold && px[1] < blackThreshold && px[2] < blackThreshold) {
			counter++;
		} else {
			if (counter > 5) {
				col.addHeight(counter, i- (counter/2));
			}
			counter = 0;
		}
	}
	return col;
}

LineScanner::~LineScanner() {
	// TODO Auto-generated destructor stub
};
