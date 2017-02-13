package com.winter.testblur;

/**
 * Created by songhongji on 2017/2/13.
 */

public class NdkJniUtils {
    public native String getCLanguageString();

    static {
        System.loadLibrary("TestJniLibName");   //defaultConfig.ndk.moduleName
    }
}


