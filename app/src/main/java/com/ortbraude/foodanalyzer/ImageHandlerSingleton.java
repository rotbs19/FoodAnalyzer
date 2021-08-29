package com.ortbraude.foodanalyzer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

//this class will hold all the images that are currently active in the app
public class ImageHandlerSingleton {
    // static variable single_instance of type Singleton
    private static ImageHandlerSingleton single_instance = null;

    public ArrayList <Bitmap> newAlbum;
    public ArrayList <Bitmap> galleries;
    public ArrayList <ParseObject>galleriesObjects;
    public ArrayList <Bitmap> meals;

    private String TAG = "ImageHandlerSingleton";
    // private constructor restricted to this class itself
    private ImageHandlerSingleton()
    {
        newAlbum = new ArrayList<>();
        galleries = new ArrayList<>();
        meals = new ArrayList<>();
        galleriesObjects = new ArrayList<>();
    }

    // static method to create instance of Singleton class
    public static ImageHandlerSingleton getInstance()
    {
        if (single_instance == null)
            single_instance = new ImageHandlerSingleton();

        return single_instance;
    }

    public void resetSingleton(){
        newAlbum.clear();
    }

    public void uploadAlbum(){
        Log.i(TAG,"saving new album");
        ParseObject  thumbnail = null;

        for(Bitmap image : newAlbum) {
            ParseObject newPic = new ParseObject("Images");
            if(newAlbum.indexOf(image)==0){
                thumbnail = newPic;
                //If it is the thumbnail the user will also be saved
                newPic.put("user",ParseUser.getCurrentUser());
            }else newPic.put("gallery", thumbnail);

            //changing bitmap to a file that we can save to Parse
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte [] byteArray= stream.toByteArray();
            ParseFile file = new ParseFile("album.jpeg",byteArray);
            newPic.put("image", file);

            newPic.saveInBackground();
        }
    }

    public void getGalleries(GalleryActivity galleryActivity){
        Log.i(TAG,"getting user galleries");
        galleries.clear();
        galleriesObjects.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Images").whereEqualTo("user",ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0){
                    for(ParseObject object : objects){
                        galleriesObjects.add(object);
                        byte [] image = new byte[0];
                        try {
                            image = ((ParseFile)object.get("image")).getData();
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        galleries.add(BitmapFactory.decodeByteArray(image, 0, image.length));
                    }
                    galleryActivity.programAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void getMeal(GalleryActivity galleryActivity, ParseObject thumbnail){
        Log.i(TAG,"getting a specific meal");
        meals.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Images").whereEqualTo("gallery",thumbnail);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0){
                    for(ParseObject object : objects){
                        byte [] image = new byte[0];
                        try {
                            image = ((ParseFile)object.get("image")).getData();
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        meals.add(BitmapFactory.decodeByteArray(image, 0, image.length));
                    }
                    galleryActivity.programAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
