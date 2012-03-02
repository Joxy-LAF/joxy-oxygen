/**
 * This file is a part of the Joxy Look and Feel for Java Swing.
 */

#include "joxy_utils_JoxyGraphics.h"
#include <QtGui/QApplication>
#include <QtGui/qimage.h>
#include <QtGui/qx11info_x11.h>
#include <QtGui/qpainter.h>
#include <QtGui/qlabel.h>
#include <QtGui/qpushbutton.h>
#include <QtGui/qstyleoption.h>
#include <QtGui/qmotifstyle.h>
#include "jawt_md.h"

/*
 * Class:     joxy_utils_JoxyGraphics
 * Method:    drawStringNative
 * Signature: (Ljava/awt/Graphics;Ljava/lang/String;FF)V
 */
JNIEXPORT void JNICALL Java_joxy_utils_JoxyGraphics_drawStringNative
(JNIEnv* env, jclass canvas, jobject graphics, jclass realcanvas, jstring string, jfloat x, jfloat y)
{
    JAWT awt;
    JAWT_DrawingSurface* ds;
    JAWT_DrawingSurfaceInfo* dsi;
    JAWT_X11DrawingSurfaceInfo* dsi_x11;
    jboolean result;
    jint lock;
    GC gc;
    
    short	i;

    /* Get the AWT */
    awt.version = JAWT_VERSION_1_4;
    if (JAWT_GetAWT(env, &awt) == JNI_FALSE) {
        printf("AWT Not found\n");
        return;
    }

    /* Get the drawing surface */
    ds = awt.GetDrawingSurface(env, realcanvas);
    if (ds == NULL) {
        printf("NULL drawing surface\n");
        return;
    }

    /* Lock the drawing surface */
    lock = ds->Lock(ds);
    if((lock & JAWT_LOCK_ERROR) != 0) {
        printf("Error locking surface\n");
        awt.FreeDrawingSurface(ds);
        return;
    }

    /* Get the drawing surface info */
    dsi = ds->GetDrawingSurfaceInfo(ds);
    if (dsi == NULL) {
        printf("Error getting surface info\n");
        ds->Unlock(ds);
        awt.FreeDrawingSurface(ds);
        return;
    }

    /* Get the platform-specific drawing info */
    dsi_x11 = (JAWT_X11DrawingSurfaceInfo*)dsi->platformInfo;


    /* Now paint */
    gc = XCreateGC(dsi_x11->display, dsi_x11->drawable, 0, 0);
    
    int argc = 0;
    char** argv;
    QApplication a(argc, argv);
    
    QImage* image = new QImage(500, 500, QImage::Format_RGB32);

    QPainter* painter = new QPainter(image);

    //style->drawItemText(painter, QRect(0, 0, 50, 50), Qt::AlignLeft, button->palette(), false, "test");

    painter->fillRect(0, 0, 500, 500, QColor::fromRgb(255, 255, 255));
    painter->drawText(0, 10, "Dit is native!");
    painter->drawText(0, 25, "Het werkt!");
    painter->drawText(0, 40, "The game!");

    //QMotifStyle style = new QMotifStyle();
    //style.drawControl(QStyle::painter, QRect(0, 30, 50, 50), Qt::AlignLeft, button->palette(), false, "test");

    Visual* visual = XDefaultVisual(dsi_x11->display, 0);
    int depth = 24;

    XImage *xi = XCreateImage(dsi_x11->display, visual, depth, ZPixmap, 0, reinterpret_cast<char*>(image->bits()), 499, 500, 32, image->bytesPerLine());

    XPutImage(dsi_x11->display, dsi_x11->drawable, gc, xi, 0, 0, 0, 0, 499, 500);

    //XDestroyImage(xi);

    XFreeGC(dsi_x11->display, gc);



    /* Free the drawing surface info */
    ds->FreeDrawingSurfaceInfo(dsi);

    /* Unlock the drawing surface */
    ds->Unlock(ds);

    /* Free the drawing surface */
    awt.FreeDrawingSurface(ds);
}
