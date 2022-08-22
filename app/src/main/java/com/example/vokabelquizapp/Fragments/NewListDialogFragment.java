package com.example.vokabelquizapp.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.widget.Toast;

import com.example.vokabelquizapp.Activities.MainActivity;
import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.classes.SharedPrefVocabList;
import com.example.vokabelquizapp.classes.VocabData.VocabList;

public class NewListDialogFragment extends DialogFragment {

    public NewListDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Uncompleted Vocabularies will get deleted!")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        VocabList list = AppData.currentVocabList;
                        list.getVocabList().removeIf(Tuple -> Tuple.getMainVocab().equals(""));
                        AppData.loadedLanguageVocabs.add(list);
                        SharedPrefVocabList sharedPrefVocabList = new SharedPrefVocabList(getActivity(),AppData.lanListPrefTag);
                        sharedPrefVocabList.saveVocabList(AppData.loadedLanguageVocabs);
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        Toast.makeText(getActivity(), "New Vocabulary List added.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewListDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}