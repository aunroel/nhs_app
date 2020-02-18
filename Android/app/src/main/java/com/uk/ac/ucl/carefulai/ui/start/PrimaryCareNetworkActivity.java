package com.uk.ac.ucl.carefulai.ui.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.R;

import java.util.ArrayList;

public class PrimaryCareNetworkActivity extends AppCompatActivity {

    public static final String myPreference = "careNetwork";
    public static final String firstContactName = "nameKey1";
    public static final String secondContactName = "nameKey2";
    public static final String thirdContactName = "nameKey3";
    public static final String firstContactPhone = "phoneKey1";
    public static final String secondContactPhone = "phoneKey2";
    public static final String thirdContactPhone = "phoneKey3";

    private EditText name1, phone1, name2, phone2, name3, phone3;

    private SharedPreferences careNetworkPreferences;

    private ArrayList<EditText> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_fragment_2);
        name1 = findViewById(R.id.name1);
        phone1 = findViewById(R.id.phone1);

        name2 = findViewById(R.id.name2);
        phone2 = findViewById(R.id.phone2);

        name3 = findViewById(R.id.name3);
        phone3 = findViewById(R.id.phone3);

        careNetworkPreferences = getApplicationContext().getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);

        if (careNetworkPreferences.contains(firstContactName)) {
            name1.setText(careNetworkPreferences.getString(firstContactName, ""));
        }
        if (careNetworkPreferences.contains(secondContactName)) {
            name2.setText(careNetworkPreferences.getString(secondContactName, ""));
        }
        if (careNetworkPreferences.contains(thirdContactName)) {
            name3.setText(careNetworkPreferences.getString(thirdContactName, ""));
        }
        if (careNetworkPreferences.contains(firstContactPhone)) {
            phone1.setText(careNetworkPreferences.getString(firstContactPhone, ""));
        }
        if (careNetworkPreferences.contains(secondContactPhone)) {
            phone2.setText(careNetworkPreferences.getString(secondContactPhone, ""));
        }
        if (careNetworkPreferences.contains(thirdContactPhone)) {
            phone3.setText(careNetworkPreferences.getString(thirdContactPhone, ""));
        }

        setTitle("Setup Your Primary Care Network");
    }


    public void activityscreen(View view) {
        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        values = new ArrayList<EditText>() {{
            add(name1);
            add(name2);
            add(name3);
            add(phone1);
            add(phone2);
            add(phone3);
        }};

        int i = 1;
        int j = 1;

        for (EditText editText : values) {
            String value = editText.getText().toString();

            if (i < 4) {
                careNetworkPreferencesEditor.putString("nameKey" + i, value);
                i++;
            } else if (j < 4) {
                careNetworkPreferencesEditor.putString("phoneKey" + j, value);
                j++;
            } else {
                break;
            }
        }


        if (careNetworkPreferencesEditor.commit())
            startActivity(new Intent(this, SetupActivitiesActivity.class));

    }

}
