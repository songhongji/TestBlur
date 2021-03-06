package com.winter.testblur;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class AdvancedJniBitmapBlurActivity extends AppCompatActivity {

    ImageView image = null;
    TextView showTime = null;
    TextView text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_jni_bitmap_blur);

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
        int radius = 2;
        float scaleFactor = 8;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor), (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = StackBlur.blurNativelyBitmap(overlay, radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));

        showTime.setText("cost " + (System.currentTimeMillis() - startMs) + "ms");
    }
}
