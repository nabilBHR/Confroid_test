package com.example.test_confroid.application;

import android.app.Application;

public class ConfroidTestApplication extends Application {

    private static ConfroidTestApplication mContext;

    public static ConfroidTestApplication getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}