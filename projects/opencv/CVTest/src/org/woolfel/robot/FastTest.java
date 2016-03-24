package org.woolfel.robot;

import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.*;

public class FastTest {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Highgui cv2 = new Highgui();
		Mat imagematrix = cv2.imread("./samples/image1-small.jpg");
		
		FeatureDetector dt = FeatureDetector.create(FeatureDetector.FAST);
		int loop = 20;
		long start = System.nanoTime();
		for (int i=0; i < loop; i++) {
			MatOfKeyPoint kp = new MatOfKeyPoint();
			dt.detect(imagematrix, kp);
		}
		long end = System.nanoTime();
		long et = end - start;
		long avg = et/loop;
		System.out.println("total time=" + et + " ns");
		System.out.println("avg=" + (avg/1000000) + " ms");

	}

}
