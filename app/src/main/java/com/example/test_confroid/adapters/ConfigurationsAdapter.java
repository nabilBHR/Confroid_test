package com.example.test_confroid.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_confroid.R;
import com.example.test_confroid.ui.ShowConfigurationActivity;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import static com.example.test_confroid.ui.DataShareBaseActivity.MODE_PRIVATE;
import static com.example.test_confroid.ui.DataShareBaseActivity.configurations;
import static com.example.test_confroid.ui.DataShareBaseActivity.configurationsMaps;
import static com.example.test_confroid.ui.DataShareBaseActivity.prefs;

public class ConfigurationsAdapter extends RecyclerView.Adapter<ConfigurationsAdapter.ViewHolder> {
    private Activity activity;
    private List<Map<String, String>> confsMaps;
    private AlertDialog.Builder builder;
    private ConfigurationsAdapter adpt;

    public ConfigurationsAdapter(Activity activity) {
        this.activity = activity;
        this.confsMaps = configurationsMaps;
        adpt = this;
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

    private void showTagDialog(String configName) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.set_tag_dialog);

        EditText et_tag = dialog.findViewById(R.id.et_tag);
        Button bt_save_tag = dialog.findViewById(R.id.bt_save_tag);
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
                map.put("sent", "F");
                configurationsMaps.set(index1, map);
                configurations.set(index2, new Gson().toJson(map));
                confsMaps = configurationsMaps;
                Log.d("TAGgg", "" + configurationsMaps);
                Log.d("TAGgg", "" + confsMaps);
                String configListSave = TextUtils.join("|", configurations);
                if (prefs == null) prefs = activity.getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("CONFIGS", configListSave);
                editor.apply();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView configurationTitle;
        private Button butTag;
        private Button butView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            configurationTitle = itemView.findViewById(R.id.configurationTitle);
            butTag = itemView.findViewById(R.id.butTag);
            butView = itemView.findViewById(R.id.butView);
        }

        public void update(Map<String, String> conf) {
            String[] nameConf = conf.get("configName").split("\\.");
            String name = (nameConf[nameConf.length - 1]);
            configurationTitle.setText("nom : " + name);
            butTag.setOnClickListener(view -> showTagDialog(conf.get("configName")));

            butView.setOnClickListener(view -> {
                Intent intent = new Intent(activity, ShowConfigurationActivity.class);
                intent.putExtra("configName", conf.get("configName"));
                activity.startActivity(intent);
            });
        }
    }

}
