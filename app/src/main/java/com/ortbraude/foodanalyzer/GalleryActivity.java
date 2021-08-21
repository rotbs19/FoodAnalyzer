package com.ortbraude.foodanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//this class will handle every type of gallery (all albums, single album, new album)
public class GalleryActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "GalleryActivity";
    private RecyclerView imageRecycler;
    public RecyclerView.Adapter programAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageHandlerSingleton singleton;
    private GalleryMode mode;


    public void doneClicked(View v){
        Log.i(TAG,"done pressed - saves new meal album");
        if(singleton.newAlbum.size()>0) {
            singleton.uploadAlbum();
            singleton.resetSingleton();
        }
        finish();
    }

    public void cancelClicked(View v){
        Log.i(TAG,"cancel pressed - cancels new meal album");
        singleton.resetSingleton();
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v instanceof ImageView){
            Intent intent;
            //get index of image in array
            int index = (int)((ImageView)v).getTag();
            switch (mode){
                case newMeal:
                    Log.i(TAG,"New meal instance - opens new meal gallery");
                    intent = new Intent(this,SingleImageActivity.class);
                    intent.putExtra("index",index);
                    intent.putExtra("Single image mode","new meal image");
                    startActivityForResult(intent,1);
                    break;

                case gallery:
                    intent = new Intent(this,GalleryActivity.class);
                    intent.putExtra("index",index);
                    intent.putExtra("Gallery mode","meal");
                    startActivity(intent);
                    break;

                case meal:
                    intent = new Intent(this,SingleImageActivity.class);
                    intent.putExtra("index",index);
                    intent.putExtra("Single image mode","album image");
                    startActivity(intent);
                    break;
            }
        }
    }

    public void cameraClicked(View v){
        openCamera();
    }

    private void openCamera(){
        Log.i(TAG,"Camera clicked  - opens CameraActivity to add pictures to nea meal album");
        Intent intent = new Intent(this,CameraActivity.class);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.i(TAG,"Activity result from SingleImageActivity  - returns if an image was deleted");
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(GalleryActivity.this, "This image was removed" , Toast.LENGTH_LONG).show();
                singleton.newAlbum.remove((int) data.getExtras().get("delete"));
                programAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 2) {
            Log.i(TAG,"Activity result from CameraActivity  - returns pictures taken");
            programAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setTitle("Gallery");

        singleton = ImageHandlerSingleton.getInstance();
        imageRecycler = findViewById(R.id.imageRecycler);
        imageRecycler.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,3);
        imageRecycler.setLayoutManager(layoutManager);


        Intent intent = getIntent();

        // set a mode of gallery
        switch (intent.getStringExtra("Gallery mode")){
            case "new meal":
                Log.i(TAG,"Gallery of new meal Type - sets the type and sets the programAdapter to singleton.newAlbum");
                mode = GalleryMode.newMeal;
                programAdapter = new ProgramAdapter(this,singleton.newAlbum);
                break;

            case "gallery":
                Log.i(TAG,"Gallery of gallery Type - sets the type and sets the programAdapter to singleton.galleries (will present thumbnails of all the user galleries)");
                mode = GalleryMode.gallery;
                //hides done and cancel button
                findViewById(R.id.constraintLayout).setVisibility(View.INVISIBLE);
                singleton.getGalleries(this);
                programAdapter = new ProgramAdapter(this,singleton.galleries);
                break;

            case "meal":
                Log.i(TAG,"Gallery of meal Type - sets the type and sets the programAdapter to singleton.meals (will present all the pictures of a single meal)");
                mode = GalleryMode.meal;
                //hides done and cancel button
                findViewById(R.id.constraintLayout).setVisibility(View.INVISIBLE);
                singleton.getMeal(this,singleton.galleriesObjects.get(intent.getIntExtra("index",0)));
                programAdapter = new ProgramAdapter(this,singleton.meals);
                break;
        }
        //sets the adapter for the recycler
        imageRecycler.setAdapter(programAdapter);

        //opens the camera automatically in case of a new meal
        if(mode == GalleryMode.newMeal)
            openCamera();
    }


}