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
    private TextView tv_notfication;
    private String token;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_request_token = findViewById(R.id.bt_request_token);
        tv_notfication = findViewById(R.id.tv_notification);

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        if (prefs != null) {
            token = prefs.getString("TOKEN", "");
        }

        bt_request_token.setOnClickListener(arg0 -> {

            // pour le test
            // token = "";
            // A enlever !!!!!!!!!!!!!!!!!!!!!!!!!

            if (!token.equals("")) {
                tv_notfication.setText("Vous avez déja un token !");
                tv_notfication.setTextColor(getResources().getColor(R.color.red));
            } else {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello from test App");
                sendIntent.putExtra("intent_type", "first_intent_request_token");
                sendIntent.putExtra("app_name", "test_confroid");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivityForResult(shareIntent, REQUEST_TOKEN_CODE);
            }
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
                tv_notfication.setText("Un token a été créé pour cette application !");
                tv_notfication.setTextColor(getResources().getColor(R.color.green));

                Toast.makeText(this, "Mon Token est " + tokenRequestResultIntent.getStringExtra("TOKEN"), Toast.LENGTH_LONG).show();
            }
        }
    }
}