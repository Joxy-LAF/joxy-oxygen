package joxy.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.Test;

public class JoxyGraphicsBenchmark {

	@Test
	public void benchmarkNativeTextRendering() {
		Graphics2D g2 = (Graphics2D) new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB).getGraphics();
		
		long beforeTime = System.nanoTime();
		
		for (int i = 0; i < 100; i++) {
			g2.setColor(new Color(i, i, i)); // to remove caching effects
			JoxyGraphics.drawString(g2, "Test", 0, 0);
		}
		
		long afterTime = System.nanoTime();
		Output.print("Without caching, native text rendering took " + ((afterTime - beforeTime) / 100000) / 1000f + " ms per string.");

		beforeTime = System.nanoTime();
		
		for (int i = 0; i < 100; i++) {
			JoxyGraphics.drawString(g2, "Test", 0, 0); // every time exactly the same string, so cache should be used
		}
		
		afterTime = System.nanoTime();
		Output.print("With caching, native text rendering took " + ((afterTime - beforeTime) / 100000) / 1000f + " ms per string.");
	}

}
