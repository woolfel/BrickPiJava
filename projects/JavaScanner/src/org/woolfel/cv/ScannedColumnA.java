package org.woolfel.cv;

import java.lang.Integer;
import java.util.*;

/**
 * Encapsulates the results of scanning an image for black lines
 * 
 * @author peter lin
 *
 */
public class ScannedColumnA {

	private int columnIndex = 0;
	private List<Integer> results = new ArrayList<Integer>(2);
	private List<Integer> centerPoint = new ArrayList<Integer>(2);
	
	public ScannedColumnA() {
	}

	public void addHeight(int height, int center) {
		results.add(height);
		centerPoint.add(center);
	}
	
	public void setColumnIndex(int in) {
		this.columnIndex = in;
	}
	
	public int getColumnIndex() {
		return this.columnIndex;
	}
	
	public int getHeight(int index) {
		if (index >= this.results.size()) {
			return 0;
		} else {
			return this.results.get(index);
		}
	}
	
	public int getCenter(int index) {
		if (index >= this.centerPoint.size()) {
			return 0;
		} else {
			return this.centerPoint.get(index);
		}
	}
	
	/**
	 * Returns an array: height, center
	 * @param index
	 * @return
	 */
	public int[] getValues(int index) {
		int[] data = new int[2];
		data[0] = this.results.get(index);
		data[1] = this.centerPoint.get(index);
		return data;
	}
	
	public boolean isEmpty() {
		return this.results.size() == 0;
	}
	
	public String toString() {
		return columnIndex + ":"+ this.results.toString() + " - " + this.centerPoint.toString();
	}
}
