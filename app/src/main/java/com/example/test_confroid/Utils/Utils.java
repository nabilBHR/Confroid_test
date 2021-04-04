package com.example.test_confroid.Utils;

import com.example.test_confroid.ui.DataShareBaseActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static String getJsonString(Map<String, String> configuration) {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            try {
                if (!entry.getKey().equals("configName")) {
                    if (!entry.getKey().equals("sent")) json.put(entry.getKey(), entry.getValue());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json.toString();
    }

    public static Map<String,String> jsonToMap(String json){
        return new Gson().fromJson(json, HashMap.class);
    }
}