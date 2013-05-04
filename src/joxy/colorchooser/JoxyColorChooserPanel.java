package joxy.colorchooser;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 * A color chooser panel in Oxygen style.
 */
public class JoxyColorChooserPanel extends AbstractColorChooserPanel {
	
	DiagramComponent dc;
	
	@Override
	protected void buildChooser() {
		dc = new DiagramComponent(getColorFromModel());
		
		dc.setListener(new ColorChangeListener() {
			
			@Override
			public void colorChanged(Color newColor) {
				getColorSelectionModel().setSelectedColor(newColor);
			}
		});
		
		add(dc);
	}
	
	@Override
	public void updateChooser() {
		dc.setColor(getColorFromModel());
	}

	@Override
	public String getDisplayName() {
		return "Colors"; // TODO what to return here exactly?
	}

	@Override
	public Icon getSmallDisplayIcon() {
		return null;
	}

	@Override
	public Icon getLargeDisplayIcon() {
		return null;
	}
}