package org.woolfel.cv;

import java.awt.image.BufferedImage;
import java.io.File;
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
public class AWTScanAndDraw {

	public AWTScanAndDraw() {
	}

	public static void main(String[] args) {
		try {
			System.out.println("AWTTest -----");
			String file = "./samples/image1-320.jpg";
			int scanwidth = 5;
			if (args != null && args.length > 0) {
				file = args[0];
			}
			if (args != null && args.length == 2) {
				scanwidth = Integer.parseInt(args[1]);
			}
			BufferedImage image = ImageIO.read(new java.io.File(file));
			ScanAndRender scanner = new ScanAndRender();
			ScanAndRender.setScanwidth(scanwidth);
			ScanAndRender.minHeight = 8;
			ScanAndRender.startRow = 20;
			ScanAndRender.endRow = 220;
			long t = scanner.scanImage(image);
			ScannedColumn[] result = scanner.getResults();
			for (int i=0; i < result.length; i++) {
				ScannedColumn c = result[i];
				System.out.println(c);
			}
			BufferedImage newimg = scanner.renderImage(image);
			String output = "./scanned-result.jpg";
			ImageIO.write(newimg, "jpg", new File(output));
			System.out.println("time= " + t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
