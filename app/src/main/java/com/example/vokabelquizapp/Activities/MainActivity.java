package com.example.vokabelquizapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vokabelquizapp.Activities.TakePicture.CameraActivity;
import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.Adapter.VocabListAdapter;
import com.example.vokabelquizapp.classes.ExitButtonDialog;
import com.example.vokabelquizapp.classes.GalleryImageImport;

public class MainActivity extends AppCompatActivity {

    private ImageView mainLanguageImage;
    private ImageView learnLanguageImage;
    private ListView vocabListView;

    private LinearLayout languageSelectLayout;

    private int mainLanguageTmp;

    private boolean firstAppInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView toolbarTitle = findViewById(R.id.toolbarText);

        toolbarTitle.setText(R.string.your_lists);

        languageSelectLayout = findViewById(R.id.selectLanguageLayout);
        mainLanguageImage = findViewById(R.id.mainLanguageImage);
        learnLanguageImage = findViewById(R.id.learnLanguageImage);

        // App started for first time!
        if(AppData.learningLanguage == AppData.NOT_DEFINED || AppData.mainLanguage == AppData.NOT_DEFINED){
            firstAppInit = true;
            languageSelectLayout.setVisibility(View.INVISIBLE);
            showLanguageSelectorPopup(0);
        }

        setSelectedLanguageImages();

        vocabListView = findViewById(R.id.vocabList);
        renderVocabList();

        languageSelectLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showLanguageSelectorPopup(0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderVocabList();
    }
    private void renderVocabList(){
        if(AppData.loadedLanguageVocabs != null)
        {
            VocabListAdapter adapter = new VocabListAdapter(this, AppData.loadedLanguageVocabs);
            vocabListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void showLanguageSelectorPopup(int main){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.select_language_popup, null);
        dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout englishLayout = dialogView.findViewById(R.id.englishLayout);
        LinearLayout germanLayout = dialogView.findViewById(R.id.germanLayout);
        LinearLayout frenchLayout = dialogView.findViewById(R.id.frenchLayout);
        if(!firstAppInit)
            new ExitButtonDialog(dialogView, dialogBuilder);
        else
        {
            dialogBuilder.setCancelable(false);
            dialogBuilder.setCanceledOnTouchOutside(false);
        }

        TextView chooseText = dialogView.findViewById(R.id.toolbarText);

        if(main == 1) {
            // Select learning Language
            chooseText.setText(R.string.chooseLearningLang);
            // Hide mainLanguageLayout
            switch (mainLanguageTmp) {
                case AppData.GERMAN:
                    germanLayout.setVisibility(View.GONE);
                    break;
                case AppData.ENGLISH:
                    englishLayout.setVisibility(View.GONE);
                    break;
                case AppData.FRENCH:
                    frenchLayout.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
        else
            // Select main Language
            chooseText.setText(R.string.chooseYourLang);

        englishLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickLanguageSelectorLayout(dialogBuilder, AppData.ENGLISH, main);
            }
        });

        germanLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickLanguageSelectorLayout(dialogBuilder, AppData.GERMAN, main);
            }
        });

        frenchLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickLanguageSelectorLayout(dialogBuilder, AppData.FRENCH, main);
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
    private void onClickLanguageSelectorLayout(AlertDialog dialogBuilder, int language, int languageType){
        AppData appData = (AppData) getApplication();
        if(languageType == 0){
            mainLanguageTmp = language;
            showLanguageSelectorPopup(1);
        }
        else{
            appData.saveLanguages(mainLanguageTmp,language);
            setSelectedLanguageImages();
            languageSelectLayout.setVisibility(View.VISIBLE);
            renderVocabList();
        }
        dialogBuilder.dismiss();
        //mainLanguageTmp = -1;
    }

    private void setSelectedLanguageImages(){
        int mainId;
        int learnId;
        switch(AppData.mainLanguage){
            case AppData.GERMAN:
                mainId = R.drawable.de;
                break;
            case AppData.FRENCH:
                mainId = R.drawable.fr;
                break;
            case AppData.ENGLISH:
                mainId = R.drawable.gb;
                break;
            default:
                mainId = -1;
                break;
        }
        switch(AppData.learningLanguage){
            case AppData.GERMAN:
                learnId = R.drawable.de;
                break;
            case AppData.FRENCH:
                learnId = R.drawable.fr;
                break;
            case AppData.ENGLISH:
                learnId = R.drawable.gb;
                break;
            default:
                learnId = -1;
                break;
        }
        if(learnId == -1 || mainId == -1)
            return;
        mainLanguageImage.setBackgroundResource(mainId);
        learnLanguageImage.setBackgroundResource(learnId);
    }
}