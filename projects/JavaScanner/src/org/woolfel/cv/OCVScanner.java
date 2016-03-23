package org.woolfel.cv;

import org.opencv.core.*;

/**
 * <p>
 * OCVScanner uses OpenCV to read the image data. It passes Mat object
 * to scanImage method. Calling C++ code from Java has always had an
 * overhead with JNI. Regardless of the Java code, making native calls
 * can have a 20% overhead.</p>
 * <p>
 * This class demonstrates how to use OpenCV Mat object. To run this,
 * you will need to have OpenCV binaries on Linux and openCV DLL
 * on windows.
 * </p>
 */
public class OCVScanner {
	
	public static double blackThreshold = 60;
	public static int scanwidth = 10;
	public static int minHeight = 8;
	private ScannedColumn[] result = null;
	private int rows = 480;
	
	public OCVScanner() {
		super();
	}
	
	public static double getBlackThreshold() {
		return blackThreshold;
	}

	public static void setBlackThreshold(double blackThreshold) {
		OCVScanner.blackThreshold = blackThreshold;
	}

	public static void setMinHeight(int height) {
		minHeight = height;
	}
	
	public static int getMinHeight() {
		return minHeight;
	}
	
	public static int getScanwidth() {
		return scanwidth;
	}

	public static void setScanwidth(int scanwidth) {
		OCVScanner.scanwidth = scanwidth;
	}

	public ScannedColumn[] getResults() {
		return this.result;
	}
	
	public long scanImage(Mat imageMatrix) {
		double size = (double)imageMatrix.cols()/(double)scanwidth;
		this.result = new ScannedColumn[(int)Math.ceil(size)];
		rows = imageMatrix.rows();
		long start = System.nanoTime();
		int itr = 0;
		for (int i=0; i < imageMatrix.cols(); i+=scanwidth) {
			ScannedColumn col = new ScannedColumn();
			col.setColumnIndex(i);
			scanColumn(col, imageMatrix, i, this.rows);
			result[itr] = col;
			itr++;
		}
		long end = System.nanoTime();
		long et = end - start;
		return et;
	}
	
	private static void scanColumn(ScannedColumn col, Mat image, int columnNumber, int rows) {
		int counter = 0;
		double[] bgr = null;
		for (int i=0; i < rows; i++) {
			bgr = image.get(i, columnNumber);
			if (bgr[0] < blackThreshold && bgr[1] < blackThreshold && bgr[2] < blackThreshold) {
				counter++;
			} else {
				if (counter > minHeight) {
					col.addHeight(counter, (i + counter/2));
				}
				counter = 0;
			}
		}
	}
}
