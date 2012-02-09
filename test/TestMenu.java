import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class TestMenu extends JMenuBar {

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
		fileMenu.add(new JMenuItem("Quit", KeyEvent.VK_Q));
		this.add(fileMenu);
		// Edit
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		editMenu.add(new JMenuItem("Settings", KeyEvent.VK_S));
		this.add(editMenu);
		// Tools
		JMenu toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		toolsMenu.add(new JMenuItem("Don't know", KeyEvent.VK_D));
		this.add(toolsMenu);
		// Help
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		JMenuItem item = new JMenuItem("About KDE", KeyEvent.VK_A);
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
