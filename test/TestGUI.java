import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import joxy.utils.Utils;

public class TestGUI {
	
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
		frame = new JFrame("The Joxytester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setJMenuBar(new TestMenu());
		
		frame.setLayout(new BorderLayout(5, 5));
		
		JToolBar tb = new JToolBar("Toolbar");
		tb.add(new JButton("New", new ImageIcon("/usr/share/icons/oxygen/22x22/actions/document-new.png")));
		tb.add(new JButton("Open", new ImageIcon("/usr/share/icons/oxygen/22x22/actions/document-open-folder.png")));
		tb.add(new JButton("Save", new ImageIcon("/usr/share/icons/oxygen/22x22/actions/document-save.png")));
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

		JButton j = new JButton("Open a file chooser");
		j.setForeground(Color.RED);
		j.setFont(new Font("Times new Roman", Font.PLAIN, 14));
		j.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser f = new JFileChooser();
				f.showOpenDialog(frame);
			}
		});
		tab1.add(j);
		JButton j2 = new JButton("Open a color chooser");
		j2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser.showDialog(frame, "A color chooser", Color.BLACK);
			}
		});
		tab1.add(j2);
		tab1.add(new JTextField("Some text field..."));
		tab1.add(new JToggleButton("A toggle button"));
		Vector<String> items = new Vector<String>();
		items.add("An item");
		items.add("Another item");
		items.add("Still another item");
		items.add("The last item");
		tab1.add(new JComboBox(items));
		JComboBox box = new JComboBox(items);
		box.setEditable(true);
		tab1.add(box);
		tab1.add(new JSpinner());
		t.addTab("Selection of components", tab1);
		
		JPanel tab2 = new JPanel();
		tab2.setPreferredSize(new Dimension(600, 200));
		tab2.add(new JButton("Test"));
		tab2.add(new JButton("This button is a little longer"));
		tab2.add(new JButton("Buttons scale appropiately"));
		tab2.add(new JButton("<html><s>This is a button containing HTML.</s> The previous text should be striped."));
		JButton disabledButton = new JButton("This button is disabled. Yes, I know you didn't see that. We don't draw that yet...");
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
			/** Serial version UID */
			private static final long serialVersionUID = 5596697860474540908L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.BLACK);
				g2.fillOval(getWidth() / 3, getHeight() / 3, getWidth() / 2, getHeight() / 2);
				g2.setColor(Color.WHITE);
				g2.drawString("This panel has a custom paintComponent.", getWidth() / 3 + 50, getHeight() / 2);
			}
		};
		t.addTab("Custom tab", tab4);
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
		vect.add("Hoi");
		vect.add("Hallo");
		vect.add("Dag");
		vect.add("Doei");
		JList list = new JList(vect);
		t.addTab("JList", list);
		
		tab2.setLayout(new BoxLayout(tab2, BoxLayout.PAGE_AXIS));
		t.addTab("JScrollPane", new JScrollPane(tab2));
		
		t.addTab("JEditorPane", new JEditorPane("", "This is a test with a JEditorPane. It needs some work as you can see..."));
		t.addTab("JEditorPane with HTML", new JEditorPane("text/html", "<html><head><title>Joxy!!!</title></head><body>Hi! This is an HTML page.<br><br><font face=\"DejaVu Sans\">testing... testing...</font></body></html>"));
		
		frame.add(t, BorderLayout.CENTER);
		
		frame.setSize(800, 600);
		frame.setVisible(true);

		JOptionPane.showMessageDialog(frame, "This is some information. Very useful, isn't it?", "Information", JOptionPane.INFORMATION_MESSAGE);
		JOptionPane.showMessageDialog(frame, "Watch out! There is an error coming...", "Warning", JOptionPane.WARNING_MESSAGE);
		JOptionPane.showMessageDialog(frame, "Oh no, something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
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
		trayIcon.displayMessage("Joxy", "This is the Joxy test GUI. Do you see this notification?", MessageType.INFO);
		
		// The Joxy method
		Utils.displayNotification("Joxy", "This is the Joxy test GUI. Do you see this notification?", MessageType.INFO);
	}
}
