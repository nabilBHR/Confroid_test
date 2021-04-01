package com.example.test_confroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TokenPuller extends Service {
    private static String Token = "";
    public static final String PACKAGE_NAME = "com.example.confroid_project";
    public static final String TOKEN_DISPENSER = "com.example.confroid_project.receivers.TokenDispenser";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("token_puller", "Start service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Token = intent.getStringExtra("token");
        Log.d("token_puller", Token==null? "null":Token);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    public static void pullToken(Context context) {
        Intent intent = new Intent("GET_TOKEN");
        intent.putExtra("receiver", "com.example.test_confroid.services.TokenPuller");
        intent.putExtra("name", context.getPackageName());
        //intent.setClassName(PACKAGE_NAME, TOKEN_DISPENSER);
        context.sendBroadcast(intent);
    }

    public static String getToken() {
        return Token;
    }
}