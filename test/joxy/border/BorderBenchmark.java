package joxy.border;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.border.BevelBorder;

import joxy.utils.Output;

import org.junit.Test;

/**
 * This class implements benchmarks for all borders (in <code>joxy.border</code>).
 */
public class BorderBenchmark {
	
	Graphics2D g2 = (Graphics2D) new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB).getGraphics();
	
	@Test
	public void benchmarkJoxyBevelBorderRaised() {
		JoxyBevelBorder border = new JoxyBevelBorder(BevelBorder.RAISED);
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			border.paintBorder(null, g2, 0, 0, 400, 400);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkJoxyBevelBorderLowered() {
		JoxyBevelBorder border = new JoxyBevelBorder(BevelBorder.LOWERED);
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			border.paintBorder(null, g2, 0, 0, 400, 400);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkJoxyTitledBorder() {
		JoxyTitledBorder border = new JoxyTitledBorder("");
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			border.paintBorder(null, g2, 0, 0, 400, 400);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
}
