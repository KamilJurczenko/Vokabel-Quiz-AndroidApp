package com.example.vokabelquizapp.classes;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.classes.VocabData.VocabList;
import com.example.vokabelquizapp.classes.VocabData.VocabTuple;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WriteCSV {

    public WriteCSV(VocabList exportList, Context context) {
        ArrayList<VocabTuple> tuples = exportList.getVocabList();
        String[] header = {AppData.learningLanguageString, AppData.mainLanguageString};
        List<String[]> csvData = new ArrayList<>();
        csvData.add(header);
        for(VocabTuple v : tuples){
            String[] element = {v.getLearningVocab(),v.getMainVocab()};
            csvData.add(element);
        }
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        File file = new File(context.getExternalFilesDir(null), exportList.getName() + System.currentTimeMillis() + ".csv");

        try{
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            writer.writeAll(csvData);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(context, file.getName() + " created in internal Storage.", Toast.LENGTH_SHORT).show();
    }
}
