package joxy.test;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;

/**
 * A graphical user interface containing a lot of buttons.
 */
public class ButtonGUI {
	
	static JFrame frame;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel("joxy.JoxyLookAndFeel");
					
					//UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel");
					//UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
					//UIManager.setLookAndFeel("net.sourceforge.napkinlaf.NapkinLookAndFeel");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				showSomeGUI();
			}
		});
	}
	
	protected static void showSomeGUI() {
		frame = new JFrame("A lot of buttons");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLayout(new GridLayout(0, 3));
		
		for (int i = 1; i <= 30; i++) {
			frame.add(new JButton("Button " + i));
		}
		
		frame.pack();
		frame.setVisible(true);
	}
}
