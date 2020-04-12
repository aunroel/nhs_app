package com.uk.ac.ucl.carefulai.ui.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.SearchContactActivity;

public class PrimaryCareNetworkActivity extends AppCompatActivity {

    public static final String myPreference = "careNetwork"; //care network data stored under this shared preference
    //list of keys for care network contacts for shared preferences
    public static final String firstContactName = "nameKey1";
    public static final String secondContactName = "nameKey2";
    public static final String thirdContactName = "nameKey3";
    public static final String firstContactPhone = "phoneKey1";
    public static final String secondContactPhone = "phoneKey2";
    public static final String thirdContactPhone = "phoneKey3";
    private static final String userName = "userName";

    private TextView name1, phone1, name2, phone2, name3, phone3; //text views for each field

    private EditText name; //user's name

    private SharedPreferences careNetworkPreferences; //shared preferences to store the user's chosen contacts

    private Button nothanks2; //back button used in back() method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_fragment_2);

        name = (EditText) findViewById(R.id.userName);

        // get each TextView from the layout
        // similar onClickListeners for each TextView, opens the SearchContactActivity to allow the user to select a contact
        // intent extra contactID marks which TextView is being edited
        // intent extra isSetupCN marks whether the editing is occurring from the setup flow or the main flow, as the same layout is used for both
        name1 = findViewById(R.id.name1);
        name1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrimaryCareNetworkActivity.this, SearchContactActivity.class);
                intent.putExtra("contactId", 1);
                intent.putExtra("isSetupCN", true);
                startActivity(intent);
            }
        });
        phone1 = findViewById(R.id.phone1);
        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrimaryCareNetworkActivity.this, SearchContactActivity.class);
                intent.putExtra("contactId", 1);
                intent.putExtra("isSetupCN", true);
                startActivity(intent);
            }
        });

        name2 = findViewById(R.id.name2);
        name2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrimaryCareNetworkActivity.this, SearchContactActivity.class);
                intent.putExtra("contactId", 2);
                intent.putExtra("isSetupCN", true);
                startActivity(intent);
            }
        });
        phone2 = findViewById(R.id.phone2);
        phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrimaryCareNetworkActivity.this, SearchContactActivity.class);
                intent.putExtra("contactId", 2);
                intent.putExtra("isSetupCN", true);
                startActivity(intent);
            }
        });

        name3 = findViewById(R.id.name3);
        name3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrimaryCareNetworkActivity.this, SearchContactActivity.class);
                intent.putExtra("contactId", 3);
                intent.putExtra("isSetupCN", true);
                startActivity(intent);
            }
        });
        phone3 = findViewById(R.id.phone3);
        phone3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrimaryCareNetworkActivity.this, SearchContactActivity.class);
                intent.putExtra("contactId", 3);
                intent.putExtra("isSetupCN", true);
                startActivity(intent);
            }
        });

        careNetworkPreferences = getApplicationContext().getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);

        //check if there already exists any saved data in careNetworkPreferences for each TextView, if so, display it
        //else default to default string as appropriate
        //needed for when the setup flow is triggered again from the main flow

        if (careNetworkPreferences.contains(userName)) {
            name.setText(careNetworkPreferences.getString(userName, ""));
        }
        else {
            name.setText("Enter Name");
        }

        if (careNetworkPreferences.contains(firstContactName)) {
            name1.setText(careNetworkPreferences.getString(firstContactName, ""));
        }
        else {
            name1.setText("Name 1");
        }
        if (careNetworkPreferences.contains(secondContactName)) {
            name2.setText(careNetworkPreferences.getString(secondContactName, ""));
        }
        else {
            name2.setText("Name 2");
        }
        if (careNetworkPreferences.contains(thirdContactName)) {
            name3.setText(careNetworkPreferences.getString(thirdContactName, ""));
        }
        else {
            name3.setText("Name 3");
        }
        if (careNetworkPreferences.contains(firstContactPhone)) {
            phone1.setText(careNetworkPreferences.getString(firstContactPhone, ""));
        }
        else {
            phone1.setText("Phone 1");
        }
        if (careNetworkPreferences.contains(secondContactPhone)) {
            phone2.setText(careNetworkPreferences.getString(secondContactPhone, ""));
        }
        else {
            phone2.setText("Phone 2");
        }
        if (careNetworkPreferences.contains(thirdContactPhone)) {
            phone3.setText(careNetworkPreferences.getString(thirdContactPhone, ""));
        }
        else {
            phone3.setText("Phone 3");
        }

        nothanks2 = findViewById(R.id.noThanks2);

        //sets onClickListener for noThanks2 button
        nothanks2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        setTitle("Setup Your Primary Care Network");
    }

    private void back() { //go back to the InitialInfoActivity
        startActivity(new Intent(this, InitialInfoActivity.class));
    }


    public void activityscreen(View view){ //proceed to SetupActivitesActivity when 'Save' is clicked (onClick attribute set in res)

        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        String nameString = name.getText().toString(); //get the user's name

        //ensure that there is a name put into the app
        if (nameString.isEmpty()) {
            Toast.makeText(this, "Please Input Your Name", Toast.LENGTH_SHORT).show(); //inform the user that they need to put in a name
            return;
        }

        careNetworkPreferencesEditor.putString(userName, nameString); //put the name to sharedpreferences

        //if the commit is successful, start the activity setup
        if (careNetworkPreferencesEditor.commit()) startActivity(new Intent(this, SetupActivitiesActivity.class));

    }

}
