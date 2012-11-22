package joxy.test;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;


public class TestMenu extends JMenuBar {

	/** Serial version UID */
	private static final long serialVersionUID = 8183484172726704741L;

	public TestMenu() {
		// File
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(new JMenuItem("New", new ImageIcon("/usr/share/icons/oxygen/16x16/actions/document-new.png")));
		fileMenu.add(new JMenuItem("Open", new ImageIcon("/usr/share/icons/oxygen/16x16/actions/document-open-folder.png")));
		fileMenu.add(new JMenuItem("Save", new ImageIcon("/usr/share/icons/oxygen/16x16/actions/document-save.png")));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem("A very long name for a menu item", KeyEvent.VK_Q));
		JMenu fileSubmenu = new JMenu("A submenu");
		fileSubmenu.setMnemonic(KeyEvent.VK_E);
		fileSubmenu.add(new JMenuItem("With an item", KeyEvent.VK_S));
		fileMenu.add(fileSubmenu);
		fileMenu.addSeparator();
		JMenuItem quitItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
		quitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // ugly, I know
			}
		});
		fileMenu.add(quitItem);
		this.add(fileMenu);
		// Edit
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		editMenu.add(new JMenuItem("Settings", KeyEvent.VK_S));
		JMenuItem changeLAFItem = new JMenuItem("Change LAF");
		changeLAFItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK | Event.SHIFT_MASK));
		changeLAFItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LookAndFeelInfo[] lafsinfo = UIManager.getInstalledLookAndFeels();
				String[] lafs = new String[lafsinfo.length];
				for (int i = 0; i < lafsinfo.length; i++) {
					lafs[i] = lafsinfo[i].getName();
				}
				int result = JOptionPane.showOptionDialog(null, "Choose the LAF to use.", "The Joxytester - Change LAF",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						lafs, null);
				
				if (result != JOptionPane.CLOSED_OPTION) {
					try {
						UIManager.setLookAndFeel(lafsinfo[result].getClassName());
						SwingUtilities.updateComponentTreeUI(getTopLevelAncestor());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Something went wrong while setting that LAF. " +
								"Message: (" + e1.getClass().getName() + "): \"" + e1.getMessage() + "\".",
								"The Joxytester - Change LAF error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		editMenu.add(changeLAFItem);
		this.add(editMenu);
		// Tools
		JMenu toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		toolsMenu.add(new JLabel("Some radio button menu items:"));
		toolsMenu.add(new JRadioButtonMenuItem("Foo"));
		toolsMenu.add(new JRadioButtonMenuItem("Bar"));
		toolsMenu.add(new JRadioButtonMenuItem("Baz"));
		toolsMenu.addSeparator();
		toolsMenu.add(new JLabel("Some check box menu items:"));
		toolsMenu.add(new JCheckBoxMenuItem("Foo"));
		toolsMenu.add(new JCheckBoxMenuItem("Bar"));
		toolsMenu.add(new JCheckBoxMenuItem("Baz"));
		toolsMenu.addSeparator();
		toolsMenu.add(new JLabel("Some non-menu components:"));
		JSpinner spinner = new JSpinner();
		spinner.setAlignmentX(0);
		toolsMenu.add(spinner);
		JComboBox<String> combo = new JComboBox<String>(new String[]{"A combobox", "Inside a menu", "Weird, isn't it?"});
		combo.setAlignmentX(0);
		toolsMenu.add(combo);
		this.add(toolsMenu);
		// Help
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		JMenuItem item = new JMenuItem("About KDE", KeyEvent.VK_A);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new AboutKDEWindow();
			}
		});
		helpMenu.add(item);
		this.add(helpMenu);
	}
	
}
