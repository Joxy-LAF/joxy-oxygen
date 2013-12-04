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

package joxy.testgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

/**
 * Class with some miscelleaneous methods, for example a method to list
 * all keys to be used in the UIManager defaults table.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class Miscellaneous {

	public static void main(String[] args) {
		printOSInfo();
	}
	
	public static void printOSInfo() {
		System.out.println("OS name: " + System.getProperty("os.name"));
		System.out.println("OS version: " + System.getProperty("os.version"));
		System.out.println("OS architecture: " + System.getProperty("os.arch"));
	}
	
	public static void printSwingColorKeys() {
		List<String> colors = new ArrayList<String>();
		for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
		    if (entry.getValue() instanceof Color) {
		        colors.add((String) entry.getKey()); // all the keys are strings
		    }
		}
		Collections.sort(colors);
		for (String name : colors) {
		    System.out.println(name);
		}
	}
	
}
