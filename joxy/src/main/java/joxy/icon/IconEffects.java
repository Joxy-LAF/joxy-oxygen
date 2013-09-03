/**
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

package joxy.icon;

import java.awt.color.ColorSpace;
import java.awt.image.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This class provides methods to derive 'disabled' and 'active' variants of icons.
 * 
 * <p>Icons aren't cached at the moment, but this will be supported in the future.</p>
 */
public class IconEffects {
	
	/**
	 * Returns a 'disabled' version of an icon (that is, the fainter version you can see
	 * in KDE when a button is disabled).
	 * 
	 * @param icon The icon to use as the base icon for the effect.
	 * @return The produced icon.
	 */
	public static ImageIcon getDisabledIcon(Icon icon) {
		BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		icon.paintIcon(null, image.getGraphics(), 0, 0);
		op.filter(image, image);
		
		short[] red = new short[256];
		short[] green = new short[256];
		short[] blue = new short[256];
		short[] alpha = new short[256];
		for (short i = 0; i < 256; i++) {
			red[i] = green[i] = blue[i] = i;
			alpha[i] = (short) (i / 2.5f);
		}
		op = new LookupOp(new ShortLookupTable(0, new short[][] {red, green, blue, alpha}), null);
		op.filter(image, image);
		
		return new ImageIcon(image);
	}
	
	/**
	 * Returns an 'active' version of an icon (that is, the lighter version you can see
	 * in KDE when hovering a toolbar button or menu item).
	 * 
	 * @param icon The icon to use as the base icon for the effect.
	 * @return The produced icon.
	 */
	public static ImageIcon getActiveIcon(Icon icon) {
		BufferedImageOp op = new RescaleOp(1.05f, 10, null);
		BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		icon.paintIcon(null, image.getGraphics(), 0, 0);
		op.filter(image, image);
		return new ImageIcon(image);
	}
}
