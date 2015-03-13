package org.woolfel.cv;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class JavaScanner {
	
	public static double blackThreshold = 60;
	public static int scanwidth = 10;
	public static int minHeight = 5;
	private ScannedColumn[] result = null;
	private int rows = 480;
	
	public JavaScanner() {
		super();
	}
	
	public static double getBlackThreshold() {
		return blackThreshold;
	}

	public static void setBlackThreshold(double blackThreshold) {
		JavaScanner.blackThreshold = blackThreshold;
	}

	public static int getScanwidth() {
		return scanwidth;
	}

	public static void setScanwidth(int scanwidth) {
		JavaScanner.scanwidth = scanwidth;
	}

	public ScannedColumn[] getResults() {
		return this.result;
	}
	
	/**
	 * Returns the elapse time in nano seconds
	 * @param imageMatrix
	 * @return
	 */
	public long scanImage(BufferedImage imageMatrix) {
		this.result = new ScannedColumn[imageMatrix.getWidth()/scanwidth];
		rows = imageMatrix.getHeight();
		long start = System.nanoTime();
		int itr = 0;
		for (int i=0; i < imageMatrix.getWidth(); i+=scanwidth) {
			ScannedColumn col = new ScannedColumn();
			col.setColumnIndex(i);
			scanColumnByColor(col, imageMatrix, i, this.rows);
			result[itr] = col;
			itr++;
		}
		long et = System.nanoTime() - start;
		// System.out.println("et=" + ((double)et/1000000.0) + " ms");
		return et;
	}
	
	private static void scanColumnByColor(ScannedColumn col, BufferedImage image, int columnNumber, int rows) {
		int counter = 0;
		for (int i=0; i < rows; i++) {
			Color bgr = new Color(image.getRGB(columnNumber,i));
			if (bgr.getRed() < blackThreshold && bgr.getBlue() < blackThreshold && bgr.getGreen() < blackThreshold) {
				counter++;
			} else {
				if (counter > minHeight) {
					col.addHeight(counter, (i - counter/2));
				}
				counter = 0;
			}
		}
	}
}
