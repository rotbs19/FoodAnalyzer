package com.ortbraude.foodanalyzer;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class NutrientsActivity extends AppCompatActivity {
    private String component;
    private Double precent;
    private double energy;
    private double fat;
    private double protein;
    private double cholesterol;
    private double sodium;
    private double carbohydrates;

    TextView food1TV;
    TextView food2TV;
    TextView percentFood1TV;
    TextView percentFood2TV;
    TextView energy1TV;
    TextView energy2TV;
    TextView fat1TV;
    TextView fat2TV;
    TextView protein1TV;
    TextView protein2TV;
    TextView cholesterol1TV;
    TextView cholesterol2TV;
    TextView sodium1TV;
    TextView sodium2TV;
    TextView carbohydrates1TV;
    TextView carbohydrates2TV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrients);
        this.component =  getIntent().getStringExtra("component");
        this.precent = getIntent().getDoubleExtra("precent",0);

        food1TV = findViewById(R.id.food1TV);
        food1TV.setText(getIntent().getStringExtra("food1"));
        food2TV = findViewById(R.id.food1TV);
        food2TV.setText(getIntent().getStringExtra("food2"));
        percentFood1TV = findViewById(R.id.food1TV);
        percentFood1TV.setText(getIntent().getStringExtra("percent1"));
        percentFood2TV  = findViewById(R.id.food1TV);
        percentFood2TV.setText(getIntent().getStringExtra("percent2"));
        energy1TV  = findViewById(R.id.food1TV);
        energy2TV  = findViewById(R.id.food1TV);
        fat1TV  = findViewById(R.id.food1TV);
        fat2TV  = findViewById(R.id.food1TV);
        protein1TV  = findViewById(R.id.food1TV);
        protein2TV  = findViewById(R.id.food1TV);
        cholesterol1TV  = findViewById(R.id.food1TV);
        cholesterol2TV  = findViewById(R.id.food1TV);
        sodium1TV  = findViewById(R.id.food1TV);
        sodium2TV  = findViewById(R.id.food1TV);
        carbohydrates1TV  = findViewById(R.id.food1TV);
        carbohydrates2TV  = findViewById(R.id.food1TV);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int imageSizeInCm = (int) ((dpHeight/111)*8.5 + (dpWidth/70)*54);
        int multiplayer;
        double multiPer100grams = 0;

        // retrieves a specific foods feature vectors from the database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("vectorDB");
        query.whereEqualTo("name", food1TV.getText());
        try {
            multiplayer = (int) ((Double.parseDouble(percentFood1TV.getText().toString())/100)*imageSizeInCm)/100;
            multiPer100grams = (multiplayer * (Double)query.getFirst().get("density")* (Double)query.getFirst().get("thikness"))/100;
            protein1TV.setText((int) ((Double)query.getFirst().get("Protein")* multiplayer));
            fat1TV.setText((int) ((Double)query.getFirst().get("fat")* multiplayer));
            energy1TV.setText((int) ((Double)query.getFirst().get("Energy")* multiplayer));
            sodium1TV.setText((int) ((Double)query.getFirst().get("Sodium")* multiplayer));
            carbohydrates1TV.setText((int) ((Double)query.getFirst().get("Carbohydrates")* multiplayer));
            cholesterol1TV.setText((int) ((Double)query.getFirst().get("Cholesterol")* multiplayer));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // retrieves a specific foods feature vectors from the database
        query.whereEqualTo("name", food2TV.getText());
        try {
            multiplayer = (int) ((Double.parseDouble(percentFood2TV.getText().toString())/100)*imageSizeInCm)/100;
            multiPer100grams = (multiplayer * (Double)query.getFirst().get("density")* (Double)query.getFirst().get("thikness"))/100;
            protein2TV.setText((int) ((Double)query.getFirst().get("Protein")* multiplayer));
            fat2TV.setText((int) ((Double)query.getFirst().get("fat")* multiplayer));
            energy2TV.setText((int) ((Double)query.getFirst().get("Energy")* multiplayer));
            sodium2TV.setText((int) ((Double)query.getFirst().get("Sodium")* multiplayer));
            carbohydrates2TV.setText((int) ((Double)query.getFirst().get("Carbohydrates")* multiplayer));
            cholesterol2TV.setText((int) ((Double)query.getFirst().get("Cholesterol")* multiplayer));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }






}