package com.example.vokabelquizapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;

import com.example.vokabelquizapp.classes.Database.DatabaseManager;
import com.example.vokabelquizapp.classes.SharedPrefVocabList;
import com.example.vokabelquizapp.classes.VocabData.LanguageVocabs;
import com.example.vokabelquizapp.classes.VocabData.VocabList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppData extends Application {

    public static ArrayList<VocabList> loadedLanguageVocabs;
    public static DatabaseManager databaseManager;
    public static String lanListPrefTag;
    public static VocabList currentVocabList;
    public static TextToSpeech textToSpeech;
    public static boolean ttsInit = false;

    private SharedPreferences sharedPref;
    private final String MAIN_LANGUAGE = "MainLanguage";
    private final String LEARNING_LANGUAGE = "LearningLanguage";

    // Language Constants
    public static final int GERMAN = 0;
    public static final int ENGLISH = 1;
    public static final int FRENCH = 2;
    public static final int NOT_DEFINED = -1;

    public static int mainLanguage;
    public static int learningLanguage;

    public static String mainLanguageString;
    public static String learningLanguageString;

    @Override
    public void onCreate() {
        super.onCreate();

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                ttsInit = true;
            }
        });

        String LANGUAGES_KEY = "StringLanguages";
        sharedPref = getSharedPreferences(LANGUAGES_KEY, MODE_PRIVATE);

        mainLanguage = sharedPref.getInt(MAIN_LANGUAGE, NOT_DEFINED);
        learningLanguage = sharedPref.getInt(LEARNING_LANGUAGE, NOT_DEFINED);

        if(mainLanguage == NOT_DEFINED || learningLanguage == NOT_DEFINED){
            // Application started for first time
            // Manage in MainActivity
            return;
        }
        UpdateLanguages();

        LoadDictionaryDBandLanList();
    }

    public void UpdateLanguages(){
        switch(mainLanguage){
            case GERMAN:
                mainLanguageString = "German";
                break;
            case FRENCH:
                mainLanguageString = "French";
                break;
            case ENGLISH:
                mainLanguageString = "English";
                break;
            default:
                mainLanguageString = "NOT DEFINED";
                break;
        }
        switch(learningLanguage){
            case GERMAN:
                learningLanguageString = "German";
                textToSpeech.setLanguage(Locale.GERMAN);
                break;
            case FRENCH:
                learningLanguageString = "French";
                textToSpeech.setLanguage(Locale.FRENCH);
                break;
            case ENGLISH:
                learningLanguageString = "English";
                textToSpeech.setLanguage(Locale.ENGLISH);
                break;
            default:
                learningLanguageString = "NOT DEFINED";
                break;
        }
    }

    public void LoadDictionaryDBandLanList(){
        if(mainLanguage == GERMAN && learningLanguage == ENGLISH){
            databaseManager = new DatabaseManager(this, "de-en.sqlite3");
            lanListPrefTag = "10";
        }
        else if(mainLanguage == GERMAN && learningLanguage == FRENCH){
            databaseManager = new DatabaseManager(this, "de-fr.sqlite3");
            lanListPrefTag = "20";
        }
        else if(mainLanguage == ENGLISH && learningLanguage == GERMAN){
            databaseManager = new DatabaseManager(this, "en-de.sqlite3");
            lanListPrefTag = "01";
        }
        else if(mainLanguage == ENGLISH && learningLanguage == FRENCH){
            databaseManager = new DatabaseManager(this, "en-fr.sqlite3");
            lanListPrefTag = "21";
        }
        else if(mainLanguage == FRENCH && learningLanguage == GERMAN){
            databaseManager = new DatabaseManager(this, "fr-de.sqlite3");
            lanListPrefTag = "02";
        }
        else if(mainLanguage == FRENCH && learningLanguage == ENGLISH){
            databaseManager = new DatabaseManager(this, "fr-en.sqlite3");
            lanListPrefTag = "12";
        }
        else{
            // should never be reached
            return;
        }
        SharedPrefVocabList sharedPrefVocabList = new SharedPrefVocabList(this, lanListPrefTag);
        loadedLanguageVocabs = sharedPrefVocabList.loadLanguageVocab();
        databaseManager.createDatabase();
    }

    public void saveLanguages(int mainLang, int learnLang){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(MAIN_LANGUAGE, mainLang);
        mainLanguage = mainLang;
        editor.putInt(LEARNING_LANGUAGE, learnLang);
        learningLanguage = learnLang;
        UpdateLanguages();
        LoadDictionaryDBandLanList();
        editor.apply();
    }
}
