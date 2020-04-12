package com.uk.ac.ucl.carefulai.ui.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.R;

import java.util.ArrayList;

public class SetupActivitiesActivity extends AppCompatActivity {

    private static final String myPreference = "careNetwork"; //care network data stored under this shared preference
    //list of keys for care network activities and target data for shared preferences
    private static final String firstActivity = "activityKey1";
    private static final String secondActivity = "activityKey2";
    private static final String thirdActivity = "activityKey3";
    private static final String stepsTarget = "stepsTarget";
    private static final String contactTarget = "contactTarget";
    private static final String carerSupportRef = "carer_support_ref";
    private static final String postCode = "postcode";

    private Button noThanks3; //back button used in back() method

    EditText activity1, activity2, activity3, steps, contact, carer_support_ref, postcode; //text views for each field

    SharedPreferences careNetworkPreferences;

    private ArrayList<EditText> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_fragment_3);

        // get each text field from the layout

        activity1 = (EditText) findViewById(R.id.activity1);
        activity2 = (EditText) findViewById(R.id.activity2);
        activity3 = (EditText) findViewById(R.id.activity3);
        steps = (EditText) findViewById(R.id.steps);
        contact = (EditText) findViewById(R.id.contact);
        carer_support_ref = (EditText) findViewById(R.id.carer_support_ref);
        postcode = (EditText) findViewById(R.id.postcode);

        // careNetworkPreferences used to store Care Network data
        careNetworkPreferences = getApplicationContext().getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);

        //check if there already exists any saved data in careNetworkPreferences for each EditText, if so, display it
        //else default to default string as appropriate
        //needed for when the setup flow is triggered again from the main flow

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
        //sets onClickListener for noThanks3 button
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

    public void wellbeingscreen(View view){ //onClick method for 'Save' button in UI; saves the data to careNetworkPreferences, and go to next activity

        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        values = new ArrayList<EditText>() {{ add(activity1); add(activity2); add(activity3); }};

        int i = 1;

        for (EditText textView : values) { //iterate through each activity, and label each activity activityKey + i with its value

            String value = textView.getText().toString();

            careNetworkPreferencesEditor.putString("activityKey" + i, value);

            i++;

        }

        String userSteps = steps.getText().toString(); //steps target

        String userContact = contact.getText().toString(); //contacts target

        String userSupportRef = carer_support_ref.getText().toString(); //support reference code

        String userPostcode = postcode.getText().toString(); //postcode


        if (userSteps.isEmpty() || userContact.isEmpty() || userSupportRef.isEmpty() || userPostcode.isEmpty()) { //required fields
            Toast.makeText(this, "Please Fill In All Fields ", Toast.LENGTH_SHORT).show();
            return;
        }

        //store targets, support ref, and postcode in careNetworkPreferences

        careNetworkPreferencesEditor.putInt(stepsTarget, Integer.parseInt(userSteps));
        careNetworkPreferencesEditor.putInt(contactTarget, Integer.parseInt(userContact));
        careNetworkPreferencesEditor.putString(carerSupportRef, userSupportRef);
        careNetworkPreferencesEditor.putString(postCode, userPostcode);

        //commit then go to PermissionsActivity
        if (careNetworkPreferencesEditor.commit()) startActivity(new Intent(this, PermissionsActivity.class));

    }

}
