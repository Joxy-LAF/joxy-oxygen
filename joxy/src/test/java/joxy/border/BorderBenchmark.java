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
