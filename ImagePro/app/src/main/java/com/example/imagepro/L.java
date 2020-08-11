package com.example.imagepro;

import android.util.Log;

public class L {

    //开关
    public static final boolean DEBUG = true;
    //TAG
    public static final String TAG = "APP";

    public static void e(String text){
        if(DEBUG){
            Log.e(TAG,text);
        }
    }
}