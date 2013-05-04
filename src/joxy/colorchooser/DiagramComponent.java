package joxy.colorchooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;

import joxy.JoxyArrowButton;

/**
 * A JPanel that displays the colour space.
 * 
 * <p>The panel consists of two parts. On the left part, a square is shown with two
 * dimensions of the colour space; on the right part, a line is shown with the remaining
 * dimension. For example, if the <i>Hue</i> radio button is selected, the line shows the
 * varying hues of the saturation and value in the square.</p>
 * 
 * <p>This class has the same name as {@link javax.swing.colorchooser.DiagramComponent},
 * but since that class is not visible, works a bit different and most importantly, is not
 * documented at all, we decided to reimplement this functionality.</p>
 * 
 * <p>Note: at the moment only <i>Red</i> mode is supported.</p>
 */
public class DiagramComponent extends JPanel implements MouseListener, MouseMotionListener {
	
	/**
	 * A cache for the left part (the colour square).
	 */
	BufferedImage leftCache;
	
	/**
	 * A cache for the right part (the colour line).
	 */
	BufferedImage rightCache;
	
	/**
	 * The x coordinate of the selected colour in the colour square.
	 */
	int selectedSquareX;
	
	/**
	 * The y coordinate of the selected colour in the colour square.
	 */
	int selectedSquareY;
	
	/**
	 * The y coordinate of the selected colour on the colour line.
	 */
	int selectedLineY;
	
	/**
	 * The component used for painting. By default, this is a {@link JoxyArrowButton}.
	 */
	JComponent arrow = new JoxyArrowButton(JoxyArrowButton.EAST);
	
	/**
	 * A listener that will be notified of any changes to the selected colour.
	 */
	ColorChangeListener listener = null;
	
	/**
	 * Creates the panel and sets its defaults.
	 * 
	 * @param color The colour to initialize the panel with.
	 */
	public DiagramComponent(Color color) {
		// the width is 30 pixels larger, since we need additional space for the right
		// component
		setPreferredSize(new Dimension(286, 256));
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setColor(color);
	}
	
	/**
	 * Sets the colour displayed by this panel.
	 * 
	 * @param color The new colour to show.
	 */
	public void setColor(Color color) {
		selectedSquareX = color.getGreen();
		selectedSquareY = 255 - color.getBlue();
		selectedLineY = 255 - color.getRed();
	}
	
	/**
	 * Gets the colour displayed by this panel.
	 * 
	 * @return The currently selected colour.
	 */
	public Color getColor() {
		return new Color(255 - selectedLineY, selectedSquareX, 255 - selectedSquareY);
	}
	
	/**
	 * Sets the {@link ColorChangeListener} on this {@link DiagramComponent}.
	 * 
	 * <p>Note: there can only be one listener at a time.</p>
	 * 
	 * @param listener The new listener.
	 */
	public void setListener(ColorChangeListener listener) {
		this.listener = listener;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (leftCache == null) {
			leftCache = paintColorSquare();
		}
		g.drawImage(leftCache, 0, 0, null);

		if (rightCache == null) {
			rightCache = paintColorLine();
		}
		g.drawImage(rightCache, 266, 0, null);
		
		g.translate(260, selectedLineY - 3);
		arrow.setSize(new Dimension(7, 7));
		arrow.paint(g);
		g.translate(-260, -selectedLineY + 3);
		
		g.setColor(Color.WHITE);
		g.drawOval(selectedSquareX - 3, selectedSquareY - 3, 7, 7);
	}

	/**
	 * This method paints the colour square (left part of the panel) into an image.
	 * 
	 * @return The painted image.
	 */
	protected BufferedImage paintColorSquare() {
		BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_3BYTE_BGR);
		
		for (int x = 0; x < 256; x++) {
			for (int y = 0; y < 256; y++) {
				image.setRGB(x, y, ((255 - selectedLineY) << 16) + (x << 8) + ((255 - y) << 0));
			}
		}
		
		return image;
	}

	/**
	 * This method paints the colour square (left part of the panel) into an image.
	 * 
	 * @return The painted image.
	 */
	protected BufferedImage paintColorLine() {
		BufferedImage image = new BufferedImage(20, 256, BufferedImage.TYPE_3BYTE_BGR);
		
		for (int y = 0; y < 256; y++) {
			for (int x = 0; x < 20; x++) {
				image.setRGB(x, y, ((255 - y) << 16) + (selectedSquareX << 8) + ((255 - selectedSquareY) << 0));
			}
		}
		
		return image;
	}
	
	protected void handleMouseEvent(int x, int y) {
		
		// bounds checking, this is necessary for drag events
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if (y > 255) {
			y = 255;
		}
		
		if (x < 256) {
			// inside the colour square
			selectedSquareX = x;
			selectedSquareY = y;
			rightCache = null;
			repaint();
			
		} else if (x > 266) {
			// inside the colour line
			selectedLineY = y;
			leftCache = null;
			repaint();
		}
		
		if (listener != null) {
			listener.colorChanged(getColor());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		handleMouseEvent(e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		handleMouseEvent(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
