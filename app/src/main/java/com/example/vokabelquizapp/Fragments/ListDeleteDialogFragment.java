package com.example.vokabelquizapp.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.vokabelquizapp.Activities.MainActivity;
import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.classes.SharedPrefVocabList;

public class ListDeleteDialogFragment extends DialogFragment {

    public ListDeleteDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure to delete " + AppData.currentVocabList.getName() + "?")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "List deleted successfully!", Toast.LENGTH_SHORT).show();
                        AppData.loadedLanguageVocabs.remove(AppData.currentVocabList);
                        // Save List
                        SharedPrefVocabList sharedPrefVocabList = new SharedPrefVocabList(getActivity(),"VocabList");
                        sharedPrefVocabList.saveVocabList(AppData.loadedLanguageVocabs);
                        // Return to Mainactivity
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ListDeleteDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
