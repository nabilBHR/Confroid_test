package com.example.test_confroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final int REQUEST_TOKEN_CODE = 123;
    private TextView tv_notification;
    private String token;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_notification = findViewById(R.id.tv_notification);
        Button bt_request_token = findViewById(R.id.bt_request_token);
        Button bt_create_configuration = findViewById(R.id.bt_create_configuartion);

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        if (prefs != null) {
            token = prefs.getString("TOKEN", "");
        }

        Intent intentAddConfig = getIntent();
        if (intentAddConfig != null) {
            tv_notification.setText(intentAddConfig.getStringExtra("AddedConfig"));
            tv_notification.setTextColor(getResources().getColor(R.color.green));
        }

        bt_request_token.setOnClickListener(arg0 -> {

            // pour le test
            //token = "";
            // A enlever !!!!!!!!!!!!!!!!!!!!!!!!!

            if (!token.equals("")) {
                tv_notification.setText(getResources().getString(R.string.already_have_token));
                tv_notification.setTextColor(getResources().getColor(R.color.red));
            } else {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra("intent_type", getResources().getString(R.string.token_request_intent_type));
                sendIntent.putExtra("app_name", getResources().getString(R.string.app_name));
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivityForResult(shareIntent, REQUEST_TOKEN_CODE);
            }
        });

        bt_create_configuration.setOnClickListener(arg0 -> {
            Intent intent = new Intent(this, AddConfigurationActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent tokenRequestResultIntent) {
        if (requestCode == REQUEST_TOKEN_CODE) {
            // On vérifie aussi que l'opération s'est bien déroulée et qu'on a bien recu le token
            if (resultCode == RESULT_OK) {
                // on sauvegarde le token dans les shared preferences de l'application
                prefs = getSharedPreferences("prefs", MODE_PRIVATE); // recupération
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("TOKEN", tokenRequestResultIntent.getStringExtra("TOKEN"));
                editor.apply();
                tv_notification.setText(getResources().getString(R.string.token_creation_succes));
                tv_notification.setTextColor(getResources().getColor(R.color.green));

                Toast.makeText(this, "Mon Token est " + tokenRequestResultIntent.getStringExtra("TOKEN"), Toast.LENGTH_LONG).show();
            }
        }
    }
}