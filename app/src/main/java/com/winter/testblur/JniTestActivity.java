package com.winter.testblur;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class JniTestActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_test);

        TextView textView = (TextView) findViewById(R.id.jni_test_text);

        textView.setText(new NdkJniUtils().getCLanguageString());

        ImageView imageView = (ImageView) findViewById(R.id.image_view_test);

        Bitmap btm = imageView.getDrawingCache();

        StackNative.blurBitmap(btm, 1);

        imageView.setBackground(new BitmapDrawable(getResources(), btm));
    }
}
