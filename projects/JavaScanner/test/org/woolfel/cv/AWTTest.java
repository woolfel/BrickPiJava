package org.woolfel.cv;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * AWTTest uses pure Java to do the line scanning and doesn't use
 * OpenCV. It uses BufferedImage and samples the image with the
 * given interval.
 * 
 * Note: BufferedImage 0,0 is the upper left corner.
 * 
 * @author Peter Lin
 *
 */
public class AWTTest {

	public AWTTest() {
	}

	public static void main(String[] args) {
		try {
			System.out.println("AWTTest -----");
			String file = "./samples/image1-small.jpg";
			int scanwidth = 10;
			if (args != null && args.length > 0) {
				file = args[0];
			}
			if (args != null && args.length == 2) {
				scanwidth = Integer.parseInt(args[1]);
			}
			BufferedImage image = ImageIO.read(new java.io.File(file));
			JavaScanner scanner = new JavaScanner();
			JavaScanner.setScanwidth(scanwidth);
			int loop = 10 * scanwidth;
			int ignore = 5;
			double total = 0;
			for (int i=0; i < loop; i++) {
				long t = scanner.scanImage(image);
				if (i > ignore) {
					total += t;
				}
			}
			ScannedColumn[] result = scanner.getResults();
			for (int i=0; i < result.length; i++) {
				ScannedColumn c = result[i];
				System.out.println(c);
			}
			System.out.println("Avg=" + (total/(loop-ignore))/1000000.0 + "ms");
			System.out.println("file: " + file + " : width=" + scanwidth);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
