package com.ortbraude.foodanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ClassifyActivity2 extends AppCompatActivity {


    private  ImageHandlerSingleton singleton;
    // selected classifier information received from extras
    private static String chosen = "inception_float.tflite";
    private static String labels = "labels.txt";
    private boolean quant;

    private static final int RESULTS_TO_SHOW = 3;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    // activity elements
    private ImageView selected_image;
    private TextView label1;
    private TextView label2;
    private TextView label3;
    private TextView labelMessage;
    private TextView Confidence1;
    private TextView Confidence2;
    private TextView Confidence3;

    ///
    private Bitmap image;

    // options for model interpreter
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    // tflite graph
    private Interpreter tflite;
    // holds all the possible labels for model
    private List<String> labelList;
    // holds the selected image data as bytes
    private ByteBuffer imgData = null;
    // holds the probabilities of each label for non-quantized graphs
    private float[][] labelProbArray = null;
    // holds the probabilities of each label for quantized graphs
    private byte[][] labelProbArrayB = null;
    // array that holds the labels with the highest probabilities
    private String[] topLables = null;
    // array that holds the highest probabilities
    private String[] topConfidence = null;

    // int array to hold image data
    private int[] intValues;

    //uri to store file
    private Uri filePathImage;
    private Uri fileName;
    private String imagePath;

    // input image dimensions for the Inception Model
    private int DIM_IMG_SIZE_X = 299;
    private int DIM_IMG_SIZE_Y = 299;
    private int DIM_PIXEL_SIZE = 3;




    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify2);
        setTitle("What is your food?");
        singleton = ImageHandlerSingleton.getInstance();
        image = singleton.newAlbum.get(0);
        // get all selected classifier data from classifiers
        //chosen = (String) getIntent().getStringExtra("chosen");
        quant = (boolean) getIntent().getBooleanExtra("quant", false);
        // initialize array that holds image data
        intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];

        //initilize graph and labels
        try {
            tflite = new Interpreter(loadModelFile(), tfliteOptions);
            labelList = loadLabelList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // initialize byte array. The size depends if the input data needs to be quantized or not
        if (quant) {
            imgData =
                    ByteBuffer.allocateDirect(
                            DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        } else {
            imgData =
                    ByteBuffer.allocateDirect(
                            4 * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        }
        imgData.order(ByteOrder.nativeOrder());

        // initialize probabilities array. The datatypes that array holds depends if the input data needs to be quantized or not
        if (quant) {
            labelProbArrayB = new byte[1][labelList.size()];
        } else {
            if (labelList != null) {
                labelProbArray = new float[1][labelList.size()];
            }
        }
        //setContentView(R.layout.activity_classify);


        // labels that hold top three results of CNN
        label1 = (TextView) findViewById(R.id.label1);
        label2 = (TextView) findViewById(R.id.label2);
        label3 = (TextView) findViewById(R.id.label3);
        // displays the probabilities of top labels
        Confidence1 = (TextView) findViewById(R.id.Confidence1);
        Confidence2 = (TextView) findViewById(R.id.Confidence2);
        Confidence3 = (TextView) findViewById(R.id.Confidence3);
        // initialize imageView that displays selected image to the user
        selected_image = (ImageView) findViewById(R.id.selected_image);
        selected_image.setImageBitmap(image);

//        labelMessage = (TextView) findViewById(R.id.labelMessage);


        // initialize array to hold top labels
        topLables = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence = new String[RESULTS_TO_SHOW];

        // allows user to go back to activity to select a different image
//        save_button = (Button) findViewById(R.id.save_button);
//        save_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveImageToFirebase();
//            }
//        });



//        Bitmap bitmap_orig = ((BitmapDrawable) selected_image.getDrawable()).getBitmap();
        // resize the bitmap to the required input size to the CNN
        Bitmap bitmap = getResizedBitmap(image, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
        // convert bitmap to byte array
        convertBitmapToByteBuffer(bitmap);
        // pass byte data to the graph
        if (quant) {
            tflite.run(imgData, labelProbArrayB);
        } else {
            tflite.run(imgData, labelProbArray);
        }
        // display the results
        printTopKLabels();


//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathImage);
//            selected_image.setImageBitmap(bitmap);
//            // not sure why this happens, but without this the image appears on its side
//            selected_image.setRotation(selected_image.getRotation() + Integer.parseInt(getIntent().getStringExtra("angle")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (OutOfMemoryError error) {
//            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//            alertDialog.setTitle("OutOfMemoryError ");
//            alertDialog.setMessage("This photo can't be uploaded\nNot enough memory");
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();
//        }


//        About = (Button) findViewById(R.id.btnAbout);
//        About.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("MainActivity", "onOptionsItemSelected_menu_about");
//                AlertDialog alertDialog = new AlertDialog.Builder(Classify.this).create();
//                alertDialog.setTitle("About");
//                alertDialog.setMessage("Food Analyzer\n\nWas created by:\nBar Plaisant\nShani Harris\n\nSupervisor:\n Prof. Zeev Volkovich\n\n©2020 All Rights Reserved");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
//
//            }
//        });
//
//        Menu = (Button) findViewById(R.id.btnMenu1);
//        Menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(Classify.this, "menu prasses", Toast.LENGTH_LONG).show();
//
//                PopupMenu pm = new PopupMenu(Classify.this, Menu);
//                pm.getMenuInflater().inflate(R.menu.menu_layout, pm.getMenu());
//                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.home_page_item:
//                                Intent intentRegisterHomePage = new Intent(Classify.this, HomePage.class);
//                                startActivity(intentRegisterHomePage);
//                                return true;
//
//                            case R.id.upload_item:
//                                Intent intentRegisterUpload = new Intent(Classify.this, ChooseModel.class);
//                                startActivity(intentRegisterUpload);
//
//                                return true;
//
//                            case R.id.history_item:
//                                Intent intentRegisterHistory = new Intent(Classify.this, History.class);
//                                startActivity(intentRegisterHistory);
//                                return true;
//
//                            case R.id.program_diet_item:
//
//                                Toast.makeText(Classify.this, "program diet", Toast.LENGTH_LONG).show();
//                                dietProgram();
//
//                                return true;
//
//                            case R.id.settings_item:
//                                // need to do
//                                Toast.makeText(Classify.this, "Need to do", Toast.LENGTH_LONG).show();
//                                return true;
//
//                            case R.id.logout_item:
//                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                startActivity(intent);
//                                return true;
//
//
//                        }
//
//                        return true;
//                    }
//                });

//                pm.show();
//
//            }
//        });
    }





    // converts bitmap to byte array which is passed in the tflite graph
    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // loop through all pixels
        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = intValues[pixel++];
                // get rgb values from intValues where each int holds the rgb values for a pixel.
                // if quantized, convert each rgb value to a byte, otherwise to a float
                if (quant) {
                    imgData.put((byte) ((val >> 16) & 0xFF));
                    imgData.put((byte) ((val >> 8) & 0xFF));
                    imgData.put((byte) (val & 0xFF));
                } else {
                    imgData.putFloat((((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat((((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat((((val) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                }

            }
        }
    }

    // loads tflite grapg from file
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd(chosen);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private List<String> loadLabelList() throws IOException {
        List<String> labelList = new ArrayList<>();
//        AssetManager am =this.getAssets();
//        InputStream is = am.open("labels");

        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getAssets().open("food_labels.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    private void printTopKLabels() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            if (quant) {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), (labelProbArrayB[0][i] & 0xff) / 255.0f));
            } else {
                sortedLabels.add(
                        new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            }
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }

        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; ++i) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            topLables[i] = label.getKey();
            topConfidence[i] = String.format("%.0f%%", label.getValue() * 10);
        }

        // set the corresponding textviews with the results
        label1.setText("1. " + topLables[2]);
        label1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnalyzeActivity.class);
                intent.putExtra("LABEL", topLables[2]);
                startActivity(intent);
            }
        });
        label2.setText("2. " + topLables[1]);
        label2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnalyzeActivity.class);
                intent.putExtra("LABEL", topLables[1]);
                startActivity(intent);
            }
        });
        label3.setText("3. " + topLables[0]);
        label3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnalyzeActivity.class);
                intent.putExtra("LABEL", topLables[0]);
                startActivity(intent);
            }
        });
        Confidence1.setText(topConfidence[2]);
        Confidence2.setText(topConfidence[1]);
        Confidence3.setText(topConfidence[0]);
    }




}