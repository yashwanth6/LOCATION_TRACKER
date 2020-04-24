package com.example.secure;

import android.app.Application;
import android.content.Context;

/**
 * Created by manikanta on 11/8/2018.
 */

public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
}
