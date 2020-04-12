package com.uk.ac.ucl.carefulai.ui.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.DatabaseHelper;
import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;

//requests for first user wellbeing score to add to diary

public class FirstEntryActivity extends AppCompatActivity {

    private static final String myPreference = "dataPreference"; //used below for dataPreferences

    SharedPreferences dataPreferences; //used below to put the inital score as the most recent score into Shared Preferences to display on AppActivity

    private SeekBar simpleSeekBar; //seekbar to input wellbeing score

    private Button saveUserScore; //button to save and continue to main flow

    private DatabaseHelper myDb; // add first week data (just score) to SQLite Database as week 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_page_fragment_4);

        dataPreferences = this.getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        simpleSeekBar = (SeekBar) findViewById(R.id.introSeekBar);

        saveUserScore = (Button) findViewById(R.id.save_intro_seekbar);

        myDb = new DatabaseHelper(this);

        takeFirstScore(dataPreferences, simpleSeekBar, saveUserScore, myDb); // function below that sets the onClickListener, and the scale of the seekbar

        //setTitle("Setup Your Activities");
    }


        private void takeFirstScore(final SharedPreferences dataPreferences, final SeekBar simpleSeekBar, Button saveUserScore, final DatabaseHelper myDb) {

            simpleSeekBar.setProgress(5); //start from the middle at 5

            simpleSeekBar.setMax(10); // wellbeing scores from 1 to 10

            saveUserScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveUserScore(dataPreferences, simpleSeekBar, myDb); // function that adds score to week 1 and continues to next activity
                }
            });

        }

        private void saveUserScore(SharedPreferences dataPreferences, SeekBar simpleSeekBar, DatabaseHelper myDb) {

            SharedPreferences.Editor editor = dataPreferences.edit();

            int userScore = simpleSeekBar.getProgress(); //get the user score input (default at 5)

            int week = (int) myDb.getThisWeekNumber(); //get the latest week number (should be 1)

            myDb.insertUserScore(String.valueOf(week),userScore); //put the user score into db
            myDb.insertData(0,0,0); //no steps, calls, or texts collected so all 0
            myDb.insertScore(Long.toString(myDb.getThisWeekNumber()), userScore); //user score and calculated score the same, so insert userScore there as well

            editor.putInt("recentScore", userScore); //put the user score into Shared Prefs

            // if save is successful, go to main flow (AppActivity)
            if (editor.commit()) startActivity(new Intent(this, AppActivity.class));

        }


}
