package com.example.test_confroid.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apg.mobile.roundtextview.RoundTextView;
import com.example.test_confroid.R;
import com.example.test_confroid.services.TokenPuller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends DataShareBaseActivity {

    private RoundTextView tv_notification;
    private Button bt_request_token;
    private Button bt_create_configuration;
    private Button bt_display_configurations;
    private Button bt_pull_configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_notification = findViewById(R.id.tv_notification);
        bt_request_token = findViewById(R.id.bt_request_token);
        bt_create_configuration = findViewById(R.id.bt_create_configuartion);
        bt_display_configurations = findViewById(R.id.bt_display_configurations);
        bt_pull_configuration = findViewById(R.id.bt_pull_configurations);

        Button bt_show_configuartion = findViewById(R.id.bt_show_configuartion);

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        //*****************************************************
        token = prefs.getString("TOKEN", "");
        String configurationsStr = prefs.getString("CONFIGS", "");
        Boolean emptyPref = token.equals("") || configurationsStr.equals("");
        Log.d("prefs ok", String.valueOf(emptyPref));
        //*****************************************************
        if (!emptyPref) {
            Log.d("PREFERENCE","OKEEEEEEEEEEEEY");
            token = prefs.getString("TOKEN", "");

            configurationsMaps = new ArrayList<>();
            configurations = new ArrayList<>();
            configurationsStr = prefs.getString("CONFIGS", "");
            if (!configurationsStr.equals("")) {
                String[] configurationsTab = configurationsStr.split("\\|");
                for (String s : configurationsTab) {
                    java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    HashMap<String, String> map = new Gson().fromJson(s, type);
                    configurationsMaps.add(map);
                    configurations.add(new Gson().toJson(map));
                }
            }
         //********* pull a token *******************
        }else {
            Log.d("no token", "pulling the token");
            TokenPuller.pullToken(this.getApplicationContext());
            Toast.makeText(this, TokenPuller.getToken(), Toast.LENGTH_LONG).show();
            Log.d("token_main", " " + TokenPuller.getToken());
        }
        //*******************************************

        Intent intentAddConfig = getIntent();
        if (intentAddConfig != null && intentAddConfig.getStringExtra("intent_type") != null) {
            if (intentAddConfig.getStringExtra("intent_type").equals(getResources().getString(R.string.add_configuration_result_intent))) {
                tv_notification.setText(intentAddConfig.getStringExtra("AddedConfig"));
                tv_notification.setBgColor(getResources().getColor(R.color.green));
            }
        }

        bt_request_token.setOnClickListener(arg0 -> {
            if (!token.equals("")) {
                tv_notification.setText(getResources().getString(R.string.already_have_token));
                tv_notification.setBgColor(getResources().getColor(R.color.red));
                Button bt_show_token = findViewById(R.id.bt_show_token);
                bt_show_token.setVisibility(View.VISIBLE);
                bt_request_token.setVisibility(View.GONE);
                bt_show_token.setOnClickListener(arg1 -> {
                    Intent intent = new Intent(this, ShowTokenActivity.class);
                    startActivity(intent);
                });
            } else {
                token = TokenPuller.getToken();
                Log.d("button get token", token);

                prefs = getSharedPreferences("prefs", MODE_PRIVATE); // recupération
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("TOKEN", token);
                editor.apply();
                tv_notification.setText(getResources().getString(R.string.token_creation_succes));
                tv_notification.setTextColor(getResources().getColor(R.color.green));
            }
        });

        bt_create_configuration.setOnClickListener(arg0 -> {
            if (token.equals("")) {
                tv_notification.setText(getResources().getString(R.string.dont_have_token_yet));
                tv_notification.setBgColor(getResources().getColor(R.color.red));
            } else {
                Intent intent = new Intent(this, AddConfigurationActivity.class);
                startActivity(intent);
            }
        });

        bt_display_configurations.setOnClickListener(arg0 -> {
            Intent configsList = new Intent(this, ConfigurationsListActivity.class);
            startActivity(configsList);
        });

        bt_show_configuartion.setOnClickListener(arg0 -> {
            if (token.equals("")) {
                tv_notification.setText(getResources().getString(R.string.dont_have_token_yet));
                tv_notification.setBgColor(getResources().getColor(R.color.red));
            } else {
                Intent showActualConfig = new Intent(this, DisplayConfigurationActivity.class);
                startActivity(showActualConfig);
            }
        });

        bt_pull_configuration.setOnClickListener(arg0 -> {
            Intent intent = new Intent("SERVICE_PULLER");
            intent.putExtra("token", token);
            intent.putExtra("name", "com.example.test_confroid.me");
            intent.putExtra("app_name", this.getPackageName());
            intent.putExtra("receiver", "com.example.test_confroid.services.ConfigurationPuller");
            //intent.putExtra("version", 1);
            intent.putExtra("requestId", REQUEST_ID);

            intent.setClassName(confroid, servicePuller);

            ComponentName c = this.startService(intent);
            if (c == null)
                Log.e("faillllll", "failed to start with " + intent);
            else
                Log.d("senddd", "");
        });
    }
}