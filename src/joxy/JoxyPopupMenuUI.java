package joxy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.DefaultMenuLayout;

public class JoxyPopupMenuUI extends BasicPopupMenuUI {
	
	private static final int ARC = 10;
	
    public static ComponentUI createUI(JComponent c) {
        return new JoxyPopupMenuUI();
    }
    
    @Override
	public void installDefaults() {
        if (popupMenu.getLayout() == null ||
            popupMenu.getLayout() instanceof UIResource)
            popupMenu.setLayout(new DefaultMenuLayout(popupMenu, BoxLayout.Y_AXIS));

        //LookAndFeel.installProperty(popupMenu, "opaque", Boolean.TRUE);
        popupMenu.setBorder(BorderFactory.createEmptyBorder(7, 8, 10, 8));
        //LookAndFeel.installColorsAndFont(popupMenu, "PopupMenu.background", "PopupMenu.foreground", "PopupMenu.font");
    }
    
    @Override
    public void paint(Graphics g, JComponent c) {
    	Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// Shadow
		g2.setColor(new Color(0, 0, 0, 5));
		for (int i = 0; i < 3; i++) {
	    	g2.fill(new RoundRectangle2D.Float(i, 0.9f * i, c.getWidth() - 2*i, c.getHeight() - 2*i, ARC - i + 6, ARC - i + 6));
		}
		
		g2.setColor(new Color(0, 0, 0, 15));
		for (int i = 3; i < 6; i++) {
	    	g2.fill(new RoundRectangle2D.Float(i, 0.9f * i, c.getWidth() - 2*i, c.getHeight() - 2*i, ARC - i + 6, ARC - i + 6));
		}
		
		// Inner side
		Color color = UIManager.getColor("Window.background");
		// [ws] TODO search real colors
	    Color backgroundTopColor = JoxyRootPaneUI.getBackgroundTopColor(color);
        Color backgroundBottomColor = JoxyRootPaneUI.getBackgroundBottomColor(color);
        
    	g2.setPaint(new GradientPaint(0, 0, backgroundTopColor,
    			                      0, 100, backgroundBottomColor));
    	g2.fill(new RoundRectangle2D.Float(6, 4, c.getWidth() - 12, c.getHeight() - 12, ARC, ARC));
    	
    	// Border
    	g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(0.25f));
		g2.draw(new RoundRectangle2D.Float(6, 4, c.getWidth() - 13, c.getHeight() - 13, ARC, ARC));

    }
}
