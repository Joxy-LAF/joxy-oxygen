/**
 * This file is a part of the Joxy Look and Feel for Java Swing.
 */

#include "joxy_utils_JoxyGraphics.h"
#include <QtGui/QApplication>
#include <QtGui/qcolor.h>
#include <QtGui/qimage.h>
#include <QtGui/qpainter.h>

JNIEXPORT void JNICALL Java_joxy_utils_JoxyGraphics_drawStringNative
  (JNIEnv *env, jclass cl, jstring str, jobject image, jint width, jstring fontname, jint fontsize, jint color) {


    int argc = 0;
    char** argv;
    QApplication a(argc, argv);

    QImage* qimage = new QImage(width, 30, QImage::Format_ARGB32);
    QColor qcolor = QColor::fromRgb(color);
    qcolor.setAlpha(0);
    qimage->fill(qcolor);
    QPainter* painter = new QPainter(qimage);
    painter->setPen(QColor::fromRgb(color));

    const char* cfontname = env->GetStringUTFChars(fontname, JNI_FALSE);
    painter->setFont(QFont(cfontname, fontsize, 0, false));
    env->ReleaseStringUTFChars(str, cfontname);

    const char* cstr = env->GetStringUTFChars(str, JNI_FALSE);
    painter->drawText(0, 10, cstr);
    env->ReleaseStringUTFChars(str, cstr);

	jclass cls = env->GetObjectClass(image);
	jmethodID mid = env->GetMethodID(cls, "setRGB", "(III)V");
    if (mid == NULL) {
    	printf("Couldn't find method setRGB(int)");
        return;
    }

    int height = qimage->height();

	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
	    	int pixel = qimage->pixel(i, j);

	    	// if it is transparent, it is useless to copy it...
	    	if (pixel != 0) {
	    		env->CallVoidMethod(image, mid, i, j, pixel);
	    	}
	    }
	}
}
