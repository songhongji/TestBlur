/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_winter_testblur_StackNative */

#ifndef _Included_com_winter_testblur_StackNative
#define _Included_com_winter_testblur_StackNative
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_winter_testblur_StackNative
 * Method:    blurPixels
 * Signature: ([IIII)V
 */
JNIEXPORT void JNICALL Java_com_winter_testblur_StackNative_blurPixels
  (JNIEnv *, jclass, jintArray, jint, jint, jint);

/*
 * Class:     com_winter_testblur_StackNative
 * Method:    blurBitmap
 * Signature: (Landroid/graphics/Bitmap;I)V
 */
JNIEXPORT void JNICALL Java_com_winter_testblur_StackNative_blurBitmap
  (JNIEnv *, jclass, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif