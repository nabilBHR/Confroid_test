package com.example.test_confroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class TokenPuller extends Service {
    public static final String PACKAGE_NAME = "com.example.confroid_project";
    public static final String TOKEN_DISPENSER = "com.example.confroid_project.receivers.TokenDispenser";
    private static String Token = "";

    public static void pullToken(Context context) {
        Intent intent = new Intent("GET_TOKEN");
        intent.putExtra("receiver", "com.example.test_confroid.services.TokenPuller");
        intent.putExtra("name", context.getPackageName());
        context.sendBroadcast(intent);
    }

    public static String getToken() {
        return Token;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Token = intent.getStringExtra("token");
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}