package com.ortbraude.foodanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class SingleImageActivity extends AppCompatActivity {

    public void checkClicked(View v){
        finish();
    }

    public void closeClicked(View v){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
    }
}