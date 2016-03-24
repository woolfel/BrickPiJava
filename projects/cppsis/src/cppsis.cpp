//============================================================================
// Name        : cppsis.cpp
// Author      : peter lin
// Version     :
// Copyright   : apache
// Description : Simple image processing for racing robots
//============================================================================

#include <stdio.h>
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

int main(int argc, const char* argv[]) {

	string inputfile = "./samples/image1-320.jpg";
	int width = 10;
	cout << "args - " << argc << endl;
	if (argc > 1) {
		inputfile = argv[1];
	}
	if (argc > 2) {
		width = std::atoi(argv[2]);
	}
	cout << "reading: " << inputfile << endl;
	Mat image = imread(inputfile);
	cout << "image size: " << image.cols << " x " << image.rows << endl;
	LineScanner scanner = LineScanner();
	scanner.scanWidth = width;
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
