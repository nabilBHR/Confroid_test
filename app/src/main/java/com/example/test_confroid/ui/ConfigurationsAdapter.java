package com.example.test_confroid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

import java.util.List;
import java.util.Map;

public class ConfigurationsAdapter extends RecyclerView.Adapter<ConfigurationsAdapter.ViewHolder> {
    private Activity activity;
    private ConfigsList cl = new ConfigsList();
    private Context context;
    private List<Map<String, String>> configurationsMaps;
    private Map<String, String> currentConf;
    private AlertDialog.Builder builder;

    public ConfigurationsAdapter(Activity activity, List<Map<String, String>> configs, Map<String, String> currentConf) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.configurationsMaps = configs;
        this.currentConf = currentConf;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.config_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.update(configurationsMaps.get(i));
    }

    @Override
    public int getItemCount() {
        return configurationsMaps.size();
    }

    public void showTagDialog(String configName) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.set_tag_dialog);

        EditText et_tag = (EditText) dialog.findViewById(R.id.et_tag);
        Button bt_save_tag = (Button) dialog.findViewById(R.id.bt_save_tag);
        dialog.show();
        bt_save_tag.setOnClickListener(arg0 -> {
            String tag = et_tag.getText().toString();
            if (!tag.equals("")) {
                cl.addTagToConfiguration(configName, tag);
            }
            dialog.dismiss();
        });
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
            butTag.setOnClickListener(view -> showTagDialog(name));
            butDefault.setOnClickListener(view -> cl.setCurrent(conf));
            butSend.setOnClickListener(view -> cl.sendConfig(conf));
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
            valueConf.setText(String.valueOf(conf.get("value")));
        }

        builder.setPositiveButton("VALIDER", (dialog, id) -> {
            Toast.makeText(context, "Modification validée.", Toast.LENGTH_SHORT).show();});
        builder.setNegativeButton("ANNULER", (dialog, id) -> {
            Toast.makeText(context, "Modification annulée !", Toast.LENGTH_SHORT).show();
            dialog.cancel();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
