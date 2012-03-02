/**
 * This file is a part of the Joxy Look and Feel for Java Swing.
 */

#include "joxy_utils_JoxyGraphics.h"
#include <QtGui/QApplication>
#include <QtGui/qcolor.h>
#include <QtGui/qimage.h>
#include <QtGui/qpainter.h>

JNIEXPORT void JNICALL Java_joxy_utils_JoxyGraphics_drawStringNative
  (JNIEnv *env, jclass cl, jstring str, jobject image) {


    int argc = 0;
    char** argv;
    QApplication a(argc, argv);

    const char* cstr = env->GetStringUTFChars(str, JNI_FALSE);
    QImage* qimage = new QImage(400, 30, QImage::Format_ARGB32);
    QPainter* painter = new QPainter(qimage);
    qimage->fill(QColor::fromRgb(0, 0, 0, 0));
    painter->drawText(0, 10, cstr);
    env->ReleaseStringUTFChars(str, NULL);

    //printf("Test");

	jclass cls = env->GetObjectClass(image);
	jmethodID mid = env->GetMethodID(cls, "setRGB", "(III)V");
    if (mid == NULL) {
    	printf("Couldn't find method setRGB(int)");
        return;
    }

	for (int i = 0; i < qimage->width(); i++) {
	    for (int j = 0; j < qimage->height(); j++) {
	    	int pixel = qimage->pixel(i, j);

	    	//image.setRGB(i, j, pixel);
	        env->CallVoidMethod(image, mid, i, j, pixel);
	    }
	}
}
