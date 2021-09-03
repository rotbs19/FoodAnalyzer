package com.ortbraude.foodanalyzer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;

public class ImageProcessing {
    private String TAG = "ImageProcessing";
    //best performance is 80 - MUST be uniform;
    public int pixelSize = 80;
    //set to 32;
    public int greyLevels = 32;


    public void addFoodToDB(String name, ArrayList<Bitmap> images,String objectId) throws IOException {
        ArrayList<ArrayList<Double>> vectorsForDB = new ArrayList<>();
        ArrayList<ArrayList<Double>> currImageVecForDB = new ArrayList<>();
        ArrayList<ArrayList<Double>> allImageVectors;
        ArrayList<Double> avgVectorForImage;

        for (int pic = 0; pic < images.size(); pic++) {
            allImageVectors = (ArrayList<ArrayList<Double>>) getImageVectors(images.get(pic)).get(0);
            avgVectorForImage = avgVectors(allImageVectors);
            for (ArrayList<Double> checkVector : allImageVectors) {
                if(!optimizeVectors(vectorsForDB,checkVector)) {
                    if(getVectorDistance(checkVector,avgVectorForImage)<0.7){
                        currImageVecForDB.add(checkVector);
                        if(pic == 0){
                            vectorsForDB.add(checkVector);
                        }
                    }
                }
            }
            if(pic != 0){
                vectorsForDB.addAll(currImageVecForDB);
            }
            currImageVecForDB.clear();
        }
        Log.i(TAG,"Adding "+name+" to data base");
        // Configure Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("vectorDB");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject food, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "" + name + " was added successfully");
                    food.put("vectors",vectorsForDB);
                    food.saveInBackground();
                } else {
                    Log.i(TAG, "" + name + " was not added due to an error");
                }
            }
        });
    }

    // retrieves all the vectors of an image
    public ArrayList<Object> getImageVectors(Bitmap image) throws IOException {
        ArrayList<ArrayList<Double>> imageVectors = new ArrayList<>();
        ArrayList<ArrayList<Integer>> location = new ArrayList<>();
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
                ArrayList<Integer> currLocation = new ArrayList<>();
                currLocation.add(i);
                currLocation.add(j);
                location.add(currLocation);
            }
        }
        ArrayList<Object> arrayLists = new ArrayList<>();
        arrayLists.add(imageVectors);
        arrayLists.add(location);
        return arrayLists;
    }

    public boolean optimizeVectors(ArrayList<ArrayList<Double>> allVectors , ArrayList<Double> newVector){
        boolean exists = false;
        for (ArrayList<Double> oldVector : allVectors) {
            if (getVectorDistance(newVector,oldVector) < 0.06) {
               return true;
            }
        }
        return false;
    }

    public ArrayList<Double> avgVectors(ArrayList<ArrayList<Double>> allVectors ){
        ArrayList<Double> avgVector = new ArrayList<>();
        avgVector = allVectors.get(0);
        for (ArrayList<Double> nextVector : allVectors) {
            ArrayList<Double> newVector = new ArrayList<>();
            newVector.add(((avgVector.get(0)*(allVectors.indexOf(nextVector)+1))+nextVector.get(0))/(allVectors.indexOf(nextVector)+2));
            newVector.add(((avgVector.get(1)*(allVectors.indexOf(nextVector)+1))+nextVector.get(1))/(allVectors.indexOf(nextVector)+2));
            newVector.add(((avgVector.get(2)*(allVectors.indexOf(nextVector)+1))+nextVector.get(2))/(allVectors.indexOf(nextVector)+2));
            newVector.add(((avgVector.get(3)*(allVectors.indexOf(nextVector)+1))+nextVector.get(3))/(allVectors.indexOf(nextVector)+2));
            newVector.add(((avgVector.get(4)*(allVectors.indexOf(nextVector)+1))+nextVector.get(4))/(allVectors.indexOf(nextVector)+2));
            avgVector = newVector;
        }
        return avgVector;
    }

    public static Double getVectorDistance(ArrayList<Double> vector1,ArrayList<Double> vector2){
        double distance = 0;
        for (int feature = 0; feature < vector1.size(); feature++) {
            distance +=(Math.abs(vector1.get(feature) - vector2.get(feature)) / (Math.abs(vector1.get(feature)) + Math.abs(vector2.get(feature))));
        }
        return distance;
    }

    public void colorBitmap(ArrayList<ArrayList<Integer>> squaresToColor, Bitmap image){
        image = image.copy( Bitmap.Config.ARGB_8888 , true);
        for (ArrayList<Integer> location : squaresToColor) {
            for (int i =  location.get(0); i <  location.get(0)+ pixelSize; i ++) {
                for (int j =  location.get(1); j <  location.get(1) + pixelSize; j ++) {
                    int pixel = image.getPixel(i,j);

                    image.setPixel(i,j,Color.rgb(Color.red(pixel),Color.green(pixel)+130,Color.blue(pixel)));
                }
            }
        }
        ImageHandlerSingleton singleton = ImageHandlerSingleton.getInstance();
        singleton.tintedImages.add(image);
    }

    public double compareNew(Bitmap image , String food_name) throws IOException {
        //will compare an image to database in order to get a percent value of every food in the image
        ArrayList<ArrayList<Double>> foodVectors = null;
        ArrayList<ArrayList<Double>> imageVectors;
        ArrayList<ArrayList<Integer>> location = new ArrayList<>();
        ArrayList<ArrayList<Integer>> squaresToColor = new ArrayList<>();
        double distance = 0;
        double percent = 0;

        // retrieves a specific foods feature vectors from the database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("vectorDB");
        query.whereEqualTo("name", food_name);
        try {
            foodVectors = (ArrayList<ArrayList<Double>>) query.getFirst().get("vectors");
            distance = (double)query.getFirst().get("distance");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //gets the new image vectors
        Log.i(TAG,"new image vectors were retrieved");
        ArrayList<Object> imageObjects = getImageVectors(image);
        imageVectors = (ArrayList<ArrayList<Double>>) imageObjects.get(0);
        location = (ArrayList<ArrayList<Integer>>) imageObjects.get(1);
        int similarVectors = 0;
        for (ArrayList<Double> arrVector : imageVectors) {
            for (ArrayList<Double> foodVector : foodVectors) {
                if (getVectorDistance(arrVector, foodVector) < distance) {
                    similarVectors++;
                    if (similarVectors == 1) {
                        similarVectors = 0;
                        percent += (double) 100 / imageVectors.size();
                        squaresToColor.add(location.get(imageVectors.indexOf(arrVector)));
                        break;
                    }
                }
            }
        }
        colorBitmap(squaresToColor,image);
        return percent;
    }

}

