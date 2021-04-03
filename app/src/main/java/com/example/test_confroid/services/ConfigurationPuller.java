package com.example.test_confroid.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.example.test_confroid.Utils.Utils;
import com.google.gson.Gson;

import java.util.Map;

import static com.example.test_confroid.ui.DataShareBaseActivity.REQUEST_ID;
import static com.example.test_confroid.ui.DataShareBaseActivity.configurations;
import static com.example.test_confroid.ui.DataShareBaseActivity.prefs;

public class ConfigurationPuller extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int requestId = intent.getIntExtra("requestId", 0);
        if (requestId == REQUEST_ID){
            String config = intent.getStringExtra("content");
            String name = intent.getStringExtra("config_name");

            if (config==null || name==null){
//                Intent nullIntent = new Intent(this, GetConfigurationActivity.class);
//                nullIntent.putExtra("null_data", getResources().getString(R.string.empty_data));
//                startActivity(nullIntent);


            }else{
                Map<String,String> addedConfigMap = Utils.jsonToMap(config);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    addedConfigMap.putIfAbsent("configName",name);
                }else{
                    addedConfigMap.put("configName",name);
                }
                String configuration = new Gson().toJson(addedConfigMap);
                configurations.add(configuration);
                String configListSave = TextUtils.join("|", configurations);
                prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("CONFIGS", configListSave);
                editor.apply();
            }
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