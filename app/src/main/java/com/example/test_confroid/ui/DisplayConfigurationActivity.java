package com.example.test_confroid.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        mainlayout.setGravity(Gravity.CENTER);
        mainlayout.setOrientation(LinearLayout.VERTICAL);

        String actuakConfiguration = prefs.getString("actualConfig", "");
        if (!actuakConfiguration.equals("")) {
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            actualConfigurationMap = new Gson().fromJson(actuakConfiguration, type);
        }
        if (actualConfigurationMap != null && actualConfigurationMap.size() > 0) {

            LinearLayout headerLayout = new LinearLayout(this);
            headerLayout.setOrientation(LinearLayout.VERTICAL);
            headerLayout.setLayoutParams(params);

            TextView k = new TextView(this);
            k.setText("Nom de la configuration");
            k.setLayoutParams(params);
            k.setBackgroundColor(Color.parseColor("#0099cc"));
            k.setTextColor(Color.WHITE);
            k.setTextSize(30);
            headerLayout.addView(k);

            TextView v = new TextView(this);
            v.setText(actualConfigurationMap.get("configName").substring(actualConfigurationMap.get("configName").lastIndexOf(".") + 1));
            v.setLayoutParams(params);
            v.setTextSize(20);
            v.setPadding(0, 0, 0, 80);
            v.setTypeface(null, Typeface.BOLD);
            headerLayout.addView(v);

            TextView k2 = new TextView(this);
            k2.setText("Contenu");
            k2.setLayoutParams(params);
            k2.setBackgroundColor(Color.parseColor("#0099cc"));
            k2.setTextColor(Color.WHITE);
            k2.setTextSize(30);
            headerLayout.addView(k2);

            mainlayout.addView(headerLayout);

            for (Map.Entry<String, String> entry : actualConfigurationMap.entrySet()) {
                if (!entry.getKey().equals("configName")) {
                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    layout.setLayoutParams(params);

                    TextView key = new TextView(this);
                    key.setText(entry.getKey() + " : ");
                    key.setLayoutParams(params);
                    key.setTextSize(20);
                    key.setTypeface(null, Typeface.BOLD);
                    layout.addView(key);

                    TextView val = new TextView(this);
                    val.setText(entry.getValue());
                    val.setLayoutParams(params);
                    val.setTextSize(20);
                    val.setTypeface(null, Typeface.BOLD);
                    layout.addView(val);

                    mainlayout.addView(layout);
                }
            }
        } else {
            TextView empty = new TextView(this);
            empty.setText("Vous n'avez pas encore défini de configuration par defaut !");
            empty.setLayoutParams(params);
            mainlayout.addView(empty);
        }

        setContentView(mainlayout);

    }

}
