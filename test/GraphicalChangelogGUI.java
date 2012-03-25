import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import joxy.JoxyLookAndFeel;

public class GraphicalChangelogGUI {
	
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
		frame = new JFrame("Graphical changelog");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel cp = (JPanel) frame.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(new JLabel("<html><center><big>This is Joxy version <b>" + ((JoxyLookAndFeel) UIManager.getLookAndFeel()).getVersion()), BorderLayout.PAGE_START);
		
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		
		cp.add(p);
		
		p.add(new JButton("JButton"));
		p.add(new JCheckBox("JCheckBox", true));
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("JComboBox");
		p.add(comboBox);
		JComboBox comboBox2 = new JComboBox();
		comboBox2.setEditable(true);
		comboBox2.addItem("JComboBox (editable)");
		p.add(comboBox2);
		p.add(new JEditorPane("text/html", "JEditorPane<br>JEditorPane"));
		FileSystemView fileChooserView = new FileSystemView() {
			
			@Override
			public File createNewFolder(File containingDir) throws IOException {
				return null;
			}
			
			@Override
			public File[] getFiles(File dir, boolean useFileHiding) {
				return new File[]{new File("JFileChooser        "), new File("JFileChooser"), new File("JFileChooser"), new File("JFileChooser")};
			}
			
			@Override
			public File getDefaultDirectory() {
				return new File("/");
			}
		};
		JFileChooser fileChooser = new JFileChooser(fileChooserView);
		fileChooser.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		fileChooser.setPreferredSize(new Dimension(350, 210));
		p.add(fileChooser);
		p.add(new JFormattedTextField("JFormattedTextField"));
		p.add(new JLabel("JLabel"));
		Vector listVector = new Vector();
		listVector.add("JList          ");
		listVector.add("JList          ");
		listVector.add("JList          ");
		listVector.add("JList          ");
		JList list = new JList(listVector);
		list.setSelectedIndex(0);
		p.add(list);
		JOptionPane optionPane = new JOptionPane("JOptionPane\n(information)", JOptionPane.INFORMATION_MESSAGE);
		optionPane.setPreferredSize(new Dimension(170, 98));
		optionPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		p.add(optionPane);
		JOptionPane optionPane2 = new JOptionPane("JOptionPane\n(warning)", JOptionPane.WARNING_MESSAGE);
		optionPane2.setPreferredSize(new Dimension(170, 98));
		optionPane2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		p.add(optionPane2);
		JOptionPane optionPane3 = new JOptionPane("JOptionPane\n(error)", JOptionPane.ERROR_MESSAGE);
		optionPane3.setPreferredSize(new Dimension(170, 98));
		optionPane3.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		p.add(optionPane3);
		p.add(new JPasswordField("JPasswordField"));
		JProgressBar progressBar = new JProgressBar();
		progressBar.setString("JProgressBar");
		progressBar.setValue(50);
		progressBar.setStringPainted(true);
		p.add(progressBar);
		p.add(new JRadioButton("JRadioButton", true));
		JLabel scrollLabel = new JLabel("JScrollPane");
		scrollLabel.setOpaque(true);
		scrollLabel.setBackground(Color.WHITE);
		scrollLabel.setPreferredSize(new Dimension(200, 100));
		scrollLabel.setVerticalAlignment(JLabel.TOP);
		JScrollPane scrollPane = new JScrollPane(scrollLabel);
		scrollPane.setPreferredSize(new Dimension(130, 80));
		p.add(scrollPane);
		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(5, 40));
		separator.setOrientation(JSeparator.VERTICAL);
		p.add(separator);
		JSeparator separator2 = new JSeparator();
		separator2.setPreferredSize(new Dimension(40, 5));
		separator2.setOrientation(JSeparator.HORIZONTAL);
		p.add(separator2);
		JSlider slider = new JSlider(0, 10, 5);
		slider.setMajorTickSpacing(5);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		p.add(slider);
		p.add(new JSpinner());
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("JTabbedPane", new JPanel());
		tabbedPane.addTab("JTabbedPane", new JPanel());
		tabbedPane.setPreferredSize(new Dimension(210, 50));
		p.add(tabbedPane);		TableModel dataModel = new AbstractTableModel() {
			/** Serial version UID */
			private static final long serialVersionUID = 1666347484034514225L;

			@Override
			public int getColumnCount() {
				return 3;
			}

			@Override
			public int getRowCount() {
				return 3;
			}

			@Override
			public Object getValueAt(int row, int col) {
				return "JTable";
			}
		};
		JTable table = new JTable(dataModel);
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setPreferredSize(new Dimension(170, 80));
		p.add(tableScroll);
		p.add(new JTextArea("JTextArea"));
		p.add(new JTextField("JTextField"));
		p.add(new JToggleButton("JToggleButton", true));
		Vector treeVector = new Vector();
		treeVector.add("JTree          ");
		treeVector.add("JTree          ");
		treeVector.add("JTree          ");
		treeVector.add("JTree          ");
		p.add(new JTree(treeVector));
		
		frame.setSize(600, 650);
		frame.setVisible(true);
	}
}
