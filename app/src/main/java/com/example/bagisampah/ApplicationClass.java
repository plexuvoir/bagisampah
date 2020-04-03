package com.example.bagisampah;

import android.app.ActivityManager;
import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONObject;

public class ApplicationClass extends Application {

    private static ApplicationClass mInstance;

    public ApplicationClass(){
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized ApplicationClass getInstance(){
        return mInstance;
    }

}



