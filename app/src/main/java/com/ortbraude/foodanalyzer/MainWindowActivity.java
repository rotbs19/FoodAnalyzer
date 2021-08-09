package com.ortbraude.foodanalyzer;

import android.content.Intent;
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

public class MainWindowActivity extends AppCompatActivity {
    private String TAG = "MainWindowActivity";
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    public void feedClicked(View v) throws IOException {

        /*
        GetFeatures glcmfe = new GetFeatures((BitmapFactory.decodeResource(getResources(), R.drawable.sample)), 15);
        glcmfe.extract();
        System.out.println("Contrast: "+glcmfe.getContrast());
        System.out.println("Homogenity: "+glcmfe.getHomogenity());
        System.out.println("Entropy: "+glcmfe.getEntropy());
        System.out.println("Energy: "+glcmfe.getEnergy());
        System.out.println("Dissimilarity: "+glcmfe.getDissimilarity());*/

    }

    public void personalInfoClicked(View v){

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
        setContentView(R.layout.activity_main_window);
        setTitle("Food Analyzer");
        singleton = ImageHandlerSingleton.getInstance();
    }
}