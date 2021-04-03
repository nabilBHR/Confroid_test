package com.example.test_confroid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_confroid.R;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import static com.example.test_confroid.Utils.Utils.getJsonString;
import static com.example.test_confroid.ui.DataShareBaseActivity.*;

public class ConfigurationsAdapter extends RecyclerView.Adapter<ConfigurationsAdapter.ViewHolder> {
    private Activity activity;
    private ConfigsList cl = new ConfigsList();
    private Context context;
    private List<Map<String, String>> confsMaps;
    private Map<String, String> currentConf;
    private AlertDialog.Builder builder;

    public ConfigurationsAdapter(Activity activity, List<Map<String, String>> configs, Map<String, String> currentConf) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.confsMaps = configs;
        this.currentConf = currentConf;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.config_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.update(confsMaps.get(i));
    }

    @Override
    public int getItemCount() {
        return confsMaps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView configurationTitle;
        private Button butTag;
        private Button butDefault;
        private Button butSend;
        private Button butView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            configurationTitle = itemView.findViewById(R.id.configurationTitle);
            butTag = itemView.findViewById(R.id.butTag);
            butDefault = itemView.findViewById(R.id.butDefault);
            butSend = itemView.findViewById(R.id.butSend);
            butView = itemView.findViewById(R.id.butView);
        }

        public void update(Map<String, String> conf) {
            String[] nameConf = conf.get("configName").split("\\.");
            String name = (nameConf[nameConf.length - 1]);
            configurationTitle.setText(name);
            butTag.setOnClickListener(view -> showTagDialog(conf.get("configName")));
            butDefault.setOnClickListener(view -> {
                cl.setCurrent(conf);
                Toast.makeText(activity, "Définie comme configuration actuelle !", Toast.LENGTH_LONG).show();
            });
            butSend.setOnClickListener(view -> {
                if (conf.get("TAG") == null || !conf.get("sent").equals("T")) {
                    Bundle config = new Bundle();
                    config.putString("JSON", getJsonString(conf));
                    config.putString("TOKEN", DataShareBaseActivity.token);
                    config.putString("APP", activity.getPackageName());
                    config.putString("CONF_NAME", conf.get("configName"));
                    //*************************************************
                    Intent sendIntent = new Intent("SERVICE_PUSHER");
                    sendIntent.setClassName(DataShareBaseActivity.confroid, DataShareBaseActivity.servicePusher);
                    sendIntent.putExtra("CONFIG", config);
                    ComponentName c = activity.startService(sendIntent); //start service

                    if (c == null) {
                        Log.e("failll", "failed to start with " + sendIntent);
                    }
                    else{
                        updateSentField(conf.get("configName"), "T");
                        Log.d("senddd", config.toString());
                    }
                } else {
                    Toast.makeText(activity, "Vous avez Déja Envoyé cette configuration ! Elle est déja à jour ! ", Toast.LENGTH_LONG).show();
                }
            });
            butView.setOnClickListener(view -> editTaskDialog(conf));
        }
    }

    private void editTaskDialog(Map<String, String> conf) {
        builder = new AlertDialog.Builder(activity);
        builder.setTitle("Modifier configuration");
        builder.setCancelable(false);
        LayoutInflater customLayout = LayoutInflater.from(activity);
        View customLayoutS = customLayout.inflate(R.layout.show_config, null);
        builder.setView(customLayoutS);

        final EditText nameConf = customLayoutS.findViewById(R.id.nameConfig);
        final EditText valueConf = customLayoutS.findViewById(R.id.valueConf);

        if (conf != null) {
            nameConf.setText(String.valueOf(conf.get("configName")));
            StringBuilder value = new StringBuilder();
            for (Map.Entry<String, String> a: conf.entrySet()) {
                if(!a.getKey().equals("configName")){
                    value.append(a).append("\n");}
            }
            valueConf.setText(value.toString());
        }
        builder.setNegativeButton("ANNULER", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showTagDialog(String configName) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.set_tag_dialog);

        EditText et_tag = (EditText) dialog.findViewById(R.id.et_tag);
        Button bt_save_tag = (Button) dialog.findViewById(R.id.bt_save_tag);
        dialog.show();
        bt_save_tag.setOnClickListener(arg0 -> {
            String tag = et_tag.getText().toString();
            if (!tag.equals("")) {
                addTagToConfiguration(configName, tag);
                Log.d("TAGgg", "CN : "+configName+"/TAG : "+tag);
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
                map.put("sent", "F");
                configurationsMaps.set(index1, map);
                configurations.set(index2, new Gson().toJson(map));
                confsMaps = configurationsMaps;
                Log.d("TAGgg", ""+configurationsMaps);
                Log.d("TAGgg", ""+confsMaps);
                String configListSave = TextUtils.join("|", configurations);
                if (prefs==null) prefs = activity.getSharedPreferences("prefs", MODE_PRIVATE);
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
                map.put("sent", TorF);
                configurationsMaps.set(index1, map);
                configurations.set(index2, new Gson().toJson(map));
                String configListSave = TextUtils.join("|", configurations);
                if (prefs==null) prefs = activity.getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("CONFIGS", configListSave);
                editor.apply();
            }
        }
    }

}
