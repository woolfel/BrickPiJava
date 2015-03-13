/*
 * LineScanner.h
 *
 *  Created on: Mar 10, 2015
 *      Author: peter
 */

#include <vector>
#include "ScannedColumn.h"

using namespace cv;
using namespace std;

class LineScanner {
public:
	LineScanner();
	~LineScanner();
	long scanImage( Mat imageMatrix );
	void setBlackThreshold(int value);
	int getBlackThreshold(void);
	void setScanWidth(int width);
	int getScanWidth(void);
	void printResults(void);

	int blackThreshold;
	int scanWidth;
	int rows;
	int size;
	ScannedColumn *result;
private:
	ScannedColumn scanColumn(ScannedColumn col, Mat image, int column, int row);
};
