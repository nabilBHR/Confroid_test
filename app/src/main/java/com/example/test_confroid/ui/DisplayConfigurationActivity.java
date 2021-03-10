package com.example.test_confroid.ui;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class DisplayConfigurationActivity extends DataShareBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout mainlayout = new LinearLayout(this);
        mainlayout.setOrientation(LinearLayout.VERTICAL);

        String actuakConfiguration = prefs.getString("actualConfig", "");
        if (!actuakConfiguration.equals("")) {
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            actualConfigurationMap = new Gson().fromJson(actuakConfiguration, type);
        }
        if (actualConfigurationMap != null && actualConfigurationMap.size() > 0) {

            for (Map.Entry<String, String> entry : actualConfigurationMap.entrySet()) {
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TextView key = new TextView(this);
                key.setText(entry.getKey() + " : ");
                key.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(key);

                TextView val = new TextView(this);
                val.setText(entry.getValue());
                val.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(val);

                mainlayout.addView(layout);
            }
        } else {
            TextView empty = new TextView(this);
            empty.setText("Vous n'avez pas encore défini de configuration par defaut !");
            empty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mainlayout.addView(empty);
        }

        setContentView(mainlayout);

    }

}
