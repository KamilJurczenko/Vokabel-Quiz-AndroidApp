package com.example.vokabelquizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vokabelquizapp.R;

// Debug Activity for OCR Results
public class ocrResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_result);

        TextView ocr_TextView = findViewById(R.id.ocrTextView);

        Intent myIntent = getIntent();
        String ocrResult = myIntent.getStringExtra("OCR_Result");

        ocr_TextView.setText(ocrResult);
    }
}