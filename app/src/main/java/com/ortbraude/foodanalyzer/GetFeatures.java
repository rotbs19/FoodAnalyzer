package com.ortbraude.foodanalyzer;


import android.graphics.Bitmap;
import android.graphics.Color;

public class GetFeatures {

    public void getGreyScale(Bitmap src){
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = null;
        bmOut.setWidth(width);
        bmOut.setHeight(height);

        int pixel,A,R,G,B;

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = src.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int)(0.299 * R + 0.587 * G + 0.114 * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
    }

    }

}
