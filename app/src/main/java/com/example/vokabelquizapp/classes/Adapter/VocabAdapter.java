package com.example.vokabelquizapp.classes.Adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.Fragments.VocabItemEditDialogFragment;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.VocabData.VocabTuple;

import java.util.ArrayList;

public class VocabAdapter extends BaseAdapter {

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
        view = inflater.inflate(R.layout.vocab_item, parent, false);

        // get the reference of textView and button
        LinearLayout vocabItemLayout = view.findViewById(R.id.vocabItemLayout);
        ImageButton ttsBtn = view.findViewById(R.id.textToSpeechBtn);
        TextView firstVocabText = view.findViewById(R.id.firstVocabEdit);
        TextView secondVocabText = view.findViewById(R.id.secondVocabEdit);

        // Set Vocabs Text
        firstVocabText.setText(mArrVocabData.get(position).getLearningVocab());
        secondVocabText.setText(mArrVocabData.get(position).getMainVocab());

        firstVocabText.setHint(AppData.learningLanguageString + " Vocab");
        secondVocabText.setHint(AppData.mainLanguageString + " Vocab");

        vocabItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((FragmentActivity)mContext).getSupportFragmentManager();
                new VocabItemEditDialogFragment(mArrVocabData.get(position), mContext).show(fm, "fragment_alert");
            }
        });

        ttsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppData.textToSpeech.speak(mArrVocabData.get(position).getLearningVocab(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        return view;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }}
