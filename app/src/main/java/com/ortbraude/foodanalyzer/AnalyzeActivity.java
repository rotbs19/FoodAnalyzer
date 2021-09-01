package com.ortbraude.foodanalyzer;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import usdaFood.app.USDApp;
import usdaFood.usda.USDAClient;

public class AnalyzeActivity extends AppCompatActivity {

    private TextView main_label;
    private ImageProcessing imageProcessing;
    private ImageHandlerSingleton singleton;
    private String TAG = "AnalyzeActivity";
    private String dishName;
    private USDA usda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        usda = new USDA(getApplicationContext());
        dishName = getIntent().getStringExtra("LABEL");
        usda.searchFood("steak");
        main_label = (TextView) findViewById(R.id.main_label);
        main_label.setText(this.dishName);
        imageProcessing = new ImageProcessing(80,32);
        singleton = ImageHandlerSingleton.getInstance();
        analyzeDish();
    }

    public void analyzeDish() {
        try {
//        main_label.setText("There are "+imageProcessing.compareNew(singleton.newAlbum.get(0),this.dishName)+"% of steak in the dish "+this.dishName); ;
        main_label.setText("There are "+imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.test),this.dishName)+"% of steak in the dish "+this.dishName); ;

        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    public void getInfo(View v) throws JSONException, InterruptedException, ExecutionException, IOException {
        USDApp usdApp = new USDApp();
        USDAClient usdaClient = usdApp.getUSDAClient();
        JSONObject jsonObject = usdaClient.searchFood("steak");
        System.out.println("");
    }

}