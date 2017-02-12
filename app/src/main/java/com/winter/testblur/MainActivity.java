package com.winter.testblur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button renderScriptBlur = null;
    Button fastBlur = null;
    Button advancedFastBlur = null;
    Button jniBlur = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renderScriptBlur = (Button) findViewById(R.id.test_render_script_blur);
        fastBlur = (Button) findViewById(R.id.test_java_blur);
        jniBlur = (Button) findViewById(R.id.test_jni_blur);
        advancedFastBlur = (Button) findViewById(R.id.test_advanced_java_blur);
        renderScriptBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RenderScriptBlurTestActivity.class);
                startActivity(intent);
            }
        });

    }
}
