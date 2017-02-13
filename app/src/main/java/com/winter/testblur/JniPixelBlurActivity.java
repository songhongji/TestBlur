package com.winter.testblur;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class JniPixelBlurActivity extends AppCompatActivity {

    ImageView image = null;
    TextView showTime = null;
    TextView text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_pixel_blur);

        image = (ImageView) findViewById(R.id.picture);
        showTime = (TextView) findViewById(R.id.show_time);
        text = (TextView) findViewById(R.id.text);
        applyBlur();
    }

    private void applyBlur() {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();
                Bitmap bmp = image.getDrawingCache();
                blur(bmp, text);
                return true;
            }
        });
    }

    // RenderScript 是在3.0以后引入的，内置的compute kernel在JELLY_BEAN_MR1中引入 api 17以后引入的
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        int radius = 20;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()), (int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);
        overlay = StackBlur.blurNativelyPixels(overlay, radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));

        showTime.setText("cost " + (System.currentTimeMillis() - startMs) + "ms");
    }
}
