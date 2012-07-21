package joxy;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import joxy.utils.JoxyGraphics;

/**
 * Joxy's UI delegate for the JTableHeader.
 * 
 * <p>The paint methods are overridden to let the table header look more KDE-like.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyTableHeaderUI extends BasicTableHeaderUI {
	
	public static ComponentUI createUI(JComponent h) {
        return new JoxyTableHeaderUI();
    }
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		header.setOpaque(false);
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		// first render the background
		renderBackground(g2, c);
		
		// then render the names of the columns
		renderColumns(g2, c);
	}
	
	/**
	 * Paints the background of the table header.
	 * @param g2 The {@link Graphics2D} object to paint with.
	 * @param c The {@link JTableHeader} object to paint the background for.
	 */
	public void renderBackground(Graphics2D g2, JComponent c) {
		g2.setColor(UIManager.getColor("Window.background"));
		g2.fillRect(0, 0, c.getWidth(), c.getHeight());
		
		g2.setColor(Color.WHITE);
		g2.drawLine(0, c.getHeight() - 2, c.getWidth(), c.getHeight() - 2);
		g2.setColor(Color.GRAY);
		g2.drawLine(0, c.getHeight() - 1, c.getWidth(), c.getHeight() - 1);
	}
	
	/**
	 * Paints the column names and the separators between them. This method is partially
	 * copied from {@link BasicTableHeaderUI#paint(Graphics, JComponent)}.
	 * @param g2 The {@link Graphics2D} object to paint with.
	 * @param c The {@link JTableHeader} object to paint the columns for.
	 */
	public void renderColumns(Graphics2D g2, JComponent c) {
		if (header.getColumnModel().getColumnCount() <= 0) {
            return;
        }
		
		Rectangle clip = g2.getClipBounds();
		Point left = clip.getLocation();
		Point right = new Point(clip.x + clip.width - 1, clip.y);

		TableColumnModel cm = header.getColumnModel();
		int cMin = header.columnAtPoint(left);
		int cMax = header.columnAtPoint(right);

		// this should never happen
		if (cMin == -1) {
			cMin = 0;
		}

		// if the table does not have enough columns to fill the view we'll get -1;
		// replace this with the index of the last column
		if (cMax == -1) {
			cMax = cm.getColumnCount() - 1;
		}

		TableColumn draggedColumn = header.getDraggedColumn();
		int columnWidth;
		Rectangle cellRect = header.getHeaderRect(cMin);
		TableColumn aColumn;
        
		// loop over all columns and paint them
		for (int column = cMin; column <= cMax; column++) {
			aColumn = cm.getColumn(column);
			columnWidth = aColumn.getWidth();
			cellRect.width = columnWidth;
			if (aColumn != draggedColumn) {
				paintColumnName(g2, cellRect, column);
				if (column != cMin) {
					paintColumnSeparator(g2, cellRect);
				}
			}
			cellRect.x += columnWidth;
		}
		
		// TODO render the dragged column
	}
	
	/**
	 * Paints the name of a column.
	 * @param g2 The {@link Graphics2D} object to paint with.
	 * @param cellRect A {@link Rectangle} object that indicates the size and position of
	 * the column.
	 * @param columnIndex The index of the column (used for finding the name).
	 */
	private void paintColumnName(Graphics2D g2, Rectangle cellRect, int columnIndex) {
		g2.setColor(Color.BLACK);
		String name = header.getColumnModel().getColumn(columnIndex).getHeaderValue().toString();
		FontMetrics f = header.getFontMetrics(header.getFont());
		int w = f.stringWidth(name);
		int h = f.getHeight();
		JoxyGraphics.drawString(g2, name, cellRect.x + (cellRect.width - w) / 2, cellRect.y + (cellRect.height + h) / 2 - 3);
    }
	
	/**
	 * Paints a column separator <i>before</i> the given column.
	 * @param g2 The {@link Graphics2D} object to paint with.
	 * @param cellRect A {@link Rectangle} object that indicates the size and position of
	 * the column.
	 */
	private static void paintColumnSeparator(Graphics2D g2, Rectangle cellRect) {
		g2.setColor(new Color(220, 220, 220));
		g2.drawOval(cellRect.x, cellRect.y + cellRect.height / 2 - 3, 1, 1);
		g2.drawOval(cellRect.x, cellRect.y + cellRect.height / 2, 1, 1);
		g2.drawOval(cellRect.x, cellRect.y + cellRect.height / 2 + 3, 1, 1);
		
		g2.setColor(Color.GRAY);
		g2.drawOval(cellRect.x - 1, cellRect.y + cellRect.height / 2 - 4, 1, 1);
		g2.drawOval(cellRect.x - 1, cellRect.y + cellRect.height / 2 - 1, 1, 1);
		g2.drawOval(cellRect.x - 1, cellRect.y + cellRect.height / 2 + 2, 1, 1);
    }
}
