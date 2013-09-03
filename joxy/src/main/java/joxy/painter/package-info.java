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

/**
 * This package contains painters, which are classes that are able to paint
 * GUI elements, such as the backgrounds of buttons. Painters are not Swing
 * classes; they are defined by Joxy and used by the UI delegates to perform
 * the painting.
 * 
 * <p>All painters inherit from Painter, the base class of the
 * painters. This class also implements caching for all painters. To use a
 * painter, the UI delegate only has to instantiate one of them, and use
 * the {@link Painter#paint(java.awt.Graphics2D, int, int, int, int)} method. See
 * the Painter documentation.</p>
 * 
 * <p>(At the moment, there are also some older painters which have not yet been
 * converted to be a {@link joxy.painter.Painter} subclass. These contain a static
 * method to paint the object.)</p>
 */
package joxy.painter;
