package com.example.test_confroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test_confroid.R;
import com.example.test_confroid.Utils.LocalConfigurationsManager;
import com.example.test_confroid.services.ConfigurationPuller;

import java.util.HashMap;
import java.util.Map;

public class ShowConfigurationActivity extends DataShareBaseActivity {
    private Map<String,String > configuration = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_selected_configuration);

        TextView nameConfig = findViewById(R.id.nameConfig);
        TextView valueConf = findViewById(R.id.valueConf);
        Button bt_default = findViewById(R.id.bt_default);
        Button bt_send = findViewById(R.id.bt_send);
        Button bt_delete = findViewById(R.id.bt_delete);

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("configName") != null) {
            nameConfig.setText(intent.getStringExtra("configName"));
            configuration = configurationsMaps.get(ConfigurationPuller.getIndexByName(intent.getStringExtra("configName")));
            StringBuilder value = new StringBuilder();
            for (Map.Entry<String, String> a : configuration.entrySet()) {
                if (!a.getKey().equals("configName")) {
                    value.append(a).append("\n");
                }
            }
            valueConf.setText(value.toString());
        }

        bt_default.setOnClickListener(view -> {
            LocalConfigurationsManager.setCurrentConfiguration(configuration);
            Toast.makeText(this, "Définie comme configuration actuelle !", Toast.LENGTH_LONG).show();
        });

        bt_send.setOnClickListener(view -> {
            LocalConfigurationsManager.sendConfiguration(configuration, this);
        });

        bt_delete.setOnClickListener(view -> {
            LocalConfigurationsManager.deleteConfiguration(configuration, this);
        });

    }
}