package com.ortbraude.foodanalyzer;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import usdaFood.app.USDApp;
import usdaFood.usda.USDAClient;

public class AnalyzeActivity extends AppCompatActivity {

    private TextView main_label;
    private ImageProcessing imageProcessing;
    private ImageHandlerSingleton singleton;
    private String TAG = "AnalyzeActivity";
    private String dishName;
    private ProgressBar spinner;
    private ImageView tintedImage;
    private Button analyze;
    Map<String, Double> precent = null ;
    ArrayList<String> component = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        this.analyze = (Button) findViewById(R.id.analyzBtn);
        this.dishName = "Steak";/* getIntent().getStringExtra("LABEL");*/
        this.main_label = (TextView) findViewById(R.id.main_label);
        this.tintedImage = findViewById(R.id.tintedImage);
        this.imageProcessing = new ImageProcessing();
        this.singleton = ImageHandlerSingleton.getInstance();
        this.spinner = (ProgressBar)findViewById(R.id.progressBar1);
        this.spinner.setVisibility(View.GONE);
        analyzeDish();
        try {
            USDAFood();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void analyzeDish() {
        String msg = "";
        try {
            component = split_label();
        } catch (IOException e) {
            Log.i(TAG,e.getMessage());
        }
        try {
            if(component.size() != 0) {
                precent = new HashMap<>();
                for (int i = 0; i < component.size(); ++i) {
                    precent.put(component.get(i),imageProcessing.compareNew(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.test3)/*singleton.newAlbum.get(0)*/, component.get(i)));
                }
                main_label.setText("This picture are contain: "+precent.get(component.get(0))+ " of "+component.get(0));
                tintedImage.setImageBitmap(ImageHandlerSingleton.getInstance().tintedImage);

//                this.spinner.setVisibility(View.GONE);
            }
            else{
                Toast.makeText(this, "This food is not in the DATA BASE.", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }


    }




    public ArrayList<String> split_label() throws IOException {
        ArrayList<String> Final_ingr = new ArrayList<>();
        ArrayList<String> temp_ingr = new ArrayList<>();
        String[] arrOffood = dishName.split(" ", 4);
        for (String a : arrOffood) {
            temp_ingr.add(a);
        }
        for(int i = 0 ; i<temp_ingr.size() ; ++i){
            if(getAllDataSet(temp_ingr.get(i))){
                Final_ingr.add(temp_ingr.get(i));
            }
        }
        return Final_ingr;
    }


    private boolean getAllDataSet(String name) throws IOException {
        ArrayList<String> temp = new ArrayList<>();
        // retrieves a specific foods feature vectors from the database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("vectorDB");
        query.whereEqualTo("name", name);
        try {
            temp = (ArrayList<String>) query.getFirst().get("vectors");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(temp.size() == 0){
            return Boolean.FALSE;
        }
        else return Boolean.TRUE;
    }




    public void USDAFood() throws JSONException, InterruptedException, ExecutionException, IOException {
        USDApp usdApp = new USDApp();
        USDAClient usdaClient = usdApp.getUSDAClient();
        JSONObject jsonObject = usdaClient.searchFood("steak");
        System.out.println("");
    }


    public void getInfo(View v) throws JSONException, InterruptedException, ExecutionException, IOException {
        analyzeDish();
        USDApp usdApp = new USDApp();
        USDAClient usdaClient = usdApp.getUSDAClient();
        JSONObject jsonObject = usdaClient.searchFood("steak");
        System.out.println("");
    }

    public void onClick(View v) {
        if (v.getId() == R.id.analyzBtn) {
            Intent intent = new Intent(getApplicationContext(), FinalActivity.class);
            intent.putExtra("precent",precent.get(component.get(0)));
            intent.putExtra("component",component.get(0));
            startActivity(intent);
        }
    }



}