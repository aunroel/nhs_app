package com.uk.ac.ucl.carefulai.ui.start;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;

//prompts user to allow the app to collect steps and contact data, as well as optionally send data to map visualisation

public class PermissionsActivity extends AppCompatActivity {
    private View statsbutton; // 'Start Tracking' button
    private TextView toggleTracking; // 'Start Tracking' text
    private Button mainlauncher; // 'Save' button
    private Switch aSwitch; // Switch that allows the app to send data to map
    private SharedPreferences dataPreferences; // stores preferences for tracking and sharing
    private final String myPreferences = "dataPreference";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        dataPreferences = this.getSharedPreferences(myPreferences, Context.MODE_PRIVATE); //use "dataPreference" as the key-value pair list, separate from careNetwork

        toggleTracking = (TextView) findViewById(R.id.toggleTracking);

        if (dataPreferences.getBoolean("isTracking", false)) //if activity was started from main flow instead of initial setup
            toggleTracking.setText("Stop Tracking"); //set toggleTracking to "Stop Tracking"

        else toggleTracking.setText("Start Tracking"); //else set to "Start Tracking"

        statsbutton = (View) findViewById(R.id.view2);
        //toggle the text on the button onClick
        statsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (toggleTracking.getText().equals("Stop Tracking")) {
                    toggleTracking.setText("Start Tracking");
                    return;
                }
                toggleTracking.setText("Stop Tracking");

            }
        });

        aSwitch = findViewById(R.id.switch1);

        mainlauncher = (Button) findViewById(R.id.mainstarter);

        mainlauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoscreen(dataPreferences);
            }
        });

        Button permissionsBack = findViewById(R.id.permissionsBackButton);
        // "No Thanks" button in Permissions Activity

        permissionsBack.setOnClickListener(new View.OnClickListener() { //onClick, go back to SetupActivities

            @Override
            public void onClick(View view) {
                startActivity(new Intent(PermissionsActivity.this, SetupActivitiesActivity.class));
            }
        });

    }


    @SuppressLint("SetTextI18n")
    public void infoscreen(SharedPreferences dataPreferences) { // called when "Save" button is clicked

        SharedPreferences.Editor editor = dataPreferences.edit();

        if (dataPreferences.getBoolean("isTracking", false)) { //if isTracking, then activity was called from main flow
            if (toggleTracking.getText().equals("Start Tracking")) { //if the tracking is disabled, change dataPreferences
                editor.putBoolean("isTracking", false);
                editor.commit();
            }
            startActivity(new Intent(this, AppActivity.class)); //go back to AppActivity
        }
        //isTracking is off, so if tracking is still disabled, user cannot continue so prompt user to enable tacking
        else if (toggleTracking.getText().equals("Start Tracking")){
            Toast.makeText(PermissionsActivity.this, "Please enable data tracking to continue. Click on 'Start Tracking'!", Toast.LENGTH_LONG).show();
        }
        //tracking is enabled
        else {
            if (aSwitch.isChecked()) { //check if map sharing is enabled
                editor.putBoolean("remoteSharing", true); //if so add remoteSharing to dataPreferences as true
            } else {
                editor.putBoolean("remoteSharing", false); //else so add remoteSharing to dataPreferences as false
            }

            editor.putBoolean("isTracking", true); //put isTracking as true in dataPreferences

            // if data is saved continue to the first user entry activity
            if (editor.commit()) startActivity(new Intent(this, FirstEntryActivity.class));
        }
    }
}
