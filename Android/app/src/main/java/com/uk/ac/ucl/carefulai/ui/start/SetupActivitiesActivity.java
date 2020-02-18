package com.uk.ac.ucl.carefulai.ui.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;

import java.util.ArrayList;

public class SetupActivitiesActivity extends AppCompatActivity {

    private static final String myPreference = "careNetwork";
    private static final String firstActivity = "activityKey1";
    private static final String secondActivity = "activityKey2";
    private static final String thirdActivity = "activityKey3";
    private static final String stepsTarget = "steps";
    private static final String contactTarget = "contact";
    private static final String carerSupportRef = "carer_support_ref";
    private static final String postCode = "postcode";

    TextView activity1, activity2, activity3, steps, contact, carer_support_ref, postcode;

    SharedPreferences careNetworkPreferences;

    private ArrayList<TextView> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_fragment_3);

        activity1 = findViewById(R.id.activity1);
        activity2 = findViewById(R.id.activity2);
        activity3 = findViewById(R.id.activity3);
        steps = findViewById(R.id.steps);
        contact = findViewById(R.id.contact);
        carer_support_ref = findViewById(R.id.carer_support_ref);
        postcode = findViewById(R.id.postcode);

        careNetworkPreferences = getApplicationContext().getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);

        if (careNetworkPreferences.contains(firstActivity)) {
            activity1.setText(careNetworkPreferences.getString(firstActivity, ""));
        }
        if (careNetworkPreferences.contains(secondActivity)) {
            activity3.setText(careNetworkPreferences.getString(secondActivity, ""));
        }
        if (careNetworkPreferences.contains(thirdActivity)) {
            contact.setText(careNetworkPreferences.getString(thirdActivity, ""));
        }
        if (careNetworkPreferences.contains(stepsTarget)) {
            activity2.setText(careNetworkPreferences.getString(stepsTarget, ""));
        }
        if (careNetworkPreferences.contains(contactTarget)) {
            steps.setText(careNetworkPreferences.getString(contactTarget, ""));
        }
        if (careNetworkPreferences.contains(carerSupportRef)) {
            carer_support_ref.setText(careNetworkPreferences.getString(carerSupportRef, ""));
        }
        if (careNetworkPreferences.contains(postCode)) {
            postcode.setText(careNetworkPreferences.getString(postCode, ""));
        }

        setTitle("Setup Your Activities");
    }


    public void wellbeingscreen(View view) {
        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        values = new ArrayList<TextView>() {{
            add(activity1);
            add(activity2);
            add(activity3);
        }};

        int i = 1;

        for (TextView textView : values) {

            String value = textView.getText().toString();

            careNetworkPreferencesEditor.putString("activityKey" + i, value);

            i++;

        }

        careNetworkPreferencesEditor.putString(stepsTarget, steps.getText().toString());
        careNetworkPreferencesEditor.putString(contactTarget, contact.getText().toString());
        careNetworkPreferencesEditor.putString(carerSupportRef, carer_support_ref.getText().toString());
        careNetworkPreferencesEditor.putString(postCode, postcode.getText().toString());

        if (careNetworkPreferencesEditor.commit())
            startActivity(new Intent(this, AppActivity.class));

    }

}
