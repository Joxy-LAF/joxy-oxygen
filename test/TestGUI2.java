import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TestGUI2 {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel("joxy.JoxyLookAndFeel");
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
		JFrame frame = new JFrame("Joxytester 2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setJMenuBar(new TestMenu());
		
		Container cp = frame.getContentPane();
		cp.setLayout(new BorderLayout(5, 5));
		
		JDesktopPane dinges = new JDesktopPane();
		JInternalFrame frame1 = new JInternalFrame("Een frame");
		frame1.setBounds(100, 100, 300, 300);
		dinges.add(frame1);
		cp.add(new JScrollPane(dinges), BorderLayout.CENTER);
		JPanel paneel = new JPanel();
		paneel.setLayout(new BoxLayout(paneel, BoxLayout.PAGE_AXIS));
		paneel.add(new JLabel("Gereedschap"));
		paneel.add(new JButton("Tekst knippen"));
		paneel.add(new JButton("Tekst kopiëren"));
		paneel.add(new JButton("Tekst plakken"));
		paneel.add(new JTextField("Een instelling"));
		cp.add(paneel, BorderLayout.LINE_END);
		cp.add(new JLabel("Dit is een statusbalk"), BorderLayout.PAGE_END);
		
		frame.pack();
		frame.setVisible(true);
	}
}
