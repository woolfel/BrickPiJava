//============================================================================
// Name        : cppsis.cpp
// Author      : peter lin
// Version     :
// Copyright   : apache
// Description : Simple image processing for racing robots
//============================================================================

#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include "LineScanner.h"

using namespace cv;
using namespace std;

class cppsis
{
public:


private:

};

int main() {
	cout << "Lets get ready to rumble!!!" << endl;
	Mat image = imread("./samples/image1-small.jpg");
	cout << "image size: " << image.rows << " x " << image.cols << endl;
	LineScanner scanner = LineScanner();
	int loop = 20;
	double total = 0;
	for (int i=0; i < loop; i++) {
		long t = scanner.scanImage(image);
		if (i > 5) {
			total += t;
		}
	}
	scanner.printResults();
	double avg = total / (loop - 5);
	cout << "total: " << total << endl;
	cout << "avg time: " << avg << endl;
	return 0;
}
