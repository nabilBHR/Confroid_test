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
    protected final static int SEND_CONFIGURATION_CODE = 124;
    protected static String token;
    protected static SharedPreferences prefs;
    protected static Map<String, String> actualConfigurationMap = new HashMap<>();
    protected static List<String> configurations = new ArrayList<>();
    protected static List<Map<String, String>> configurationsMaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}