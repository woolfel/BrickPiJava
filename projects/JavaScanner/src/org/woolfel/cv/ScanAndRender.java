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
	public static int startRow = 0;
	public static int endRow = 800;
	
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
		long start = System.currentTimeMillis();
		int itr = 0;
		for (int i=0; i < imageMatrix.getWidth(); i+=scanwidth) {
			ScannedColumn col = new ScannedColumn();
			col.setColumnIndex(i);
			scanColumnByColor(col, imageMatrix, i, this.rows);
			result[itr] = col;
			itr++;
		}
		long et = System.currentTimeMillis() - start;
		return et;
	}
	
	public BufferedImage renderImage(BufferedImage imageMatrix) {
		Graphics2D image = imageMatrix.createGraphics();
		image.setColor(Color.RED);
		this.drawLine(image);
		return imageMatrix;
	}
	
	private void drawBlocks(Graphics2D image) {
		for (int i=1; i < result.length; i++) {
			ScannedColumn sc = result[i];
			if (sc.count() > 0) {
				for (int s=0; s < sc.count(); s++) {
					image.drawLine(sc.getColumnIndex(), sc.getCenter(s) -1, sc.getColumnIndex(), sc.getCenter(s) +1);
				}
			}
		}
	}
	
	private void drawLine(Graphics2D image) {
		for (int i=1; i < result.length; i++) {
			ScannedColumn sc = result[i];
			ScannedColumn next = null;
			if (i < (result.length - 1) && result[i + 1].count() > 0) {
				next = result[i + 1];
			}
			if (sc.count() > 0) {
				for (int s=0; s < sc.count(); s++) {
					if (next != null) {
						image.drawLine(sc.getColumnIndex(), sc.getCenter(s), next.getColumnIndex(), next.getCenter(s));
					} else {
						image.drawLine(sc.getColumnIndex(), sc.getCenter(s) -1, sc.getColumnIndex(), sc.getCenter(s) +1);
					}
				}
			}
		}
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
