package com.uk.ac.ucl.carefulai.ui.nudge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ActivitySupportActivity extends AppCompatActivity {

    private SharedPreferences careNetworkPreferences;

    private SharedPreferences dataPreferences;

    private static final String myPreference = "careNetwork";

    private String chosenActivity, chosenHelp, userName, phoneNumber;

    private Button sendButton, cancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        setTitle("Activity Support");

        careNetworkPreferences = this.getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        dataPreferences = this.getSharedPreferences("dataPreference", Context.MODE_PRIVATE);

        userName = careNetworkPreferences.getString("userName", "");

        phoneNumber = careNetworkPreferences.getString("carer_support_ref", "");

        sendButton = findViewById(R.id.activity_support_send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });

        cancelButton = findViewById(R.id.activity_support_cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome();
            }
        });
        populateActivitySpinner(careNetworkPreferences, dataPreferences);

        populateYNSpinner();
    }

    private void backToHome() {
        startActivity(new Intent(this, AppActivity.class));
    }
    private void populateActivitySpinner(SharedPreferences careNetworkPreferences, SharedPreferences dataPreferences) {

        ArrayList<String> activities = new ArrayList<>();

        SharedPreferences.Editor editor = dataPreferences.edit();

        for (int i = 1; i < 4; i++) {

            if (dataPreferences.getBoolean("activityNudge" + i, false)) {
                activities.add(careNetworkPreferences.getString("activityKey" + i, ""));
                editor.putBoolean("activityNudge" + i, false);
            }
        }

        editor.apply();

        Spinner activitySpinner = (Spinner) findViewById(R.id.support_activity_spinner);

        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activities);

        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activitySpinner.setAdapter(activityAdapter);

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                chosenActivity = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void populateYNSpinner() {
        Spinner activitySpinner = (Spinner) findViewById(R.id.support_yn_spinner);

        ArrayList<String> activities = new ArrayList<String>(){{ add("Yes"); add("No"); }};

        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activities);

        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activitySpinner.setAdapter(activityAdapter);

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                chosenHelp = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean isNullOrEmpty(ArrayList<String> strings) {

        for (int i = 0; i < strings.size(); i++) {

            String str = strings.get(i);

            if(str == null || str.trim().isEmpty()) {

                return true;
            }

        }
        return false;
    }

    private void sendSMS() {

        final ArrayList<String> params = new ArrayList<String>() {{ add(chosenActivity); add(chosenHelp); }};


        if (isNullOrEmpty(params)) {

            Toast.makeText(this, "Please Fill In All Fields", Toast.LENGTH_SHORT).show();

            return;

        }

        if ("No".equals(chosenHelp)) {
            Toast.makeText(this, "Please select 'Yes' to request carer support", Toast.LENGTH_SHORT).show();
            return;
        }

        String textMessage = "Hello "
                + ", Just to let you know that I am interested in"
                + chosenActivity
                + ". Would it be possible to join a local group to learn what is happening locally? "
                + " All the best, "
                + userName;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,null, textMessage,null,null);
            Toast.makeText(this, "To: " + phoneNumber + ", " + textMessage, Toast.LENGTH_LONG).show();

            startActivity(new Intent(this, AppActivity.class));

            //root.setEnabled(false);
        }
        catch (Exception e){
            Toast.makeText(this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}
