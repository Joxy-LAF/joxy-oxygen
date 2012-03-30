package joxy;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.metal.MetalFileChooserUI;

import joxy.utils.Utils;

/**
 * A {@link JFileChooser} implementation for Joxy. We create the file chooser completely
 * ourselves, i.e. without using the {@link BasicFileChooserUI}. (Note: we do extend it.)
 * TODO is extending the {@link BasicFileChooserUI} really needed?
 * TODO we temporarily extend {@link MetalFileChooserUI} until we have a real implementation
 * TODO this should be rewritten completely
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
					FileNameMap fileNameMap = URLConnection.getFileNameMap();
					MimetypesFileTypeMap map = new MimetypesFileTypeMap();
					map.addMimeTypes("application/pdf pdf");
					map.addMimeTypes("application/x-compress zip gz tar rar");
					map.addMimeTypes("application/x-font-otf otf OTF");
					map.addMimeTypes("application/x-font-ttf ttf TTF");
					map.addMimeTypes("application/x-java-applet class");
					map.addMimeTypes("application/x-java-archive jar");
					map.addMimeTypes("application/x-sharedlib o");
					map.addMimeTypes("application/x-shellscript sh");
					map.addMimeTypes("application/x-trash autosave");
					map.addMimeTypes("image/x-generic bmp jpg jpeg png tif tiff xpm");
					map.addMimeTypes("image/x-eps eps");
					map.addMimeTypes("image/svg+xml svg");
					map.addMimeTypes("text/x-tex tex");
					map.addMimeTypes("text/plain txt");
					map.addMimeTypes("text/xml xml");
				    String mimeType = map.getContentType(f);
				    
					//String mimeType = fileNameMap.getContentTypeFor(f.getName());
					if (mimeType != null) {
						mimeType = mimeType.replace('/', '-');
					}

					newIcon = Utils.getOxygenIcon("mimetypes/" + mimeType, 16);
				}
				
				return newIcon == null ? icon : newIcon;
			}
		};
	}
}
