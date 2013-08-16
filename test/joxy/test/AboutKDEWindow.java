package joxy.test;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import joxy.utils.Output;

/**
 * A copy of the <i>About KDE</i> window, in Swing.
 */
public class AboutKDEWindow extends JFrame {
	/** Serial version UID */
	private static final long serialVersionUID = 4039760594384966275L;

	public AboutKDEWindow() {
		super("About KDE");
		
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
			Output.warning("Konqi image not found");
		}
		cp.add(new ContentPanel(), BorderLayout.CENTER);
		cp.add(new LowerPanel(), BorderLayout.PAGE_END);
	}
	
	private class UpperPanel extends JPanel {
		/** Serial version UID */
		private static final long serialVersionUID = 835631753655713033L;

		public UpperPanel() {
			setLayout(new GridLayout());
			
			JLabel contentLabel = new JLabel("<html><font size=5>KDE - Be free!</font><br><b>Joxy Look and Feel</b>");
			contentLabel.setIcon(new ImageIcon("/usr/share/icons/oxygen/48x48/animations/process-idle-kde.png"));
			contentLabel.setVerticalTextPosition(JLabel.TOP);
			add(contentLabel);
		}
	}
	
	private class LeftPanel extends JLabel {
		/** Serial version UID */
		private static final long serialVersionUID = -6478578696701775202L;

		public LeftPanel() throws IOException {
			try {
				setIcon(new ImageIcon(ImageIO.read(new File("/usr/share/kde4/apps/kdeui/pics/aboutkde.png")))); // Debian, Kubuntu
			} catch (IOException e) {
				setIcon(new ImageIcon(ImageIO.read(new File("/usr/share/apps/kdeui/pics/aboutkde.png")))); // Chakra
			}
		}
	}
	
	private class ContentPanel extends JPanel {
		/** Serial version UID */
		private static final long serialVersionUID = -2334173606481835530L;

		public ContentPanel() {
			setLayout(new GridLayout());
			
			JTabbedPane tp = new JTabbedPane();
			
			JLabel label1 = new JLabel("<html><b>KDE</b> is a world-wide network of software engineers, "
					+ "artists, writers, translators and facilitators who are committed to "
					+ "<a href=something>Free Software</a> development. This community has "
					+ "created hundreds of Free Software applcations as part of the KDE Development "
					+ "Platform and KDE Software Distribution.<br>"
					+ "<br>"
					+ "KDE is a cooperative enterprise in which no single entity controls the "
					+ "efforts or products of KDE to the exclusion of others. Everyone is welcome "
					+ "to join and contribute to KDE, including you.<br>"
					+ "<br>"
					+ "Visit <a href=http://www.kde.org/>http://www.kde.org/</a> for more information "
					+ "about the KDE community and the software we produce.");
			label1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			label1.setVerticalAlignment(JLabel.TOP);
			tp.addTab("About", label1);
			
			tp.addTab("Report Bugs or Wishes", new JLabel());
			tp.addTab("Join KDE", new JLabel());
			tp.addTab("Support KDE", new JLabel());
			
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
