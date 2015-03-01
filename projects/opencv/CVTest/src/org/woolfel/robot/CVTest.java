package org.woolfel.robot;

import org.opencv.core.*;
import org.opencv.highgui.*;

public class CVTest {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Highgui cv2 = new Highgui();
		Mat imagematrix = cv2.imread("./samples/image1-small.jpg");
		System.out.println( imagematrix.rows() + " x " + imagematrix.cols());
	}

}
