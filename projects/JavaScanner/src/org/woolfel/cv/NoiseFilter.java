package org.woolfel.cv;

/**
 * Depending on the environment, the simple JavaScanner can detect multiple
 * lines in the field of view. The robot needs to filter the data and pick
 * the data that is most likely the line. To do this, the filter needs to
 * compare the current data to columns before and after it.
 * 
 * Since the race track uses a thick black horizontal line, we can ignore
 * lines above the walls of the course and any shadows below the line.
 * 
 * @author Peter Lin
 *
 */
public class NoiseFilter implements DataFilter {

	private int minLineHeight = 10;
	private int maxLineHeight = 100;
	
	public NoiseFilter() {
	}

	public int getMinLineHeight() {
		return minLineHeight;
	}

	public void setMinLineHeight(int minLineHeight) {
		this.minLineHeight = minLineHeight;
	}

	public int getMaxLineHeight() {
		return maxLineHeight;
	}

	public void setMaxLineHeight(int maxLineHeight) {
		this.maxLineHeight = maxLineHeight;
	}

	public ScannedColumn[] filter(ScannedColumn[] input) {
		ScannedColumn prev = null;
		ScannedColumn next = null;
		ScannedColumn current = null;
		ScannedColumn[] filtered = new ScannedColumn[input.length];
		int lineCenter = 0;
		for (int i=0; i < input.length; i++) {
			filtered[i] = input[i].clone();
			if (i > 0) {
				prev = filtered[i - 1];
			}
			current = input[i];
			if (i < input.length - 1) {
				next = input[i + 1];
			} else {
				next = null;
			}
			if (prev == null) {
				// it's the first column
				if (current.count() == 1) {
					// set the lineCenter
					lineCenter = current.getCenter(0);
				} else {
					lineCenter = guessCenter(current);
				}
			} else if (next == null) {
				// we are at the last column
			} else {
				
			}
		}
		return filtered;
	}
	
	/**
	 * the algorithm for guessing the center is simple. Return the center
	 * of the first line that is between 60 and 100 pixels high.
	 * @param column
	 * @return
	 */
	public int guessCenter(ScannedColumn column) {
		int center = 0;
		for (int i=0; i < column.count(); i++) {
			if (column.getHeight(i) < 100 && column.getHeight(i) > 60) {
				center = column.getCenter(i);
				break;
			}
		}
		return center;
	}
}
