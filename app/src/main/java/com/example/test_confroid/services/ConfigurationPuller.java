package com.example.test_confroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.test_confroid.ui.DataShareBaseActivity;

public class ConfigurationPuller extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int requestId = intent.getIntExtra("requestId", 0);
        if (requestId == DataShareBaseActivity.REQUEST_ID){
            String config = intent.getStringExtra("content");
            String name = intent.getStringExtra("config_name");
            Log.d("received config",config+" "+name);
        }else {
            Log.e("received config", "error");
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}