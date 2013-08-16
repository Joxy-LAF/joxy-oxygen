package joxy.utils;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Test;

public class ColorUtilsTest {

	@Test
	public void testShade() {
		Color input = new Color(100, 150, 200);
		Color result = ColorUtils.shade(input, 0.0f);
	    Assert.assertEquals(input, result);
	}
}
