package joxy;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import joxy.painter.InputFieldPainter;
import joxy.painter.TextFieldFocusIndicatorPainter;
import joxy.painter.TextFieldHoverIndicatorPainter;
import joxy.utils.Utils;

/**
 * Joxy's UI delegate for the JTree.
 */
public class JoxyTreeUI extends BasicTreeUI {

	public static ComponentUI createUI(JComponent c) {
		JoxyTreeUI ui = new JoxyTreeUI();
		return ui;
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setClosedIcon(Utils.getOxygenIcon("places/folder", 16));
		renderer.setOpenIcon(Utils.getOxygenIcon("places/folder", 16));
		renderer.setLeafIcon(Utils.getOxygenIcon("mimetypes/application-x-zerosize", 16));
		tree.setCellRenderer(renderer);
		
		tree.setOpaque(false);
		tree.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	
	    Rectangle vr = tree.getVisibleRect();
	
		InputFieldPainter.paint(g2, vr.x, vr.y, vr.width, vr.height);
		
		Shape oldClip = g2.getClip();
		g2.setClip(vr.x + 2, vr.y + 2, vr.width - 4, vr.height - 5);
		super.paint(g2, c); // this also paints the contents of the list
		g2.setClip(oldClip);
		
		if (c.isEnabled()) {
			//TextFieldFocusIndicatorPainter.paint(g2, vr.x, vr.y, vr.width, vr.height, focusAmount);
			//TextFieldHoverIndicatorPainter.paint(g2, vr.x, vr.y, vr.width, vr.height, Math.max(0, hoverAmount - focusAmount));
		}
	}
	
	@Override
	protected void paintExpandControl(Graphics g, Rectangle clipBounds,
			Insets insets, Rectangle bounds, TreePath path, int row,
			boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        
        Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		
		Object value = path.getLastPathComponent();
		
		Color c = g2.getColor();
		Stroke s = g2.getStroke();
		
		// Draw icons if not a leaf and either hasn't been loaded,
        // or the model child count is > 0.
        if (!isLeaf && (!hasBeenExpanded || treeModel.getChildCount(value) > 0)) {
        	int middleXOfKnob;
        	
            if (tree.getComponentOrientation().isLeftToRight()) {
                middleXOfKnob = bounds.x - getRightChildIndent() + 1;
            } else {
                middleXOfKnob = bounds.x + bounds.width + getRightChildIndent() - 1;
            }
            int middleYOfKnob = bounds.y + (bounds.height / 2);
            
            // Paint the expand arrow with the calculated center point
    		g2.setColor(Color.WHITE);
    		g2.setStroke(new BasicStroke(4f));
    		
    		Point2D center = new Point2D.Double(middleXOfKnob, middleYOfKnob);
            double width, height;
            
            if (isExpanded) {
            	width = 5;
            	height = 2.5f;
            	Rectangle2D paintRect = new Rectangle2D.Double(center.getX() - width / 2, center.getY() - height / 2, width, height);
            	g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMinY(), paintRect.getCenterX(), paintRect.getMaxY()));
    			g2.draw(new Line2D.Double(paintRect.getMaxX(), paintRect.getMinY(), paintRect.getCenterX(), paintRect.getMaxY()));
            } else {
            	width = 2.5f;
            	height = 5f;
            	Rectangle2D paintRect = new Rectangle2D.Double(center.getX() - width / 2, center.getY() - height / 2, width, height);
            	g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMinY(), paintRect.getMaxX(), paintRect.getCenterY()));
    			g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMaxY(), paintRect.getMaxX(), paintRect.getCenterY()));
            }
            
            // Paint the expand arrow again in black
    		g2.setColor(Color.BLACK);
    		g2.setStroke(new BasicStroke(1.25f));
            
            if (isExpanded) {
            	width = 5;
            	height = 2.5f;
            	Rectangle2D paintRect = new Rectangle2D.Double(center.getX() - width / 2, center.getY() - height / 2, width, height);
            	g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMinY(), paintRect.getCenterX(), paintRect.getMaxY()));
    			g2.draw(new Line2D.Double(paintRect.getMaxX(), paintRect.getMinY(), paintRect.getCenterX(), paintRect.getMaxY()));
            } else {
            	width = 2.5f;
            	height = 5f;
            	Rectangle2D paintRect = new Rectangle2D.Double(center.getX() - width / 2, center.getY() - height / 2, width, height);
            	g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMinY(), paintRect.getMaxX(), paintRect.getCenterY()));
    			g2.draw(new Line2D.Double(paintRect.getMinX(), paintRect.getMaxY(), paintRect.getMaxX(), paintRect.getCenterY()));
            }
        }

		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		g2.setStroke(s);
		g2.setColor(c);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>This was overridden to ensure that the correct AA settings are chosen
	 * for the text, even if custom renderers are used (unless these renderers
	 * happen to reset these settings, but then it is their fault).
	 */
	@Override
	protected void paintRow(Graphics g, Rectangle clipBounds, Insets insets,
			Rectangle bounds, TreePath path, int row, boolean isExpanded,
			boolean hasBeenExpanded, boolean isLeaf) {
		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		super.paintRow(g, clipBounds, insets, bounds, path, row, isExpanded,
				hasBeenExpanded, isLeaf);
	}
}
