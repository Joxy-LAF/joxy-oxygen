package joxy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;

public class JoxyTextAreaUI extends BasicTextAreaUI {

	/**
	 * The width and height of the arcs that form up
	 * the corners of the rounded rectangles. 
	 */
	public static final int ARC = 8;
	
	/**
	 * The text area we are painting for.
	 */
	JTextArea textArea;
	
	public static ComponentUI createUI(JComponent c) {
		//c.setOpaque(false);
		return new JoxyTextAreaUI(c);
	}
	
	public JoxyTextAreaUI(JComponent c) {
		textArea = (JTextArea) c;
	}
	
	@Override
	protected void installDefaults() {
		// TODO Auto-generated method stub
		super.installDefaults();
		
		// [ws] TODO deze dingen kunnen eigenlijk ook gewoon in de defaults...
		textArea.setBorder(BorderFactory.createEmptyBorder(3, 1, 3, 1));
		textArea.setFont(UIManager.getFont("Button.font"));
		//textField.setSelectedTextColor(UIManager.getColor("TextField.selectionBackground"));
	}
	
    @Override
    protected void paintBackground(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Double(0, 0, textArea.getWidth() - 1, textArea.getHeight() - 1, ARC, ARC));

		g2.setStroke(new BasicStroke(1.2f));
		g2.setColor(new Color(140, 140, 140));
		g2.draw(new RoundRectangle2D.Double(0, 0, textArea.getWidth(), textArea.getHeight(), ARC, ARC));

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }
}
