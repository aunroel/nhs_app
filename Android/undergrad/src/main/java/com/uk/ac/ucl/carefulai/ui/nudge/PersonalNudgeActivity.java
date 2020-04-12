package com.uk.ac.ucl.carefulai.ui.nudge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;

//Started in AppActivity if a nudge was triggered
public class PersonalNudgeActivity extends AppCompatActivity {

    private ImageView personalYes, personalNo; //buttons to ask the user whether they would like to send a wellbeing message or not

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
        }); //if yes, open the SendActivity to construct the message

        personalNo = findViewById(R.id.personal_no);
        personalNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToHome();
            }
        }); //if no, go back to AppActivity

    }

    //open SendActivity
    private void startSendActivity() {
        startActivity(new Intent(this, SendActivity.class));
    }

    //open AppActivity
    private void backToHome() {
        startActivity(new Intent(this, AppActivity.class));
    }

}
