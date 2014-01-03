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

import javax.swing.ImageIcon;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

	@Test
	public void testGetOxygenIcon() {
		ImageIcon applicationExitIcon = Utils.getOxygenIcon("application-exit", 32);
		Assert.assertNotNull(applicationExitIcon);
		
		applicationExitIcon = Utils.getOxygenIcon("action/application-exit", 48);
		Assert.assertNotNull(applicationExitIcon);
		
		ImageIcon nonExistingIcon = Utils.getOxygenIcon("non-existing-name", 32);
		Assert.assertNull(nonExistingIcon);
		
		nonExistingIcon = Utils.getOxygenIcon("application-exit", -42);
		Assert.assertNull(nonExistingIcon);
	}
	
}
