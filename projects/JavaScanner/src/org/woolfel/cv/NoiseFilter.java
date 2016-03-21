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
public class NoiseFilter {

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

	public void filter(ScannedColumn[] input) {
		ScannedColumn prev = null;
		ScannedColumn next = null;
		ScannedColumn current = null;
		int lineCenter = 0;
		for (int i=0; i < input.length; i++) {
			if (i > 0) {
				prev = input[i - 1];
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
	}
	
	public int guessCenter(ScannedColumn column) {
		return 0;
	}
}
