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

package joxy.testgui;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import joxy.utils.JoxyGraphics;
import joxy.utils.Output;
import joxy.utils.Utils;

/**
 * A graphical user interface containing all kinds of GUI elements, for
 * testing purposes.
 * 
 * <p>To run this from the Joxy JAR file, use the command
 * <code>java -cp joxy.jar joxy.test.TestGUI</code>.</p>
 */
public class TestGUI {
	
	private static JFrame frame;
	
	/**
	 * The main method, that sets the look-and-feel to Joxy and shows the GUI.
	 * @param args The command-line arguments; these are ignored.
	 */
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
				
				showTestGUI();
			}
		});
	}

	private static BufferedImage konqi;
	
	static {
		try {
			konqi = ImageIO.read(new File("/usr/share/kde4/apps/kdeui/pics/aboutkde.png")); // Debian, Kubuntu
		} catch (IOException e) {
			try {
				konqi = ImageIO.read(new File("/usr/share/apps/kdeui/pics/aboutkde.png")); // Chakra
			} catch (IOException e2) {
				Output.warning("Konqi image not found");
			}
		}
	}
	
	/**
	 * Shows the GUI.
	 */
	protected static void showTestGUI() {
		frame = new JFrame("Test GUI for Joxy");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setJMenuBar(new TestMenu(frame));
		
		frame.setLayout(new BorderLayout(5, 5));
		
		JToolBar tb = new JToolBar("Toolbar");
		tb.add(new JButton("New", new ImageIcon("/usr/share/icons/oxygen/22x22/actions/document-new.png")));
		tb.add(new JButton("Open", new ImageIcon("/usr/share/icons/oxygen/22x22/actions/document-open-folder.png")));
		JButton saveButton = new JButton("Save", new ImageIcon("/usr/share/icons/oxygen/22x22/actions/document-save.png"));
		saveButton.setEnabled(false);
		tb.add(saveButton);
		frame.add(tb, BorderLayout.NORTH);
		
		JTabbedPane t = new JTabbedPane(JTabbedPane.TOP);
		final JProgressBar pr = new JProgressBar();
		pr.setValue((int) (Math.random() * 101));
		pr.setStringPainted(true);
		pr.addMouseListener(new MouseListener() {
			
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				pr.setValue((int) (Math.random() * 101));
			}
		});
		JPanel tab1 = new JPanel();
		tab1.add(new JLabel("<html><i>This is a GUI that can be used to test Joxy."));
		tab1.add(pr);
		JProgressBar pr2 = new JProgressBar();
		pr2.setIndeterminate(true);
		tab1.add(pr2);
		
		tab1.add(new JCheckBox("A check box"));
		tab1.add(new JRadioButton("A radio button"));

		JButton j = new JButton("Open a file chooser");
		j.setForeground(Color.RED);
		j.setToolTipText("A tooltip");
		j.setFont(new Font("Times new Roman", Font.PLAIN, 14));
		j.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser f = new JFileChooser();
				f.showOpenDialog(frame);
			}
		});
		tab1.add(j);

		JButton jawt = new JButton("Open an AWT file chooser");
		jawt.setToolTipText("A tooltip");
		jawt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				java.awt.FileDialog f = new FileDialog(frame);
				f.setVisible(true);
			}
		});
		tab1.add(jawt);
		JButton j2 = new JButton("Open a color chooser");
		j2.setToolTipText("<html>Another tooltip, this time with <big>HTML</big>");
		j2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(frame, "A color chooser", Color.GREEN);
				JOptionPane.showMessageDialog(frame, "Color chosen: " + c);
			}
		});
		tab1.add(j2);
		tab1.add(new JTextField("Some text field..."));
		JTextField tf = new JTextField("Some disabled text field...");
		tf.setEnabled(false);
		tab1.add(tf);
		tab1.add(new JToggleButton("A toggle button"));
		Vector<String> items = new Vector<String>();
		for (int i = 0; i < 20; i++) {
			items.add("This is item " + i);
		}
		tab1.add(new JComboBox(items));
		JComboBox box = new JComboBox(items);
		box.setEditable(true);
		tab1.add(box);
		tab1.add(new JSpinner());
		tab1.add(new JFormattedTextField("A formatted text field"));
		tab1.add(new JPasswordField(10));
		JButton notificationButton = new JButton("Show notification");
		notificationButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.displayNotification("Test notification", "This is a test notification "
						+ "from the Joxy test GUI.", MessageType.INFO);
			}
		});
		tab1.add(notificationButton);
		t.addTab("Selection of components", tab1);
		
		t.addTab("Disabled tab", new JPanel());
		t.setEnabledAt(1, false);
		
		JPanel styleTab = new JPanel();
		JLabel styleBoldLabel = new JLabel("This text is bold.");
		styleBoldLabel.setFont(styleBoldLabel.getFont().deriveFont(Font.BOLD));
		styleTab.add(styleBoldLabel);
		JLabel styleItalicLabel = new JLabel("This text is italic.");
		styleItalicLabel.setFont(styleItalicLabel.getFont().deriveFont(Font.ITALIC));
		styleTab.add(styleItalicLabel);
		JLabel styleBoldItalicLabel = new JLabel("This text is bold and italic.");
		styleBoldItalicLabel.setFont(styleBoldItalicLabel.getFont().deriveFont(Font.BOLD + Font.ITALIC));
		styleTab.add(styleBoldItalicLabel);
		t.addTab("Styling", styleTab);
		
		JPanel tab2 = new JPanel();
		tab2.setPreferredSize(new Dimension(600, 200));
		tab2.add(new JButton("Test"));
		tab2.add(new JButton("This button is a little longer"));
		tab2.add(new JButton("Buttons scale appropiately"));
		tab2.add(new JButton("<html><s>This is a button containing HTML.</s> The previous text should be striped."));
		JButton disabledButton = new JButton("This button is disabled, but you noticed that, didn't you? No, there is no focus if you hover me.");
		disabledButton.setEnabled(false);
		tab2.add(disabledButton);
		tab2.add(new JButton("Notice that the focus works well"));
		JButton j3 = new JButton("This button has no background painted");
		j3.setContentAreaFilled(false);
		tab2.add(j3);
		tab2.add(new JTextField("Oh wait, this is not a button!"));
		tab2.add(new JButton("Sadly, there is no animation yet"));
		tab2.add(new JButton("<html><center>Another button containing <b>HTML</b>. We have <font color=red>font colors</font>! And &lt;br&gt;!<br>" +
				"Wow. This button is pretty <big>big</big>.<br>" +
				"The size is exactly the wanted size :)"));
		t.addTab("JButton", tab2);
		t.addTab("JScrollpane", new JScrollPane(new JLabel("<html>This is a <b>JLabel</b> with HTML-support. No native text rendering yet however ;(<br>" +
				"But it looks nice, <i>right</i>?<br>" +
				"You can change the <font color=red>font color</font>.<br>" +
				"Or make the text <big>bigger</big> or <small>smaller</small>.<br>" +
				"And the text is anti-aliased!")));
		JPanel tab4 = new JPanel() {
			{
				setPreferredSize(new Dimension(620, 440));
			}
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawImage(konqi, 100, 150, null);
				g2.setColor(Color.WHITE);
				g2.fillOval(190, 60, 300, 50);
				g2.setColor(Color.BLACK);
				g2.drawOval(190, 60, 300, 50);
				GeneralPath balloonPath = new GeneralPath();
				balloonPath.moveTo(220, 99);
				balloonPath.lineTo(210, 170);
				balloonPath.lineTo(240, 103);
				g2.setColor(Color.WHITE);
				g2.fill(balloonPath);
				g2.setColor(Color.BLACK);
				g2.draw(balloonPath);
				g2.setFont(getFont());
				JoxyGraphics.drawString(g2, "This panel has a custom paintComponent.", 206, 90);
			}
		};
		t.addTab("Custom tab", new JScrollPane(tab4));
		
		t.addTab("JTextArea", new JTextArea("This is a JTextArea."));
		t.addTab("JTextPane", new JTextPane());
		
		TableModel dataModel = new AbstractTableModel() {
			/** Serial version UID */
			private static final long serialVersionUID = 1666347484034514225L;

			@Override
			public int getColumnCount() {
				return 10;
			}

			@Override
			public int getRowCount() {
				return 10;
			}

			@Override
			public Object getValueAt(int row, int col) {
				return new Integer(row * col);
			}
		};
		JTable table = new JTable(dataModel);
		JScrollPane scrollpane = new JScrollPane(table);
		t.addTab("JTable (in a JScrollPane)", scrollpane);
		
		Vector<String> vect = new Vector<String>();
		vect.add("These ...");
		vect.add("... are ...");
		vect.add("... items ...");
		vect.add("... in ...");
		vect.add("... a ...");
		vect.add("... JList.");
		JList list = new JList(vect);
		t.addTab("JList", new JScrollPane(list));
		
		tab2.setLayout(new BoxLayout(tab2, BoxLayout.PAGE_AXIS));
		t.addTab("JScrollPane", new JScrollPane(tab2));
		
		t.addTab("JEditorPane", new JEditorPane("", "This is a test with a JEditorPane. It needs some work as you can see..."));
		t.addTab("JEditorPane with HTML", new JEditorPane("text/html", "<html><head><title>Joxy!!!</title></head><body>Hi! This is an HTML page.<br><br>" +
				"<font face=\"DejaVu Sans\">This text is written in another font.</font><br><br>" +
				"<font color=green>This text is written in another color.</font></body></html>"));
		
		frame.add(t, BorderLayout.CENTER);
		
		frame.setSize(800, 600);
		frame.setVisible(true);
		
		// setWindowOpacity(frame, 0.9);

		//JOptionPane.showMessageDialog(frame, "This is some information. Very useful, isn't it?", "Information", JOptionPane.INFORMATION_MESSAGE);
		//JOptionPane.showMessageDialog(frame, "Watch out! There is an error coming...", "Warning", JOptionPane.WARNING_MESSAGE);
		//JOptionPane.showMessageDialog(frame, "Oh no, something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
		//JOptionPane.showConfirmDialog(frame, "This is a question.", "Question", JOptionPane.YES_NO_CANCEL_OPTION);
		
		// The generic Java method to send a notification
		TrayIcon trayIcon = null;
		try {
			trayIcon = new TrayIcon(ImageIO.read(new File("/usr/share/icons/oxygen/22x22/actions/document-new.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		//trayIcon.displayMessage("Joxy", "This is the Joxy test GUI. Do you see this notification?", MessageType.INFO);
		//trayIcon.displayMessage("Joxy", "And a warning!", MessageType.WARNING);
		//trayIcon.displayMessage("Joxy", "And an error!", MessageType.ERROR);
		
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Java LAFs");
		JTree tree = new JTree(node);
		t.addTab("A JTree", new JScrollPane(tree));
		node.add(new DefaultMutableTreeNode("Metal"));
		node.add(new DefaultMutableTreeNode("Motif"));
		node.add(new DefaultMutableTreeNode("Windows"));
		node.add(new DefaultMutableTreeNode("Aqua"));
		node.add(new DefaultMutableTreeNode("GTK+"));
		DefaultMutableTreeNode joxy = new DefaultMutableTreeNode("Joxy");
		node.add(joxy);
		joxy.add(new DefaultMutableTreeNode("Joxy 0.0.1"));
		joxy.add(new DefaultMutableTreeNode("Joxy 0.0.2"));
		joxy.add(new DefaultMutableTreeNode("Joxy 0.0.3"));
		joxy.add(new DefaultMutableTreeNode("Joxy 0.1.0"));
		joxy.add(new DefaultMutableTreeNode("Joxy Git"));
		
		t.addTab("A JSplitPane", new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JButton("left"), new JButton("right")));
		
		JPanel tab5 = new JPanel(new GridLayout(3, 3));
		
		tab5.add(new JLabel());
		tab5.add(new JLabel("opaque"));
		tab5.add(new JLabel("non-opaque"));
		
		String text = "This is quite a long text in a JTextArea. ";
		text += text;
		text += text;
		text += text;
		text += text;
		text += text;
		
		tab5.add(new JLabel("editable"));
		JTextArea editor1 = new JTextArea(text);
		editor1.setLineWrap(true);
		editor1.setWrapStyleWord(true);
		tab5.add(new JScrollPane(editor1));
		JTextArea editor2 = new JTextArea(text);
		editor2.setLineWrap(true);
		editor2.setWrapStyleWord(true);
		editor2.setOpaque(false);
		tab5.add(new JScrollPane(editor2));
		
		tab5.add(new JLabel("non-editable"));
		JTextArea editor3 = new JTextArea(text);
		editor3.setLineWrap(true);
		editor3.setWrapStyleWord(true);
		editor3.setEditable(false);
		tab5.add(new JScrollPane(editor3));
		JTextArea editor4 = new JTextArea(text);
		editor4.setLineWrap(true);
		editor4.setWrapStyleWord(true);
		editor4.setOpaque(false);
		editor4.setEditable(false);
		tab5.add(new JScrollPane(editor4));
		
		t.addTab("Text components", tab5);
		
		JDesktopPane tab6 = new JDesktopPane();
		
		JInternalFrame window1 = new JInternalFrame("A window", true, true, true, true);
		window1.setBounds(100, 100, 400, 300);
		window1.setVisible(true);
		JMenuBar menu = new JMenuBar();
		JMenu menu1 = new JMenu("Foo");
		menu1.add(new JMenuItem("Fooooooooooo"));
		menu.add(menu1);
		JMenu menu2 = new JMenu("Bar");
		menu2.add(new JMenuItem("Baaaaaaaaaar"));
		menu.add(menu2);
		JMenu menu3 = new JMenu("Baz");
		menu3.add(new JMenuItem("Baaaaaaaaaaz"));
		menu.add(menu3);
		window1.setJMenuBar(menu);
		tab6.add(window1);
		JInternalFrame window2 = new JInternalFrame("Another window", true, true, true, true);
		window2.setBounds(300, 250, 400, 300);
		window2.setVisible(true);
		window2.add(new JButton("Do nothing"));
		tab6.add(window2);
		
		t.addTab("Internal frames", tab6);
		
		JPanel tab7 = new JPanel(new GridLayout(3, 3, 5, 5));
		
		// line border
		JPanel border1 = new JPanel();
		border1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		tab7.add(border1);

		// raised etched border
		JPanel border2 = new JPanel();
		border2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		tab7.add(border2);
		
		// lowered etched border
		JPanel border3 = new JPanel();
		border3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		tab7.add(border3);
		
		// raised bevel border
		JPanel border4 = new JPanel();
		border4.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		tab7.add(border4);
		
		// lowered bevel border
		JPanel border5 = new JPanel();
		border5.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		tab7.add(border5);

		// soft raised bevel border
		JPanel border6 = new JPanel();
		border6.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
		tab7.add(border6);
		
		// soft lowered bevel border
		JPanel border7 = new JPanel();
		border7.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED));
		tab7.add(border7);
		
		// titled border
		JPanel border8 = new JPanel();
		border8.setBorder(BorderFactory.createTitledBorder("Title"));
		tab7.add(border8);
		
		t.addTab("Borders", tab7);
		
		JPanel tab8 = new JPanel();
		
		for (int i = 1; i <= 1000; i++) {
			JButton button = new JButton("Button " + i);
			tab8.add(button);
			button.setBackground(new Color((int) (Math.random() * 255),
					(int) (Math.random() * 255),
					(int) (Math.random() * 255)));
		}
		
		t.addTab("Buttons", tab8);
	}
	
	/**
	 * For integration with Oxygen-Transparent, set the opacity and blur.
	 * 
	 * @param frame The JFrame to set the transparency on.
	 * @param opacity The opacity, from 0 to 1.
	 */
    protected static void setWindowOpacity(Frame frame, double opacity) {
		try {
			// long windowId = peer.getWindow();
			Field peerField = Component.class.getDeclaredField("peer");
			peerField.setAccessible(true);
			Class<?> xWindowPeerClass = Class.forName("sun.awt.X11.XWindowPeer");
			Method getWindowMethod = xWindowPeerClass.getMethod("getWindow", new Class[0]);
			long windowId = ((Long) getWindowMethod.invoke(peerField.get(frame), new Object[0])).longValue();

			sun.awt.X11.XAtom.get("_NET_WM_WINDOW_OPACITY").setCard32Property(windowId, (int) (0xff * opacity) << 24);
			sun.awt.X11.XAtom.get("_KDE_NET_WM_BLUR_BEHIND_REGION").setCard32Property(windowId, 32);
			sun.awt.X11.XAtom.get("_NET_WM_OPAQUE_REGION").setCard32Property(windowId, 32);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
