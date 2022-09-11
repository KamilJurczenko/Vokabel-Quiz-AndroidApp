package com.example.vokabelquizapp.Activities.TakePicture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import androidx.camera.core.ZoomState;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vokabelquizapp.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.interfaces.Detector;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

// Kamera Erlaubnis erhalten. Starte OCR Texterkennung.
// Kamera ermöglicht mithilfe von Android Studios CameraX Bibliothek
// OCR durch Google's ML Vision Kit
public class CameraActivity extends AppCompatActivity {

    private PreviewView cameraView;
    private ImageButton snapBtn;
    private ImageView picturePreview;

    private ImageCapture imageCapture;

    // cameraControl can control Zoom, Torch(Flashlight), Focus,...
    private CameraControl cameraControl;
    // For querying information and states.
    private CameraInfo cameraInfo;
    private Executor cameraExecutor; // cameraExecutor.shutdown();

    ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        picturePreview = findViewById(R.id.picture_preview);
        cameraView = findViewById(R.id.camera_preview);
        snapBtn = findViewById(R.id.snapBtn);
        cameraExecutor = ContextCompat.getMainExecutor(this);

        picturePreview.setVisibility(View.INVISIBLE);

        startCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        picturePreview.setVisibility(View.INVISIBLE);
    }

    private void startCamera() {
        // Starte Kamera
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture
                = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider processCameraProvider = cameraProviderFuture.get();
                bindPreview(processCameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // This should never be reached.
                Log.e("Preview", "Exception: " + Log.getStackTraceString(e));
            }
        }, cameraExecutor);
    }

    private void bindPreview(ProcessCameraProvider processCameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();
        imageCapture = builder.build();

        preview.setSurfaceProvider(cameraView.getSurfaceProvider());

        Camera camera = processCameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
        cameraControl = camera.getCameraControl();
        cameraInfo = camera.getCameraInfo();
        // Setup Zoom And Tap Focus
        setupCameraZoomAndFocus();
        handleZoom();
        snapBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                captureImage();
            }
        });
    }

    private void captureImage(){
        // Show result as Bitmap on top of Camera Preview
        Bitmap imageBitmap = cameraView.getBitmap();

        picturePreview.setImageBitmap(imageBitmap);

        picturePreview.setVisibility(View.VISIBLE);

        File file = new File(CameraActivity.this.getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".jpeg");

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(file).build();

        imageCapture.takePicture(outputFileOptions ,cameraExecutor,
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(getApplicationContext(), "Photo capture success.", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(CameraActivity.this, ImagePreviewActivity.class);
                        i.putExtra("ImageURI", outputFileResults.getSavedUri());
                        i.putExtra("Caller", "CameraActivity");
                        startActivity(i);
                    }
                    @Override
                    public void onError(ImageCaptureException e) {
                        Log.e("Capture", "Image Capture Failed: " + Log.getStackTraceString(e));
                    }
                }
        );
    }

    private void setupCameraZoomAndFocus(){
        cameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleFocus(event);
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void handleZoom() {
        ScaleGestureDetector.SimpleOnScaleGestureListener listener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                LiveData<ZoomState> ZoomRatio = cameraInfo.getZoomState();
                float currentZoomRatio = 0;
                try {
                    currentZoomRatio = ZoomRatio.getValue().getZoomRatio();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                float delta = detector.getScaleFactor();
                cameraControl.setZoomRatio(currentZoomRatio * delta);
                return true;
            }
        };
        scaleGestureDetector = new ScaleGestureDetector(CameraActivity.this, listener);
    }

    private void handleFocus(MotionEvent event){
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return;
        }
        MeteringPointFactory pointFactory = cameraView.getMeteringPointFactory();
        MeteringPoint point = pointFactory.createPoint(event.getX(), event.getY());
        FocusMeteringAction action = new FocusMeteringAction.Builder(point).build();
        cameraControl.startFocusAndMetering(action);
    }
}