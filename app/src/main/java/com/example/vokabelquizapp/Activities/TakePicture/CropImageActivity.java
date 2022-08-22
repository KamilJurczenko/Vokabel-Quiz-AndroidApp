package com.example.vokabelquizapp.Activities.TakePicture;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.example.vokabelquizapp.Activities.NewVocabsListViewActivity;
import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.VocabData.VocabTuple;
import com.example.vokabelquizapp.classes.WordFinder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CropImageActivity extends AppCompatActivity {

    private Uri image;
    private Uri[] croppedImages;
    private Text[] ocrResults;

    private WordFinder wordFinder;

    private final String titleBegin = "Crop ";
    private final String titleEnd = " Words";

    private final ActivityResultLauncher<CropImageContractOptions> cropImage =
            registerForActivityResult(new CropImageContract(), this::onCropImageResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        // croppedImages[0] Language 1 Vocabs
        // croppedImages[1] Language 2 Vocabs
        croppedImages = new Uri[2];
        ocrResults = new Text[2];
        wordFinder = new WordFinder();

        Intent myIntent = getIntent();
        image =  myIntent.getParcelableExtra("ImageURI");

        startCrop(image, titleBegin + AppData.learningLanguageString + titleEnd);
    }

    private void startCrop(Uri imgUri, String title){
        CropImageContractOptions options = new CropImageContractOptions(imgUri, new CropImageOptions())
                .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(1, 1)
                .setMaxZoom(4)
                .setAutoZoomEnabled(true)
                .setMultiTouchEnabled(false)
                .setCenterMoveEnabled(true)
                .setShowCropOverlay(true)
                .setAllowFlipping(false)
                .setSnapRadius(3f)
                .setTouchRadius(48f)
                .setInitialCropWindowPaddingRatio(0.1f)
                .setBorderLineThickness(8f)
                .setBorderLineColor(Color.argb(170, 255, 255, 255))
                .setBorderCornerThickness(20f)
                .setBorderCornerOffset(9f)
                .setBorderCornerLength(40f)
                .setBorderCornerColor(Color.argb(255, 255, 255, 255))
                .setGuidelinesThickness(1f)
                .setGuidelinesColor(Color.argb(170, 255, 255, 255))
                .setBackgroundColor(Color.argb(119, 0, 0, 0))
                .setMinCropWindowSize(24, 24)
                .setMinCropResultSize(20, 20)
                .setMaxCropResultSize(99999, 99999)
                .setActivityTitle(title)
                .setActivityMenuIconColor(0)
                .setOutputUri(null)
                .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                .setOutputCompressQuality(90)
                .setRequestedSize(0, 0)
                .setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setInitialCropWindowRectangle(null)
                .setInitialRotation(0)
                .setAllowCounterRotation(false)
                .setFlipHorizontally(false)
                .setFlipVertically(false)
                .setCropMenuCropButtonTitle(null)
                .setCropMenuCropButtonIcon(0)
                .setAllowRotation(false)
                .setNoOutputImage(false)
                .setFixAspectRatio(false);
        cropImage.launch(options);
    }

    public void showErrorMessage(@NotNull String message) {
        Log.e("Camera Error:", message);
        Toast.makeText(CropImageActivity.this, "Crop failed: " + message, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onCropImageResult(@NonNull CropImageView.CropResult result) {
        if (result.isSuccessful()) {
            if(croppedImages[0] == null){
                croppedImages[0] = result.getUriContent();
                startCrop(image, titleBegin + AppData.mainLanguageString + titleEnd);
            }
            else{
                croppedImages[1] = result.getUriContent();
                ocrTextResult(croppedImages[0]);
                ocrTextResult(croppedImages[1]);
            }
        } else if (result.equals(CropImage.CancelledResult.INSTANCE)) {
            showErrorMessage("cropping image was cancelled by the user");
        } else {
            showErrorMessage("cropping image failed");
        }
    }

    private void ocrTextResult(Uri filePath){
        @SuppressLint("UnsafeOptInUsageError")
        InputImage inputImage;
        try {
            inputImage =
                    InputImage.fromFilePath(CropImageActivity.this, filePath);
        } catch (Exception e){
            Log.e("ReadFromPath", "Cannot Read from Path: " + Log.getStackTraceString(e));
            return;
        }
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(inputImage) // Task<Text>
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text result) {
                        //String resultText = result.getText();
                        if(ocrResults[0] == null){
                            ocrResults[0] = result;
                        }
                        else{
                            ocrResults[1] = result;
                            ArrayList<VocabTuple> processedList = wordFinder.processOCRResult(ocrResults[0], ocrResults[1]);
                            for(VocabTuple v : processedList)
                                AppData.currentVocabList.addTupleToVocabList(v);

                            Intent i = new Intent(CropImageActivity.this, NewVocabsListViewActivity.class);
                            startActivity(i);
                        }
                        // Show results when last Image taken
                        /*Intent i = new Intent(CropImageActivity.this, ocrResultActivity.class);
                        i.putExtra("OCR_Result", resultText);
                        startActivity(i);*/
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("OCR", "OCR Failed: " + Log.getStackTraceString(e));
                            }
                        });
    }
}