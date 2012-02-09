package joxy.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

/**
 * Class with some miscelleaneous methods, for example a method to list
 * all keys to be used in the UIManger defaults table.
 * This class should not be part of the public release.
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
