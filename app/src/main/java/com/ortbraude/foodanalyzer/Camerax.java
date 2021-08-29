package com.ortbraude.foodanalyzer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceOrientedMeteringPointFactory;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Camerax extends AppCompatActivity {

    private ImageHandlerSingleton singleton;
    private ImageCapture imageCapture = null;
    ExecutorService cameraExecutor;
    private int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 44;
    private String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE"};
    public CameraControl cControl;
    public CameraInfo cInfo;
    private ProcessCameraProvider cameraProvider;
    private Camera camera;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
    public ScaleGestureDetector scaleGestureDetector;
    private View focusView;
    private PreviewView viewFinder;
    private Preview preview;
    private ImageButton galleryBtn;
    private ImageView cardBorder;
    private ViewGroup relativeLayout;
    private int xDelta;
    private int yDelta;
    private int rotation;
    private Handler handler = new Handler();
    private OrientationEventListener orientationEventListener;
    private String TAG = "Camerax";

    private Runnable focusingTOInvisible = new Runnable() {
        @Override
        public void run() {
            focusView.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camerax);
        setTitle("Camera X");
        singleton = ImageHandlerSingleton.getInstance();
        galleryBtn = findViewById(R.id.galleryBtnCamera);
        if(!singleton.newAlbum.isEmpty()) {
            Bitmap bitmap = singleton.newAlbum.get(singleton.newAlbum.size() - 1);
            galleryBtn.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));
        }else{galleryBtn.setImageResource(R.drawable.gallery);}
        focusView = findViewById(R.id.focus);
        viewFinder = findViewById(R.id.viewFinder);
        cardBorder = (ImageView) findViewById(R.id.cardBorder);
        cardBorder.setOnTouchListener(onTouchListener());
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        OrientationEventListener orientationEventListener = new OrientationEventListener((Context)this) {
            @Override
            public void onOrientationChanged(int orientation) {
                // Monitors orientation values to determine the target rotation value
                if (orientation >= 45 && orientation < 135) {
                    rotation = Surface.ROTATION_270;
                } else if (orientation >= 135 && orientation < 225) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation >= 225 && orientation < 315) {
                    rotation = Surface.ROTATION_90;
                } else {
                    rotation = Surface.ROTATION_0;
                }
            }
        };

        orientationEventListener.enable();
        //Request Camera permission
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        }
        cameraExecutor = Executors.newSingleThreadExecutor();
        if(singleton.newAlbum.size()!=0){ // will show only for the first picture
            Toast tost = Toast.makeText(this, "The first picture must be taken in parallel to the dish (directly from above) and a wallet card must fit the black rectangle",Toast.LENGTH_LONG);
            tost.setGravity(Gravity.CENTER_VERTICAL,0,0);
            tost.show();
        }

    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //handleFocus(event);
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;



                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }

                relativeLayout.invalidate();
                return true;
            }
        };
    }

    public void takePictureClicked(View v){
        Log.i(TAG,"take picture BTN was clicked");
        takeBitmapPhotos();
    }

    public void galleryClicked(View v){finish();}

    private void takeBitmapPhotos() {
        imageCapture.takePicture(ContextCompat.getMainExecutor(getBaseContext()), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                Log.i(TAG,"the capture was successful");
                Bitmap imageBitmap = imageProxyToBitmap(image);
                Matrix matrix = new Matrix();
                imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 640, 480, true);

                switch (rotation){
                    //will calculate the rotation of the camera
                    case Surface.ROTATION_270:{
                        matrix.postRotate(180);
                    }
                    case Surface.ROTATION_90:{
                        matrix.postRotate(270);
                    }
                    case Surface.ROTATION_0:{
                        matrix.postRotate(90);
                    }
                }
                imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
                singleton.newAlbum.add(imageBitmap);
                galleryBtn.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 200, 200, false));
                Log.i(TAG,"gallery image updated");
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e(TAG,exception.getMessage());
                super.onError(exception);
            }
        });
    }

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
        ByteBuffer buffer = planeProxy.getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    @SuppressLint({"ClickableViewAccessibility", "RestrictedApi"})
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        try {
            orientationEventListener.enable();
        }catch (Exception e){

        }
        cameraProviderFuture.addListener(()->{
            try{
                cameraProvider = cameraProviderFuture.get();
                //bind Camera Preview to Surface provider ie:viewFinder in my case
                preview = new Preview.Builder().build();

                imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).setTargetAspectRatio(AspectRatio.RATIO_4_3).build();
                Preview.SurfaceProvider surfaceProvider = viewFinder.getSurfaceProvider();
                preview.setSurfaceProvider(surfaceProvider);

                try {
                    cameraProvider.unbindAll();
                    camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
                    cControl = camera.getCameraControl();
                    cInfo = camera.getCameraInfo();
                    //AutoFocus Every X Seconds
                    MeteringPointFactory AFfactory = new SurfaceOrientedMeteringPointFactory((float)viewFinder.getWidth(),(float)viewFinder.getHeight());
                    float centerWidth = (float)viewFinder.getWidth()/2;
                    float centerHeight = (float)viewFinder.getHeight()/2;
                    MeteringPoint AFautoFocusPoint = AFfactory.createPoint(centerWidth, centerHeight);
                    try {
                        FocusMeteringAction action = new FocusMeteringAction.Builder(AFautoFocusPoint,FocusMeteringAction.FLAG_AF).setAutoCancelDuration(1,TimeUnit.SECONDS).build();
                        cControl.startFocusAndMetering(action);
                    }catch (Exception e){

                    }

                    //AutoFocus CameraX
                    viewFinder.setOnTouchListener((v, event) -> {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            handler.removeCallbacks(focusingTOInvisible);
                            focusView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_focus));
                            focusView.setVisibility(View.VISIBLE);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            MeteringPointFactory factory = new SurfaceOrientedMeteringPointFactory((float) viewFinder.getWidth(), (float)viewFinder.getHeight());
                            MeteringPoint autoFocusPoint = factory.createPoint(event.getX(), event.getY());
                            FocusMeteringAction action = new FocusMeteringAction.Builder(autoFocusPoint,FocusMeteringAction.FLAG_AF).setAutoCancelDuration(5,TimeUnit.SECONDS).build();
                            ListenableFuture future = cControl.startFocusAndMetering(action);

                            future.addListener(()->{
                                handler.postDelayed(focusingTOInvisible,3000);
                                try{
                                    FocusMeteringResult result = (FocusMeteringResult) future.get();
                                    if(result.isFocusSuccessful()){
                                        focusView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_focus_green));

                                    }
                                }catch (Exception e){

                                }
                            },cameraExecutor);

                            return true;
                        } else {

                            return false;
                        }
                    });

                }catch (Exception e){
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
                }
            }catch (ExecutionException | InterruptedException e){

            }
        },ContextCompat.getMainExecutor(this));
    }

    private boolean allPermissionsGranted() {
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}