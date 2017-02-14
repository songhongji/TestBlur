package com.winter.testblur;

/**
 * Created by songhongji on 2017/2/13.
 */

import android.graphics.Bitmap;

/**
 * This use jni blurJava bitmap and pixels
 * Blur arithmetic is StackBlur
 */
class StackNative {

    /**
     * Load genius jni file
     */

    /**
     * Blur Image By Pixels
     *
     * @param img Img pixel array
     * @param w   Img width
     * @param h   Img height
     * @param r   Blur radius
     */
    protected static native void blurPixels(int[] img, int w, int h, int r);

    /**
     * Blur Image By Bitmap
     *
     * @param bitmap Img Bitmap
     * @param r      Blur radius
     */
    protected static native void blurBitmap(Bitmap bitmap, int r);

    static {
        System.loadLibrary("TestJniLibName");   //defaultConfig.ndk.moduleName
    }
}