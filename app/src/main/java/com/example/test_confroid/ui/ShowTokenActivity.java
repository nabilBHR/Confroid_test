package com.example.test_confroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.test_confroid.R;

public class ShowTokenActivity extends DataShareBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_token);

        TextView tv_token = findViewById(R.id.tv_token);
        tv_token.setText(token);
        Button bt_back_main = findViewById(R.id.bt_back_main);
        bt_back_main.setOnClickListener(arg0 -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}