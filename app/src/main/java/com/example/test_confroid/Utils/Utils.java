package com.example.test_confroid.Utils;

import android.os.Bundle;

import java.util.Map;

public class Utils {

    public static Bundle convertToBundle(Map<String, String> configuration) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }
        return bundle;
    }
}