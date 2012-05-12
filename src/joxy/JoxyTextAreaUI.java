package joxy;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;

import joxy.painter.InputFieldPainter;

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
		textArea.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		textArea.setFont(UIManager.getFont("Button.font"));
		//textField.setSelectedTextColor(UIManager.getColor("TextField.selectionBackground"));
	}
	
    @Override
    protected void paintBackground(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		InputFieldPainter.paint(g2, 1, 1, textArea.getWidth() - 2, textArea.getHeight() - 2);

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }
}
