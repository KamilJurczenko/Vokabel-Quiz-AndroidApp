package com.example.vokabelquizapp.classes;

import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;

import com.example.vokabelquizapp.R;

public class ExitButtonDialog {

    public ExitButtonDialog(View dialogView, AlertDialog dialog) {
        ImageButton exitBtn = dialogView.findViewById(R.id.closeBtn);

        exitBtn.setVisibility(View.VISIBLE);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
