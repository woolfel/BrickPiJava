package org.woolfel.cv;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Scan and render will detect the line and draw a line through
 * the center of the lines detected.
 * 
 * @author Peter Lin
 *
 */
public class ScanAndRender {
	
	public static double blackThreshold = 60;
	public static int scanwidth = 10;
	public static int minHeight = 8;
	private ScannedColumn[] result = null;
	private int rows = 480;
	
	public ScanAndRender() {
		super();
	}
	
	public static double getBlackThreshold() {
		return blackThreshold;
	}

	public static void setBlackThreshold(double blackThreshold) {
		ScanAndRender.blackThreshold = blackThreshold;
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
		ScanAndRender.scanwidth = scanwidth;
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
		Graphics2D image = imageMatrix.createGraphics();
		for (int i=1; i < result.length; i++) {
			ScannedColumn col0 = result[i - 1];
			ScannedColumn col1 = result[i];
			//image.drawLine(x1, y1, x2, y2);
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
