package joxy;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.metal.MetalFileChooserUI;

import joxy.utils.Output;

/**
 * A {@link JFileChooser} implementation for Joxy. We create the file chooser completely
 * ourselves, i.e. without using the {@link BasicFileChooserUI}. (Note: we do extend it.)
 * TODO is extending the {@link BasicFileChooserUI} really needed?
 */
public class JoxyFileChooserUI extends BasicFileChooserUI {
	
	/** The JFileChooser we are providing the LAF for. */
	JFileChooser fc;
	
    public static ComponentUI createUI(JComponent c) {
        return new JoxyFileChooserUI((JFileChooser) c);
    }
    
	public JoxyFileChooserUI(JFileChooser b) {
		super(b);
	}

	/**
	 * Add the GUI elements to the file chooser.
	 */
	private void installComponents() {
		// Why is this method not being called?
    	Output.debug();
		fc.add(new JLabel("Hier moet een JFileChooser komen!"));
	}
}
