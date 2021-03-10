package com.example.test_confroid.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test_confroid.R;
import com.example.test_confroid.Utils.Utils;
import com.google.gson.Gson;

import java.util.Map;

public class ConfigurationsListActivity extends DataShareBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations_list);
        LinearLayout l_configs_list = findViewById(R.id.l_configs_list);

        for (Map<String, String> map : configurationsMaps) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView tv_config_name = new TextView(this);
            tv_config_name.setText(map.get("configName"));
            tv_config_name.setLayoutParams(new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(tv_config_name);

            Button bt_set_tag = new Button(this);
            bt_set_tag.setText("TAG");
            bt_set_tag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(bt_set_tag);

            Button bt_set_current = new Button(this);
            bt_set_current.setText("Definir");
            bt_set_current.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(bt_set_current);

            Button bt_send_conf = new Button(this);
            bt_send_conf.setText("Envoyer");
            bt_send_conf.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(bt_send_conf);

            l_configs_list.addView(layout);

            bt_send_conf.setOnClickListener(arg0 -> {
                if (map.get("sent") == null || map.get("sent").equals("F")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra("intent_type", getResources().getString(R.string.configuration_send_intent_type));
                    sendIntent.putExtra("app_name", getResources().getString(R.string.app_name));
                    updateSentField(map.get("configName"), "T");
                    Bundle config = Utils.convertToBundle(map);
                    config.putString("TOKEN", token);
                    sendIntent.putExtra("CONFIG", config);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivityForResult(shareIntent, SEND_CONFIGURATION_CODE);
                } else {
                    Toast.makeText(this, "Vous avez Déja Envoyé cette configuration ! Elle est déja à jour ! ", Toast.LENGTH_LONG).show();
                }

            });

            bt_set_tag.setOnClickListener(arg1 -> showTagDialog(map.get("configName")));

            bt_set_current.setOnClickListener(arg1 -> {
                actualConfigurationMap = map;
                prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                String configurationSave = new Gson().toJson(actualConfigurationMap);
                editor.putString("actualConfig", configurationSave);
                editor.apply();
                Toast.makeText(this, "Définie comme configuration actuelle !", Toast.LENGTH_LONG).show();

            });
        }
    }

    private void showTagDialog(String configName) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.set_tag_dialog);

        EditText et_tag = (EditText) dialog.findViewById(R.id.et_tag);
        Button bt_save_tag = (Button) dialog.findViewById(R.id.bt_save_tag);
        dialog.show();
        bt_save_tag.setOnClickListener(arg0 -> {
            String tag = et_tag.getText().toString();
            if (!tag.equals("")) {
                addTagToConfiguration(configName, tag);
            }
            dialog.dismiss();
        });
    }

    private void addTagToConfiguration(String configName, String tag) {
        for (Map<String, String> map : configurationsMaps) {
            if (map.get("configName").equals(configName)) {
                int index1 = configurationsMaps.indexOf(map);
                int index2 = configurations.indexOf(new Gson().toJson(map));
                map.put("TAG", tag);
                map.put("sent", "F"); // pour pouvoir l'envoyer de nouveau car elle est modifiée !
                configurationsMaps.set(index1, map);
                configurations.set(index2, new Gson().toJson(map));

                String configListSave = TextUtils.join("|", configurations);
                prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("CONFIGS", configListSave);
                editor.apply();
            }
        }
    }

    // lorsqu'on envoie on met "T" et lorsqu'on modifie on met "F"
    private void updateSentField(String configName, String TorF) {
        for (Map<String, String> map : configurationsMaps) {
            if (map.get("configName").equals(configName)) {
                int index1 = configurationsMaps.indexOf(map);
                int index2 = configurations.indexOf(new Gson().toJson(map));
                map.put("sent", "T");
                configurationsMaps.set(index1, map);
                configurations.set(index2, new Gson().toJson(map));

                String configListSave = TextUtils.join("|", configurations);
                prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("CONFIGS", configListSave);
                editor.apply();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent tokenRequestResultIntent) {
        super.onActivityResult(requestCode, resultCode, tokenRequestResultIntent);

        if (requestCode == SEND_CONFIGURATION_CODE) {
            if (resultCode == RESULT_OK) {
                // supprimer la configuration en local apres envoi ?
                Toast.makeText(this, "Configuration Envoyée ! ", Toast.LENGTH_LONG).show();
            }
        }

    }

}
