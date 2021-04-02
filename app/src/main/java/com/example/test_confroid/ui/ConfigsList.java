package com.example.test_confroid.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_confroid.R;
import com.example.test_confroid.Utils.Utils;
import com.google.gson.Gson;

import java.util.Map;

public class ConfigsList  extends DataShareBaseActivity {
    private RecyclerView recyclerView;
    private ConfigurationsAdapter confAdapter;
    //private TextView ifEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations_list);

        //ifEmpty = findViewById(R.id.vide);
        recyclerView = findViewById(R.id.recyclerViewConfig);

    }

    @Override
    protected void onResume() {
        super.onResume();
        confAdapter = new ConfigurationsAdapter(this,configurationsMaps,actualConfigurationMap);
        recyclerView.setAdapter(confAdapter);
        recyclerView.setLayoutManager(createLM());

        if (confAdapter.getItemCount() != 0) {
            Log.d("onResume", "0 element");
            //ifEmpty.setVisibility(View.INVISIBLE);
        }
    }

    private RecyclerView.LayoutManager createLM() {
        int span = 1;
        int orientation = RecyclerView.VERTICAL;
        return new GridLayoutManager(this, span, orientation, false);
    }

    public void setCurrent(Map<String, String> map){
        actualConfigurationMap = map;
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String configurationSave = new Gson().toJson(actualConfigurationMap);
        editor.putString("actualConfig", configurationSave);
        editor.apply();
        Toast.makeText(this, "Définie comme configuration actuelle !", Toast.LENGTH_LONG).show();
    }

    public void sendConfig(Map<String, String> map){
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

    void addTagToConfiguration(String configName, String tag) {
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
}
