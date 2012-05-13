/**
 * This file is a part of the Joxy Look and Feel for Java Swing.
 */

#include "joxy_utils_JoxyGraphics.h"
#include <QtGui/QApplication>
#include <QtGui/qcolor.h>
#include <QtGui/qimage.h>
#include <QtGui/qpainter.h>
#include <kcmdlineargs.h>
#include <kapplication.h>

bool initialized = false;
KApplication* a;
jmethodID mid;

// Initialization

void init() {
	initialized = true;
	int argc = 0;
	char** argv;
	KCmdLineArgs::init(argc, argv, "joxy", "Joxy", ki18n("0.0.3"), "Some description");
	a = new KApplication();
}

// Native code for joxy.utils.JoxyGraphics

JNIEXPORT void JNICALL Java_joxy_utils_JoxyGraphics_initializeNative
  (JNIEnv *env, jclass cl) {

	if (!initialized) {
		init();
	}

	jclass cls = env->FindClass("java/awt/image/BufferedImage");

	if (cls == NULL) {
    	printf("Couldn't find class BufferedImage\n");
        return;
    }

	mid = env->GetMethodID(cls, "setRGB", "(III)V");

    if (mid == NULL) {
    	printf("Couldn't find method setRGB(int)\n");
        return;
    }
}

JNIEXPORT void JNICALL Java_joxy_utils_JoxyGraphics_drawStringNative
  (JNIEnv *env, jclass cl, jstring str, jobject image, jint width, jint height, jstring fontname, jint fontsize, jint color) {

	if (!initialized) {
		init();
	}

    QImage qimage(width, height, QImage::Format_ARGB32);
    QColor qcolor = QColor::fromRgb(color);
    qcolor.setAlpha(0);
    qimage.fill(qcolor);
    QPainter painter(&qimage);
    painter.setPen(QColor::fromRgb(color));

    const char* cfontname = env->GetStringUTFChars(fontname, JNI_FALSE);
    painter.setFont(QFont(cfontname, fontsize, 0, false));
    const char* cstr = env->GetStringUTFChars(str, JNI_FALSE);
    painter.drawText(0, 0, width + 10, height, Qt::AlignLeft, cstr);
    env->ReleaseStringUTFChars(str, cfontname);
    env->ReleaseStringUTFChars(str, cstr);

    // TODO do this with the int[] version, so the for-loop is unnecessary

	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
	    	int pixel = qimage.pixel(i, j);

	    	// if it is transparent, it is useless to copy it...
	    	if (pixel != 0) {
	    		env->CallVoidMethod(image, mid, i, j, pixel);
	    	}
	    }
	}
}

// Native code for joxy.JoxyFileChooserUI
