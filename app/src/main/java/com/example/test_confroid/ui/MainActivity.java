package com.example.test_confroid.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.test_confroid.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends DataShareBaseActivity {

    private TextView tv_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_notification = findViewById(R.id.tv_notification);
        Button bt_request_token = findViewById(R.id.bt_request_token);
        Button bt_create_configuration = findViewById(R.id.bt_create_configuartion);
        Button bt_display_configurations = findViewById(R.id.bt_display_configurations);

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        if (prefs != null) {
            token = prefs.getString("TOKEN", "");

            configurationsMaps = new ArrayList<>();
            configurations = new ArrayList<>();
            String configurationsStr = prefs.getString("CONFIGS", "");
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
        }

        Intent intentAddConfig = getIntent();
        if (intentAddConfig != null) {
            tv_notification.setText(intentAddConfig.getStringExtra("AddedConfig"));
            tv_notification.setTextColor(getResources().getColor(R.color.green));
        }

        bt_request_token.setOnClickListener(arg0 -> {
            if (!token.equals("")) {
                tv_notification.setText(getResources().getString(R.string.already_have_token));
                tv_notification.setTextColor(getResources().getColor(R.color.red));
                Button bt_show_token = findViewById(R.id.bt_show_token);
                bt_show_token.setVisibility(View.VISIBLE);
                bt_request_token.setVisibility(View.GONE);
                bt_show_token.setOnClickListener(arg1 -> {
                    tv_notification.setText(getResources().getString(R.string.show_token_message) + " " + token);
                    tv_notification.setTextColor(getResources().getColor(R.color.green));
                });
            } else {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra("intent_type", getResources().getString(R.string.token_request_intent_type));
                sendIntent.putExtra("app_name", getResources().getString(R.string.app_name));
                sendIntent.putExtra("app_package_name", getApplicationContext().getPackageName());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivityForResult(shareIntent, REQUEST_TOKEN_CODE);
            }
        });

        bt_create_configuration.setOnClickListener(arg0 -> {
            if (token.equals("")) {
                tv_notification.setText(getResources().getString(R.string.dont_have_token_yet));
                tv_notification.setTextColor(getResources().getColor(R.color.red));
            } else {
                Intent intent = new Intent(this, AddConfigurationActivity.class);
                startActivity(intent);
            }
        });

        bt_display_configurations.setOnClickListener(arg0 -> {
            Intent configsList = new Intent(this, ConfigurationsListActivity.class);
            startActivity(configsList);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent tokenRequestResultIntent) {
        super.onActivityResult(requestCode, resultCode, tokenRequestResultIntent);
        if (requestCode == REQUEST_TOKEN_CODE) {
            // On vérifie aussi que l'opération s'est bien déroulée et qu'on a bien recu le token
            if (resultCode == RESULT_OK) {
                // on sauvegarde le token dans les shared preferences de l'application
                token = tokenRequestResultIntent.getStringExtra("TOKEN");
                prefs = getSharedPreferences("prefs", MODE_PRIVATE); // recupération
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("TOKEN", token);
                editor.apply();
                tv_notification.setText(getResources().getString(R.string.token_creation_succes));
                tv_notification.setTextColor(getResources().getColor(R.color.green));
                //AS
                //Toast.makeText(this, "Mon Token est " + tokenRequestResultIntent.getStringExtra("TOKEN"), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == SEND_CONFIGURATION_CODE) {
            if (resultCode == RESULT_OK) {
                tv_notification.setText(getResources().getString(R.string.configuration_sent_succes));
                tv_notification.setTextColor(getResources().getColor(R.color.green));
            }
        }

    }
}