package joxy.painter;

import static org.junit.Assert.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import joxy.utils.Output;

import org.junit.Test;

/**
 * This class implements benchmarks for all painters (in <code>joxy.painter</code>).
 */
public class PainterBenchmark {
	
	private Graphics2D g2 = (Graphics2D) new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB).getGraphics();
	
	@Test
	public void benchmarkButtonSlab() {
		Painter painter = new ButtonSlabPainter();
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			painter.paint(g2, 0, 0, 120, 40);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkDarkEngraving() {
		Painter painter = new ButtonSlabPainter();
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			painter.paint(g2, 0, 0, 120, 40);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkFocusIndicator() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			FocusIndicatorPainter.paint(g2, 0, 0, 120, 40, 128);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkHoverIndicator() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			HoverIndicatorPainter.paint(g2, 0, 0, 120, 40, 128);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkInputField() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			InputFieldPainter.paint(g2, 0, 0, 120, 40);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkMenuItemBackground() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			MenuItemBackgroundPainter.paint(g2, 0, 0, 200, 40, 128);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkPressedButtonSlab() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			PressedButtonSlabPainter.paint(g2, 0, 0, 120, 40);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkProgressBarIndicator() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			ProgressBarIndicatorPainter.paint(g2, 0, 0, 120, 40);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkRoundButtonSlab() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			RoundButtonSlabPainter.paint(g2, 0, 0, 120, 40);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkRoundFocusIndicator() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			RoundFocusIndicatorPainter.paint(g2, 0, 0, 120, 40, 128);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkRoundHoverIndicator() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			RoundHoverIndicatorPainter.paint(g2, 0, 0, 120, 40, 128);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkScrollThumb() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			ScrollThumbPainter.paint(g2, 0, 0, 20, 200, 128);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkTextFieldFocusIndicator() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			TextFieldFocusIndicatorPainter.paint(g2, 0, 0, 120, 40, 128);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkTextFieldHoverIndicator() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			TextFieldHoverIndicatorPainter.paint(g2, 0, 0, 120, 40, 128);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
	
	@Test
	public void benchmarkToolbarHoverIndicator() {
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			ToolbarHoverIndicatorPainter.paint(g2, 0, 0, 120, 40, 128);
		}
		long afterTime = System.nanoTime();
		Output.print(((afterTime - beforeTime) / 100000) / 1000f + " ms per draw.");
	}
}
