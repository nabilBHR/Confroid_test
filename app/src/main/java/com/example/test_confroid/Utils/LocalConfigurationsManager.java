package com.example.test_confroid.Utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.test_confroid.ui.DataShareBaseActivity;
import com.example.test_confroid.ui.MainActivity;
import com.google.gson.Gson;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.test_confroid.Utils.Utils.getJsonString;
import static com.example.test_confroid.ui.DataShareBaseActivity.actualConfigurationMap;
import static com.example.test_confroid.ui.DataShareBaseActivity.configurations;
import static com.example.test_confroid.ui.DataShareBaseActivity.configurationsMaps;
import static com.example.test_confroid.ui.DataShareBaseActivity.prefs;

public class LocalConfigurationsManager {

    // lorsqu'on envoie on met "T" et lorsqu'on modifie on met "F"
    public static void updateSentField(String configName, String TorF, Activity activity) {
        for (Map<String, String> map : configurationsMaps) {
            if (map.get("configName").equals(configName)) {
                int index1 = configurationsMaps.indexOf(map);
                int index2 = configurations.indexOf(new Gson().toJson(map));
                map.put("sent", TorF);
                configurationsMaps.set(index1, map);
                configurations.set(index2, new Gson().toJson(map));
                String configListSave = TextUtils.join("|", configurations);
                if (prefs == null) prefs = activity.getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("CONFIGS", configListSave);
                editor.apply();
            }
        }
    }

    public static void sendConfiguration(Map<String, String> conf, Activity activity) {
        if (conf.get("sent") == null || !conf.get("sent").equals("T")) {
            Bundle config = new Bundle();
            config.putString("JSON", getJsonString(conf));
            config.putString("TOKEN", DataShareBaseActivity.token);
            config.putString("APP", activity.getPackageName());
            config.putString("CONF_NAME", conf.get("configName"));
            //*************************************************
            Intent sendIntent = new Intent("SERVICE_PUSHER");
            sendIntent.setClassName(DataShareBaseActivity.confroid, DataShareBaseActivity.servicePusher);
            sendIntent.putExtra("CONFIG", config);
            ComponentName c = activity.startService(sendIntent); //start service

            if (c == null) {
                Log.e("fail", "failed to start with " + sendIntent);
                Toast.makeText(activity, "Le service d'envoi est arrêté par Android ! Veuillez réessayer ", Toast.LENGTH_LONG).show();
            } else {
                updateSentField(conf.get("configName"), "T", activity);
                MainActivity.notif = "Configuration envoyée !";
                MainActivity.notifColor = "green";
                Intent backToMain = new Intent(activity, MainActivity.class);
                activity.startActivity(backToMain);
            }
        } else {
            Toast.makeText(activity, "Vous avez Déja Envoyé cette configuration ! Elle est déja à jour ! ", Toast.LENGTH_LONG).show();
        }
    }

    public static void deleteConfiguration(Map<String, String> conf, Activity activity) {
        String configuration = new Gson().toJson(conf);
        configurations.remove(configuration);
        String configListSave = TextUtils.join("|", configurations);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CONFIGS", configListSave);
        editor.apply();
        MainActivity.init();
        MainActivity.notif = "Configuration supprimée !";
        MainActivity.notifColor = "red";
        Intent backToMain = new Intent(activity, MainActivity.class);
        activity.startActivity(backToMain);
    }

    public static void setCurrentConfiguration(Map<String, String> map) {
        actualConfigurationMap = map;
        SharedPreferences.Editor editor = prefs.edit();
        String configurationSave = new Gson().toJson(actualConfigurationMap);
        editor.putString("actualConfig", configurationSave);
        editor.apply();
    }
}
