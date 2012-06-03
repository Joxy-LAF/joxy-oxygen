package joxy.utils;

import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

/**
 * General class providing basic utilities for the Joxy Look and Feel.
 * Note: this should become kind-of the public API for application developers wishing to improve
 * their application's LAF when using Joxy. But at the moment it contains primarily internals...
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class Utils {
	
	//-- VARIABLES ------------------------------------------------------------
	/** Path to home directory of the user */
	private static String homeDir = System.getProperty("user.home");
	/** System file separator */
	private static String fileSep = System.getProperty("file.separator");
	// Possible locations of the kdeglobals config file
	private static File kdeglobals1 = new File(homeDir + fileSep + ".kde"     + fileSep + "share" + fileSep + "config" + fileSep + "kdeglobals");
	private static File kdeglobals2 = new File(homeDir + fileSep + ".kdemod4" + fileSep + "share" + fileSep + "config" + fileSep + "kdeglobals");
	private static File kdeglobals3 = new File(homeDir + fileSep + ".kde4"    + fileSep + "share" + fileSep + "config" + fileSep + "kdeglobals");
	// Possible locations of the oxygenrc config file
	private static File oxygenrc1 = new File(homeDir + fileSep + ".kde"     + fileSep + "share" + fileSep + "config" + fileSep + "oxygenrc");
	private static File oxygenrc2 = new File(homeDir + fileSep + ".kdemod4" + fileSep + "share" + fileSep + "config" + fileSep + "oxygenrc");
	private static File oxygenrc3 = new File(homeDir + fileSep + ".kde4"    + fileSep + "share" + fileSep + "config" + fileSep + "oxygenrc");
	/** Contents of the kdeglobals config file, initially not set */
	private static String[] kdeConfigLines = null;
	/** HashMap with default values */
	private static HashMap<String, String> defaultsHashMap = null;
	/** Indicates if the RND color scheme should be used */
	public static boolean useRNDColorScheme = false;
	
	//-- PUBLIC METHODS -------------------------------------------------------
	/**
	 * Concatenate two arrays and return the result.
	 * This method is copied from <a href="http://stackoverflow.com/questions/80476/how-to-concatenate-two-arrays-in-java">
	 * StackOverflow</a>, answer of <i>Joachim Sauer</i> and then slightly adjusted.
	 * 
	 * @param  first  The first array.
	 * @param  second The second array.
	 * @return An array consisting of the concatenation of first and second.
	 * 		   That is, <pre>\forall i : 0 <= i < first.length : \result[i] == first[i]</pre>
	 *         and analoguous <pre>\forall i : first.length <= i < \result.length : \result[i] == second[i - first.length]</pre>.<br>
	 *         If one the two is {@code null}, the other array will be returned.
	 *         If both are {@code null}, then {@code null} will be returned.
	 */
	public static <T> T[] concatArrays(T[] first, T[] second) {
		if (first == null && second == null)  return null;
		if (first == null)  return second;
		if (second == null) return first;
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	
	/**
	 * Returns true if the window manager used on the machine the program
	 * is running on is KDE. This is determined by checking if some configuration
	 * files exist. When {@code !Utils.isLinux()}, this method always returns false
	 * and does not even try to look for configuration files.
	 * 
	 * TODO testing and updating Javadoc if it works
	 * 
	 * @return True if the window manager in use is KDE, false otherwise.
	 */
	public static boolean isKDE() {
		if (!Utils.isLinux()) {
			return false;
		}
	/*	// We are going to check if some config files exist. If so, 
		// this machine probably runs KDE.
		if (kdeglobals1.exists() || kdeglobals2.exists() || kdeglobals3.exists() ||
				oxygenrc1.exists() || oxygenrc2.exists() || oxygenrc3.exists()) {
			return true;
		return false;
		}*/
		
		// Look at environment variable KDE_FULL_SESSION
		return System.getenv("KDE_FULL_SESSION") != null;
	}
	
	/**
	 * Returns true if the OS the program is running on is Linux.
	 * 
	 * @return True if OS.name equals Linux, false otherwise.
	 */
	public static boolean isLinux() {
		return System.getProperty("os.name").equals("Linux");
	}

	/**
	 * Returns an array of key/value pairs that indicate some system colors
	 * as set by the user in his/her KDE color theme.
	 * If {@code !Utils.isKDE()} (this can happen when the color theme cannot
	 * be found too), a default set of colors is returned. This default
	 * colormap is the default KDE color theme.
	 * 
	 * @return Array of key/value pairs to be used in for example
	 *         {@code UIDefaults.putDefaults()}. This method will always return
	 *         a valid array of key/value pairs.
	 */
	public static Object[] getKDEColorMap() {
		// By default, we return an empty map (this value will never
		// be returned, but we should start with something)
		Object[] colorMap = new Object[0];
		// If on KDE, we detect the color scheme of the user...
		if (Utils.isKDE()) {
			// If the contents of the config file are not read already,
			// do it now
			if (kdeConfigLines == null) {
				readKDEConfigFiles();
			}
			// Now we create a colorMap from this
			colorMap = new Object[] {
				// Button colors
				"Button.background", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Button]", "BackgroundNormal")),
				"Button.focus", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Button]", "DecorationFocus")),
				"Button.foreground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Button]", "ForegroundNormal")),
				"Button.foregroundInactive", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Button]", "ForegroundInactive")),
				"Button.hover", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Button]", "DecorationHover")),
				
				// Window shadow
				"Shadow.activeInner", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[ActiveShadow]", "InnerColor")),
				"Shadow.activeOuter", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[ActiveShadow]", "OuterColor")),
				"Shadow.activeSize", Integer.valueOf(getKDEConfigValue(kdeConfigLines, "[ActiveShadow]", "Size")),
				"Shadow.activeUseOuter", Boolean.valueOf(getKDEConfigValue(kdeConfigLines, "[ActiveShadow]", "UseOuterColor")),
				"Shadow.activeVerticalOffset", Float.valueOf(getKDEConfigValue(kdeConfigLines, "[ActiveShadow]", "VerticalOffset")),
				"Shadow.inactiveInner", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[ActiveShadow]", "InnerColor")),
				"Shadow.inactiveOuter", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[ActiveShadow]", "OuterColor")),
				"Shadow.inactiveSize", Integer.valueOf(getKDEConfigValue(kdeConfigLines, "[ActiveShadow]", "Size")),
				"Shadow.inactiveUseOuter", Boolean.valueOf(getKDEConfigValue(kdeConfigLines, "[ActiveShadow]", "UseOuterColor")),
				"Shadow.inactiveVerticalOffset", Float.valueOf(getKDEConfigValue(kdeConfigLines, "[ActiveShadow]", "VerticalOffset")),
				// Selection color
				"TextField.selectionBackground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Selection]", "BackgroundNormal")),
				"TextField.selectionForeground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Selection]", "ForegroundNormal")),
				// Window colors
				"Window.background", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Window]", "BackgroundNormal")),

				// Table
				"Table.background", stringToColorUI("255,255,255"),
				"Table.alternateRowColor", stringToColorUI("240,240,240"),
				"Table.foreground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Button]", "ForegroundNormal")),
				"Table.gridColor", stringToColorUI("128,128,128"),
				"Table.selectionBackground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Selection]", "BackgroundNormal")),
				"Table.selectionForeground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Selection]", "ForegroundNormal")),
				
				// List
				"List.background", stringToColorUI("255,255,255"),
				"List.alternateRowColor", stringToColorUI("240,240,240"),
				"List.foreground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Button]", "ForegroundNormal")),
				"List.gridColor", stringToColorUI("128,128,128"),
				"List.selectionBackground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Selection]", "BackgroundNormal")),
				"List.selectionForeground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Selection]", "ForegroundNormal")),
				
				// Tool tips
				"ToolTip.background", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Tooltip]", "BackgroundNormal")),
				"ToolTip.foreground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Tooltip]", "ForegroundNormal")),
				
				// Tree
				"Tree.background", stringToColorUI("255,255,255"),
				"Tree.alternateRowColor", stringToColorUI("240,240,240"),
				"Tree.foreground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Button]", "ForegroundNormal")),
				"Tree.gridColor", stringToColorUI("128,128,128"),
				"Tree.selectionBackground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Selection]", "BackgroundNormal")),
				"Tree.selectionForeground", stringToColorUI(getKDEConfigValue(kdeConfigLines, "[Colors:Selection]", "ForegroundNormal")),
			};
		} else { // ...else, we use a default color scheme
			// TODO: Create a default colormap here [tca 18-nov-2011] if we are going to support non-KDE platforms [wso 18-nov-2011]
			//       Note: we can use defaultsHashMap?! [tca 18-nov-2011]
		}
		return colorMap;
	}

	/**
	 * Returns an array of key/value pairs that indicate some properties
	 * as set by the user in his/her KDE settings, for example font.
	 * If {@code !Utils.isKDE()} (this can happen when the config file cannot
	 * be found too), a default set of properties is returned. This default
	 * propertiesmap is the set of default KDE properties.
	 * 
	 * @pre    True.
	 * @return Array of key/value pairs to be used in for example
	 *         {@code UIDefaults.putDefaults()}. This method will always return
	 *         a valid array of key/value pairs.
	 */
	public static Object[] getKDEPropertiesMap() {
		// By default, we return an empty map (this value will never
		// be returned, but we should start with something)
		Object[] propMap = new Object[0];
		// If on KDE, we detect the properties of the user...
		if (Utils.isKDE()) {
			// If the contents of the config file are not read already,
			// do it now
			if (kdeConfigLines == null) {
				readKDEConfigFiles();
			}
			// Now we create a propMap from this
			propMap = new Object[] {
				// Button settings
				"Button.font", stringToFontUI(getKDEConfigValue(kdeConfigLines, "[General]", "font")),
				// Editor pane settings
				"EditorPane.font", stringToFontUI(getKDEConfigValue(kdeConfigLines, "[General]", "font")),
				// General settings
				"General.contrast", Integer.valueOf(getKDEConfigValue(kdeConfigLines, "[KDE]", "contrast")),
				// Language settings
				"Locale.country", getKDEConfigValue(kdeConfigLines, "[Locale]", "Country"),
				"Locale.dateFormat", getKDEConfigValue(kdeConfigLines, "[Locale]", "DateFormat"),
				"Locale.language", getKDEConfigValue(kdeConfigLines, "[Locale]", "Language"), // by ':' separated list of languages, first is preferred language
				"OptionPane.buttonOrientation", SwingConstants.RIGHT,
				// [ws] dit kan worden gebruikt worden met de Java-locale
				// Window decoration settings
				"Window.blendColor", getKDEConfigValue(kdeConfigLines, "[Windeco]", "BlendColor"),
				"Window.buttonSize", getKDEConfigValue(kdeConfigLines, "[Windeco]", "ButtonSize"),
				"Window.drawSeparator", getKDEConfigValue(kdeConfigLines, "[Windeco]", "DrawSeparator"),
				"Window.drawTitleOutline", getKDEConfigValue(kdeConfigLines, "[Windeco]", "DrawTitleOutline"),
				"Window.frameBorder", getKDEConfigValue(kdeConfigLines, "[Windeco]", "FrameBorder"),
				"Window.sizeGripMode", getKDEConfigValue(kdeConfigLines, "[Windeco]", "SizeGripMode"),
				"Window.tabsEnabled", getKDEConfigValue(kdeConfigLines, "[Windeco]", "TabsEnabled"),
				"Window.titleAlignment", getKDEConfigValue(kdeConfigLines, "[Windeco]", "TitleAlignment"),
				"Window.useAnimations", getKDEConfigValue(kdeConfigLines, "[Windeco]", "UseAnimations"),
				"Window.useOxygenShadows", getKDEConfigValue(kdeConfigLines, "[Windeco]", "UseOxygenShadows"),
				// Table settings
				"Table.font", stringToFontUI(getKDEConfigValue(kdeConfigLines, "[General]", "font")),
				"TableHeader.font", stringToFontUI(getKDEConfigValue(kdeConfigLines, "[General]", "font")),
				// List settings
				"List.font", stringToFontUI(getKDEConfigValue(kdeConfigLines, "[General]", "font")),
				// ToolTip settings
				"ToolTip.font", stringToFontUI(getKDEConfigValue(kdeConfigLines, "[General]", "font")),
				// Tree settings
				"Tree.font", stringToFontUI(getKDEConfigValue(kdeConfigLines, "[General]", "font")),
				"Tree.paintLines", true,
			};
		} else { // ...else, we use default properties
			// TODO: Create a default property map here, see todo above [tca 18-nov-2011] if we are going to support non-KDE platforms
		}
		return propMap;
	}
	
	/**
	 * Returns an array of key/value pairs that indicate some KDE icons.
	 * 
	 * @return Array of key/value pairs with icons.
	 */
	public static Object[] getKDEIconsMap() {
		// By default, we return an empty map (this value will never
		// be returned, but we should start with something)
		Object[] propMap = new Object[0];
		// If on KDE, we detect the properties of the user...
		if (Utils.isKDE()) {
			
			propMap = new Object[] {
			    // JFileChooser
				"FileView.directoryIcon", getOxygenIcon("places/folder", 16),
				"FileView.fileIcon", getOxygenIcon("mimetypes/unknown", 16),
				"FileView.computerIcon", getOxygenIcon("devices/computer", 16),
				"FileView.hardDriveIcon", getOxygenIcon("devices/drive-harddisk", 16),
				"FileView.floppyDriveIcon", getOxygenIcon("devices/media-floppy", 16),
				"FileChooser.newFolderIcon", getOxygenIcon("actions/folder-new", 22),
				"FileChooser.upFolderIcon", getOxygenIcon("actions/go-up", 22),
				"FileChooser.homeFolderIcon", getOxygenIcon("places/user-home", 22),
				"FileChooser.detailsViewIcon", getOxygenIcon("actions/view-list-details", 16),
				"FileChooser.listViewIcon", getOxygenIcon("actions/view-list-icons", 16),
				// JOptionPane
				"OptionPane.informationIcon", getOxygenIcon("status/dialog-information", 64),
				"OptionPane.warningIcon", getOxygenIcon("status/dialog-warning", 64),
				"OptionPane.errorIcon", getOxygenIcon("status/dialog-error", 64),
				"OptionPane.yesIcon", getOxygenIcon("actions/dialog-information", 16),
				"OptionPane.noIcon", getOxygenIcon("actions/dialog-information", 16),
				"OptionPane.cancelIcon", getOxygenIcon("actions/dialog-cancel", 16),
				"OptionPane.okIcon", getOxygenIcon("actions/dialog-ok", 16),
			};
		} else { // ...else, we use default properties
			// TODO: Create a default property map here, see todo above [tca 18-nov-2011] if we are going to support non-KDE platforms
		}
		return propMap;
	}
	
	
	//-- PRIVATE METHODS ------------------------------------------------------
	/**
	 * Get a reasonable default value (in the KDE config file format) for some key.
	 * This can for example be used if there is no value for the key in the configuration file.
	 * 
	 * @param  key The key we need a default value for. This key consists of section and key
	 *             in the kdeglobals config file.  For example, the key "font" in section "[General]"
	 *             will have the key "[General]font" as a key here.
	 * @return The value found for the given key, or null if no value is found.
	 */
	private static String getKDEDefaultConfigValue(String key) {
		// Only create hashmap if it does not exist
		if (defaultsHashMap == null) { 
			defaultsHashMap = new HashMap<String, String>();
			// Add the elements we need (sections are alphabetically sorted in code,
			//                           keys follow order in original file for convenience)
			defaultsHashMap.put("[ActiveShadow]InnerColor", "112,239,255");
			defaultsHashMap.put("[ActiveShadow]OuterColor", "84,167,240");
			defaultsHashMap.put("[ActiveShadow]Size", "29");
			defaultsHashMap.put("[ActiveShadow]UseOuterColor", "true");
			defaultsHashMap.put("[ActiveShadow]VerticalOffset", "0.1");
			defaultsHashMap.put("[Colors:Button]BackgroundNormal", "223,220,217");
			defaultsHashMap.put("[Colors:Button]DecorationFocus", "58,167,221");
			defaultsHashMap.put("[Colors:Button]ForegroundNormal", "34,31,30");
			defaultsHashMap.put("[Colors:Button]DecorationHover", "110,214,255");
			defaultsHashMap.put("[General]font", "Ubuntu,9");
			defaultsHashMap.put("[InactiveShadow]InnerColor", "0,0,0");
			defaultsHashMap.put("[InactiveShadow]OuterColor", "0,0,0");
			defaultsHashMap.put("[InactiveShadow]Size", "25");
			defaultsHashMap.put("[InactiveShadow]UseOuterColor", "false");
			defaultsHashMap.put("[InactiveShadow]VerticalOffset", "0.2");
			defaultsHashMap.put("[Windeco]FrameBorder", "Normal");
			defaultsHashMap.put("[Windeco]BlendColor", "Radial Gradient"); // transition, can be "Solid Color"
			defaultsHashMap.put("[Windeco]ButtonSize", "normal");
			defaultsHashMap.put("[Windeco]DrawSeparator", "false");	// border between windowdecoration and window content
			defaultsHashMap.put("[Windeco]DrawTitleOutline", "false"); // active windowdecoration will have a different color
			defaultsHashMap.put("[Windeco]SizeGripMode", "normal");
			defaultsHashMap.put("[Windeco]TabsEnabled", "true");
			defaultsHashMap.put("[Windeco]TitleAlignment", "center");
			defaultsHashMap.put("[Windeco]UseAnimations", "true");
			defaultsHashMap.put("[Windeco]UseOxygenShadows", "true");
		}
		String result = defaultsHashMap.get(key);
		if (result == null || result.length() == 0) {
			String string = (result == null) ? "there is no default value" : "the default value is empty";
			Output.warning("No value found for key \"" + key + "\", but " + string + ".");
		}
		return result;
	}
	
	/**
	 * Given the contents of a config file, the section to look for and a key,
	 * this method returns the value that belongs to that key. If no such key
	 * is found in the given section, a default value is returned.
	 * Note that the matching of section and key is case sensitive!
	 * 
	 * @param lines   The contents of the config file (that is, split on whitespace).
	 * @param section The name of the section to look at, for example "[Colors:Button]".
	 * @param key     The name of the key to look for, for example "BackgroundNormal".
	 * @return        Value if found, default value otherwise.
	 */
	private static String getKDEConfigValue(String[] lines, String section, String key) {
		FindSection: for (int i = 0; i < lines.length; i++) {
			// Look for the right section
			if (lines[i].equals(section)) {
				// Look for the right key in this section.
				// This means that if we reach a new section (we suppose each section starts with "[")
				// we could not find the key
				for (int j = i + 1; j < lines.length; j++) {
					// Key found? Then break and return value (everythinig after the "=")
					if (lines[j].length() > key.length() && lines[j].substring(0, key.length()).equals(key)) {
						return lines[j].split("=")[lines[j].split("=").length - 1];
					}
					// Reached the next section? then break the loop (the outer one, because
					// we can stop searching completely)
					if (lines[j].substring(0, 1).equals("[")) {
						break FindSection;
					}
				}
			}
		}
		// Key not found in the KDE config? Then return a default (KDE-ish) value...
		return getKDEDefaultConfigValue(section + key);
	}
	
	/**
	 * Read the contents of a given file into a string and return that.
	 * 
	 * @param  file The file to be read.
	 * @return String: contents of the given file (can be empty if file does
	 *         not exist, is empty, or something goes wrong while reading it).
	 */
	private static String readFileAsString(File file) {
		if (file == null)  return "";
	    byte[] buffer = new byte[(int) file.length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
	        f.read(buffer);
	    } catch (FileNotFoundException e) {
			// File not found, according to specification return empty string
	    	return "";
		} catch (IOException e) {
			// Something went wrong while reading file, according to specification return empty string
			return "";
		} finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
	}
	
	/**
	 * Read the contents of kdeglobals and oxygenrc configuration
	 * files and save them in the global variable {@code kdeConfigLines}.
	 */
	private static void readKDEConfigFiles() {
		// Check where kdeglobals config file is
		File kdeglobals = null;
		if (kdeglobals1.exists())  kdeglobals = kdeglobals1;
		if (kdeglobals2.exists())  kdeglobals = kdeglobals2;
		if (kdeglobals3.exists())  kdeglobals = kdeglobals3;
		// Read content into string and split on newlines
		Pattern p = Pattern.compile("\\n+");
		String[] kdeglobalsLines = (kdeglobals == null ? new String[0] : p.split(readFileAsString(kdeglobals)));
		// Check where oxygenrc config file is
		File oxygenrc = null;
		if (oxygenrc1.exists())  oxygenrc = oxygenrc1;
		if (oxygenrc2.exists())  oxygenrc = oxygenrc2;
		if (oxygenrc3.exists())  oxygenrc = oxygenrc3;
		// Read content into string and split on newlines
		String[] oxygenrcLines = (oxygenrc == null ? new String[0] : p.split(readFileAsString(oxygenrc)));
		// Save the concatenation of the two files globally
		kdeConfigLines = concatArrays(kdeglobalsLines, oxygenrcLines);
	}
	
	/**
	 * Converts a string in the form "rrr,ggg,bbb" or "#rrggbb" to a ColorUIResource.
	 * 
	 * @param str The string to convert.
	 * @return The ColorUIResource generated.
	 */
	private static ColorUIResource stringToColorUI(String str) {
		str = str.toLowerCase();
		if (str.matches("\\d{1,3},\\d{1,3},\\d{1,3}")) { // "KDE-format"
			int r = Integer.valueOf(str.split(",")[0]);
			int g = Integer.valueOf(str.split(",")[1]);
			int b = Integer.valueOf(str.split(",")[2]);
			return new ColorUIResource(r, g, b);
		} else if (str.matches("#[0-9a-f]{6}")) { // HTML-format
			int r = Integer.valueOf(str.substring(1, 3), 16);
			int g = Integer.valueOf(str.substring(3, 5), 16);
			int b = Integer.valueOf(str.substring(5, 7), 16);
			return new ColorUIResource(r, g, b);
		} else {
			Output.error("The input string, \"" + str + "\", is not in the correct format.");
			throw new NumberFormatException("The input string is not in the correct format.");
		}
	}
	
	/**
	 * Converts a string in the form "fontname,size[,...]" to a FontUIResource.
	 * By doing this, a {@code Font.PLAIN} font is supposed.
	 * 
	 * @param str The string to convert.
	 * @return The FontUIResource generated.
	 */
	private static FontUIResource stringToFontUI(String str) {
		//assert str.matches("\\[^,]*,\\d{1,10},*") :
		//	"stringToFontUI: the input string, \"" + str + "\", does not match the pattern name,size,other_stuff.";
		
		String name = str.split(",")[0];
		int style = Font.PLAIN;
		// TODO This is a hack: Java font sizes seem to be smaller than KDE font sizes
		int size = (int) (Integer.valueOf(str.split(",")[1]) * 1.4f);
		return new FontUIResource(name, style, size);
	}
	
	/**
	 * Gets an icon from the Oxygen theme from the /usr/share/icons/oxygen folder.
	 * Issues a warning and returns null if the icon cannot be found.
	 * @param name The name of the icon, without extension; for example actions/go-previous.
	 * @return The created ImageIcon.
	 */
	public static ImageIcon getOxygenIcon(String name, int size) {
		String oxygenFolderName = "/usr/share/icons/oxygen/"+ size + "x" + size + "/";
        File file = new File(oxygenFolderName + name + ".png");

        if (file.exists()) {
            return new ImageIcon(file.getPath());
        }
        
        // File not found? Try other folders
        String folderName = name.split("/")[0];
        String realFileName = name.split("/")[name.split("/").length - 1];
        File oxygenFolder = new File(oxygenFolderName);
        ArrayList<String> looked = new ArrayList<String>();
        if (oxygenFolder.exists() && oxygenFolder.isDirectory()) {
        	File[] subFolders = oxygenFolder.listFiles();
        	for (File subFolder : subFolders) {
        		if (subFolder.getName().equals(folderName))  continue;
        		File possibleFile = new File(subFolder.getAbsolutePath() + "/" + realFileName + ".png");
        		if (possibleFile.exists()) {
        			return new ImageIcon(possibleFile.getPath());
        		}
        		looked.add(possibleFile.getPath());
        	}
        }
        
        // Still not found...
		Output.warning("Could not find " + name + " icon");
		Output.warning("Searched on places:");
		Output.warning("  /usr/share/icons/oxygen/"+ size + "x" + size + "/" +name + ".png");
		for (String lookedAt : looked) {
			Output.warning("  " + lookedAt);
		}
		return null;
	}

	/**
	 * Displays a notification by using the <code>notify-send</code> program.
	 * @param title The title. KDE programs usually put their title in this field.
	 * @param text The text to put in the notification.
	 * @param type The type of notification as a MessageType.
	 */
	public static void displayNotification(String title, String text, MessageType type) {
		String icon = "";
		switch (type) {
		case INFO:
			icon = "--icon=/usr/share/icons/oxygen/48x48/status/dialog-information.png";
			break;
		case WARNING:
			icon = "--icon=/usr/share/icons/oxygen/48x48/status/dialog-warning.png";
			break;
		case ERROR:
			icon = "--icon=/usr/share/icons/oxygen/48x48/status/dialog-error.png";
		}
		
		ProcessBuilder pb = new ProcessBuilder("notify-send", icon, title, text);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
