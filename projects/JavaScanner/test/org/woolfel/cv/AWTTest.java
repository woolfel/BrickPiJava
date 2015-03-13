package org.woolfel.cv;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class AWTTest {

	public AWTTest() {
	}

	public static void main(String[] args) {
		try {
			BufferedImage image = ImageIO.read(new java.io.File("./samples/image1-small.jpg"));
			JavaScanner scanner = new JavaScanner();
			int loop = 20;
			double total = 0;
			for (int i=0; i < loop; i++) {
				long t = scanner.scanImage(image);
				if (i > 5) {
					total += t;
				}
			}
			ScannedColumn[] result = scanner.getResults();
			for (int i=0; i < result.length; i++) {
				ScannedColumn c = result[i];
				System.out.println(c);
			}
			System.out.println("Avg=" + (total/(loop-5))/1000000.0 + "ms");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
