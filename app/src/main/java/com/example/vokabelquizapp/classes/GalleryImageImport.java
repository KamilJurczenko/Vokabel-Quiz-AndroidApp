package com.example.vokabelquizapp.classes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.vokabelquizapp.Activities.MainActivity;
import com.example.vokabelquizapp.Activities.TakePicture.ImagePreviewActivity;

public class GalleryImageImport {

    private Intent intent;

    public static final int PICK_IMAGE = 1;

    public GalleryImageImport() {
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
    }

    public Intent onImageChoosen(int requestCode, Intent data, Context context){
        if(requestCode == PICK_IMAGE){
            Uri selectedImage = data.getData();
            Intent i = new Intent(context, ImagePreviewActivity.class);
            i.putExtra("ImageURI", selectedImage);
            i.putExtra("Caller", "MainActivity");
            return i;
        }
        return null;
    }

    public Intent getIntent() {
        return intent;
    }


}
