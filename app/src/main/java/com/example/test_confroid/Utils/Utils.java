package com.example.test_confroid.Utils;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Utils {

    public static Bundle convertToBundle(Map<String, String> configuration) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }
        return bundle;
    }

    public static String convertToJsonString(Map<String, String> configuration) {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            try {
                json.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return json.toString();
    }
}