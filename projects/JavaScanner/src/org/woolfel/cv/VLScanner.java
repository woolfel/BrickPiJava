package org.woolfel.cv;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;

public class VLScanner {
	
	public static double blackThreshold = 60;
	public static int scanwidth = 10;
	private ScannedColumn[] result = null;
	private int rows = 480;
	
	public VLScanner() {
		super();
	}
	
	public static double getBlackThreshold() {
		return blackThreshold;
	}

	public static void setBlackThreshold(double blackThreshold) {
		VLScanner.blackThreshold = blackThreshold;
	}

	public static int getScanwidth() {
		return scanwidth;
	}

	public static void setScanwidth(int scanwidth) {
		VLScanner.scanwidth = scanwidth;
	}

	public ScannedColumn[] getResults() {
		return this.result;
	}
	
	public long scanImage(Mat imageMatrix) {
		this.result = new ScannedColumn[imageMatrix.cols()/scanwidth];
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
		// System.out.println("et=" + ((double)et/1000000.0) + " ms");
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
				if (counter > 5) {
					col.addHeight(counter, (i + counter/2));
				}
				counter = 0;
			}
		}
	}
}
