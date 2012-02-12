package joxy;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

/**
 * Class overriding the default TabbedPaneUI (BasicTabbedPaneUI) to provide a good
 * integration with the Oxygen KDE style. Part of the Joxy Look and Feel.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyTabbedPaneUI extends BasicTabbedPaneUI {

	protected MouseWheelListener scrollListener;
	public static final int ARC = 8;
	
	/**
	 * Indicates whether the selected tab should over-/underflow when it is
	 * scrolled past the begin orend of the tab run. Note that KDE is not consistent
	 * with respect to this. For example <i>System Settings > Common Appearance
	 * and Behaviour > Locale > Country/Region & Language</i> does overflow,
	 * but <i>System Settings > Desktop Effects</i> does not. Perhaps this has
	 * something to do with the number of tabs.
	 */
	public static final boolean SCROLL_OVERFLOW = false;
	
	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		JoxyTabbedPaneUI ui = new JoxyTabbedPaneUI();
		return ui;
	}

	@Override
	public void installUI(final JComponent c) {
		super.installUI(c);

		tabInsets = new Insets(5, 5, 0, 5);
		tabAreaInsets = new Insets(0, 0, 0, 0);
		c.setFont(UIManager.getFont("Button.font"));

		scrollListener = new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					
					if (e.getY() < rects[0].y + rects[0].height) { // [ws] TODO replace with Y that makes more sense...
					
						if (e.getWheelRotation() == 1) {
							int currentIndex = ((JTabbedPane) c).getSelectedIndex();
							if (currentIndex == ((JTabbedPane) c).getTabCount() - 1) {
								if (SCROLL_OVERFLOW) {
									((JTabbedPane) c).setSelectedIndex(0);
								}
							} else {
								((JTabbedPane) c).setSelectedIndex(currentIndex + 1);
							}
						}
	
						if (e.getWheelRotation() == -1) {
							int currentIndex = ((JTabbedPane) c).getSelectedIndex();
							if (currentIndex == 0) {
								if (SCROLL_OVERFLOW) {
									((JTabbedPane) c).setSelectedIndex(((JTabbedPane) c).getTabCount() - 1);
								}
							} else {
								((JTabbedPane) c).setSelectedIndex(currentIndex - 1);
							}
						}
					}
				}
			}
		};
		
		c.addMouseWheelListener(scrollListener);
		
		((JTabbedPane) c).setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		
		c.removeMouseWheelListener(scrollListener);
	}
	
	@Override
	protected void paintContentBorder(Graphics g, int tabPlacement,
			int selectedIndex) {
		int width = tabPane.getWidth();
		int height = tabPane.getHeight();
		Insets insets = tabPane.getInsets();
		Insets tabAreaInsets = getTabAreaInsets(tabPlacement);

		int x = insets.left;
		int y = insets.top;
		int w = width - insets.right - insets.left;
		int h = height - insets.top - insets.bottom;

		boolean tabsOverlapBorder = UIManager
				.getBoolean("TabbedPane.tabsOverlapBorder");

		switch (tabPlacement) {
		case LEFT:
			x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
			if (tabsOverlapBorder) {
				x -= tabAreaInsets.right;
			}
			w -= (x - insets.left);
			break;
		case RIGHT:
			w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
			if (tabsOverlapBorder) {
				w += tabAreaInsets.left;
			}
			break;
		case BOTTOM:
			h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
			if (tabsOverlapBorder) {
				h += tabAreaInsets.top;
			}
			break;
		case TOP:
		default:
			y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
			if (tabsOverlapBorder) {
				y -= tabAreaInsets.bottom;
			}
			h -= (y - insets.top);
		}

		/*paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
		paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
		paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
		paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);*/

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		// TODO ook andere orientaties ondersteunen
		paintContentBorderTabsTop(g2, selectedIndex, x, y, w, h);
	}

	/**
	 * Paints the content border if the tabs are on top of the tabbed pane.
	 * @param g2 The Graphics2D object to paint on.
	 * @param selectedIndex Index of the selected tab.
	 */
	protected void paintContentBorderTabsTop(Graphics2D g2, int selectedIndex, int x, int y, int w, int h) {

		
		// If first tab is selected, we need a special case to draw the left line.
		if (selectedIndex == 0) {
			// shadow
			g2.setColor(new Color(0, 0, 0, 50));
			Shape previousClip = g2.getClip();
			g2.setClip(rects[selectedIndex].width - 1, y + 1, w, h);
			g2.draw(new RoundRectangle2D.Float(x + 1, y, w - 2, h - 2, ARC, ARC));
			g2.setClip(0, y + 4, rects[selectedIndex].width, h);
			g2.draw(new RoundRectangle2D.Float(x + 1, y, w - 2, h - 2, ARC, ARC));
			g2.setClip(previousClip);
			g2.drawLine(1, y, 1, y + 3);
			
			// white border
			g2.setColor(new Color(255, 255, 255, 128));
			g2.setClip(rects[selectedIndex].width - 1, y, w, h);
			g2.draw(new RoundRectangle2D.Float(x + 2, y, w - 4, h - 3, ARC, ARC));
			g2.setClip(0, y + 4, rects[selectedIndex].width, h);
			g2.draw(new RoundRectangle2D.Float(x + 2, y, w - 4, h - 3, ARC, ARC));
			g2.setClip(previousClip);
			g2.drawLine(2, y, 2, y + 3);
		} else {
			// shadow
			g2.setColor(new Color(0, 0, 0, 50));
			Shape previousClip = g2.getClip();
			g2.setClip(0, y + 1, w, h - 1);
			g2.draw(new RoundRectangle2D.Float(x + 1, y, w - 2, h - 2, ARC, ARC));
			g2.setClip(previousClip);
			g2.drawLine(1, y, 1, y + 1);
			
			// white border
			g2.setColor(new Color(255, 255, 255, 128));
			g2.setClip(0, y + 1, w, h - 1);
			g2.draw(new RoundRectangle2D.Float(x + 2, y, w - 4, h - 3, ARC, ARC));
			g2.setClip(0, y, rects[selectedIndex].x + 2, 1);
			g2.draw(new RoundRectangle2D.Float(x + 2, y, w - 4, h - 3, ARC, ARC));
			g2.setClip(rects[selectedIndex].x + rects[selectedIndex].width - 1, y, w, 1);
			g2.draw(new RoundRectangle2D.Float(x + 2, y, w - 4, h - 3, ARC, ARC));
			g2.setClip(previousClip);
			g2.drawLine(2, y, 2, y + 2);
		}
	}

	@Override
	protected void paintText(Graphics g, int tabPlacement, Font font,
			FontMetrics metrics, int tabIndex, String title,
			Rectangle textRect, boolean isSelected) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setFont(font);

		View v = getTextViewForTab(tabIndex);
		if (v != null) {
			// html
			v.paint(g, textRect);
		} else {
			// plain text
			g2.setColor(tabPane.getForegroundAt(tabIndex));

			g2.drawString(title, textRect.x, textRect.y + metrics.getAscent());
		}
	}

	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement,
			int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		if (isSelected) {
			Color background = new Color(255, 255, 255, 100);
			Color backgroundTransparent = new Color(background.getRed(),
					background.getGreen(), background.getBlue(), 0);
	
			switch (tabPlacement) {
			case LEFT:
				GradientPaint tab1 = new GradientPaint(x + 1, 0, background, x + w,
						0, backgroundTransparent);
				g2.setPaint(tab1);
				g2.fill(new RoundRectangle2D.Float(x + 1, y + 1, w - 1, h - 3, ARC, ARC));
				break;
			case RIGHT:
				GradientPaint tab2 = new GradientPaint(x, 0, backgroundTransparent,
						x + w - 2, 0, background);
				g2.setPaint(tab2);
				g2.fill(new RoundRectangle2D.Float(x, y + 1, w - 2, h - 3, ARC, ARC));
				break;
			case BOTTOM:
				GradientPaint tab3 = new GradientPaint(0, y, backgroundTransparent,
						0, y + h - 1, background);
				g2.setPaint(tab3);
				g2.fill(new RoundRectangle2D.Float(x + 1, y, w - 3, h - 1, ARC, ARC));
				break;
			case TOP:
			default:
				GradientPaint tab4 = new GradientPaint(0, y + 1, background, 0, y
						+ h, backgroundTransparent);
				g2.setPaint(tab4);
				g2.fill(new RoundRectangle2D.Float(x + 2, y + 1, w - 3, h - 1, ARC, ARC));
			}
		} else {
			Color background = new Color(0, 0, 0, 30);
			Color backgroundTransparent = new Color(background.getRed(),
					background.getGreen(), background.getBlue(), 0);
	
			switch (tabPlacement) {
			case LEFT:
				GradientPaint tab1 = new GradientPaint(x + 1, 0, background, x + w,
						0, backgroundTransparent);
				g2.setPaint(tab1);
				g2.fill(new RoundRectangle2D.Float(x + 1, y + 1, w - 1, h - 3, ARC, ARC));
				break;
			case RIGHT:
				GradientPaint tab2 = new GradientPaint(x, 0, backgroundTransparent,
						x + w - 2, 0, background);
				g2.setPaint(tab2);
				g2.fill(new RoundRectangle2D.Float(x, y + 1, w - 2, h - 3, ARC, ARC));
				break;
			case BOTTOM:
				GradientPaint tab3 = new GradientPaint(0, y, backgroundTransparent,
						0, y + h - 1, background);
				g2.setPaint(tab3);
				g2.fill(new RoundRectangle2D.Float(x + 1, y, w - 3, h - 1, ARC, ARC));
				break;
			case TOP:
			default:
				GradientPaint tab4 = new GradientPaint(0, y + (h/2) + 1, background, 0, y
						+ h, backgroundTransparent, true);
				g2.setPaint(tab4);
				g2.fill(new RoundRectangle2D.Float(x + 2, y + 2, w - 3, h - 1, ARC, ARC));
			}
		}
	}
	
	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
			int x, int y, int w, int h, boolean isSelected) {
		//super.paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
		
		// TODO implementeren :-D
		
		// Tijd om te prutsen! Jeej!
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		if (isSelected) {
			// momenteel denk ik alleen nog even TOP doen, kan de rest later :-)
			switch (tabPlacement) {
			case TOP:
				Shape previousClip = g2.getClip();
				g2.setClip(g2.getClipBounds().x, g2.getClipBounds().y + 1, g2.getClipBounds().width, g2.getClipBounds().height - 1);
				g2.setColor(new Color(0, 0, 0, 50));
				g2.draw(new RoundRectangle2D.Float(x + 1, y, w - 2, h + 3, ARC, ARC));
				g2.setClip(previousClip);
				g2.setColor(new Color(255, 255, 255, 128));
				g2.draw(new RoundRectangle2D.Float(x + 2, y, w - 4, h + 3, ARC, ARC));
			}
		} else {
			switch (tabPlacement) {
			case TOP:
				Shape previousClip = g2.getClip();
				g2.setClip(g2.getClipBounds().x, g2.getClipBounds().y + 2, g2.getClipBounds().width, g2.getClipBounds().height - 2);
				g2.setColor(new Color(0, 0, 0, 50));
				g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, w - 2, h + 2, ARC, ARC));
				g2.setClip(previousClip);
				g2.setColor(new Color(255, 255, 255, 128));
				g2.draw(new RoundRectangle2D.Float(x + 2, y + 1, w - 4, h + 2, ARC, ARC));
			}
		}
	}
	
	@Override
	protected void paintFocusIndicator(Graphics g, int tabPlacement,
			Rectangle[] rects, int tabIndex, Rectangle iconRect,
			Rectangle textRect, boolean isSelected) {
		// No focus indicator!
	}
}
