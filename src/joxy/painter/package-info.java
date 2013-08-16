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
