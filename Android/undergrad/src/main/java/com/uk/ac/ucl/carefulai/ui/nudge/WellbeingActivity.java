package com.uk.ac.ucl.carefulai.ui.nudge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.DatabaseHelper;
import com.uk.ac.ucl.carefulai.LifeDataUpdate;
import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;


//collects the user's indication of their wellbeing score
public class WellbeingActivity extends AppCompatActivity {

    private SeekBar simpleSeekBar; //seekbar to collect the score

    private SharedPreferences dataPreferences; //used to fetch the model calculated score

    private DatabaseHelper myDb; //used to insert the user's indication

    private LifeDataUpdate lifeDataUpdate; //after complete, reset the data to initial state for the next week

    private final String myPreferences = "dataPreference"; //get the dataPreference key-value list

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epic_yesnofeedback);

        dataPreferences = this.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);

        myDb = new DatabaseHelper(this);

        int score = dataPreferences.getInt("recentScore", 0); //recently calculated score in Alarm class

        simpleSeekBar = findViewById(R.id.wellbeingSeekBar);

        simpleSeekBar.setProgress(score); //set the default value to the model score

        simpleSeekBar.setMax(10); //max of ten

        lifeDataUpdate = new LifeDataUpdate(this);

        Button saveUserScore = (Button) findViewById(R.id.saveSeekbar);

        myDb = new DatabaseHelper(this);

        saveUserScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserScore(myDb, lifeDataUpdate); //save button onClickListener, saves user score to DB, resets LifeDataUpdate, and returns to AppActivity
            }
        });

    }

    private void saveUserScore(DatabaseHelper myDb, LifeDataUpdate lifeDataUpdate) {

        int userScore = simpleSeekBar.getProgress(); //get the user input

        int week = (int) myDb.getThisWeekNumber(); //get this week number

        myDb.insertUserScore(String.valueOf(week),userScore); //insert the userScore into the DB (used to calculate error rate in PostRequest)

        lifeDataUpdate.saveDataToInitialState(); //reset LifeDataUpdate

        startActivity(new Intent(this, AppActivity.class)); //return to AppActivity

    }
}
