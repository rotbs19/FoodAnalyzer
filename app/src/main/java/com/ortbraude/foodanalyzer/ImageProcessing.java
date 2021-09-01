package com.ortbraude.foodanalyzer;

import android.graphics.Bitmap;
import android.graphics.Color;
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
        ArrayList<Double> avgVector = new ArrayList<>();
        for (int pic = 0; pic < images.size(); pic++) {
            imageVectors = (ArrayList<ArrayList<Double>>) getImageVectors(images.get(pic)).get(0);
            if(avgVector.size()==0){
                avgVector = imageVectors.get(0);
            }
            if(pic<3) {
                for (ArrayList<Double> newVector : imageVectors) {
                    allVectors.add(newVector);
                    avgVector = avgVectors(avgVector,newVector);
                }
            }
             else {
                //removes overlapping vectors to optimize the dataset
                for(ArrayList<Double> newVector : imageVectors){
                    if(!optimizeVectors(allVectors,newVector)){
                        if(getVectorDistance(newVector,avgVector)>0.07){
                            allVectors.add(newVector);
                            avgVector = avgVectors(avgVector,newVector);
                        }
                    }
                }
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
            if (getVectorDistance(newVector,oldVector) < 0.03) {
               return true;
            }
        }
        return false;
    }

    public ArrayList<Double> avgVectors(ArrayList<Double> vector1 , ArrayList<Double> vector2){
        ArrayList<Double> newVector = new ArrayList<>();
        newVector.add((vector1.get(0)+vector2.get(0))/2);
        newVector.add((vector1.get(1)+vector2.get(1))/2);
        newVector.add((vector1.get(2)+vector2.get(2))/2);
        newVector.add((vector1.get(3)+vector2.get(3))/2);
        newVector.add((vector1.get(4)+vector2.get(4))/2);

        return newVector;
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
//                    Color newPixel = new Color();
//                    newPixel.red(Color.red(pixel)+100);
//                    newPixel.green(Color.green(pixel)+100);
//                    newPixel.blue(Color.blue(pixel)+100);

//                    image.setPixel(i,j,Color.rgb(Color.red(pixel)+10,Color.green(pixel)+10,Color.blue(pixel)+10));
                    image.setPixel(i,j,Color.GREEN);
                }
            }
        }
        System.out.println("");
    }

    public double compareNew(Bitmap image , String food_name) throws IOException {
        //will compare an image to database in order to get a percent value of every food in the image
        ArrayList<ArrayList<Double>> foodVectors = null;
        ArrayList<ArrayList<Double>> imageVectors;
        ArrayList<ArrayList<Integer>> location = new ArrayList<>();
        ArrayList<ArrayList<Integer>> squaresToColor = new ArrayList<>();
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
        ArrayList<Object> imageObjects = getImageVectors(image);
        imageVectors = (ArrayList<ArrayList<Double>>) imageObjects.get(0);
        location = (ArrayList<ArrayList<Integer>>) imageObjects.get(1);
        int similarVectors = 0;
        for (ArrayList<Double> arrVector : imageVectors) {
            for (ArrayList<Double> foodVector : foodVectors) {
                if (getVectorDistance(arrVector, foodVector) < 0.08) {
                    similarVectors++;
                    if (similarVectors == 2) {
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

