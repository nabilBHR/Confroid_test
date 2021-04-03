package com.example.test_confroid.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.apg.mobile.roundtextview.RoundTextView;
import com.example.test_confroid.R;

public class GetConfigurationActivity extends DataShareBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_configuration);
        RoundTextView tv_error = findViewById(R.id.tv_error);
        EditText et_conf_name = findViewById(R.id.et_conf_name);
        EditText et_conf_version = findViewById(R.id.et_conf_version);
        Button bt_get = findViewById(R.id.bt_get);

        bt_get.setOnClickListener(arg0 -> {
            Log.d("iffff", "pass");
            if (!et_conf_name.getText().toString().equals("")) {


                Intent intent = new Intent("SERVICE_PULLER");
                intent.putExtra("token", token);
                intent.putExtra("name", getPackageName()+"."+et_conf_name.getText().toString());
                intent.putExtra("app_name", getPackageName());
                intent.putExtra("receiver", "com.example.test_confroid.services.ConfigurationPuller");
                intent.putExtra("requestId", REQUEST_ID);
                Log.d("iffff", "pass2");

                Log.d("iffff", et_conf_version.getText().toString());

                if (!et_conf_version.getText().toString().equals("")) {
                    Log.d("iffff", "pass3");
                    intent.putExtra("version", Integer.parseInt(et_conf_version.getText().toString()));
                    Log.d("iffff", "pass4");
                }

                intent.setClassName(confroid, servicePuller);
                ComponentName c = this.startService(intent);
                if (c == null)
                    Log.e("faillllll", "failed to start with " + intent);
                else
                    Log.d("senddd", "");
            } else {
                tv_error.setText(getResources().getString(R.string.empty_config_name));
                tv_error.setBgColor(getResources().getColor(R.color.red));
            }
        });

    }
}