package com.example.test_confroid.Utils;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Utils {


    public static String getJsonString(Map<String, String> configuration) {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            try {
                if (!entry.getKey().equals("configName"))
                    json.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        return json.toString();
    }
}