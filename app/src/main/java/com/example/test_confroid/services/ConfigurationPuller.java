package com.example.test_confroid.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.example.test_confroid.R;
import com.example.test_confroid.Utils.Utils;
import com.example.test_confroid.ui.DataShareBaseActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import static com.example.test_confroid.ui.DataShareBaseActivity.REQUEST_ID;
import static com.example.test_confroid.ui.DataShareBaseActivity.configurations;
import static com.example.test_confroid.ui.DataShareBaseActivity.configurationsMaps;
import static com.example.test_confroid.ui.DataShareBaseActivity.prefs;
import static com.example.test_confroid.ui.MainActivity.notif;
import static com.example.test_confroid.ui.MainActivity.notifColor;

public class ConfigurationPuller extends Service {

    private static boolean isRunning;

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        int requestId = intent.getIntExtra("requestId", 0);
        if (requestId == REQUEST_ID) {
            String config = intent.getStringExtra("content");
            String name = intent.getStringExtra("config_name");

            if (config == null || name == null) {
                Log.d("error_conf", "configuration inexistante !");
                notif = getResources().getString(R.string.empty_data);
                notifColor = "red";
            } else {
                Map<String, String> addedConfigMap = Utils.jsonToMap(config);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    addedConfigMap.putIfAbsent("configName", name);
                } else {
                    addedConfigMap.put("configName", name);
                }
                Log.d("configuration pull", "successfully pulled");

                if (isConfigutationInLocalStorage(addedConfigMap)) {
                    replaceLocalMap(addedConfigMap);
                    configurations = new ArrayList<>();
                    String convertedConf = "";
                    for (Map<String, String> map : configurationsMaps) {
                        convertedConf = new Gson().toJson(map);
                        configurations.add(convertedConf);
                    }
                    notif = getResources().getString(R.string.configuration_pull_succes2);
                    notifColor = "green";
                } else {
                    String newPulledConf = new Gson().toJson(addedConfigMap);
                    configurations.add(newPulledConf);
                    notif = getResources().getString(R.string.configuration_pull_succes);
                    notifColor = "green";
                }

                String configListSave = TextUtils.join("|", configurations);
                prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("CONFIGS", configListSave);
                editor.apply();

            }
        } else {
            Log.e("received config", "error");
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        isRunning = false;
    }

    public static int getIndexByName(String name) {
        for (Map<String, String> localMap : DataShareBaseActivity.configurationsMaps) {
            if (localMap.get("configName").equals(name)) {
                return configurationsMaps.indexOf(localMap);
            }
        }
        return -1;
    }

    private boolean isConfigutationInLocalStorage(Map<String, String> map) {
        for (Map<String, String> localMap : DataShareBaseActivity.configurationsMaps) {
            if (localMap.get("configName").equals(map.get("configName"))) {
                return true;
            }
        }
        return false;
    }

    private void replaceLocalMap(Map<String, String> map) {
        configurationsMaps.remove(getIndexByName(map.get("configName")));
        configurationsMaps.add(map);
    }

}