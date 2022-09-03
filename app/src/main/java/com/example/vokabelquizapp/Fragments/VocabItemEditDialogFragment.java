package com.example.vokabelquizapp.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.vokabelquizapp.Activities.NewVocabsListViewActivity;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.VocabData.VocabTuple;

public class VocabItemEditDialogFragment extends DialogFragment {

    private VocabTuple vocabTuple;
    private Context mContext;

    public VocabItemEditDialogFragment(VocabTuple vocabTuple, Context mContext) {
        this.vocabTuple = vocabTuple;
        this.mContext = mContext;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.new_vocab_item_edit, null);
        dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button cancelButton = dialogView.findViewById(R.id.cancelVocabBtn);
        Button confirmButton = dialogView.findViewById(R.id.confirmVocabBtn);
        Button deleteButton = dialogView.findViewById(R.id.deleteVocabBtn);

        EditText learningVocabEditText= dialogView.findViewById(R.id.firstVocabEdit);;
        EditText mainVocabEditText = dialogView.findViewById(R.id.secondVocabEdit);

        learningVocabEditText.setText(vocabTuple.getLearningVocab());
        mainVocabEditText.setText(vocabTuple.getMainVocab());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vocabTuple.setLearningVocab(learningVocabEditText.getText().toString());
                vocabTuple.setMainVocab(mainVocabEditText.getText().toString());
                vocabTuple.setNotFoundInDict(false);
                NewVocabsListViewActivity newVocabsListViewActivity = (NewVocabsListViewActivity)mContext;
                newVocabsListViewActivity.updateDataSet();
                dialogBuilder.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                new VocabDeleteDialogFragment(vocabTuple, dialogBuilder, mContext).show(fm, "fragment_alert");
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
        return dialogBuilder;
    }
}
