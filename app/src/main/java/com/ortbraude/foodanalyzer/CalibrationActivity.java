package com.ortbraude.foodanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CalibrationActivity extends AppCompatActivity {

    private String[] instructions;
    private int instructionIndex;
    TextView instructionTextView;
    Button nextBtn;
    Button previusBtn;
    Button calibrateBtn;


    public void nextClicked(View v) {
        instructionTextView.setText(instructions[++instructionIndex]);
        previusBtn.setEnabled(true);
        if(instructionIndex == instructions.length-1){
            nextBtn.setEnabled(false);
            calibrateBtn.setVisibility(View.VISIBLE);
        }
    }

    public void previusClicked(View v) {
        instructionTextView.setText(instructions[--instructionIndex]);
        calibrateBtn.setVisibility(View.INVISIBLE);
        nextBtn.setEnabled(true);
        if(instructionIndex == 0){
            previusBtn.setEnabled(false);
        }
    }

    public void calibrateClicked(View view) {
        Log.i("Calibration","Tacking calibration picture");
        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        intent.putExtra("mode", "calibration");
        startActivity(intent);
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        instructionIndex = 0;
        instructionTextView = findViewById(R.id.instructionTextView);

        nextBtn = findViewById(R.id.nextBtn);
        previusBtn = findViewById(R.id.previusBtn);
        calibrateBtn = findViewById(R.id.calibrateBtn);

        previusBtn.setEnabled(false);
        calibrateBtn.setVisibility(View.INVISIBLE);

        instructions = new String[] {"Hi, welcome to Food Analyzer, this application is a health orientated application.\n\n" +
                "This application will help you keep track of your food nutritional value intake.\n\n" +
                "It is also possible to share your meals with other members on the app and check out other users meals. ","Now we are going to calibrate your camera.\n\n" +
                "After this short explanation you will be moved to the calibration process.", "The process is:\n\n" +
                "1) Download the calibration page and print it - LINK. \n\n" +
                "2) Take a picture of the object. ", "It is important to note a few things:\n\n" +
                "1) Every picture that is used to calculate a foods nutritional value will be taken with the same focus as the calibrated picture (the same distance from the object).\n\n" +
                "2) Don't worry, if you are not satisfied with the calibration you can always recalibrate.  "};


        instructionTextView.setText(instructions[instructionIndex]);

        }

}