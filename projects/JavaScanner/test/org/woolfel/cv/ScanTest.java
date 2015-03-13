package org.woolfel.cv;

import java.util.List;

import org.opencv.core.*;
import org.opencv.highgui.*;

public class ScanTest {

	public ScanTest() {
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat imagematrix = Highgui.imread("./samples/image1-small.jpg");
		VLScanner scanner = new VLScanner();
		int loop = 20;
		double total = 0;
		for (int i=0; i < loop; i++) {
			long t = scanner.scanImage(imagematrix);
			if (i > 5) {
				total += t;
			}
		}
		ScannedColumn[] result = scanner.getResults();
		for (int i=0; i < result.length; i++) {
			ScannedColumn c = result[i];
			System.out.println(c);
		}
		System.out.println("Avg=" + (total/(loop-5))/1000000.0 + "ms");
	}

}
