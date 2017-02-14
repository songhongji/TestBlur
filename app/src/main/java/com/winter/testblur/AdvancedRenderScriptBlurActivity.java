package com.winter.testblur;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class AdvancedRenderScriptBlurActivity extends AppCompatActivity {

    ImageView image = null;
    TextView showTime = null;
    TextView text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_render_script_blur);

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

        Bitmap overlay = StackBlur.buildBitmapCompress(bkg, view, scaleFactor);
        overlay = StackBlur.blurRenderScript(overlay, radius, getApplicationContext());
        view.setBackground(new BitmapDrawable(getResources(), overlay));

        showTime.setText("cost " + (System.currentTimeMillis() - startMs) + "ms");
    }
}
