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

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.SearchContactActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class PrimaryCareNetworkActivity extends AppCompatActivity {

    public static final String myPreference = "careNetwork";
    public static final String firstContactName = "nameKey1";
    public static final String secondContactName = "nameKey2";
    public static final String thirdContactName = "nameKey3";
    public static final String firstContactPhone = "phoneKey1";
    public static final String secondContactPhone = "phoneKey2";
    public static final String thirdContactPhone = "phoneKey3";
    private static final String userName = "userName";

    private TextView name1, phone1, name2, phone2, name3, phone3;

    private EditText name;

    private SharedPreferences careNetworkPreferences;

    private ArrayList<TextView> values;

    private Button nothanks2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_fragment_2);

        name = (EditText) findViewById(R.id.userName);

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
        nothanks2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        setTitle("Setup Your Primary Care Network");
    }

    private void back() {
        startActivity(new Intent(this, InitialInfoActivity.class));
    }


    public void activityscreen(View view){
        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        values = new ArrayList<TextView>() {{ add(name1); add(name2); add(name3); add(phone1); add(phone2); add(phone3); }};

        ArrayList<String[]> addedNames = new ArrayList<>();

        int i = 1;
        int j = 1;
        int k = 0;
        int l = 0;


        for (TextView editText : values) {
            String value = editText.getText().toString();


            boolean b = value.isEmpty() || value.contains("Name") || value.contains("Phone");
            if (i < 4) {
                if (b) {
                    Toast.makeText(this, "Please Fill In All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] contact = { value, ""};
                addedNames.add(i - 1, contact);
                careNetworkPreferencesEditor.putString("nameKey" + i, value);
                i++;
            }
            else if (j < 4) {
                if (b) {
                    Toast.makeText(this, "Please Fill In All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] contact = { addedNames.get(j - 1)[0], value};
                addedNames.remove(j - 1);
                addedNames.add(j - 1, contact);
                careNetworkPreferencesEditor.putString("phoneKey" + j, value);
                j++;
            }
            else {
                break;
            }
        }

        Set<String[]> set = new HashSet<>(addedNames);

        if(set.size() < addedNames.size()){
            Toast.makeText(this, "Please Select Three Unique Contacts", Toast.LENGTH_SHORT).show();
            return;
        }

        String nameString = name.getText().toString();

        if (nameString.isEmpty()) {
            Toast.makeText(this, "Please Input Your Name", Toast.LENGTH_SHORT).show();
            return;
        }

        careNetworkPreferencesEditor.putString(userName, nameString);

        if (careNetworkPreferencesEditor.commit()) startActivity(new Intent(this, SetupActivitiesActivity.class));

    }

}
