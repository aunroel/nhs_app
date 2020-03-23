package com.uk.ac.ucl.carefulai.ui.start;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;

import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionsActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 42;
    private View statsbutton;
    private TextView toggleTracking;
    private Button mainlauncher;
    private Switch aSwitch;
    private SharedPreferences dataPreferences;
    private final String myPreferences = "dataPreference";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        dataPreferences = this.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);

        toggleTracking = (TextView) findViewById(R.id.toggleTracking);

        if (dataPreferences.getBoolean("isTracking", false))
            toggleTracking.setText("Stop Tracking");

        else toggleTracking.setText("Start Tracking");

        statsbutton = (View) findViewById(R.id.view2);
        statsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = dataPreferences.edit();

                if (dataPreferences.getBoolean("isTracking", false)) {
                    toggleTracking.setText("Start Tracking");
                    editor.putBoolean("isTracking", false);
                    editor.commit();
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

    }


    @SuppressLint("SetTextI18n")
    public void infoscreen(SharedPreferences dataPreferences) {

        SharedPreferences.Editor editor = dataPreferences.edit();

        if (dataPreferences.getBoolean("isTracking", false)) {
            editor.putBoolean("isTracking", false);
            toggleTracking.setText("Start Tracking");
            if (editor.commit()) startActivity(new Intent(this, AppActivity.class));
        }
        else if (toggleTracking.getText().equals("Start Tracking")){
            Toast.makeText(PermissionsActivity.this, "Please enable data tracking to continue. Click on 'Start Tracking'!", Toast.LENGTH_LONG).show();
        }
        else {
            if (aSwitch.isChecked()) {
                editor.putBoolean("remoteSharing", true);
            } else {
                editor.putBoolean("remoteSharing", false);
            }

            editor.putBoolean("isTracking", true);

            if (editor.commit()) startActivity(new Intent(this, FirstEntryActivity.class));
        }
    }
}
