package com.example.test_confroid.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.test_confroid.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AddConfigurationActivity extends DataShareBaseActivity {

    private int line = 0;
    private EditText et_last_key = null;
    private EditText et_last_value = null;
    private EditText et_config_name = null;
    private Button bt_save = null;
    private LinearLayout l_add_config;
    private Map<String, String> addedConfigMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_configuration);

        Button bt_add_value = findViewById(R.id.bt_add_value);
        Button bt_validate = findViewById(R.id.bt_validate);
        l_add_config = findViewById(R.id.l_add_config);

        bt_add_value.setOnClickListener(arg0 -> {
            if (line == 0) {
                addNewLine();
            } else if (!et_last_key.getText().toString().equals("") && !et_last_value.getText().toString().equals("")) {
                addedConfigMap.put(et_last_key.getText().toString(), et_last_value.getText().toString());
                et_last_key.setEnabled(false);
                et_last_value.setEnabled(false);
                addNewLine();
            }
        });

        bt_validate.setOnClickListener(arg0 -> {
            if (!et_last_key.getText().toString().equals("") && !et_last_value.getText().toString().equals("")) {
                addedConfigMap.put(et_last_key.getText().toString(), et_last_value.getText().toString());
                addConfigurationName();
                bt_save.setOnClickListener(arg1 -> {
                    if (!et_config_name.getText().toString().equals("")) {
                        addedConfigMap.put("configName", getApplicationContext().getPackageName() + "." + et_config_name.getText().toString());
                        String configuration = new Gson().toJson(addedConfigMap);
                        configurations.add(configuration);
                        String configListSave = TextUtils.join("|", configurations);

                        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("CONFIGS", configListSave);
                        editor.apply();

                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("AddedConfig", getResources().getString(R.string.added_configuration));
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void addNewLine() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        EditText et_key = new EditText(this);
        EditText et_value = new EditText(this);
        et_last_key = et_key;
        et_last_value = et_value;
        et_key.setHint("Cle");
        et_key.setHintTextColor(getResources().getColor(R.color.green));
        et_value.setHint("Valeur ");
        et_value.setHintTextColor(getResources().getColor(R.color.green));
        et_key.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et_value.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(et_key);
        layout.addView(et_value);
        l_add_config.addView(layout);
        line++;
    }

    private void addConfigurationName() {
        et_last_key.setEnabled(false);
        et_last_value.setEnabled(false);
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        et_config_name = new EditText(this);
        et_config_name.setHint("Nom de la configuration");
        et_config_name.setHintTextColor(getResources().getColor(R.color.green));
        et_config_name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        bt_save = new Button(this);
        bt_save.setText("Enregistrer");
        bt_save.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(et_config_name);
        layout.addView(bt_save);
        l_add_config.addView(layout);
    }
}