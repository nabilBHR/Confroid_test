package com.example.test_confroid.App;

import android.app.Application;

public class MyApp extends Application {

    private static MyApp mContext;

    public static MyApp getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}