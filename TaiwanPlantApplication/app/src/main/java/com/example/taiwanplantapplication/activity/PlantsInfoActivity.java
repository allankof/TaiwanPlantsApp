package com.example.taiwanplantapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.taiwanplantapplication.R;

public class PlantsInfoActivity extends AppCompatActivity {

    RecyclerView rvPlantInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_info);
    }


}
