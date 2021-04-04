package com.example.test_confroid.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataShareBaseActivity extends AppCompatActivity {

    public final static String confroid = "com.example.confroid_project";
    public final static String servicePusher = "com.example.confroid_project.services.ConfigurationPusher";
    public final static String servicePuller = "com.example.confroid_project.services.ConfigurationPuller";

    public static final int REQUEST_ID = 123;
    public final static int SEND_CONFIGURATION_CODE = 124;
    public final static String notification = "";
    public static String token;
    public static SharedPreferences prefs;
    public static Map<String, String> actualConfigurationMap = new HashMap<>();
    public static List<String> configurations = new ArrayList<>();
    public static List<Map<String, String>> configurationsMaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}