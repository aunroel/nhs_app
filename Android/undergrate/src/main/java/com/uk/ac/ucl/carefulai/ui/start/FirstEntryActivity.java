package com.uk.ac.ucl.carefulai.ui.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

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

        simpleSeekBar = (SeekBar) findViewById(R.id.introSeekBar);

        saveUserScore = (Button) findViewById(R.id.save_intro_seekbar);

        myDb = new DatabaseHelper(this);

        takeFirstScore(dataPreferences, simpleSeekBar, saveUserScore, myDb);

        //setTitle("Setup Your Activities");
    }


        private void takeFirstScore(final SharedPreferences dataPreferences, final SeekBar simpleSeekBar, Button saveUserScore, final DatabaseHelper myDb) {

            simpleSeekBar.setProgress(5);

            simpleSeekBar.setMax(10);

            saveUserScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveUserScore(dataPreferences, simpleSeekBar, myDb);
                }
            });

        }

        private void saveUserScore(SharedPreferences dataPreferences, SeekBar simpleSeekBar, DatabaseHelper myDb) {

            SharedPreferences.Editor editor = dataPreferences.edit();

            int userScore = simpleSeekBar.getProgress();

            int week = (int) myDb.getThisWeekNumber();

            myDb.insertUserScore(String.valueOf(week),userScore);

            editor.putInt("recentScore", userScore);

            if (editor.commit()) startActivity(new Intent(this, AppActivity.class));

        }


}
