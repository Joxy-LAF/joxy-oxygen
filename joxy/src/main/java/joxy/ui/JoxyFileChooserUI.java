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

package joxy.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Scanner;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.metal.MetalFileChooserUI;

import joxy.utils.Output;
import joxy.utils.Utils;

/**
 * Joxy's UI delegate for the JFileChooser.
 * 
 * <p>We extend Metal's file chooser here, because the basic LAF file chooser
 * does nothing. We want to improve this file chooser to be more like the KDE
 * one in the future. Therefore we probably need to extend the basic LAF file
 * chooser again.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyFileChooserUI extends MetalFileChooserUI {
	
	/** The JFileChooser we are providing the LAF for. */
	JFileChooser fc;
	
    public static ComponentUI createUI(JComponent c) {
        return new JoxyFileChooserUI((JFileChooser) c);
    }
    
	public JoxyFileChooserUI(JFileChooser b) {
		super(b);
	}
	
	// This is a way to get an icon for some files. That is however quite a stupid way
	// that mostly doesn't work.
	@Override
	public FileView getFileView(JFileChooser fc) {
		return new BasicFileView() {
			@Override
			public Icon getIcon(File f) {
				Icon icon = super.getIcon(f);
				
				Icon newIcon = null;
				
				if (f.isFile()) {
					// TODO this is not used
					FileNameMap fileNameMap = URLConnection.getFileNameMap();
					MimetypesFileTypeMap map = new MimetypesFileTypeMap();
					map.addMimeTypes("application/javascript js");
					map.addMimeTypes("application/msword doc docx docm");
					map.addMimeTypes("application/pdf pdf");
					map.addMimeTypes("application/postscript ps");
					map.addMimeTypes("application/rss+xml rss");
					map.addMimeTypes("application/vnd.ms-excel xls xlsx xlsm");
					map.addMimeTypes("application/vnd.ms-powerpoint ppt pptx pptm");
					map.addMimeTypes("application/vnd.oasis.database odb");
					map.addMimeTypes("application/vnd.oasis.presentation odp");
					map.addMimeTypes("application/vnd.oasis.spreadsheet ods");
					map.addMimeTypes("application/vnd.oasis.text odt");
					map.addMimeTypes("application/x-awk awk");
					map.addMimeTypes("application/x-blender blend");
					map.addMimeTypes("application/x-cd-image iso");
					map.addMimeTypes("application/x-compress zip gz tar rar");
					map.addMimeTypes("application/x-deb deb");
					map.addMimeTypes("application/x-font-otf otf OTF");
					map.addMimeTypes("application/x-font-ttf ttf TTF");
					map.addMimeTypes("application/x-java-applet class");
					map.addMimeTypes("application/x-java-archive jar");
					map.addMimeTypes("application/x-ms-dos-executable exe msi");
					map.addMimeTypes("application/x-perl pl");
					map.addMimeTypes("application/x-php php");
					map.addMimeTypes("application/x-rpm rpm");
					map.addMimeTypes("application/x-sharedlib o");
					map.addMimeTypes("application/x-shellscript sh");
					map.addMimeTypes("application/x-trash autosave");
					map.addMimeTypes("application/xml xml");
					map.addMimeTypes("audio/ac3 ac3");
					map.addMimeTypes("audio/midi mid");
					map.addMimeTypes("audio/x-generic wav wma mp3 ogg");
					map.addMimeTypes("image/x-generic bmp jpg jpeg png tif tiff xpm wmf emf");
					map.addMimeTypes("image/x-eps eps");
					map.addMimeTypes("image/svg+xml svg svgz");
					map.addMimeTypes("text/css css");
					map.addMimeTypes("text/html htm html");
					map.addMimeTypes("text/plain txt");
					map.addMimeTypes("text/rtf rtf");
					map.addMimeTypes("text/x-bibtex bib");
					map.addMimeTypes("text/x-c++hdr h");
					map.addMimeTypes("text/x-c++src cpp c++");
					map.addMimeTypes("text/x-csrc c");
					map.addMimeTypes("text/x-java java");
					map.addMimeTypes("text/x-log log");
					map.addMimeTypes("text/x-pascal pas");
					map.addMimeTypes("text/x-po po pot");
					map.addMimeTypes("text/x-python py");
					map.addMimeTypes("text/x-sql sql");
					map.addMimeTypes("text/x-tcl tcl");
					map.addMimeTypes("text/x-tex tex");
					map.addMimeTypes("text/xml xml osm");
					map.addMimeTypes("video/x-generic wmv mpeg mp4 ogv swf mov dvd osp");
				    String mimeType = map.getContentType(f);
				    
					//String mimeType = fileNameMap.getContentTypeFor(f.getName());
					if (mimeType != null) {
						mimeType = mimeType.replace('/', '-');
					}

					newIcon = Utils.getOxygenIcon("mimetypes/" + mimeType, 16);
				} else { // f is a directory

					// special check for the file system root
					if (f.getAbsolutePath().equals("/")) {
						newIcon = Utils.getOxygenIcon("places/folder-red", 16);
					} else {
						// if there is a .directory file in it, see whether there is an Icon declaration to use
						File directoryFile = new File(f.getAbsolutePath() + "/.directory");
						
						if (directoryFile.exists()) {
							Scanner s = null;
							
							// TODO when switching to Java 7, use try-with-resources
							try {
								s = new Scanner(directoryFile);
								
								while (s.hasNextLine()) {
									String line = s.nextLine();
									if (line.startsWith("Icon=")) {
										newIcon = Utils.getOxygenIcon("places/" + line.substring(5).trim(), 16);
									}
								}
							} catch (FileNotFoundException e) {
								Output.debug(".directory file not found while it should exist");
							} finally {
								if (s != null) {
									s.close();
								}
							}
						}
					}
				}
				
				return newIcon == null ? icon : newIcon;
			}
		};
	}
}
