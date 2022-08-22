package com.example.vokabelquizapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vokabelquizapp.Activities.TakePicture.CameraActivity;
import com.example.vokabelquizapp.Activities.TakePicture.CropImageActivity;
import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.Fragments.NewListDialogFragment;
import com.example.vokabelquizapp.Fragments.SourceImageDialogFragment;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.Adapter.VocabAdapter;
import com.example.vokabelquizapp.classes.Adapter.VocabListAdapter;
import com.example.vokabelquizapp.classes.VocabData.VocabTuple;

public class NewVocabsListViewActivity extends AppCompatActivity {

    private VocabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_list_manager);

        Button submitButton = findViewById(R.id.submitVocabBtn);
        Button newScanButton = findViewById(R.id.addScanBtn);
        ImageButton newVocabButton = findViewById(R.id.newVocabTupleBtn);
        TextView listNameText = findViewById(R.id.toolbarText);
        ListView newVocabsListView = findViewById(R.id.newVocabList);

        listNameText.setText(AppData.currentVocabList.getName());

        String activity = getIntent().getStringExtra("Activity");

        if(activity != null && activity.equals("Edit Item")){
            ImageButton vocabListSettings = findViewById(R.id.vocabListSettingsBtn);
            ImageButton returnBtn = findViewById(R.id.returnBtn);
            RelativeLayout settingsLayout = findViewById(R.id.settingsLayout);
            findViewById(R.id.closeLayout).setVisibility(View.GONE);
            settingsLayout.setVisibility(View.VISIBLE);
            vocabListSettings.setVisibility(View.VISIBLE);
            returnBtn.setVisibility(View.VISIBLE);

            vocabListSettings.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(NewVocabsListViewActivity.this, ListSettingsActivity.class));
                }
            });
            returnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }

        adapter = new VocabAdapter(NewVocabsListViewActivity.this, AppData.currentVocabList.getVocabList());
        newVocabsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        newScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                new SourceImageDialogFragment().show(fm, "fragment_alert");
            }
        });

        newVocabButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNewVocabTupleDialog();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = NewVocabsListViewActivity.this;
                if(activity != null && activity.equals("Edit Item")){
                    Intent i = new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    Toast.makeText(context, "Vocabulary List Updated!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // User Confirmation to delete not completed Vocab Tuples
                FragmentManager fm = getSupportFragmentManager();
                new NewListDialogFragment().show(fm, "fragment_alert");
            }
        });
    }

    private void createNewVocabTupleDialog(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_vocab_popup, null);

        dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView title = dialogView.findViewById(R.id.toolbarText);
        ImageButton exitBtn = dialogView.findViewById(R.id.closeBtn);
        EditText firstVocabText = dialogView.findViewById(R.id.newFirstVocab);
        EditText secondVocabText = dialogView.findViewById(R.id.newSecondVocab);
        Button submitButton = dialogView.findViewById(R.id.newVocabSubmit);
        Button cancelButton = dialogView.findViewById(R.id.newVocabCancel);

        exitBtn.setVisibility(View.VISIBLE);
        title.setText(R.string.new_vocabulary);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstVocabString = firstVocabText.getText().toString();
                String secondVocabString = secondVocabText.getText().toString();
                Context context = NewVocabsListViewActivity.this;
                if(firstVocabString.equals("")){
                    Toast.makeText(context, "First Vocabulary is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(secondVocabString.equals("")){
                    Toast.makeText(context, "Second Vocabulary is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Toast.makeText(context, "New Vocabulary created!", Toast.LENGTH_SHORT).show();
                }
                VocabTuple newVocab = new VocabTuple(firstVocabString,secondVocabString,false);
                AppData.currentVocabList.addTupleToVocabList(newVocab);
                adapter.notifyDataSetChanged();
                dialogBuilder.dismiss();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}