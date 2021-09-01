package com.ortbraude.foodanalyzer;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FinalActivity extends AppCompatActivity {
    private String component;
    private Double precent;
    private double protein;
    private double fat;
    private double energy;
    private double sodium;
    private double carbohydrates;
    private double cholesterol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        this.component =  getIntent().getStringExtra("component");
        this.precent = getIntent().getDoubleExtra("precent",0);
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int imageSizeInCm = (int) ((dpHeight/111)*8.5 + (dpWidth/70)*54);
        int multiplayer = (int) ((precent/100)*imageSizeInCm)/100;
        double grams = 0;
        // retrieves a specific foods feature vectors from the database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("vectorDB");
        query.whereEqualTo("name", "Steak");
        try {
            grams = multiplayer * (Double)query.getFirst().get("density")* (Double)query.getFirst().get("thikness");
            protein = (Double)query.getFirst().get("Protein")* multiplayer;
            fat = (Double)query.getFirst().get("fat")*multiplayer;
            energy = (Double)query.getFirst().get("Energy")*multiplayer;
            sodium = (Double)query.getFirst().get("Sodium")*multiplayer;
            carbohydrates = (Double)query.getFirst().get("Carbohydrates")*multiplayer;
            cholesterol = (Double)query.getFirst().get("Cholesterol")*multiplayer;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void showIngridient(){

    }





}