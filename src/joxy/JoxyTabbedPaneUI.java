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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

import joxy.utils.JoxyGraphics;
import joxy.utils.Output;

/**
 * Joxy's UI delegate for the JTabbedPane.
 * 
 * <p>This is not complete yet; only tabs on top work reasonably well.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyTabbedPaneUI extends BasicTabbedPaneUI {

	protected MouseWheelListener scrollListener;
	public static final int ARC = 6;
	
	/**
	 * Indicates whether the selected tab should overflow when it is
	 * scrolled past the begin or end of the tab run. Note that KDE is not consistent
	 * with respect to this. For example <i>System Settings > Common Appearance
	 * and Behaviour > Locale > Country/Region & Language</i> does overflow,
	 * but <i>System Settings > Desktop Effects</i> does not. Perhaps this has
	 * something to do with the number of tabs.
	 * 
	 * <p>If this variable is false, we determine whether to overflow based on the
	 * number of tabs.</p>
	 */
	public static final boolean FORCE_SCROLL_OVERFLOW = false;
	
	public static ComponentUI createUI(JComponent c) {
		c.setOpaque(false);
		JoxyTabbedPaneUI ui = new JoxyTabbedPaneUI();
		return ui;
	}

	@Override
	public void installUI(final JComponent c) {
		super.installUI(c);
		
		tabInsets = new Insets(7, 8, 0, 6);
		tabAreaInsets = new Insets(0, 0, 0, 0);
		c.setFont(UIManager.getFont("Button.font"));

		scrollListener = new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					
					if (e.getY() < rects[0].y + rects[0].height) { // [ws] TODO replace with Y that makes more sense...
						
						boolean shouldOverflow = FORCE_SCROLL_OVERFLOW || ((JTabbedPane) c).getTabCount() > 3;
						
						JTabbedPane tp = (JTabbedPane) c;
						tp.setSelectedIndex(findFirstEnabledTabIndex(c, tp.getSelectedIndex() + e.getWheelRotation(),
								e.getWheelRotation(), shouldOverflow));
					}
				}
			}
		};
		
		c.addMouseWheelListener(scrollListener);
		
		((JTabbedPane) c).setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	/**
	 * Returns the index of the first tab that is enabled from the tab at given index.
	 * The first tab that is checked is the one at the given index, so if that tab
	 * is enabled, the given index is returned.
	 * 
	 * When a tab is disabled, the index is incremented by the given step. This might
	 * be negative of course. In doing so, the index will "overflow".
	 * 
	 * If no tab is enabled, -1 is returned.
	 * 
	 * If {@code mayOverflow == false}, the search will not "overflow".
	 * 
	 * @param c The JTabbedPane, used for checking if tabs are enabled.
	 * @param startIndex Index to start search.
	 * @param step Used in incrementing index.
	 * @param mayOverflow Indicates if the search may overflow.
	 * @return The index of an enabled tab, or -1 if all tabs are disabled.
	 */
	private int findFirstEnabledTabIndex(JComponent c, int startIndex, int step, boolean mayOverflow) {
		int curIndex = startIndex;
		int tabsChecked = 0;
		JTabbedPane tp = (JTabbedPane) c;
		
		// do not allow too big step
		step = step % tp.getTabCount();
		
		// check startindex
		if (curIndex >= tp.getTabCount() && mayOverflow) {
			curIndex = (curIndex - tp.getTabCount());
		} else if (curIndex >= tp.getTabCount()) {
			return curIndex - step;
		} else if (curIndex < 0 && mayOverflow) {
			curIndex = tp.getTabCount() + curIndex;
		} else if (curIndex < 0) {
			return curIndex - step;
		}
		
		// start searching
		while (!tp.isEnabledAt(curIndex) && tabsChecked < tp.getTabCount()) {
			curIndex += step;
			tabsChecked++;
			if (curIndex >= tp.getTabCount() && mayOverflow) {
				curIndex = (curIndex - tp.getTabCount());
			} else if (curIndex >= tp.getTabCount()) {
				curIndex -= step;
				break;
			} else if (curIndex < 0 && mayOverflow) {
				curIndex = tp.getTabCount() + curIndex;
			} else if (curIndex < 0) {
				curIndex -= step;
				break;
			}
		}
		return (tp.isEnabledAt(curIndex) ? curIndex : -1);
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
			g2.setClip(rects[0].width - 2, y + 1, w, h);
			g2.draw(new RoundRectangle2D.Float(x + 1, y, w - 2, h - 2, ARC, ARC));
			g2.setClip(0, y + 4, rects[0].width - 1, h);
			g2.draw(new RoundRectangle2D.Float(x + 1, y, w - 2, h - 2, ARC, ARC));
			g2.setClip(previousClip);
			g2.drawLine(1, y, 1, y + 3);
			
			// white border
			g2.setColor(new Color(255, 255, 255, 128));
			g2.setClip(rects[0].width - 2, y, w, h);
			g2.draw(new RoundRectangle2D.Float(x + 2, y, w - 4, h - 3, ARC, ARC));
			g2.setClip(0, y + 4, rects[0].width - 1, h);
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
			g2.setClip(0, y, rects[selectedIndex].x /* - scroll adjustment */ + 2, 1);
			g2.draw(new RoundRectangle2D.Float(x + 2, y, w - 4, h - 3, ARC, ARC));
			g2.setClip(rects[selectedIndex].x /* - scroll adjustment */ + rects[selectedIndex].width - 2, y, w, 1);
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
			g2.setColor((tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex))
							? tabPane.getForegroundAt(tabIndex)
							: tabPane.getBackgroundAt(tabIndex).brighter());

			JoxyGraphics.drawString(g2, title, textRect.x, textRect.y + metrics.getAscent());
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
				g2.fill(new RoundRectangle2D.Float(x + 1, y + 1, w - 1, h - 4, ARC, ARC));
				break;
			case RIGHT:
				GradientPaint tab2 = new GradientPaint(x, 0, backgroundTransparent,
						x + w - 2, 0, background);
				g2.setPaint(tab2);
				g2.fill(new RoundRectangle2D.Float(x, y + 1, w - 2, h - 4, ARC, ARC));
				break;
			case BOTTOM:
				GradientPaint tab3 = new GradientPaint(0, y, backgroundTransparent,
						0, y + h - 1, background);
				g2.setPaint(tab3);
				g2.fill(new RoundRectangle2D.Float(x + 1, y, w - 4, h - 1, ARC, ARC));
				break;
			case TOP:
			default:
				GradientPaint tab4 = new GradientPaint(0, y + 1, background, 0, y
						+ h, backgroundTransparent);
				g2.setPaint(tab4);
				g2.fill(new RoundRectangle2D.Float(x + 2, y + 1, w - 4, h - 1, ARC, ARC));
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
				g2.fill(new RoundRectangle2D.Float(x + 1, y + 1, w - 1, h - 4, ARC, ARC));
				break;
			case RIGHT:
				GradientPaint tab2 = new GradientPaint(x, 0, backgroundTransparent,
						x + w - 2, 0, background);
				g2.setPaint(tab2);
				g2.fill(new RoundRectangle2D.Float(x, y + 1, w - 2, h - 4, ARC, ARC));
				break;
			case BOTTOM:
				GradientPaint tab3 = new GradientPaint(0, y, backgroundTransparent,
						0, y + h - 1, background);
				g2.setPaint(tab3);
				g2.fill(new RoundRectangle2D.Float(x + 1, y, w - 4, h - 1, ARC, ARC));
				break;
			case TOP:
			default:
				GradientPaint tab4 = new GradientPaint(0, y + (h/2) + 1, background, 0, y
						+ h, backgroundTransparent, true);
				g2.setPaint(tab4);
				g2.fill(new RoundRectangle2D.Float(x + 2, y + 2, w - 4, h - 1, ARC, ARC));
			}
		}
	}
	
	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
			int x, int y, int w, int h, boolean isSelected) {
		
		// TODO at the moment, only tabs on top are supported
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		if (isSelected) {
			switch (tabPlacement) {
			case TOP:
				Shape previousClip = g2.getClip();
				g2.setClip(g2.getClipBounds().x, g2.getClipBounds().y, g2.getClipBounds().width, 2);
				g2.setColor(new Color(0, 0, 0, 13));
				g2.draw(new RoundRectangle2D.Float(x + 1, y, w - 3, h + 3, ARC, ARC));
				g2.setClip(previousClip);
				g2.setClip(g2.getClipBounds().x, g2.getClipBounds().y + 2, g2.getClipBounds().width, g2.getClipBounds().height - 2);
				g2.setColor(new Color(0, 0, 0, 50));
				g2.draw(new RoundRectangle2D.Float(x + 1, y, w - 3, h + 3, ARC, ARC));
				g2.setClip(previousClip);
				g2.setColor(new Color(255, 255, 255, 128));
				g2.draw(new RoundRectangle2D.Float(x + 2, y + 1, w - 5, h + 2, ARC, ARC));
			}
			
			return;
		}
		
		// not a selected tab
		
		switch (tabPlacement) {
		case TOP:
			Shape previousClip = g2.getClip();
			g2.setClip(g2.getClipBounds().x, g2.getClipBounds().y + 1, g2.getClipBounds().width, 2);
			g2.setColor(new Color(0, 0, 0, 13));
			g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, w - 3, h + 2, ARC, ARC));
			g2.setClip(previousClip);
			g2.setClip(g2.getClipBounds().x, g2.getClipBounds().y + 3, g2.getClipBounds().width, g2.getClipBounds().height - 3);
			g2.setColor(new Color(0, 0, 0, 50));
			g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, w - 3, h + 2, ARC, ARC));
			g2.setClip(previousClip);
			g2.setColor(new Color(255, 255, 255, 128));
			g2.draw(new RoundRectangle2D.Float(x + 2, y + 2, w - 5, h + 1, ARC, ARC));
		}
	}
	
	@Override
	protected void paintFocusIndicator(Graphics g, int tabPlacement,
			Rectangle[] rects, int tabIndex, Rectangle iconRect,
			Rectangle textRect, boolean isSelected) {
		// No focus indicator!
	}
	
	// Copy from super class.
	// We first wanted to override this to immediately return a JoxyArrowButton,
	// but that creates two ghost tabs, presumably because it is no UIResource?
	// And for some reason, ScrollableTabButton is private. Well, then we do it
	// this way. And it works!
    @Override
	protected JButton createScrollButton(int direction) {
        if (direction != SOUTH && direction != NORTH && direction != EAST &&
                                  direction != WEST) {
            throw new IllegalArgumentException("Direction must be one of: " +
                                               "SOUTH, NORTH, EAST or WEST");
        }
        return new ScrollableTabButton(direction);
    }
    
	private class ScrollableTabButton extends JoxyArrowButton implements UIResource {
		public ScrollableTabButton(int direction) {
			super(direction);
		}
	}
}
