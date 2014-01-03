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

import joxy.color.HCYColor;

import org.junit.Assert;
import org.junit.Test;

public class HCYColorTest {

    /**
     * Delta for comparing floating point values.
     */
    private static final float DELTA = 0.0001f;
    /**
     * Colours on which tests are performed.
     */
    private static final Color[] TEST_COLORS = new Color[] {
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
    /**
     * HCY values to use when testing setters.
     */
    private static final float[][] TEST_HCY_VALUES = new float[][] {
            new float[] {0.1f, 0.4f, 0.2f},
            new float[] {0.8f, 0.1f, 0.5f}
        };
    
    @Test
    public void testSameAfterConversion() {
        // Test for some colours if they are the same after conversion        
        for (Color testColor : TEST_COLORS) {
            HCYColor testHCYColor = new HCYColor(testColor);
            Assert.assertEquals("Expected HCYColor.toColor() to return the " +
                    "same colour it was constructed from.", testColor,
                    testHCYColor.toColor());
        }
    }
    
    @Test
    public void testGetters() {
        // Test if the getters return sane values
        for (Color testColor : TEST_COLORS) {
            HCYColor testHCYColor = new HCYColor(testColor);
            Assert.assertTrue("Expected the hue value to be non-negative, but " +
                    "it was " + testHCYColor.getH() + " for input color " +
                    testColor + ".", 0.0f <= testHCYColor.getH());
            Assert.assertTrue("Expected the hue value to be at most 1.0f, but " +
                    "it was " + testHCYColor.getH() + " for input color " +
                    testColor + ".", testHCYColor.getH() <= 1.0f);
            
            Assert.assertTrue("Expected the chroma value to be non-negative, but " +
                    "it was " + testHCYColor.getC() + " for input color " +
                    testColor + ".", 0.0f <= testHCYColor.getC());
            Assert.assertTrue("Expected the chroma value to be at most 1.0f, but " +
                    "it was " + testHCYColor.getC() + " for input color " +
                    testColor + ".", testHCYColor.getC() <= 1.0f);
            
            Assert.assertTrue("Expected the luma value to be non-negative, but " +
                    "it was " + testHCYColor.getY() + " for input color " +
                    testColor + ".", 0.0f <= testHCYColor.getY());
            Assert.assertTrue("Expected the luma value to be at most 1.0f, but " +
                    "it was " + testHCYColor.getY() + " for input color " +
                    testColor + ".", testHCYColor.getY() <= 1.0f);
        }
    }
    
    @Test
    public void testSetters() {
        // Test if the setters really change the value and properly restore the color
        // when set back for all test colours
        for (Color testColor : TEST_COLORS) {
            HCYColor testHCY = new HCYColor(testColor);
            float[] oldHcyVals = new float[] {
                    testHCY.getH(), testHCY.getC(), testHCY.getY()
                };
            for (float[] testHcyVals : TEST_HCY_VALUES) {
                testHCY.setH(testHcyVals[0]);
                Assert.assertEquals(testHCY.getH(), testHcyVals[0], DELTA);
                
                testHCY.setC(testHcyVals[1]);
                Assert.assertEquals(testHCY.getC(), testHcyVals[1], DELTA);
                
                testHCY.setY(testHcyVals[2]);
                Assert.assertEquals(testHCY.getY(), testHcyVals[2], DELTA);
                
                testHCY.setH(oldHcyVals[0]);
                testHCY.setC(oldHcyVals[1]);
                testHCY.setY(oldHcyVals[2]);
                Assert.assertEquals(testColor, testHCY.toColor());
            }
        }
    }
}
