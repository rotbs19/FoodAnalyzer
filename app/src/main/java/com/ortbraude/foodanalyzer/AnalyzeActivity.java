package com.ortbraude.foodanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AnalyzeActivity extends AppCompatActivity {

    private TextView main_label;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        String chosen_meal = getIntent().getStringExtra("LABEL");
        main_label = (TextView) findViewById(R.id.main_label);
        main_label.setText(chosen_meal);
    }
}