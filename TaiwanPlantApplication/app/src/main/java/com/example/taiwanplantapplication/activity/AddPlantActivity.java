package com.example.taiwanplantapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.taiwanplantapplication.R;

public class AddPlantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        Toolbar toolbar=findViewById(R.id.add_toolbar_id);

        setSupportActionBar(toolbar);

    }
}
