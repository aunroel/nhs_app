package com.uk.ac.ucl.carefulai.ui.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uk.ac.ucl.carefulai.DatabaseHelper;
import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;


import androidx.appcompat.app.AppCompatActivity;

public class FirstEntryActivity extends AppCompatActivity {

    private static final String myPreference = "dataPreference";


    SharedPreferences dataPreferences;

    private SeekBar simpleSeekBar;

    private Button saveUserScore;

    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_fragment_4);

        dataPreferences = this.getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        takeFirstScore(dataPreferences);

        //setTitle("Setup Your Activities");
    }


        private void takeFirstScore(final SharedPreferences dataPreferences) {

            simpleSeekBar = findViewById(R.id.simpleSeekBar);

            simpleSeekBar.setProgress(5);

            simpleSeekBar.setMax(10);

            saveUserScore = (Button) findViewById(R.id.save_intro_seekbar);

            saveUserScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveUserScore(dataPreferences);
                }
            });

        }

        private void saveUserScore(SharedPreferences dataPreferences) {

            SharedPreferences.Editor editor = dataPreferences.edit();

            int userScore = simpleSeekBar.getProgress();

            int week = (int) myDb.getThisWeekNumber();

            myDb.insertUserScore(String.valueOf(week),userScore);

            editor.putInt("recentScore", userScore);

            if (editor.commit()) startActivity(new Intent(this, AppActivity.class));

        }


}
