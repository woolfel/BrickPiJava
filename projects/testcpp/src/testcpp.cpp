//============================================================================
// Name        : testcpp.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

//============================================================================
// Name        : testcpp.cpp
// Author      :
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================


#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <iostream>

using namespace cv;
using namespace std;

int main( int argc, char** argv ) {
	cout << "!!!Hello World!!!" << endl; // prints !!!Hello World!!!

    Mat image = imread("./samples/image1-small.jpg");
    cout << "dimensions" << image.rows << " x " << image.cols;


	return 0;
}
