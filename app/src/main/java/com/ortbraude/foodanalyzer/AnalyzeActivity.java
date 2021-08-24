package com.ortbraude.foodanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

public class AnalyzeActivity extends AppCompatActivity {

    private TextView main_label;
    private ImageProcessing imageProcessing;
    private ImageHandlerSingleton singleton;
    private String TAG = "AnalyzeActivity";
    private String dishName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        this.dishName = getIntent().getStringExtra("LABEL");
        main_label = (TextView) findViewById(R.id.main_label);
        main_label.setText(this.dishName);
        imageProcessing = new ImageProcessing(80,32);
        singleton = ImageHandlerSingleton.getInstance();
    }

    public void analyzeDish() {







        try {
            imageProcessing.compareNew(singleton.newAlbum.get(0),this.dishName);
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
    }





}