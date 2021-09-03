package com.ortbraude.foodanalyzer;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AnalyzeActivity extends AppCompatActivity {

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
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        singleton = ImageHandlerSingleton.getInstance();
        foodTV = findViewById(R.id.foodTV);
        otherPicBtn = findViewById(R.id.otherPicBtn);
        progressBar = findViewById(R.id.progressBar);
        tintedImage = findViewById(R.id.tintedImage);
        progressBar.setVisibility(View.VISIBLE);
        label = getIntent().getStringExtra("LABEL");
        // get food 1 and 2 from LABEL (dish name from classify activity
//        food1 =
//        food2 =
        food1 = "Steak";
        food2 = "Fries";
        if(food2 == null){
            otherPicBtn.setVisibility(View.INVISIBLE);
        }
        ImageProcessing imageProcessing = new ImageProcessing();

        try {
//            percent1 = imageProcessing.compareNew(singleton.newAlbum.get(0),food1);
//            percent2 = imageProcessing.compareNew(singleton.newAlbum.get(0),food2);
            percent1 = imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steakandfries1),food1);
            percent2 = imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steakandfries1),food2);

        } catch (IOException e) {
            e.printStackTrace();
        }
        tintedImage.setImageBitmap(singleton.tintedImages.get(0));
        foodTV.setText(food1);
        progressBar.setVisibility(View.GONE);
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
}