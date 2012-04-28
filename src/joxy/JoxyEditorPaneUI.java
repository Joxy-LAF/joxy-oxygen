package joxy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;

import joxy.painter.InputFieldPainter;

public class JoxyEditorPaneUI extends BasicEditorPaneUI {

	/**
	 * The width and height of the arcs that form up
	 * the corners of the rounded rectangles. 
	 */
	public static final int ARC = 8;
	
	/**
	 * The editor pane we are painting for.
	 */
	JEditorPane editor;
	
	public static ComponentUI createUI(JComponent c) {
		JoxyEditorPaneUI ui = new JoxyEditorPaneUI(c);
		return ui;
	}

	public JoxyEditorPaneUI(JComponent c) {
		super();
		editor = (JEditorPane) c;
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		
		// force the default font to be used if the HTML does not override it... nice :-)
		editor.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
	}
	
    @Override
    protected void paintBackground(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		InputFieldPainter.paint(g2, 1, 1, editor.getWidth() - 2, editor.getHeight() - 2);

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }
}
