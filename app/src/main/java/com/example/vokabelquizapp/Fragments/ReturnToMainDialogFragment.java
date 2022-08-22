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
import com.example.vokabelquizapp.classes.VocabData.VocabList;

public class ReturnToMainDialogFragment extends DialogFragment {

    public ReturnToMainDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure you want to quit?")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ReturnToMainDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}