/*
 * Copyright 2013  Thom Castermans  thom.castermans@gmail.com
 * Copyright 2013  Willem Sonke     willemsonke@planet.nl
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License or (at your option) version 3 or any later version
 * accepted by the membership of KDE e.V. (or its successor approved
 * by the membership of KDE e.V.), which shall act as a proxy 
 * defined in Section 14 of version 3 of the license.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

import joxy.color.HSVColor;
import joxy.ui.JoxyArrowButton;

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
 * <p>The diagram panel can be in several modes; see {@link DiagramComponentMode}.
 * </p>
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
	int xSquare;
	
	/**
	 * The y coordinate of the selected colour in the colour square.
	 */
	int ySquare;
	
	/**
	 * The y coordinate of the selected colour on the colour line.
	 */
	int yLine;
	
	/**
	 * The component used for painting. By default, this is a {@link JoxyArrowButton}.
	 */
	protected JComponent arrow = new JoxyArrowButton(JoxyArrowButton.EAST);
	
	/**
	 * The modes a diagram component can be in.
	 * 
	 * <p>The diagram panel can be in <s>six</s> <b>three at the moment</b> modes:
	 * <ul>
	 * <s>
	 * <li>HSV colour model:</li>
	 * <ul>
	 * <li>hue;</li>
	 * <li>saturation;</li>
	 * <li>value;</li>
	 * </ul>
	 * </ul>
	 * </s>
	 * <ul>
	 * <li>RGB colour model:</li>
	 * <ul>
	 * <li>red;</li>
	 * <li>green;</li>
	 * <li>blue.</li>
	 * </ul>
	 * </ul>
	 * </p>
	 */
	public enum DiagramComponentMode {
		
		/**
		 * Mode where the hue can be set in the colour line, and the saturation
		 * and value in the colour square.
		 */
		HUE_MODE(new ColorModeCalculator() {

			@Override
			public Color diagramToColor(int xSquare, int ySquare, int yLine) {
				HSVColor hsv = new HSVColor(yLine / 256f, xSquare / 256f, 1 - ySquare / 256f);
				return hsv.toColor();
			}

			@Override
			public int[] colorToDiagram(Color color) {
				HSVColor hsv = new HSVColor(color);
				return new int[] {
					(int) (256 * hsv.getS()),
					(int) (256 * (1 - hsv.getV())),
					(int) (256 * hsv.getH())
				};
			}
		}),

		/**
		 * Mode where the saturation can be set in the colour line, and the hue
		 * and value in the colour square.
		 */
		SATURATION_MODE(new ColorModeCalculator() {

			@Override
			public Color diagramToColor(int xSquare, int ySquare, int yLine) {
				HSVColor hsv = new HSVColor(xSquare / 256f, 1 - yLine / 256f, 1 - ySquare / 256f);
				return hsv.toColor();
			}

			@Override
			public int[] colorToDiagram(Color color) {
				HSVColor hsv = new HSVColor(color);
				return new int[] {
					(int) (256 * hsv.getH()),
					(int) (256 * (1 - hsv.getV())),
					(int) (256 * (1 - hsv.getS()))
				};
			}
		}),

		/**
		 * Mode where the value can be set in the colour line, and the hue
		 * and saturation in the colour square.
		 */
		VALUE_MODE(new ColorModeCalculator() {

			@Override
			public Color diagramToColor(int xSquare, int ySquare, int yLine) {
				HSVColor hsv = new HSVColor(xSquare / 256f, 1 - ySquare / 256f, 1 - yLine / 256f);
				return hsv.toColor();
			}

			@Override
			public int[] colorToDiagram(Color color) {
				HSVColor hsv = new HSVColor(color);
				return new int[] {
					(int) (256 * hsv.getH()),
					(int) (256 * (1 - hsv.getS())),
					(int) (256 * (1 - hsv.getV()))
				};
			}
		}),

		/**
		 * Mode where the red component can be set in the colour line, and the green
		 * and blue components in the colour square.
		 */
		RED_MODE(new ColorModeCalculator() {

			@Override
			public Color diagramToColor(int xSquare, int ySquare, int yLine) {
				return new Color(255 - yLine, xSquare, 255 - ySquare);
			}

			@Override
			public int[] colorToDiagram(Color color) {
				return new int[] {
					color.getGreen(),
					255 - color.getBlue(),
					255 - color.getRed()
				};
			}
		}),

		/**
		 * Mode where the green component can be set in the colour line, and the red
		 * and blue components in the colour square.
		 */
		GREEN_MODE(new ColorModeCalculator() {

			@Override
			public Color diagramToColor(int xSquare, int ySquare, int yLine) {
				return new Color(xSquare, 255 - yLine, 255 - ySquare);
			}

			@Override
			public int[] colorToDiagram(Color color) {
				return new int[] {
					color.getRed(),
					255 - color.getBlue(),
					255 - color.getGreen()
				};
			}
		}),

		/**
		 * Mode where the blue component can be set in the colour line, and the red
		 * and green components in the colour square.
		 */
		BLUE_MODE(new ColorModeCalculator() {

			@Override
			public Color diagramToColor(int xSquare, int ySquare, int yLine) {
				return new Color(xSquare, 255 - ySquare, 255 - yLine);
			}

			@Override
			public int[] colorToDiagram(Color color) {
				return new int[] {
					color.getRed(),
					255 - color.getGreen(),
					255 - color.getBlue()
				};
			}
		});
		
		/**
		 * The {@link ColorModeCalculator} for this mode.
		 */
		ColorModeCalculator calculator;
		
		/**
		 * Creates a new {@link DiagramComponentMode}.
		 * @param calculator The {@link ColorModeCalculator} to use.
		 */
		DiagramComponentMode(ColorModeCalculator calculator) {
			this.calculator = calculator;
		}
	}
	
	/**
	 * The mode this DiagramComponent is in.
	 */
	protected DiagramComponentMode mode;
	
	/**
	 * A listener that will be notified of any changes to the selected colour.
	 */
	protected ColorChangeListener listener = null;
	
	/**
	 * Creates the panel and sets its defaults.
	 * 
	 * @param color The colour to initialize the panel with.
	 * @param mode The initial mode.
	 */
	public DiagramComponent(Color color, DiagramComponentMode mode) {
		// the width is 30 pixels larger, since we need additional space for the right
		// component
		setPreferredSize(new Dimension(286, 256));
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.mode = mode;
		setColor(color);
	}
	
	/**
	 * Sets the colour displayed by this panel, using the current mode.
	 * 
	 * <p>Note that this does not call the {@link ColorChangeListener}, since
	 * this change is not done by the user.</p> 
	 * 
	 * @param color The new colour to show.
	 */
	public void setColor(Color color) {
		int[] coords = mode.calculator.colorToDiagram(color);
		xSquare = coords[0];
		ySquare = coords[1];
		yLine = coords[2];
		
		repaint();
	}
	
	/**
	 * Gets the colour displayed by this panel.
	 * 
	 * @return The currently selected colour.
	 */
	public Color getColor() {
		return mode.calculator.diagramToColor(xSquare, ySquare, yLine);
	}
	
	/**
	 * Sets the mode. This method also updates the selected colour such that
	 * it stays the same, although the mode has changed.
	 * 
	 * @param mode The new mode.
	 */
	public void setMode(DiagramComponentMode mode) {
		
		// get the old colour and set the mode
		Color c = getColor();
		this.mode = mode;
		
		// clear the caches
		leftCache = null;
		rightCache = null;
		
		// put the colour back in the new mode
		setColor(c);
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
		
		g.translate(260, yLine - 3);
		arrow.setSize(new Dimension(7, 7));
		arrow.paint(g);
		g.translate(-260, -yLine + 3);
		
		g.setColor(Color.WHITE);
		g.drawOval(xSquare - 3, ySquare - 3, 7, 7);
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
				Color c = mode.calculator.diagramToColor(x, y, yLine);
				image.setRGB(x, y, c.getRGB());
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
				Color c = mode.calculator.diagramToColor(xSquare, ySquare, y);
				image.setRGB(x, y, c.getRGB());
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
			xSquare = x;
			ySquare = y;
			rightCache = null;
			repaint();
			
		} else if (x > 266) {
			// inside the colour line
			yLine = y;
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
