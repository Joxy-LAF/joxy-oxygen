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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import joxy.color.HSVColor;
import joxy.colorchooser.DiagramComponent.DiagramComponentMode;

/**
 * A color chooser panel in Oxygen style.
 */
public class JoxyColorChooserPanel extends AbstractColorChooserPanel {
	
	DiagramComponent dc;
	
	JRadioButton hueButton;
	JSpinner hueSpinner;
	JRadioButton saturationButton;
	JSpinner saturationSpinner;
	JRadioButton valueButton;
	JSpinner valueSpinner;
	JRadioButton redButton;
	JSpinner redSpinner;
	JRadioButton greenButton;
	JSpinner greenSpinner;
	JRadioButton blueButton;
	JSpinner blueSpinner;
	
	@Override
	protected void buildChooser() {
		
		setLayout(new BorderLayout(5, 5));
		
		JPanel upperPanel = new JPanel();
		
		dc = new DiagramComponent(getColorFromModel(), DiagramComponentMode.RED_MODE);
		dc.setListener(new ColorChangeListener() {
			
			@Override
			public void colorChanged(Color newColor) {
				getColorSelectionModel().setSelectedColor(newColor);
				updateSpinners();
			}
		});
		upperPanel.add(dc);
		
		add(upperPanel, BorderLayout.CENTER);

		JPanel lowerPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		
		JPanel radioPanel = new JPanel(new GridLayout(3, 4, 10, 0));
		ButtonGroup group = new ButtonGroup();
		
		// hue
		hueButton = new JRadioButton("Hue:"); // TODO should be internationalised
		hueButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dc.setMode(DiagramComponentMode.HUE_MODE);
			}
		});
		group.add(hueButton);
		radioPanel.add(hueButton);
		
		hueSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
		hueSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				updateFromSpinners();
			}
		});
		radioPanel.add(hueSpinner);
		
		// red
		redButton = new JRadioButton("Red:");
		redButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dc.setMode(DiagramComponentMode.RED_MODE);
			}
		});
		redButton.setSelected(true);
		group.add(redButton);
		radioPanel.add(redButton);
		
		redSpinner = new JSpinner(new SpinnerNumberModel(getColorFromModel().getRed(), 0, 255, 1));
		redSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				updateFromSpinners();
			}
		});
		radioPanel.add(redSpinner);
		
		// saturation
		saturationButton = new JRadioButton("Saturation:");
		saturationButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dc.setMode(DiagramComponentMode.SATURATION_MODE);
			}
		});
		group.add(saturationButton);
		radioPanel.add(saturationButton);
		
		saturationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
		saturationSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				updateFromSpinners();
			}
		});
		radioPanel.add(saturationSpinner);

		// green
		greenButton = new JRadioButton("Green:");
		greenButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dc.setMode(DiagramComponentMode.GREEN_MODE);
			}
		});
		group.add(greenButton);
		radioPanel.add(greenButton);
		
		greenSpinner = new JSpinner(new SpinnerNumberModel(getColorFromModel().getGreen(), 0, 255, 1));
		greenSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				updateFromSpinners();
			}
		});
		radioPanel.add(greenSpinner);
		
		// value
		valueButton = new JRadioButton("Value:");
		valueButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dc.setMode(DiagramComponentMode.VALUE_MODE);
			}
		});
		group.add(valueButton);
		radioPanel.add(valueButton);
		
		valueSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
		valueSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				updateFromSpinners();
			}
		});
		radioPanel.add(valueSpinner);
		
		// blue
		blueButton = new JRadioButton("Blue:");
		blueButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dc.setMode(DiagramComponentMode.BLUE_MODE);
			}
		});
		group.add(blueButton);
		radioPanel.add(blueButton);
		
		blueSpinner = new JSpinner(new SpinnerNumberModel(getColorFromModel().getBlue(), 0, 255, 1));
		blueSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				updateFromSpinners();
			}
		});
		radioPanel.add(blueSpinner);
		
		lowerPanel.add(radioPanel);
		
		add(lowerPanel, BorderLayout.PAGE_END);
	}
	
	@Override
	public void updateChooser() {
		dc.setColor(getColorFromModel());
		updateSpinners();
	}
	
	/**
	 * Updates the spinners for the colour values, depending on the currently
	 * selected colour.
	 */
	protected void updateSpinners() {
		Color c = getColorFromModel();
		HSVColor hsv = new HSVColor(c);
		
		hueSpinner.setValue((int) (hsv.getH() * 360));
		saturationSpinner.setValue((int) (hsv.getS() * 256));
		valueSpinner.setValue((int) (hsv.getV() * 256));
		redSpinner.setValue(c.getRed());
		greenSpinner.setValue(c.getGreen());
		blueSpinner.setValue(c.getBlue());
	}
	
	/**
	 * Updates the selected colour from the values selected in the spinners.
	 */
	protected void updateFromSpinners() {
		Color c = new Color((Integer) redSpinner.getValue(),
				(Integer) greenSpinner.getValue(),
				(Integer) blueSpinner.getValue());
		getColorSelectionModel().setSelectedColor(c);
		dc.setColor(c);
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
