import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

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
		frame = new JFrame("De Joxytester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setJMenuBar(new TestMenu());
		
		frame.setLayout(new BorderLayout(5, 5));
		
		JToolBar tb = new JToolBar("Werkbalk");
		tb.add(new JButton("Nieuw", new ImageIcon("/usr/share/icons/oxygen/22x22/actions/document-new.png")));
		tb.add(new JButton("Openen", new ImageIcon("/usr/share/icons/oxygen/22x22/actions/document-open-folder.png")));
		tb.add(new JButton("Opslaan", new ImageIcon("/usr/share/icons/oxygen/22x22/actions/document-save.png")));
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
		tab1.add(new JTextField("The game"));
		tab1.add(new JToggleButton("Aan of uit?"));
		t.addTab("Een tab", tab1);
		JPanel tab2 = new JPanel();
		tab2.setPreferredSize(new Dimension(600, 200));
		tab2.add(new JButton("Test"));
		tab2.add(new JButton("Mooie knopjes toch?"));
		tab2.add(new JButton("Alleen de schaduw moet nog wat beter"));
		tab2.add(new JButton("Wat raar is aangezien hij overgenomen is van de checkbox"));
		tab2.add(new JButton("En die heeft wel goede schaduw"));
		tab2.add(new JButton("<html><s>En knoppen worden niet ingedrukt als je erop klikt</s> Wel! (Nou ja, een beetje.)"));
		tab2.add(new JButton("Ten slotte worden ze niet disabled getekend"));
		tab2.add(new JButton("De focus werkt wel goed"));
		tab2.add(new JTextField("Hé! Dit is geen knop!"));
		tab2.add(new JButton("Helaas is er nog geen animatie"));
		tab2.add(new JButton("Links zie je trouwens ook een knop"));
		tab2.add(new JButton("Die is wel erg groot"));
		tab2.add(new JButton("Er zat er ook één in het dialoogvenster"));
		tab2.add(new JButton("Maar dat is waarschijnlijk al weg"));
		tab2.add(new JButton("<html><center>Aha! <b>HTML</b> werkt nu <font color=red>ook</font>! Ook met &lt;br&gt;!<br>" +
				"Wat een <big>grote</big> knop is dit!<br>" +
				"En de grootte ervan klopt precies met de tekst die erin staat."));
		t.addTab("Tijd voor knopjes", tab2);
		t.addTab("Een label", new JScrollPane(new JLabel("<html>Dit is een <b>JLabel</b> met HTML-ondersteuning.<br>" +
				"Is dat niet <i>mooi</i>?<br>" +
				"Je kunt ook <font color=red>kleurtjes</font> toevoegen.<br>" +
				"Of de tekst <big>groter</big> of <small>kleiner </small> maken.<br>" +
				"Én het heeft nog antialiasing ook!")));
		JPanel tab4 = new JPanel() {
			/** Serial version UID */
			private static final long serialVersionUID = 5596697860474540908L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				//Hole.paintHole((Graphics2D) g, Color.GRAY, Color.CYAN, 7, false, false);
			}
		};
		t.addTab("Een zelftekenende tab", tab4);
		t.addTab("Tekst", new JTextArea("Dit is een JTextArea."));
		t.addTab("Meer tekst", new JTextPane());
		
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
		t.addTab("Een tabel", scrollpane);
		
		Vector<String> vect = new Vector<String>();
		vect.add("Hoi");
		vect.add("Hallo");
		vect.add("Dag");
		vect.add("Doei");
		JList list = new JList(vect);
		t.addTab("Een lijst", list);
		
		JPanel tabZoveel = new JPanel();
		tabZoveel.setPreferredSize(new Dimension(600, 200));
		tabZoveel.setLayout(new BoxLayout(tabZoveel, BoxLayout.PAGE_AXIS));
		tabZoveel.add(new JButton("Test"));
		tabZoveel.add(new JButton("Mooie knopjes toch?"));
		tabZoveel.add(new JButton("Alleen de schaduw moet nog wat beter"));
		tabZoveel.add(new JButton("Wat raar is aangezien hij overgenomen is van de checkbox"));
		tabZoveel.add(new JButton("En die heeft wel goede schaduw"));
		tabZoveel.add(new JButton("<html><s>En knoppen worden niet ingedrukt als je erop klikt</s> Wel! (Nou ja, een beetje.)"));
		tabZoveel.add(new JButton("Ten slotte worden ze niet disabled getekend"));
		tabZoveel.add(new JButton("De focus werkt wel goed"));
		tabZoveel.add(new JTextField("Hé! Dit is geen knop!"));
		tabZoveel.add(new JButton("Helaas is er nog geen animatie"));
		tabZoveel.add(new JButton("Links zie je trouwens ook een knop"));
		tabZoveel.add(new JButton("Die is wel erg groot"));
		tabZoveel.add(new JButton("Er zat er ook één in het dialoogvenster"));
		tabZoveel.add(new JButton("Maar dat is waarschijnlijk al weg"));
		tabZoveel.add(new JButton("<html><center>Aha! <b>HTML</b> werkt nu <font color=red>ook</font>! Ook met &lt;br&gt;!<br>" +
				"Wat een <big>grote</big> knop is dit!<br>" +
				"En de grootte ervan klopt precies met de tekst die erin staat."));
		t.addTab("Een scroll-pane", new JScrollPane(tabZoveel));
		
		t.addTab("Een JEditorPane", new JEditorPane("", "Dit is een test met een JEditorPane. Hmm, daar moet " +
				"nog wel wat aan gebeuren!"));
		t.addTab("Een JEditorPane met HTML", new JEditorPane("text/html", "<html><head><title>Joxy!!!</title></head><body>Hoi! Dit is een HTML-pagina.<br>Hoe kan het nou dat deze wel geantialiast is?<br><font face=\"DejaVu Sans\">testerdetest</font></body></html>"));
		
		frame.add(t, BorderLayout.CENTER);
		
		frame.setSize(800, 600);
		frame.setVisible(true);

		JOptionPane.showMessageDialog(frame, "<html>Ja, we hebben een <b>JOptionPane</b> mét mooie achtergrond!<br>" +
				"Helaas nog zonder icoontjes.<br>" +
				"Maar daar gaan we nog wel wat aan doen.<br>" +
				"Er staat ook nog geen vinkje op de OK-knop.<br>" +
				"En de OK-knop is nog niet donker genoeg... maar dat wordt opgelost<br>" +
				"als we onze mooie nieuwe knopjescode klaar hebben.", "Een JOptionPane", JOptionPane.INFORMATION_MESSAGE);
	}
}
