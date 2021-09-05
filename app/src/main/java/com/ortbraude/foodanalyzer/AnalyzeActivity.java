package com.ortbraude.foodanalyzer;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AnalyzeActivity extends AppCompatActivity {

    myTask mytask = new myTask();
    private String TAG = "AnalyzeActivity";
    private String label;
    private String food1;
    private String food2;
    private Double percent1;
    private Double percent2;
    private ImageHandlerSingleton singleton;
    ImageView tintedImage;
    TextView foodTV;
    Button otherPicBtn;
    Button nutriantsBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        singleton = ImageHandlerSingleton.getInstance();
        foodTV = findViewById(R.id.foodTV);
        otherPicBtn = findViewById(R.id.otherPicBtn);
        nutriantsBtn = findViewById(R.id.nutriantsBtn);
        progressBar = findViewById(R.id.progressBar1);
        tintedImage = findViewById(R.id.tintedImage);
        label = getIntent().getStringExtra("LABEL");
        // get food 1 and 2 from LABEL (dish name from classify activity
//        food1 =
//        food2 =
        food1 = "Steak";
        food2 = "Fries";
        if(food2 == null){
            otherPicBtn.setVisibility(View.INVISIBLE);
        }
        mytask.execute();
        foodTV.setText(food1);
        nutriantsBtn.setClickable(false);
        otherPicBtn.setClickable(false);
    }

    public void changePicClicked(View v){
        if(foodTV.getText().equals(food1)){
            foodTV.setText(food2);
            tintedImage.setImageBitmap(singleton.tintedImages.get(1));
        }else{
            foodTV.setText(food1);
            tintedImage.setImageBitmap(singleton.tintedImages.get(0));
        }
    }

    public void nutrientsClicked(View v){
        Intent intent = new Intent(getApplicationContext(), NutrientsActivity.class);
        intent.putExtra("food1",food1);
        intent.putExtra("food2",food2);
        intent.putExtra("percent1",percent1);
        intent.putExtra("percent2",percent2);
        startActivity(intent);
    }

    class myTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        };

        @Override
        protected String doInBackground(String... params) {
            ImageProcessing imageProcessing = new ImageProcessing();
            try {
                percent1 = imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steakandfries1),food1);
                percent2 = imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steakandfries1),food2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "completed";
        }

        @Override
        protected void onPostExecute(String result) {
            tintedImage.setImageBitmap(singleton.tintedImages.get(0));
            progressBar.setVisibility(View.GONE);
            nutriantsBtn.setClickable(true);
            otherPicBtn.setClickable(true);
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }
    }
}
