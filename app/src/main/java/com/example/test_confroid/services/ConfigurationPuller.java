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
            String config = intent.getStringExtra("config");
            Log.d("received config",config);
        }else {
            Log.e("received config", "error");
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}