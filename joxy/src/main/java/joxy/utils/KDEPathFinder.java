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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class provides functions to find paths where configuration of KDE
 * is stored (both user-specific and the system-wide configuration) and
 * similarly where icons are stored on the file system.
 */
public class KDEPathFinder {

	/**
	 * Return a list of paths where KDE configuration files are stored. These
	 * are both user-specific and system-wide configuration files.
	 * 
	 * @return A list of paths where KDE configuration files are stored.
	 */
	public static String[] getConfigPaths() {
		return runKDE4Config("config");
	}
	
	/**
	 * Return a list of paths where KDE icon files are stored. These
	 * are both user-specific and system-wide icon files.
	 * 
	 * @return A list of paths where KDE icon files are stored.
	 */
	public static String[] getIconPaths() {
		return runKDE4Config("icon");
	}
	
	/**
	 * Execute <code>kde4-config --path path</code>, where <code>path</code>
	 * is the argument of this function. The return value is the list that is
	 * given as output by this call. That is, calling <code>kde4-config</code>
	 * outputs a semicolon-separated list, which is returned by this function
	 * as a regular array. 
	 * 
	 * @param path The section to find locations of. Possible values can be
	 *             "config" and "icon" for example. A full list can be found
	 *             by executing <code>kde4-config --types</code> on the cmd.
	 * @return The list returned by <code>kde4-config</code>.
	 */
	private static String[] runKDE4Config(String path) {
		// Start a process and run kde4-config
		Process kde4configProc;
		try {
			ProcessBuilder pb = new ProcessBuilder(new String[] {"kde4-config", "--path", path});
			kde4configProc = pb.start();
		} catch (IOException e) {
			Output.error("IOException with message \"" + e.getMessage()
					+ "\" while trying to run \"kde4-config --path " + path + "\".");
			return new String[0];
		} catch (Exception e) {
			Output.error("Unexpected exception with message \"" + e.getMessage()
					+ "\" while trying to run \"kde4-config --path " + path + "\".");
			return new String[0];
		}
		
		// Read the output of kde4-config, this should be only one line
		String output;
		try (BufferedReader k4cOutput = new BufferedReader(
				new InputStreamReader(kde4configProc.getInputStream()))) {
			output = k4cOutput.readLine();
			// read up all output
			while (k4cOutput.readLine() != null);
		} catch (IOException e) {
			Output.error("IOException with message \"" + e.getMessage()
					+ "\" while trying to read output of call to \"kde4-config "
					+ "--path " + path + "\".");
			return new String[0];
		}
		try {
			if (kde4configProc.waitFor() == 0) {
				return output.split(":");
			}
		} catch (InterruptedException e) {
			Output.error("InterruptedException with message \"" + e.getMessage()
					+ "\" while waiting for completion of call to \"kde4-config "
					+ "--path " + path + "\".");
			return new String[0];
		}
		
		return new String[0];
	}
}
