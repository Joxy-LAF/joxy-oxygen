/*
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

import org.junit.Assert;
import org.junit.Test;

public class HCYColorTest {

    /**
     * Colours on which tests are performed.
     */
    private Color[] testColors = new Color[] {
            new Color(255, 255, 255), // white
            new Color(0, 0, 0), // black
            new Color(255, 0, 0), // red
            new Color(0, 255, 0), // green
            new Color(0, 0, 255), // blue
            new Color(255, 128, 128), // Oxygen colour: red
            new Color(119, 183, 83), // Oxygen colour: green
            new Color(128, 179, 255), // Oxygen colour: blue
            new Color(255, 0, 255) // colour that had negative hue
        };
    
    @Test
    public void testSameAfterConversion() {
        // Test for some colours if they are the same after conversion        
        for (Color testColor : testColors) {
            HCYColor testHCYColor = new HCYColor(testColor);
            Assert.assertEquals("Expected HCYColor.toColor() to return the " +
                    "same colour it was constructed from.", testColor,
                    testHCYColor.toColor());
        }
    }
    
    @Test
    public void testGetters() {
        // Test if the getters return sane values
        for (Color testColor : testColors) {
            HCYColor testHCYColor = new HCYColor(testColor);
            Assert.assertTrue("Expected the hue value to be non-negative, but " +
                    "it was " + testHCYColor.getH() + " for input color " +
                    testColor + ".", 0.0f <= testHCYColor.getH());
            Assert.assertTrue("Expected the hue value to be at most 1.0f.",
                    testHCYColor.getH() <= 1.0f);
            Assert.assertTrue("Expected the chroma value to be non-negative.",
                    0.0f <= testHCYColor.getC());
            Assert.assertTrue("Expected the chroma value to be at most 1.0f.",
                    testHCYColor.getC() <= 1.0f);
            Assert.assertTrue("Expected the luma value to be non-negative.",
                    0.0f <= testHCYColor.getY());
            Assert.assertTrue("Expected the luma value to be at most 1.0f.",
                    testHCYColor.getY() <= 1.0f);
        }
    }
}
