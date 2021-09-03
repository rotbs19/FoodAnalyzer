package com.ortbraude.foodanalyzer;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

public class NutrientsActivity extends AppCompatActivity {

    TextView sodiumLabel;
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
    Double precent1;
    Double precent2;
    ArrayList<Object> data = null ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrients);
        food1TV = findViewById(R.id.food1TV);
        food1TV.setText(getIntent().getStringExtra("food1"));
        food2TV = findViewById(R.id.food2TV);
        food2TV.setText(getIntent().getStringExtra("food2"));
        percentFood1TV = findViewById(R.id.percentFood1TV);
        precent1 = getIntent().getDoubleExtra("percent1" , 0);
        percentFood1TV.setText(Integer.toString(precent1.intValue()));
        percentFood2TV  = findViewById(R.id.percentFood2TV);
        precent2 = getIntent().getDoubleExtra("percent2" , 0);
        percentFood2TV.setText(Integer.toString(precent2.intValue()));
        energy1TV  = findViewById(R.id.energy1TV);
        energy2TV  = findViewById(R.id.energy2TV);
        fat1TV  = findViewById(R.id.fat1TV);
        fat2TV  = findViewById(R.id.fat2TV);
        protein1TV  = findViewById(R.id.protein1TV);
        protein2TV  = findViewById(R.id.protein2TV);
        cholesterol1TV  = findViewById(R.id.cholesterol1TV);
        cholesterol2TV  = findViewById(R.id.cholesterol2TV);
        sodiumLabel = findViewById(R.id.sodiumLabel);
        sodiumLabel.setVisibility(View.GONE);
        sodium1TV  = findViewById(R.id.sodium1TV);
        sodium1TV.setVisibility(View.GONE);
        sodium2TV  = findViewById(R.id.sodium2TV);
        sodium2TV.setVisibility(View.GONE);
        carbohydrates1TV  = findViewById(R.id.carbohydrates1TV);
        carbohydrates2TV  = findViewById(R.id.carbohydrates2TV);
        ArrayList<Object> data = new ArrayList<>();
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int imageSizeInCm = (int) ((dpHeight/111)*8.5 + (dpWidth/70)*54);
        int multiplayer;
        double multiPer100grams;
        double density1 , Protein1 , fat1 , Carbohydrate1 , Cholesterol1, density2 ,Protein2 , fat2 , Carbohydrate2 ,Cholesterol2;
        int thickness1 , Energy1 , temp , thickness2, Energy2;
        // retrieves a specific foods feature vectors from the database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("vectorDB");
        query.whereEqualTo("name", food1TV.getText());
        try {
            multiplayer = (int) (precent1/100 *imageSizeInCm)/100;
            density1 = (double) query.getFirst().get("density");
            thickness1 = (int)query.getFirst().get("thickness");
            Protein1 = (double)query.getFirst().get("Protein");
            fat1 = (double)query.getFirst().get("fat");
            Energy1 = (int)query.getFirst().get("Energy");
            Carbohydrate1=(double)query.getFirst().get("Carbohydrate");
            Cholesterol1 = (double)query.getFirst().get("Cholesterol");
            multiPer100grams = ((multiplayer * density1 * thickness1)/100);
            temp = (int)(Protein1 * multiplayer);
            protein1TV.setText(Integer.toString(temp));
            temp = (int) (fat1 * multiplayer);
            fat1TV.setText(Integer.toString(temp));
            temp = Energy1 * multiplayer ;
            energy1TV.setText(Integer.toString(temp));
            temp = (int) (Carbohydrate1 * multiplayer);
            carbohydrates1TV.setText(Integer.toString(temp));
            temp = (int) (Cholesterol1 * multiplayer);
            cholesterol1TV.setText(Integer.toString(temp));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // retrieves a specific foods feature vectors from the database
        query.whereEqualTo("name", food2TV.getText());
        try {
            multiplayer = (int) (precent2/100 *imageSizeInCm)/100;
            density2 = (double) query.getFirst().get("density");
            thickness2 = (int)query.getFirst().get("thickness");
            Protein2 = (double)query.getFirst().get("Protein");
            fat2 = (double)query.getFirst().get("fat");
            Energy2 = (int)query.getFirst().get("Energy");
            Carbohydrate2=(double)query.getFirst().get("Carbohydrate");
            Cholesterol2 = (double)query.getFirst().get("Cholesterol");
            multiPer100grams = ((multiplayer * density2 * thickness2)/100);
            temp = (int)(Protein2 * multiplayer);
            protein2TV.setText(Integer.toString(temp));
            temp = (int) (fat2 * multiplayer);
            fat2TV.setText(Integer.toString(temp));
            temp = Energy2 * multiplayer ;
            energy2TV.setText(Integer.toString(temp));
            temp = (int) (Carbohydrate2 * multiplayer);
            carbohydrates2TV.setText(Integer.toString(temp));
            temp = (int) (Cholesterol2 * multiplayer);
            cholesterol2TV.setText(Integer.toString(temp));
//
//           multiplayer = (int) (precent2/100 *imageSizeInCm)/100;
//            multiPer100grams = (multiplayer * (Double)query.getFirst().get("density")* (Double)query.getFirst().get("thikness"))/100;
//            protein2TV.setText((int) ((Double)query.getFirst().get("Protein")* multiplayer));
//            fat2TV.setText((int) ((Double)query.getFirst().get("fat")* multiplayer));
//            energy2TV.setText((int) ((Double)query.getFirst().get("Energy")* multiplayer));
//            sodium2TV.setText((int) ((Double)query.getFirst().get("Sodium")* multiplayer));
//            carbohydrates2TV.setText((int) ((Double)query.getFirst().get("Carbohydrates")* multiplayer));
//            cholesterol2TV.setText((int) ((Double)query.getFirst().get("Cholesterol")* multiplayer));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }






}