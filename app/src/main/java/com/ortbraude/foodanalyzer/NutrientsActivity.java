package com.ortbraude.foodanalyzer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
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
    ProgressBar progressBar;

    getFood1 getFood1 = new getFood1();
    getFood2 getFood2 = new getFood2();

    private  int imageSizeInCm;
    private int multiplayer1,multiplayer2;
    private double multiPer100grams1,multiPer100grams2;
    private double density1 , protein1, fat1 , carbohydrate1 , cholesterol1 ,density2 , protein2, fat2 , carbohydrate2 , cholesterol2;
    private int thickness1 , energy1 , thickness2 , energy2;


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
        progressBar = findViewById(R.id.progressBar2);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        imageSizeInCm = (int) ((dpHeight/111)*8.5 + (dpWidth/70)*54);

        getFood1.execute();
        getFood2.execute();

    }


    class getFood1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        };

        @Override
        protected String doInBackground(String... params) {
            // retrieves a specific foods feature vectors from the database
            ParseQuery<ParseObject> query = ParseQuery.getQuery("vectorDB");
            query.whereEqualTo("name", food1TV.getText());
            try {
                multiplayer1 = (int) (precent1/100 *imageSizeInCm);
                density1 = (double) query.getFirst().get("density");
                thickness1 = (int)query.getFirst().get("thickness");
                protein1 = (double)query.getFirst().get("Protein");
                fat1 = (double)query.getFirst().get("fat");
                energy1 = (int)query.getFirst().get("Energy");
                carbohydrate1 =(double)query.getFirst().get("Carbohydrate");
                cholesterol1 = (double)query.getFirst().get("Cholesterol");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "completed";
        }

        @Override
        protected void onPostExecute(String result) {
            int temp;
            multiPer100grams1 = ((multiplayer1 * density1 * thickness1)/100);
            temp = (int)(protein1 * multiPer100grams1);
            protein1TV.setText(Integer.toString(temp));
            temp = (int) (fat1 * multiPer100grams1);
            fat1TV.setText(Integer.toString(temp));
            temp = (int) (energy1 * multiPer100grams1);
            energy1TV.setText(Integer.toString(temp));
            temp = (int) (carbohydrate1 * multiPer100grams1);
            carbohydrates1TV.setText(Integer.toString(temp));
            temp = (int) (cholesterol1 * multiPer100grams1);
            cholesterol1TV.setText(Integer.toString(temp));
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }
    }

    class getFood2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        };

        @Override
        protected String doInBackground(String... params) {
            // retrieves a specific foods feature vectors from the database
            ParseQuery<ParseObject> query = ParseQuery.getQuery("vectorDB");
            query.whereEqualTo("name", food2TV.getText());
            try {
                multiplayer2 = (int) (precent2/100 *imageSizeInCm);
                density2 = (double) query.getFirst().get("density");
                thickness2 = (int)query.getFirst().get("thickness");
                protein2 = (double)query.getFirst().get("Protein");
                fat2 = (double)query.getFirst().get("fat");
                energy2 = (int)query.getFirst().get("Energy");
                carbohydrate2 =(double)query.getFirst().get("Carbohydrate");
                cholesterol2 = (double)query.getFirst().get("Cholesterol");


            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "completed";
        }

        @Override
        protected void onPostExecute(String result) {
            int temp;
            multiPer100grams2 = ((multiplayer2 * density2 * thickness2)/100);
            temp = (int)(protein2 * multiPer100grams2);
            protein2TV.setText(Integer.toString(temp));
            temp = (int) (fat2 * multiPer100grams2);
            fat2TV.setText(Integer.toString(temp));
            temp = (int) (energy2 * multiPer100grams2);
            energy2TV.setText(Integer.toString(temp));
            temp = (int) (carbohydrate2 * multiPer100grams2);
            carbohydrates2TV.setText(Integer.toString(temp));
            temp = (int) (cholesterol2 * multiPer100grams2);
            cholesterol2TV.setText(Integer.toString(temp));
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

}