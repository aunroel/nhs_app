package com.uk.ac.ucl.carefulai.ui.nudge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalNudgeActivity extends AppCompatActivity {

    private ImageView personalYes, personalNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_nudge);

        personalYes = findViewById(R.id.personal_yes);

        personalYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendActivity();
            }
        });

        personalNo = findViewById(R.id.personal_no);
        personalNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToHome();
            }
        });

    }

    private void startSendActivity() {
        startActivity(new Intent(this, SendActivity.class));
    }

    private void backToHome() {
        startActivity(new Intent(this, AppActivity.class));
    }

}
