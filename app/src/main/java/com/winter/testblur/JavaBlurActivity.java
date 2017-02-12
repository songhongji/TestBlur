package com.winter.testblur;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

public class JavaBlurActivity extends AppCompatActivity {


    ImageView image = null;
    TextView text = null;
    TextView showTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_blur);

        image = (ImageView) findViewById(R.id.picture);
        text = (TextView) findViewById(R.id.text);
        showTime = (TextView) findViewById(R.id.show_time);

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        int radius = 25;

        Bitmap overlay = Bitmap.createBitmap((view.getMeasuredWidth()), (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);

        overlay = StackBlur.blur(overlay, radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));

        showTime.setText("cost " + (System.currentTimeMillis() - startMs) + "ms");
    }
}
