package com.uk.ac.ucl.carefulai.ui.nudge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import com.uk.ac.ucl.carefulai.DatabaseHelper;
import com.uk.ac.ucl.carefulai.LifeDataUpdate;
import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WellbeingActivity extends AppCompatActivity {

    private SeekBar simpleSeekBar;

    private SharedPreferences dataPreferences;

    private DatabaseHelper myDb;

    private LifeDataUpdate lifeDataUpdate;

    private final String myPreferences = "dataPreference";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epic_yesnofeedback);

        dataPreferences = this.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);

        myDb = new DatabaseHelper(this);

        int score = dataPreferences.getInt("recentScore", 0);

        simpleSeekBar = findViewById(R.id.wellbeingSeekBar);

        simpleSeekBar.setProgress(score);

        simpleSeekBar.setMax(10);

        lifeDataUpdate = new LifeDataUpdate(this);

        Button saveUserScore = (Button) findViewById(R.id.saveSeekbar);

        myDb = new DatabaseHelper(this);

        saveUserScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserScore(myDb, lifeDataUpdate);
            }
        });

    }

    private void saveUserScore(DatabaseHelper myDb, LifeDataUpdate lifeDataUpdate) {

        int userScore = simpleSeekBar.getProgress();

        int week = (int) myDb.getThisWeekNumber();

        myDb.insertUserScore(String.valueOf(week),userScore);

        lifeDataUpdate.saveDataToInitialState();

        startActivity(new Intent(this, AppActivity.class));

    }
}
