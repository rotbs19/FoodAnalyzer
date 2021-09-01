package com.ortbraude.foodanalyzer;

import android.content.Intent;
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

public class HomeWindowActivity extends AppCompatActivity {
    private String TAG = "HomeWindowActivity";
    private ImageHandlerSingleton singleton;

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
        singleton = ImageHandlerSingleton.getInstance();
        try {
            uploadNewFood();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ImageProcessing  imageProcessing = new ImageProcessing(80,32);
//        try {
//            imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.test3),"Steak");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void uploadNewFood() throws IOException {
        ImageProcessing imageProcessing = new ImageProcessing(80,32);
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

//        try {
//            imageProcessing.addFoodToDB("Steak", images);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("");
        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.test3),"Steak");
//        imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getReso
    }
}