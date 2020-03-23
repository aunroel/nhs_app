package com.uk.ac.ucl.carefulai.ui.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uk.ac.ucl.carefulai.R;


import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class SetupActivitiesActivity extends AppCompatActivity {

    private static final String myPreference = "careNetwork";
    private static final String firstActivity = "activityKey1";
    private static final String secondActivity = "activityKey2";
    private static final String thirdActivity = "activityKey3";
    private static final String stepsTarget = "stepsTarget";
    private static final String contactTarget = "contactTarget";
    private static final String carerSupportRef = "carer_support_ref";
    private static final String postCode = "postcode";

    private Button noThanks3;
    TextView activity1, activity2, activity3, steps, contact, carer_support_ref, postcode;

    SharedPreferences careNetworkPreferences;

    private ArrayList<TextView> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_fragment_3);

        activity1 = (TextView) findViewById(R.id.activity1);
        activity2 = (TextView) findViewById(R.id.activity2);
        activity3 = (TextView) findViewById(R.id.activity3);
        steps = (TextView) findViewById(R.id.steps);
        contact = (TextView) findViewById(R.id.contact);
        carer_support_ref = (TextView) findViewById(R.id.carer_support_ref);
        postcode = (TextView) findViewById(R.id.postcode);

        careNetworkPreferences = getApplicationContext().getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);

        if (careNetworkPreferences.contains(firstActivity)) {
            activity1.setText(careNetworkPreferences.getString(firstActivity, ""));
        }
        if (careNetworkPreferences.contains(secondActivity)) {
            activity2.setText(careNetworkPreferences.getString(secondActivity, ""));
        }
        if (careNetworkPreferences.contains(thirdActivity)) {
            activity3.setText(careNetworkPreferences.getString(thirdActivity, ""));
        }
        if (careNetworkPreferences.contains(stepsTarget)) {
            steps.setText(String.valueOf(careNetworkPreferences.getInt(stepsTarget, 0)));
        }
        if (careNetworkPreferences.contains(contactTarget)) {
            contact.setText((String.valueOf(careNetworkPreferences.getInt(contactTarget, 0))));
        }
        if (careNetworkPreferences.contains(carerSupportRef)) {
            carer_support_ref.setText(careNetworkPreferences.getString(carerSupportRef, ""));
        }
        if (careNetworkPreferences.contains(postCode)) {
            postcode.setText(careNetworkPreferences.getString(postCode, ""));
        }

        noThanks3 = findViewById(R.id.noThanks3);
        noThanks3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        setTitle("Setup Your Activities");
    }

    private void back() {
        startActivity(new Intent(this, PrimaryCareNetworkActivity.class));
    }

    public void wellbeingscreen(View view){
        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        values = new ArrayList<TextView>() {{ add(activity1); add(activity2); add(activity3); }};

        int i = 1;

        for (TextView textView : values) {

            String value = textView.getText().toString();

            careNetworkPreferencesEditor.putString("activityKey" + i, value);

            i++;

        }

        String userSteps = steps.getText().toString();

        String userContact = contact.getText().toString();

        String userSupportRef = carer_support_ref.getText().toString();

        String userPostcode = postcode.getText().toString();


        if (userSteps.isEmpty() || userContact.isEmpty() || userSupportRef.isEmpty() || userPostcode.isEmpty()) {
            Toast.makeText(this, "Please Fill In All Fields ", Toast.LENGTH_SHORT).show();
            return;
        }

        careNetworkPreferencesEditor.putInt(stepsTarget, Integer.parseInt(userSteps));
        careNetworkPreferencesEditor.putInt(contactTarget, Integer.parseInt(userContact));
        careNetworkPreferencesEditor.putString(carerSupportRef, userSupportRef);
        careNetworkPreferencesEditor.putString(postCode, userPostcode);

        if (careNetworkPreferencesEditor.commit()) startActivity(new Intent(this, PermissionsActivity.class));

    }

}
