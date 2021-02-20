package com.example.test_confroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button bt_request_token;
    private final int REQUEST_TOKEN_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_request_token = (Button) findViewById(R.id.bt_request_token);
        bt_request_token.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello from test App");
                sendIntent.putExtra("intent_type", "first_intent_request_token");
                sendIntent.putExtra("app_name", "test_confroid");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                //startActivity(shareIntent);
                startActivityForResult(shareIntent, REQUEST_TOKEN_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent tokenRequestResultIntent) {
        // On vérifie tout d'abord à quel intent on fait référence ici à l'aide de notre identifiant
        if (requestCode == REQUEST_TOKEN_CODE) {
            // On vérifie aussi que l'opération s'est bien déroulée
            if (resultCode == RESULT_OK) {
                // on sauvegarde le token dans les shared preferences de l'application
                Toast.makeText(this, "Mon Token est " + tokenRequestResultIntent.getStringExtra("TOKEN"), Toast.LENGTH_LONG).show();
            }
        }
    }
}