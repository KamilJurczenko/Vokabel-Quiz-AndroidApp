package com.example.vokabelquizapp.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.vokabelquizapp.Activities.MainActivity;
import com.example.vokabelquizapp.Activities.NewVocabsListViewActivity;
import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.classes.Adapter.VocabAdapter;
import com.example.vokabelquizapp.classes.SharedPrefVocabList;
import com.example.vokabelquizapp.classes.VocabData.VocabTuple;

public class VocabDeleteDialogFragment extends DialogFragment {

    private VocabTuple vocabTuple;
    private AlertDialog fromDialog;
    private Context mContext;

    public VocabDeleteDialogFragment(VocabTuple vocabTuple, AlertDialog fromDialog, Context mContext) {
        this.vocabTuple = vocabTuple;
        this.fromDialog = fromDialog;
        this.mContext = mContext;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Confirm deletion?")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Vocab item deleted successfully!", Toast.LENGTH_SHORT).show();
                        AppData.currentVocabList.getVocabList().remove(vocabTuple);
                        NewVocabsListViewActivity newVocabsListViewActivity = (NewVocabsListViewActivity)mContext;
                        newVocabsListViewActivity.updateDataSet();
                        fromDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        VocabDeleteDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
