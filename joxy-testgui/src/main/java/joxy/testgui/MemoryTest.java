package joxy.testgui;

import java.awt.event.*;
import javax.swing.*;

public class MemoryTest {
	
	static JFrame frame;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel("joxy.JoxyLookAndFeel");
					//UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				showSomeGUI();
			}
		});
	}

	protected static void showSomeGUI() {
		frame = new JFrame("Memory test for Joxy");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JLabel label = new JLabel("starting");
		
		Timer t = new Timer(10, new ActionListener() {
			
			long value = 10000000000000l;
			@Override
			public void actionPerformed(ActionEvent e) {
				value++;
				label.setText("This is a memory test for Joxy. We want to see how much memory the string caching needs. The current value is " + value);
			}
		});
		t.start();
		
		frame.add(label);
		
		frame.pack();
		frame.setVisible(true);
	}
}
