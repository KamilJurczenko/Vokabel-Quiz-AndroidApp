package com.example.vokabelquizapp.Activities.TakePicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.vokabelquizapp.Activities.MainActivity;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.GalleryImageImport;

public class ImagePreviewActivity extends AppCompatActivity {

    private GalleryImageImport imageImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        imageImport = new GalleryImageImport();

        Button continueBtn = findViewById(R.id.continueBtn);
        Button newImgBtn = findViewById(R.id.newImageBtn);
        ImageView previewImage = findViewById(R.id.imagePreviewView);

        Intent myIntent = getIntent();
        Uri imageUri =  myIntent.getParcelableExtra("ImageURI");
        String calledActivity = myIntent.getStringExtra("Caller");

        try {
            previewImage.setImageDrawable(Drawable.createFromStream(
                    getContentResolver().openInputStream(imageUri),
                    null));
        } catch (Exception e){
            Log.e("PreviewImage", "Image not Found: " + Log.getStackTraceString(e));
        }
        continueBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ocrTextResult(imageUri);
                Intent i = new Intent(ImagePreviewActivity.this, CropImageActivity.class);
                i.putExtra("ImageURI", imageUri);
                i.putExtra("Caller", "MainActivity");
                startActivity(i);
            }
        });

        newImgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(calledActivity.equals("MainActivity")){
                    startActivityForResult(Intent.createChooser(imageImport.getIntent(), "Select Picture"), GalleryImageImport.PICK_IMAGE);
                }
                else{
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null)
            startActivity(imageImport.onImageChoosen(requestCode,data, ImagePreviewActivity.this));
    }

}