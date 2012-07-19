package joxy;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Joxy's UI delegate for the JTableHeader.
 * 
 * <p>There still is a lot of work to do in this class.</p>
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class JoxyTableHeaderUI extends BasicTableHeaderUI {
	
	public static ComponentUI createUI(JComponent h) {
        return new JoxyTableHeaderUI();
    }
	
	@Override
	public void paint(Graphics g, JComponent c) {
		if (header.getColumnModel().getColumnCount() <= 0) {
            return;
        }

		Rectangle clip = g.getClipBounds();
		Point left = clip.getLocation();
		Point right = new Point(clip.x + clip.width - 1, clip.y);

        TableColumnModel cm = header.getColumnModel();
		int cMin = header.columnAtPoint(left);
        int cMax = header.columnAtPoint(right);
        // This should never happen.
        if (cMin == -1) {
            cMin =  0;
        }
        // If the table does not have enough columns to fill the view we'll get -1.
        // Replace this with the index of the last column.
        if (cMax == -1) {
            cMax = cm.getColumnCount()-1;
        }

        TableColumn draggedColumn = header.getDraggedColumn();
        int columnWidth;
        Rectangle cellRect = header.getHeaderRect(cMin);
        TableColumn aColumn;
        
		for (int column = cMin; column <= cMax; column++) {
			aColumn = cm.getColumn(column);
			columnWidth = aColumn.getWidth();
			cellRect.width = columnWidth;
			if (aColumn != draggedColumn) {
				paintCell(g, cellRect, column);
			}
			cellRect.x += columnWidth;
		}
	}
	
	private void paintCell(Graphics g, Rectangle cellRect, int columnIndex) {
        //Component component = getHeaderRenderer(columnIndex);
        //rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y,
                            //cellRect.width, cellRect.height, true);
    }
}
