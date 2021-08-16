package com.ortbraude.foodanalyzer;

import android.graphics.Bitmap;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;

public class ImageProcessing {
    private String TAG = "ImageProcessing";
    //best performance is 80 - MUST be uniform;
    public int pixelSize;
    //set to 32;
    public int greyLevels;

    public ImageProcessing(int pixelSize, int greyLevels) {
        // sets the image
        this.pixelSize = pixelSize;
        this.greyLevels = greyLevels;
    }

    public void addFoodToDB(String name, ArrayList<Bitmap> images) throws IOException {
        ArrayList<ArrayList<Double>> allVectors = new ArrayList<>();
        ArrayList<ArrayList<Double>> imageVectors;

        for (int pic = 0; pic < images.size(); pic++) {
            imageVectors = getImageVectors(images.get(pic));
            //removes overlapping vectors to optimize the dataset
            for(ArrayList<Double> newVector : imageVectors){
                if(!optimizeVectors(allVectors,newVector))
                    allVectors.add(newVector);
            }
        }

        Log.i(TAG,"Adding "+name+" to data base");
        // Configure Query
        ParseObject vectors = new ParseObject("vectorDB");
        // Store an object
        vectors.put("name",name);
        vectors.put("vectors",allVectors);
        // Saving object
        vectors.saveInBackground(new SaveCallback() {
            @Override
            public void done (ParseException e){
                if (e == null) {
                    Log.i(TAG, "" + name + " was added successfully");
                } else {
                    Log.i(TAG, "" + name + " was not added due to an error");
                }
            }
        });
    }
    // retrieves all the vectors of an image
    public ArrayList<ArrayList<Double>> getImageVectors(Bitmap image) throws IOException {
        ArrayList<ArrayList<Double>> imageVectors = new ArrayList<>();
        //will cut the image to pixel portions (squares) and saves their vectors
        for (int i = 0; i < image.getWidth() - pixelSize; i += pixelSize) {
            for (int j = 0; j < image.getHeight() - pixelSize; j += pixelSize) {
                int[] pixels = new int[image.getWidth() * image.getHeight()];
                // separates specific pixels from the image
                image.getPixels(pixels, 0, image.getWidth(), i, j, pixelSize, pixelSize);
                Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
                // sets the pixels to a bitmap
                bitmap.setPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
                Glcm glcm = new Glcm(bitmap,pixelSize, greyLevels);
                //extracts the features
                glcm.extract();
                imageVectors.add(glcm.getVector());
            }
        }
        return imageVectors;
    }

    public boolean optimizeVectors(ArrayList<ArrayList<Double>> allVectors , ArrayList<Double> newVector){
        boolean exists = false;
        for (ArrayList<Double> oldVector : allVectors) {
            if (getVectorDistance(newVector,oldVector) < 0.05) {
               return true;
            }
        }
        return false;
    }

    public static Double getVectorDistance(ArrayList<Double> vector1,ArrayList<Double> vector2){
        double distance = 0;
        for (int feature = 0; feature < vector1.size(); feature++) {
            distance +=(Math.abs(vector1.get(feature) - vector2.get(feature)) / (Math.abs(vector1.get(feature)) + Math.abs(vector2.get(feature))));
        }
        return distance;
    }

    public void compareNew(Bitmap image) throws IOException {
        //will compare an image to database in order to get a percent value of every food in the image
        ArrayList<ArrayList<Double>> foodVectors = null;
        ArrayList<ArrayList<Double>> imageVectors;
        double percent = 0;

        // retrieves a specific foods feature vectors from the database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("vectorDB");
        query.whereEqualTo("name", "Steak");
        try {
            foodVectors = (ArrayList<ArrayList<Double>>) query.getFirst().get("vectors");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //gets the new image vectors
        Log.i(TAG,"new image vectors were retrieved");
        imageVectors = getImageVectors(image);


        for (ArrayList<Double> arrVector : imageVectors) {
            for (ArrayList<Double> foodVector : foodVectors) {
                if(getVectorDistance(arrVector,foodVector)<0.08){
                    percent+= (double)100/imageVectors.size();
                    break;
                }
            }
        }

        System.out.println("percent = "+percent);
    }
}



