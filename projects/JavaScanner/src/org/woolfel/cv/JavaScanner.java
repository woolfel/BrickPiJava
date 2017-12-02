package org.woolfel.cv;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Note: setting blackThreshold, scanwidth and minHeight are not
 * thread safe, since they are static. For a robot, this should
 * only be called by one thread.
 * Doesn't make sense to use multi-threading for a raspberry pi
 * robot right now. 
 * 
 * @author Peter Lin
 *
 */
public class JavaScanner {
	
	public static double blackThreshold = 60;
	public static int scanwidth = 10;
	public static int minHeight = 8;
	private ScannedColumn[] result = null;
	private int rows = 480;
	public static int startRow = 0;
	public static int endRow = 800;
	
	public JavaScanner() {
		super();
	}
	
	public static double getBlackThreshold() {
		return blackThreshold;
	}

	public static void setBlackThreshold(double blackThreshold) {
		JavaScanner.blackThreshold = blackThreshold;
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
		double size = (double)imageMatrix.getWidth()/(double)scanwidth;
		this.result = new ScannedColumn[(int)Math.ceil(size)];
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
		return et;
	}
	
	/**
	 * 
	 * @param col
	 * @param image
	 * @param columnNumber
	 * @param rows
	 */
	private static void scanColumnByColor(ScannedColumn col, BufferedImage image, int columnNumber, int rows) {
		int counter = 0;
		for (int i=startRow; i < endRow; i++) {
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
