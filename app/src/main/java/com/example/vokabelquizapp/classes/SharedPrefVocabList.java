package com.example.vokabelquizapp.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.vokabelquizapp.classes.VocabData.LanguageVocabs;
import com.example.vokabelquizapp.classes.VocabData.VocabList;
import com.example.vokabelquizapp.classes.VocabData.VocabTuple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefVocabList {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String PREFS_TAG;

    public SharedPrefVocabList(Context context, String PREFS_TAG) {
        this.PREFS_TAG = PREFS_TAG;
        sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveVocabList(ArrayList<VocabList> languageVocabs){
        Gson gson = new Gson();
        String json = gson.toJson(languageVocabs);

        editor.putString(PREFS_TAG, json);
        editor.commit();
    }

    public ArrayList<VocabList> loadLanguageVocab(){
        ArrayList<VocabList> item;
        String serializedObject = sharedPreferences.getString(PREFS_TAG,null);
        if(serializedObject != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<VocabList>>(){}.getType();
            item = gson.fromJson(serializedObject, type);
        }
        else{
            Log.e("LanguageVocabs load","LanguageVocabs not Found. Initializing new List");
            item = new ArrayList<VocabList>();
        }
        return item;
    }
}
