package com.example.vokabelquizapp.classes.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.example.vokabelquizapp.Activities.MainActivity;
import com.example.vokabelquizapp.Activities.NewVocabsListViewActivity;
import com.example.vokabelquizapp.Activities.quizOneActivity;
import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.Fragments.SourceImageDialogFragment;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.VocabData.VocabList;


import java.util.List;

public class VocabListAdapter extends BaseAdapter {

    private Context mContext;
    private List<VocabList> mArrVocabData;

    public VocabListAdapter(Context context, List arrVocabData) {
        super();
        mContext = context;
        mArrVocabData = arrVocabData;
    }

    public int getCount() {
        // return the number of records
        return mArrVocabData.size() + 1;
    }

    // getView method is called for each item of ListView
    public View getView(int position, View view, ViewGroup parent) {
        // inflate the layout for each item of listView
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // New List Item
        if(position == 0){
            view = inflater.inflate(R.layout.vocablist_new_item, parent, false);

            LinearLayout createNewList = view.findViewById(R.id.createVocabListLayout);

            createNewList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showEnterListNameDialog();
                }
            });
        }
        // List Item
        else {

            view = inflater.inflate(R.layout.vocablist_item, parent, false);

            LinearLayout editVocabListBtn = view.findViewById(R.id.editVocabListLayout);
            TextView vocabListName = view.findViewById(R.id.vocabListName);
            TextView vocabNumber = view.findViewById(R.id.vocabTuplesAmount);
            ImageButton playQuizBtn = view.findViewById(R.id.playQuizBtn);

            vocabNumber.setText(String.valueOf(mArrVocabData.get(position - 1).getVocabList().size()));
            vocabListName.setText(mArrVocabData.get(position - 1).getName());

            playQuizBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VocabList item = mArrVocabData.get(position - 1);
                    if(item.getVocabList().size() > 0)
                        startQuiz(item);
                    else
                        Toast.makeText(mContext, "Please add Vocabularies to the List first.", Toast.LENGTH_SHORT).show();
                }
            });
            editVocabListBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(mContext, NewVocabsListViewActivity.class);
                    AppData.currentVocabList = mArrVocabData.get(position - 1);
                    //i.putExtra("VocabData", mArrVocabData.get(position - 1).getVocabList());
                    i.putExtra("Activity","Edit Item");
                    mContext.startActivity(i);
                }
            });
        }
        return view;
    }

    private void showEnterListNameDialog(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(mContext).create();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.enter_vocablistname, null);
        dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView title = dialogView.findViewById(R.id.toolbarText);
        ImageButton closeBtn = dialogView.findViewById(R.id.closeBtn);
        title.setText(R.string.new_List);
        EditText vocabListName = dialogView.findViewById(R.id.enterListName);
        ImageButton exitBtn = dialogView.findViewById(R.id.closeBtn);
        Button submitBtn = dialogView.findViewById(R.id.submitNameBtn);
        submitBtn.setEnabled(false);
        closeBtn.setVisibility(View.VISIBLE);

        vocabListName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                submitBtn.setEnabled(!editable.toString().equals(""));
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*if(vocabListName.getText().toString().equals("")){
                    Toast.makeText(mContext, "Enter a Vocabulary List Name!", Toast.LENGTH_SHORT).show();
                    //Animation growlAnim = AnimationUtils.loadAnimation(mContext, R.anim.input_error);
                    //vocabListName.startAnimation(growlAnim);
                    return;
                }*/
                dialogBuilder.dismiss();
                AppData.currentVocabList = new VocabList(vocabListName.getText().toString());
                FragmentManager fm = ((MainActivity)mContext).getSupportFragmentManager();
                new SourceImageDialogFragment().show(fm, "fragment_alert");
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void startQuiz(VocabList vocabList){
        Intent i = new Intent(mContext, quizOneActivity.class);
        i.putExtra("QuizList", vocabList.getVocabList());
        mContext.startActivity(i);
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
}
