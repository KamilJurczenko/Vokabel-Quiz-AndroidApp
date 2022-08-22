package com.example.vokabelquizapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.Fragments.NewListDialogFragment;
import com.example.vokabelquizapp.Fragments.ReturnToMainDialogFragment;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.VocabData.VocabTuple;

import java.util.ArrayList;
import java.util.Collections;

public class quizOneActivity extends AppCompatActivity {

    private TextView queryVocab;
    private EditText inputVocab;
    private ImageButton textToSpeechBtn;
    private Button confirmInputVocab;
    private ImageView feedbackImg;
    private TextView correctVocab;

    private ArrayList<VocabTuple> vocabularies;
    private VocabTuple currentVocabQueryTuple;
    private int currentVocabIndex;

    private int maxVocables;
    private int correctVocables;

    private View firstView;
    private View secondView;
    private View currentView;
    private View nextView;

    private boolean animationStopped = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_one);

        firstView = findViewById(R.id.vocabQLayout1);
        secondView = findViewById(R.id.vocabQLayout2);
        feedbackImg = findViewById(R.id.feedbackImage);

        confirmInputVocab = findViewById(R.id.nextVocabBtn);
        TextView skipVocabQuery = findViewById(R.id.skipVocabQuery);
        TextView title = findViewById(R.id.toolbarText);
        ImageButton exitBtn = findViewById(R.id.closeBtn);

        exitBtn.setVisibility(View.VISIBLE);
        title.setVisibility(View.INVISIBLE);

        currentVocabIndex = 0;

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                new ReturnToMainDialogFragment().show(fm, "fragment_alert");
            }
        });

        confirmInputVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(animationStopped)
                    checkInputVocab();
            }
        });

        skipVocabQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(animationStopped)
                    nextVocabQuery();
            }
        });

        // Get Vocabulary List
        vocabularies = (ArrayList<VocabTuple>)getIntent().getSerializableExtra("QuizList");
        maxVocables = vocabularies.size();
        // Randomize Vocabulary List and Query them one by one afterwards
        Collections.shuffle(vocabularies);
        nextVocabQuery();
    }

    private void initMainView(){
        if(currentView == firstView){
            nextView = firstView;
            currentView = secondView;
        }
        else{
            currentView = firstView;
            nextView = secondView;
        }
        feedbackImg.setVisibility(View.INVISIBLE);
        confirmInputVocab.setEnabled(false);
        correctVocab = currentView.findViewById(R.id.correctionVocab);
        queryVocab = currentView.findViewById(R.id.queryVocabText);
        inputVocab = currentView.findViewById(R.id.inputVocab);
        inputVocab.setHint("Enter " + AppData.learningLanguageString  + " Word");
        inputVocab.setTextColor(ContextCompat.getColor(quizOneActivity.this, R.color.white));
        inputVocab.setText("");
        correctVocab.setText("");
        textToSpeechBtn = currentView.findViewById(R.id.textToSpeechBtn);
        textToSpeechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppData.textToSpeech.speak(currentVocabQueryTuple.getLearningVocab(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        inputVocab.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                confirmInputVocab.setEnabled(!s.toString().equals(""));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void checkInputVocab(){
        String inputString = inputVocab.getText().toString();
        feedbackImg.setVisibility(View.VISIBLE);
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        if(inputString.equals(currentVocabQueryTuple.getLearningVocab())){
            // Correct Vocable Input
            feedbackImg.setBackgroundResource(R.drawable.roundedbutton_green);
            feedbackImg.setImageResource(R.drawable.hook);
            correctVocables++;
            inputVocab.setTextColor(ContextCompat.getColor(quizOneActivity.this, R.color.green));
        }
        else{
            feedbackImg.setBackgroundResource(R.drawable.roundedbutton_red);
            feedbackImg.setImageResource(R.drawable.cross);
            inputVocab.setTextColor(ContextCompat.getColor(quizOneActivity.this, R.color.red));
            correctVocab.setText(currentVocabQueryTuple.getLearningVocab());
            correctVocab.startAnimation(scaleUp);
        }
        feedbackImg.startAnimation(scaleUp);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                nextVocabQuery();
            }
        }, 1250);

    }

    private void nextVocabQuery(){
        if(currentVocabIndex + 1 > vocabularies.size()){
            showResultPopup();
            return;
        }
        currentVocabQueryTuple = vocabularies.get(currentVocabIndex);
        if(currentVocabIndex > 0) {
            Animation currentViewTrans = AnimationUtils.loadAnimation(this, R.anim.layout_escape_left);
            Animation nextViewTrans = AnimationUtils.loadAnimation(this, R.anim.layout_showfrom_right);

            View tmpView = currentView;
            animationStopped = false;
            currentView.startAnimation(currentViewTrans);
            nextView.startAnimation(nextViewTrans);
            nextView.setVisibility(View.VISIBLE);
            currentViewTrans.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tmpView.setVisibility(View.INVISIBLE);
                    animationStopped = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        initMainView();
        queryVocab.setText(currentVocabQueryTuple.getMainVocab());
        currentVocabIndex++;
    }

    private void showResultPopup(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.quiz_one_result_popup, null);
        dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setCanceledOnTouchOutside(false);

        TextView title = dialogView.findViewById(R.id.toolbarText);
        TextView correctVocablesText = dialogView.findViewById(R.id.correctVocablesText);
        TextView maxVocablesText = dialogView.findViewById(R.id.maxVocablesText);
        TextView resultText = dialogView.findViewById(R.id.resultText);
        Button returnToHomeBtn = dialogView.findViewById(R.id.returnToMainBtn);
        Button repeatVocabQueryBtn = dialogView.findViewById(R.id.retryQuizOneBtn);

        float div = 0;
        if(correctVocables > 0)
            div = maxVocables / correctVocables;

        if(div > 0.7)
            resultText.setText(R.string.resultGood);
        else
            resultText.setText(R.string.resultBad);

        title.setText(R.string.result);
        correctVocablesText.setText(String.valueOf(correctVocables));
        maxVocablesText.setText(String.valueOf(maxVocables));

        returnToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                startActivity(new Intent(quizOneActivity.this, MainActivity.class));
            }
        });
        // TODO Spring animation effect

        repeatVocabQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                Intent i = new Intent(quizOneActivity.this, quizOneActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("QuizList", vocabularies);
                startActivity(i);
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}