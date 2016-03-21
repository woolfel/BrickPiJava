package org.woolfel.cv;

import java.lang.Integer;
import java.util.*;

/**
 * Encapsulates the results of scanning an image for black lines
 * 
 * @author peter lin
 *
 */
public class ScannedColumn {

	private int columnIndex = 0;
	private int[] results = new int[0];
	private int[] centerPoint = new int[0];
	
	public ScannedColumn() {
	}

	public void addHeight(int height, int center) {
		int[] newrs = new int[results.length + 1];
		int[] newcp = new int[centerPoint.length + 1];
		System.arraycopy(results, 0, newrs, 0, results.length);
		System.arraycopy(centerPoint, 0, newcp, 0, centerPoint.length);
		newrs[results.length] = height;
		newcp[results.length] = center;
		results = newrs;
		centerPoint = newcp;
	}
	
	public int count() {
		return results.length;
	}
	
	public void setColumnIndex(int in) {
		this.columnIndex = in;
	}
	
	public int getColumnIndex() {
		return this.columnIndex;
	}
	
	public int getHeight(int index) {
		if (index >= this.results.length) {
			return 0;
		} else {
			return this.results[index];
		}
	}
	
	public int getCenter(int index) {
		if (index >= this.centerPoint.length) {
			return 0;
		} else {
			return this.centerPoint[index];
		}
	}
	
	/**
	 * Returns an array: height, center
	 * @param index
	 * @return
	 */
	public int[] getValues(int index) {
		int[] data = new int[2];
		data[0] = this.results[index];
		data[1] = this.centerPoint[index];
		return data;
	}
	
	public boolean isEmpty() {
		return this.results.length == 0;
	}
	
	public String toString() {
		return columnIndex + ":"+ arrayString(this.results) + " - " + arrayString(centerPoint);
	}
	
	private String arrayString(int[] array) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i=0; i < array.length; i++) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append(array[i]);
		}
		buf.append("]");
		return buf.toString();
	}
}
