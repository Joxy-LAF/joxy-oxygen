package joxy;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.metal.MetalFileChooserUI;

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
	
	// FIXME [ws] This is a hack to prevent NullPointerExceptions
/*	@Override
	public FileView getFileView(JFileChooser fc) {
		return new BasicFileView() {
			@Override
			public Icon getIcon(File f) {
				return new ImageIcon();
			}
		};
	}*/
}
