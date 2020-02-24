package com.example.bagisampah;

import android.app.ActivityManager;
import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.onesignal.OneSignal;

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
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

    }

    public static synchronized ApplicationClass getInstance(){
        return mInstance;
    }

}



