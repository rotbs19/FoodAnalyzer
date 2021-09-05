package com.ortbraude.foodanalyzer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;

public class HomeWindowActivity extends AppCompatActivity {
    private String TAG = "HomeWindowActivity";

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //creates an option menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handles the option menu
        if(item.getItemId() == R.id.logout){
            Log.i(TAG,"LogOut pressed - logging out from the user");
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void addMealClicked(View v){
        Log.i(TAG,"Add meal pressed - starting a new meal gallery");
        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
        intent.putExtra("Gallery mode", "new meal");
        startActivity(intent);
    }

    public void galleryClicked(View v){
        Log.i(TAG,"Gallery pressed - opening gallery");
        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
        intent.putExtra("Gallery mode", "gallery");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_window);
        setTitle("Food Analyzer");
        try {
            uploadNewFood();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void uploadNewFood() throws IOException {
        ImageProcessing imageProcessing = new ImageProcessing();

//        ArrayList<Bitmap> images = new ArrayList<>();
//        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steaktop1));
//        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steaktop2));
//        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steaktop3));
//        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steak1));
//        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steak2));
//        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steak3));
//        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steak4));
//        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steak5));
//        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steak6));
//        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steak7));
//        imageProcessing.addFoodToDB("steak",images,"NYqlD0Fxho");

        ArrayList<Bitmap> images = new ArrayList<>();
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries1));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries2));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries3));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries4));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries5));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries6));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries7));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries8));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries9));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries10));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries11));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries12));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries13));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries14));
        images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fries15));
//        imageProcessing.addFoodToDB("fries",images,"JvHXwoTQ1B");

//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.newtest9),"Fries");
//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.newtest9),"Steak");
//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steakandfries1),"Fries");
////        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.newtest3),"Fries");

//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steakandfries1),"Fries");
//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.testfries1),"Fries");
//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.testfries2),"Fries");
//
//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.steakandfries1),"Steak");
//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.teststeak1),"Steak");
//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.teststeak2),"Steak");
//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.teststeak3),"Steak");

    }
}