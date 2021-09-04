package com.ortbraude.foodanalyzer;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class NutrientsActivity extends AppCompatActivity {

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
    TextView carbohydrates1TV;
    TextView carbohydrates2TV;
    Double precent1;
    Double precent2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrients);


        food1TV = findViewById(R.id.food1TV);
        food1TV.setText(getIntent().getStringExtra("food1"));
        food2TV = findViewById(R.id.food2TV);
        food2TV.setText(getIntent().getStringExtra("food2"));
        precent1 = getIntent().getDoubleExtra("percent1" , 0);
        percentFood1TV = findViewById(R.id.percentFood1TV);
        percentFood1TV.setText(Integer.toString(precent1.intValue()));
        precent2 = getIntent().getDoubleExtra("percent2" , 0);
        percentFood2TV  = findViewById(R.id.percentFood2TV);
        percentFood2TV.setText(Integer.toString(precent2.intValue()));
        energy1TV  = findViewById(R.id.energy1TV);
        energy2TV  = findViewById(R.id.energy2TV);
        fat1TV  = findViewById(R.id.fat1TV);
        fat2TV  = findViewById(R.id.fat2TV);
        protein1TV  = findViewById(R.id.protein1TV);
        protein2TV  = findViewById(R.id.protein2TV);
        cholesterol1TV  = findViewById(R.id.cholesterol1TV);
        cholesterol2TV  = findViewById(R.id.cholesterol2TV);
        carbohydrates1TV  = findViewById(R.id.carbohydrates1TV);
        carbohydrates2TV  = findViewById(R.id.carbohydrates2TV);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int imageSizeInCm = (int) ((dpHeight/111)*8.5 + (dpWidth/70)*54);
        int multiplayer;
        double multiPer100grams;
        double density , protein, fat , carbohydrate , cholesterol;
        int thickness , energy , temp;

        // retrieves a specific foods feature vectors from the database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("vectorDB");
        query.whereEqualTo("name", food1TV.getText());
        try {
            multiplayer = (int) (precent1/100 *imageSizeInCm);
            density = (double) query.getFirst().get("density");
            thickness = (int)query.getFirst().get("thickness");
            protein = (double)query.getFirst().get("Protein");
            fat = (double)query.getFirst().get("fat");
            energy = (int)query.getFirst().get("Energy");
            carbohydrate=(double)query.getFirst().get("Carbohydrate");
            cholesterol = (double)query.getFirst().get("Cholesterol");
            multiPer100grams = ((multiplayer * density * thickness)/100);
            temp = (int)(protein * multiPer100grams);
            protein1TV.setText(Integer.toString(temp));
            temp = (int) (fat * multiPer100grams);
            fat1TV.setText(Integer.toString(temp));
            temp = (int) (energy * multiPer100grams);
            energy1TV.setText(Integer.toString(temp));
            temp = (int) (carbohydrate * multiPer100grams);
            carbohydrates1TV.setText(Integer.toString(temp));
            temp = (int) (cholesterol * multiPer100grams);
            cholesterol1TV.setText(Integer.toString(temp));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // retrieves a specific foods feature vectors from the database
        query.whereEqualTo("name", food2TV.getText());
        try {
            multiplayer = (int) (precent2/100 *imageSizeInCm);
            density = (double) query.getFirst().get("density");
            thickness = (int)query.getFirst().get("thickness");
            protein = (double)query.getFirst().get("Protein");
            fat = (double)query.getFirst().get("fat");
            energy = (int)query.getFirst().get("Energy");
            carbohydrate=(double)query.getFirst().get("Carbohydrate");
            cholesterol = (double)query.getFirst().get("Cholesterol");
            multiPer100grams = ((multiplayer * density * thickness)/100);
            temp = (int)(protein * multiPer100grams);
            protein2TV.setText(Integer.toString(temp));
            temp = (int) (fat * multiPer100grams);
            fat2TV.setText(Integer.toString(temp));
            temp = (int) (energy * multiPer100grams);
            energy2TV.setText(Integer.toString(temp));
            temp = (int) (carbohydrate * multiPer100grams);
            carbohydrates2TV.setText(Integer.toString(temp));
            temp = (int) (cholesterol * multiPer100grams);
            cholesterol2TV.setText(Integer.toString(temp));

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}