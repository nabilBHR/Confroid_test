package com.example.test_confroid.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_confroid.R;
import com.example.test_confroid.adapters.ConfigurationsAdapter;

public class ConfigurationsListActivity extends DataShareBaseActivity {
    private RecyclerView recyclerView;
    private ConfigurationsAdapter confAdapter;
    private TextView ifEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations_list);
        ifEmpty = findViewById(R.id.empty);
        recyclerView = findViewById(R.id.recyclerViewConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        confAdapter = new ConfigurationsAdapter(this);
        recyclerView.setAdapter(confAdapter);
        recyclerView.setLayoutManager(createLM());

        if (confAdapter.getItemCount() != 0) {
            Log.d("onResume", "" + confAdapter.getItemCount());
            ifEmpty.setVisibility(View.INVISIBLE);
        }
    }

    private RecyclerView.LayoutManager createLM() {
        int span = 1;
        int orientation = RecyclerView.VERTICAL;
        return new GridLayoutManager(this, span, orientation, false);
    }

}
