package com.example.vokabelquizapp.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.vokabelquizapp.Activities.MainActivity;
import com.example.vokabelquizapp.Activities.TakePicture.CameraActivity;
import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.GalleryImageImport;
import com.example.vokabelquizapp.classes.SharedPrefVocabList;

public class SourceImageDialogFragment extends DialogFragment {

    private GalleryImageImport imageImport;

    private final static int CAMERA_REQUEST_CODE = 10;
    private final static int EXTERNAL_STORAGE_PERMISSION_CODE = 23;
    private final static String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private final static String[] STORAGE_PERMISSION = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    public SourceImageDialogFragment() {
        imageImport = new GalleryImageImport();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.select_import_popup, null);

        TextView title = dialogView.findViewById(R.id.toolbarText);
        ImageButton exitBtn = dialogView.findViewById(R.id.closeBtn);
        exitBtn.setVisibility(View.VISIBLE);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        ImageButton cameraButton = dialogView.findViewById(R.id.scanBtn);
        ImageButton importButton = dialogView.findViewById(R.id.importBtn);
        dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        title.setText(R.string.source);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestPermissions(CAMERA_PERMISSION, CAMERA_REQUEST_CODE);
            }
        });

        importButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestPermissions(STORAGE_PERMISSION, EXTERNAL_STORAGE_PERMISSION_CODE);
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
        int width = (int)(Resources.getSystem().getDisplayMetrics().widthPixels * 0.65);
        //int height = (int)(Resources.getSystem().getDisplayMetrics().heightPixels * 0.5);
        dialogBuilder.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        return dialogBuilder;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (hasReadExternalStoragePermission()) {
                startActivityForResult(Intent.createChooser(imageImport.getIntent(), "Select Picture"), GalleryImageImport.PICK_IMAGE);
            } else {
                Toast.makeText(getActivity(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                //this.finish(); // Already in MainActivity
            }
        }
        else if (requestCode == CAMERA_REQUEST_CODE) {
            if (hasCameraPermission()) {
                startActivity(new Intent(getActivity(), CameraActivity.class));
            } else {
                Toast.makeText(getActivity(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                //this.finish();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null)
            startActivity(imageImport.onImageChoosen(requestCode,data,getActivity()));
    }

    private boolean hasReadExternalStoragePermission(){
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        else{
            return true;
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }
}
