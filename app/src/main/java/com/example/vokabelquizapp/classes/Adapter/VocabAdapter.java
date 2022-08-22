package com.example.vokabelquizapp.classes.Adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.VocabData.VocabTuple;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class VocabAdapter extends BaseAdapter {

    // TODO Delete Vocab items with button

    private Context mContext;
    private ArrayList<VocabTuple> mArrVocabData;

    public VocabAdapter(Context context, ArrayList arrVocabData) {
        super();
        mContext = context;
        mArrVocabData = arrVocabData;
    }

    public int getCount() {
        // return the number of records
        return mArrVocabData.size();
    }

    // getView method is called for each item of ListView
    public View getView(int position, View view, ViewGroup parent) {
        // inflate the layout for each item of listView
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.new_vocab_item, parent, false);

        // get the reference of textView and button
        ImageButton ttsBtn = view.findViewById(R.id.textToSpeechBtn);
        TextInputEditText firstVocabText = view.findViewById(R.id.firstVocabName);
        TextInputEditText secondVocabText = view.findViewById(R.id.secondVocabName);

        // TODO move Item above input keyboard

        // Set Vocabs Text
        firstVocabText.setText(mArrVocabData.get(position).getLearningVocab());
        secondVocabText.setText(mArrVocabData.get(position).getMainVocab());

        firstVocabText.setHint(AppData.learningLanguageString + " Vocab");
        secondVocabText.setHint(AppData.mainLanguageString + " Vocab");

        ttsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppData.textToSpeech.speak(mArrVocabData.get(position).getLearningVocab(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        firstVocabText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeVocab(firstVocabText, position, true);
            }
        });

        secondVocabText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeVocab(secondVocabText, position, false);
            }
        });

        return view;
    }

    private void changeVocab(TextInputEditText textInputEditText, int position, boolean firstVocab){
        VocabTuple vocab = mArrVocabData.get(position);
        if(firstVocab)
            vocab.setLearningVocab(textInputEditText.getText().toString());
        else
            vocab.setMainVocab(textInputEditText.getText().toString());
        vocab.setNotFoundInDict(false);
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }}
