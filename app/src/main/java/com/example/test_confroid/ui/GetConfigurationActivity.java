package com.example.test_confroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.apg.mobile.roundtextview.RoundTextView;
import com.example.test_confroid.R;

public class GetConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_configuration);

        RoundTextView tv_error = findViewById(R.id.tv_error);
        EditText et_conf_name = findViewById(R.id.et_conf_name);
        EditText et_conf_version = findViewById(R.id.et_conf_version);
        Button bt_get = findViewById(R.id.bt_get);

        bt_get.setOnClickListener(arg0 -> {
            if (!et_conf_name.getText().toString().equals("")) {
                if (et_conf_version.getText().toString().equals("")) {
                    // si l'utilisateur n'a pas saisi de version on récupère la derniere !

                }
                // recuperer la configuration ici
                // apres la recuperation il faut l'ajouter a la liste des configuration

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                tv_error.setText(getResources().getString(R.string.empty_config_name));
                tv_error.setBgColor(getResources().getColor(R.color.red));
            }
        });

    }
}