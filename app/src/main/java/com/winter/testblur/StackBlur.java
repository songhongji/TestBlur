package com.winter.testblur;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Created by songhongji on 2017/2/12.
 */

public class StackBlur extends StackNative{

    private static Bitmap buildBitmap(Bitmap original, boolean canReuseInBitmap) {
        // First we should check the original
        if (original == null)
            throw new NullPointerException("Blur bitmap original isn't null");

        Bitmap.Config config = original.getConfig();
        if (config != Bitmap.Config.ARGB_8888 && config != Bitmap.Config.RGB_565) {
            throw new RuntimeException("Blur bitmap only supported Bitmap.Config.ARGB_8888 and Bitmap.Config.RGB_565.");
        }

        // If can reuse in bitmap return this or copy
        Bitmap rBitmap;
        if (canReuseInBitmap) {
            rBitmap = original;
        } else {
            rBitmap = original.copy(config, true);
        }
        return (rBitmap);
    }

    // 压缩
    public static Bitmap buildBitmapCompress(Bitmap original, View view, float scaleFactor) {

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor), (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(original, 0, 0, paint);

        return overlay;
    }

    /**
     * StackBlur By Java Bitmap
     *
     * @param original         Original Image
     * @param radius           Blur radius
     * @param canReuseInBitmap Can reuse In original Bitmap
     * @return Image Bitmap
     */
    public static Bitmap blurJava(Bitmap original, int radius, boolean canReuseInBitmap) {
        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blurJava
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
        if (radius < 1) {
            return (null);
        }

        Bitmap bitmap = buildBitmap(original, canReuseInBitmap);

        // Return this none blurJava
        if (radius == 1) {
            return bitmap;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        // get array
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        // run Blur
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        short r[] = new short[wh];
        short g[] = new short[wh];
        short b[] = new short[wh];
        int rSum, gSum, bSum, x, y, i, p, yp, yi, yw;
        int vMin[] = new int[Math.max(w, h)];

        int divSum = (div + 1) >> 1;
        divSum *= divSum;

        short dv[] = new short[256 * divSum];
        for (i = 0; i < 256 * divSum; i++) {
            dv[i] = (short) (i / divSum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackPointer;
        int stackStart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routSum, goutSum, boutSum;
        int rinSum, ginSum, binSum;

        for (y = 0; y < h; y++) {
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rSum = gSum = bSum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rbs = r1 - Math.abs(i);
                rSum += sir[0] * rbs;
                gSum += sir[1] * rbs;
                bSum += sir[2] * rbs;
                if (i > 0) {
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                } else {
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }
            }
            stackPointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rSum];
                g[yi] = dv[gSum];
                b[yi] = dv[bSum];

                rSum -= routSum;
                gSum -= goutSum;
                bSum -= boutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];

                if (y == 0) {
                    vMin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vMin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];

                rSum += rinSum;
                gSum += ginSum;
                bSum += binSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[(stackPointer) % div];

                routSum += sir[0];
                goutSum += sir[1];
                boutSum += sir[2];

                rinSum -= sir[0];
                ginSum -= sir[1];
                binSum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinSum = ginSum = binSum = routSum = goutSum = boutSum = rSum = gSum = bSum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rSum += r[yi] * rbs;
                gSum += g[yi] * rbs;
                bSum += b[yi] * rbs;

                if (i > 0) {
                    rinSum += sir[0];
                    ginSum += sir[1];
                    binSum += sir[2];
                } else {
                    routSum += sir[0];
                    goutSum += sir[1];
                    boutSum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackPointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rSum] << 16) | (dv[gSum] << 8) | dv[bSum];

                rSum -= routSum;
                gSum -= goutSum;
                bSum -= boutSum;

                stackStart = stackPointer - radius + div;
                sir = stack[stackStart % div];

                routSum -= sir[0];
                goutSum -= sir[1];
                boutSum -= sir[2];

                if (x == 0) {
                    vMin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vMin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinSum += sir[0];
                ginSum += sir[1];
                binSum += sir[2];

                rSum += rinSum;
                gSum += ginSum;
                bSum += binSum;

                stackPointer = (stackPointer + 1) % div;
                sir = stack[stackPointer];

                routSum += sir[0];
                goutSum += sir[1];
                boutSum += sir[2];

                rinSum -= sir[0];
                ginSum -= sir[1];
                binSum -= sir[2];

                yi += w;
            }
        }

        // set Bitmap
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurRenderScript(Bitmap original, int radius, Context context) {

        Bitmap.Config config = original.getConfig();
        if (config != Bitmap.Config.ARGB_8888 && config != Bitmap.Config.RGB_565) {
            throw new RuntimeException("Blur bitmap only supported Bitmap.Config.ARGB_8888 and Bitmap.Config.RGB_565.");
        }

        // 初始化 RenderScript Content
        RenderScript rs = RenderScript.create(context);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, original);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(original);
        rs.destroy();

        return original;
    }

    /**
     * StackBlur By Jni Bitmap
     *
     * @param original         Original Image
     * @param radius           Blur radius
     * @param canReuseInBitmap Can reuse In original Bitmap
     * @return Image Bitmap
     */
    public static Bitmap blurNativelyBitmap(Bitmap original, int radius, boolean canReuseInBitmap) {
        if (radius < 1) {
            return null;
        }

        Bitmap bitmap = buildBitmap(original, canReuseInBitmap);

        // Return this none blurJava
        if (radius == 1) {
            return bitmap;
        }

        //Jni BitMap Blur
        blurBitmap(bitmap, radius);

        return (bitmap);
    }

    /**
     * StackBlur By Jni Pixels
     *
     * @param original         Original Image
     * @param radius           Blur radius
     * @param canReuseInBitmap Can reuse In original Bitmap
     * @return Image Bitmap
     */
    public static Bitmap blurNativelyPixels(Bitmap original, int radius, boolean canReuseInBitmap) {
        if (radius < 1) {
            return null;
        }

        Bitmap bitmap = buildBitmap(original, canReuseInBitmap);

        // Return this none blurJava
        if (radius == 1) {
            return bitmap;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        // Jni Pixels Blur
        blurPixels(pix, w, h, radius);

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

}
