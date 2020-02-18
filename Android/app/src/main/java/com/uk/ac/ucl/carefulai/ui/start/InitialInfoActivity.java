package com.uk.ac.ucl.carefulai.ui.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.R;

public class InitialInfoActivity extends AppCompatActivity {

    private static final String myPreference = "careNetwork";
    private static final String userName = "userName";
    private static final String defaultName = "What Do People Call You?";
    private SharedPreferences careNetworkPreferences;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_fragment_1);


        careNetworkPreferences = getApplicationContext().getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);

        name = findViewById(R.id.userName);

        if (careNetworkPreferences.contains(userName)) {
            name.setText(careNetworkPreferences.getString(userName, ""));
        }
        setTitle("Welcome to Careful AI!");
    }

    public void setupscreen(View view) {
        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        String nameString = name.getText().toString();

        if (nameString.isEmpty() || defaultName.equals(nameString)) {
            Toast.makeText(this, "Please Input Your Name", Toast.LENGTH_SHORT).show();
            return;
        }

        careNetworkPreferencesEditor.putString(userName, nameString);

        if (careNetworkPreferencesEditor.commit())
            startActivity(new Intent(this, PrimaryCareNetworkActivity.class));

    }

}
