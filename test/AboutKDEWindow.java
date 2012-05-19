import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


public class AboutKDEWindow extends JFrame {
	/** Serial version UID */
	private static final long serialVersionUID = 4039760594384966275L;

	public AboutKDEWindow() {
		super("Info over KDE");
		
		createContentPane((JPanel) getContentPane());
		
		setSize(680, 480);
		setVisible(true);
	}

	private void createContentPane(JPanel cp) {
		cp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		cp.setLayout(new BorderLayout(10, 10));
		
		cp.add(new UpperPanel(), BorderLayout.PAGE_START);
		try {
			cp.add(new LeftPanel(), BorderLayout.LINE_START);
		} catch (IOException e) {
			System.out.println("Konqi image not found");
		}
		cp.add(new ContentPanel(), BorderLayout.CENTER);
		cp.add(new LowerPanel(), BorderLayout.PAGE_END);
	}
	
	private class UpperPanel extends JPanel {
		/** Serial version UID */
		private static final long serialVersionUID = 835631753655713033L;

		public UpperPanel() {
			setLayout(new GridLayout());
			
			JLabel contentLabel = new JLabel("<html><font size=5>KDE - Wees vrij!</font><br><b>Joxy Look and Feel voor Java Swing</b>");
			contentLabel.setIcon(new ImageIcon("/usr/share/icons/oxygen/48x48/animations/process-idle-kde.png"));
			contentLabel.setVerticalTextPosition(JLabel.TOP);
			add(contentLabel);
		}
	}
	
	private class LeftPanel extends JLabel {
		/** Serial version UID */
		private static final long serialVersionUID = -6478578696701775202L;

		public LeftPanel() throws IOException {
			super(new ImageIcon(ImageIO.read(new File("/usr/share/kde4/apps/kdeui/pics/aboutkde.png"))));
		}
	}
	
	private class ContentPanel extends JPanel {
		/** Serial version UID */
		private static final long serialVersionUID = -2334173606481835530L;

		public ContentPanel() {
			setLayout(new GridLayout());
			
			JTabbedPane tp = new JTabbedPane();
			
			JLabel label1 = new JLabel("<html><b>KDE</b> is een wereldwijd netwerk van software-" +
					"ingenieurs, artiesten, schrijvers, vertalers en facilitators, die aan de " +
					"ontwikkeling van <a href=iets>vrije software</a> zijn toegewijd. Deze " +
					"gemeenschap heeft honderden vrijesoftwareprogramma's ontwikkeld als onderdeel " +
					"van het KDE-ontwikkelplatform en de KDE-softwaredistributie.<br>" +
					"<br>" +
					"KDE is een samenwerkingsverband waarin geen enkele entiteit de controle " +
					"heeft over de inspanningen of producten van KDE of exclusief eigenaar is. " +
					"Iedereen is welkom om mee te doen en te werken aan KDE, inclusief u.<br>" +
					"<br>" +
					"Kijk voor meer informatie over het KDE-project en over de software die we " +
					"maken op <a href=http://www.kde.org/>http://www.kde.org/</a>.");
			label1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			label1.setVerticalAlignment(JLabel.TOP);
			tp.addTab("Over KDE", label1);
			
			tp.addTab("Feedback doorgeven", new JLabel());
			tp.addTab("Lid worden van KDE", new JLabel());
			tp.addTab("KDE ondersteunen", new JLabel());
			
			add(tp);
		}
	}
	
	private class LowerPanel extends JPanel {
		/** Serial version UID */
		private static final long serialVersionUID = -3840377528268742462L;

		public LowerPanel() {
			setLayout(new BorderLayout());
			
			JButton closeButton = new JButton("Sluiten");
			closeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					AboutKDEWindow.this.dispose();
				}
			});
			
			add(closeButton, BorderLayout.LINE_END);
		}
	}
}
