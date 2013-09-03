/**
 * Copyright 2013  Thom Castermans  thom.castermans@gmail.com
 * Copyright 2013  Willem Sonke     willemsonke@planet.nl
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License or (at your option) version 3 or any later version
 * accepted by the membership of KDE e.V. (or its successor approved
 * by the membership of KDE e.V.), which shall act as a proxy 
 * defined in Section 14 of version 3 of the license.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
		Painter painter = new InputFieldPainter();
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			painter.paint(g2, 0, 0, 120, 40);
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
		Painter painter = new ProgressBarIndicatorPainter();
		long beforeTime = System.nanoTime();
		for (int i = 0; i < 100; i++) {
			painter.paint(g2, 0, 0, 120, 40);
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
