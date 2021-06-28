package com.ortbraude.foodanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SingleImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private int index;
    private ImageHandlerSingleton singleton;

    public void revertClicked(View v){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void deleteClicked(View v){
        Intent intent=new Intent();
        intent.putExtra("delete",index);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
        Intent intent = getIntent();
        index = intent.getIntExtra("index",0);
        imageView = findViewById(R.id.singleImageView);
        singleton = ImageHandlerSingleton.getInstance();

        switch (intent.getStringExtra("Single image mode")){
            case "new meal image":
                imageView.setImageBitmap(singleton.newAlbum.get(index));
                break;
            case "album image":
                //hides delete button
                findViewById(R.id.deletBtn).setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(singleton.meals.get(index));
        }








    }
}