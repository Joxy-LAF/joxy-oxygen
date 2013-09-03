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

import java.awt.Component;
import java.awt.Container;

/**
 * This class provides methods to output messages to the "outside world".
 * All output from the Joxy LookAndFeel should come via this class, where
 * can be decided if it should be print or not. That is, debug messages
 * will only be print if in debug mode. Also,
 * it is possible to suppress all output via this class.<br>
 *
 * All messages will be output with a flag:
 * <ul>
 *  <li>[D] - Debug message;</li>
 *  <li>[I] - Regular message;</li>
 *  <li>[W] - Warning message;</li>
 *  <li>[E] - Error message.</li>
 * </ul>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class Output {

	//-- PUBLIC VARIABLES -----------------------------------------------------
	// All output modi. Note that the ordering is important, because it is used
	// in setMode()! That is, the value of "DEBUG" should be greater than
	// the value of "VERBOSE", et cetera.
	/** Debug mode: all messages, including debug, are print. */
	public static final int OUTPUT_MODE_DEBUG = 4;
	/** Verbose mode: all messages, except for debug, are print. */
	public static final int OUTPUT_MODE_VERBOSE = 3;
	/** Warning mode: only warning and error messages are print. */
	public static final int OUTPUT_MODE_WARNING = 2;
	/** Error mode: only error messages are print. */
	public static final int OUTPUT_MODE_ERROR = 1;
	/** Quiet mode: all messages are suppressed. */
	public static final int OUTPUT_MODE_QUIET = 0;
	
	//-- PRIVATE VARIABLES ----------------------------------------------------
	/** Indicates the mode we are currently in. */
	private static int mode = OUTPUT_MODE_DEBUG;
	/** Indicates if only debugmessages from a certain class should
	 *  be output, or all ("*") messages (if OUTPUT_MODE_DEBUG of course).
	 *  Multiple classnames can be given, separated by ":". */
	private static String debugClass = "*";
	/** Indicates if debugmessages from a certain class should not
	 *  be output, or no ("*") classes are "blocked".
	 *  Multiple classnames can be given, separated by ":". */
	private static String debugNotClass = "*";
	
	//-- PUBLIC METHODS -------------------------------------------------------
	/**
	 * Print the given message in the form
	 * "[D] callerClass.callerMethod(): message", for example
	 * "[D] joxy.utils.Utils.method(): this is a debugmessage".
	 * Message is print only in debug mode.
	 * 
	 * @param message The message to be output.
	 */
	public static void debug(Object message) {
		if (!isValid(message))  return;
		String msg = message.toString();
		msg = (msg.length() == 0) ? " is called." : ": " + msg;
		String callerClass = new Throwable().fillInStackTrace().getStackTrace()[1].getClassName();
		String callerMethod = new Throwable().fillInStackTrace().getStackTrace()[1].getMethodName();
		if (mode >= OUTPUT_MODE_DEBUG && isInDebugClass(callerClass) && !isInNotDebugClass(callerClass)) {
			System.out.println("[D] " + callerClass + "." + callerMethod + "()" + msg);
		}
	}
	
	/**
	 * Print the caller class and caller method.
	 */
	public static void debug() {
		debug("");
	}

	/**
	 * Print the given message in the form
	 * "[I] callerClass.callerMethod(): message", for example
	 * "[I] joxy.utils.Utils.method(): this is a message".
	 * Message is print only in verbose and debug mode.
	 * 
	 * @param message The message to be output.
	 */
	public static void print(Object message) {
		if (!isValid(message))  return;
		String callerClass = new Throwable().fillInStackTrace().getStackTrace()[1].getClassName();
		String callerMethod = new Throwable().fillInStackTrace().getStackTrace()[1].getMethodName();
		if (mode >= OUTPUT_MODE_VERBOSE) {
			System.out.println("[I] " + callerClass + "." + callerMethod + "(): " + message.toString());
		}
	}
	
	/**
	 * Print the given message in the form
	 * "[W] callerClass.callerMethod(): message", for example
	 * "[W] joxy.utils.Utils.method(): this is a warning".
	 * Message is print only when not in quiet or error mode.
	 * 
	 * @param message The message to be output.
	 */
	public static void warning(Object message) {
		if (!isValid(message))  return;
		String callerClass = new Throwable().fillInStackTrace().getStackTrace()[1].getClassName();
		String callerMethod = new Throwable().fillInStackTrace().getStackTrace()[1].getMethodName();
		if (mode >= OUTPUT_MODE_WARNING) {
			System.out.println("[W] " + callerClass + "." + callerMethod + "(): " + message.toString());
		}
	}
	
	/**
	 * Print the given message in the form
	 * "[E] callerClass.callerMethod(): message", for example
	 * "[E] joxy.utils.Utils.method(): this is an error".
	 * Message is print only when not in quiet mode.
	 * 
	 * @param message The message to be output.
	 */
	public static void error(Object message) {
		if (!isValid(message))  return;
		String callerClass = new Throwable().fillInStackTrace().getStackTrace()[1].getClassName();
		String callerMethod = new Throwable().fillInStackTrace().getStackTrace()[1].getMethodName();
		if (mode >= OUTPUT_MODE_ERROR) {
			System.out.println("[E] " + callerClass + "." + callerMethod + "(): " + message.toString());
		}
	}
	
	/**
	 * Change the output mode.
	 * 
	 * @param outputMode The new output mode.
	 * @post  If outputMode is one of the valid output modes, as defined
	 *        in this class (the OUTPUT_MODE_* variables), the output mode
	 *        is changed.
	 */
	public static void setMode(int outputMode) {
		if (outputMode == OUTPUT_MODE_DEBUG ||
				outputMode == OUTPUT_MODE_VERBOSE ||
				outputMode == OUTPUT_MODE_WARNING ||
				outputMode == OUTPUT_MODE_ERROR ||
				outputMode == OUTPUT_MODE_QUIET) {
			mode = outputMode;
		}
	}
	
	//-- PRIVATE METHODS ------------------------------------------------------
	/**
	 * Check a given message for validity.
	 * That is, being not null.
	 */
	private static boolean isValid(Object message) {
		return message != null;
	}
	
	/**
	 * Returns if a given class is in the list of classes of which debug
	 * messages can be print.
	 * 
	 * @param className The classname to check for.
	 */
	private static boolean isInDebugClass(String className) {
		if (debugClass.equals("*"))  return true;
		String[] debugClasses = debugClass.split(":");
		for (int i = 0; i < debugClasses.length; i++) {
			if (debugClasses[i].equals(className))  return true;
		}
		return false;
	}
	
	/**
	 * Returns if a given class is in the list of classes of which debug
	 * messages should not be print.
	 * 
	 * @param className The classname to check for.
	 */
	private static boolean isInNotDebugClass(String className) {
		if (debugNotClass.equals("*"))  return false;
		String[] debugNotClasses = debugNotClass.split(":");
		for (int i = 0; i < debugNotClasses.length; i++) {
			if (debugNotClasses[i].equals(className))  return true;
		}
		return false;
	}
	
	/**
	 * Prints debug information about a component, and does the same for its
	 * children.
	 * 
	 * @param component The component to display information about.
	 * @param indent The indentation. This should be "" on the first call.
	 */
	public static void debugComponent(Component component, String indent) {
		System.out.println(indent + component.getClass().getCanonicalName() + ",  name " + component.getName());
		
		if (component instanceof Container) {
			for (Component child : ((Container) component).getComponents()) {
				debugComponent(child, indent + "  ");
			}
		}
	}
}
