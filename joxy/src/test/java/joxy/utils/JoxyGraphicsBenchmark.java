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
