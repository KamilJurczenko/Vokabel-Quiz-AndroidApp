package com.example.vokabelquizapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vokabelquizapp.AppData;
import com.example.vokabelquizapp.Fragments.ListDeleteDialogFragment;
import com.example.vokabelquizapp.R;
import com.example.vokabelquizapp.classes.SharedPrefVocabList;
import com.example.vokabelquizapp.classes.WriteCSV;

public class ListSettingsActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_settings);

        context = this;

        ImageButton returnSettingsBtn = findViewById(R.id.returnBtn);
        TextView title = findViewById(R.id.toolbarText);
        LinearLayout changeListNameLyt = findViewById(R.id.changeListNameLayout);
        LinearLayout deleteListLyt = findViewById(R.id.deleteListLayout);
        LinearLayout exportListLyt = findViewById(R.id.exportListLayout);

        returnSettingsBtn.setVisibility(View.VISIBLE);
        title.setText(R.string.list_settings);

        returnSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        exportListLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WriteCSV(AppData.currentVocabList,context);
            }
        });

        changeListNameLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialogBuilder = new AlertDialog.Builder(ListSettingsActivity.this).create();
                LayoutInflater inflater = ListSettingsActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.enter_vocablistname, null);
                dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                TextView title = dialogView.findViewById(R.id.toolbarText);
                ImageButton exitBtn = dialogView.findViewById(R.id.closeBtn);
                Button submitButton = dialogView.findViewById(R.id.submitNameBtn);
                EditText vocabListName = dialogView.findViewById(R.id.enterListName);

                exitBtn.setVisibility(View.VISIBLE);
                vocabListName.setText(AppData.currentVocabList.getName());
                title.setText(R.string.newVocabListname);

                exitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                        Toast.makeText(context, "List renamed successfully!", Toast.LENGTH_SHORT).show();
                        AppData.currentVocabList.setName(vocabListName.getText().toString());
                        SharedPrefVocabList sharedPrefVocabList = new SharedPrefVocabList(context,"VocabList");
                        sharedPrefVocabList.saveVocabList(AppData.loadedLanguageVocabs);
                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }
        });

        deleteListLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                new ListDeleteDialogFragment().show(fm, "fragment_alert");
            }
        });
    }
}