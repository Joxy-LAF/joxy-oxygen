package joxy.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.Test;

public class JoxyGraphicsTest {

	@Test
	public void testNativeTextRenderingBenchmark() {
		Graphics2D g2 = (Graphics2D) new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB).getGraphics();
		
		long beforeTime = System.currentTimeMillis();
		
		for (int i = 0; i < 100; i++) {
			g2.setColor(new Color(i, i, i)); // to remove caching effects
			JoxyGraphics.drawString(g2, "Test", 0, 0);
		}
		
		long afterTime = System.currentTimeMillis();
		System.out.println("Without caching, native text rendering took " + ((afterTime - beforeTime) / 10) / 10f + " ms per string.");

		beforeTime = System.currentTimeMillis();
		
		for (int i = 0; i < 100; i++) {
			JoxyGraphics.drawString(g2, "Test", 0, 0); // every time exactly the same string, so cache should be used
		}
		
		afterTime = System.currentTimeMillis();
		System.out.println("With caching, native text rendering took " + ((afterTime - beforeTime) / 10) / 10f + " ms per string.");
	}

}
