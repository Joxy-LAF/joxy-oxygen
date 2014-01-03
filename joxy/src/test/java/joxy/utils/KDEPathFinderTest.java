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

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class KDEPathFinderTest {

	@Test
	public void testGetConfigPaths() {
		String[] configPaths = KDEPathFinder.getConfigPaths();
		Assert.assertTrue("Expected the KDE Path Finder to find at "
				+ "least one path where configuration is stored.",
				configPaths.length > 0);
		for (String configPath : configPaths) {
			Assert.assertTrue("Expected all paths to have non-zero "
					+ "length.", configPath.length() > 0);
			File configPathFile = new File(configPath);
			Assert.assertTrue("Expected all paths to actually "
					+ "exist.", configPathFile.exists());
			Assert.assertTrue("Expected all paths to be a "
					+ "directory.", configPathFile.isDirectory());
			Output.print(configPath);
		}
	}
	
	@Test
	public void testGetIconPaths() {
		String[] iconPaths = KDEPathFinder.getIconPaths();
		Assert.assertTrue("Expected the KDE Path Finder to find at "
				+ "least one path where icons are stored.",
				iconPaths.length > 0);
		for (String iconPath : iconPaths) {
			Assert.assertTrue("Expected all paths to have non-zero "
					+ "length.", iconPath.length() > 0);
			File iconPathFile = new File(iconPath);
			Assert.assertTrue("Expected all paths to actually "
					+ "exist.", iconPathFile.exists());
			Assert.assertTrue("Expected all paths to be a "
					+ "directory.", iconPathFile.isDirectory());
			Output.print(iconPath);
		}
	}
	
}
