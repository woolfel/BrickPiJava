package org.woolfel.cv;

public interface DataFilter {
	public ScannedColumn[] filter(ScannedColumn[] input);
}
