package com.winter.testblur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button renderScriptBlur = null;
    Button advancedRenderScriptBlur = null;
    Button javaBlur = null;
    Button advancedJavaBlur = null;
    Button jniBlur = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renderScriptBlur = (Button) findViewById(R.id.test_render_script_blur);
        advancedRenderScriptBlur = (Button) findViewById(R.id.test_advanced_render_script_blur);
        javaBlur = (Button) findViewById(R.id.test_java_blur);
        jniBlur = (Button) findViewById(R.id.test_jni_blur);
        advancedJavaBlur = (Button) findViewById(R.id.test_advanced_java_blur);
        renderScriptBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RenderScriptBlurTestActivity.class);
                startActivity(intent);
            }
        });

        javaBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), JavaBlurActivity.class);
                startActivity(intent);
            }
        });

        advancedJavaBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AdvancedJavaBlurActivity.class);
                startActivity(intent);
            }
        });

        advancedRenderScriptBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AdvancedRenderScriptBlurActivity.class);
                startActivity(intent);
            }
        });
    }
}
