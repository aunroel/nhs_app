package com.uk.ac.ucl.carefulai.ui.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uk.ac.ucl.carefulai.R;

import java.util.ArrayList;


public class ConfigFragment extends Fragment {

    private static final String myPreference = "careNetwork";
    private static final String firstContactName = "nameKey1";
    private static final String secondContactName = "nameKey2";
    private static final String thirdContactName = "nameKey3";
    private static final String firstContactPhone = "phoneKey1";
    private static final String secondContactPhone = "phoneKey2";
    private static final String thirdContactPhone = "phoneKey3";
    private ConfigViewModel configViewModel;
    private EditText name1, phone1, name2, phone2, name3, phone3;
    private Button saveButton;
    private FloatingActionButton editButton;
    private SharedPreferences careNetworkPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        configViewModel =
                ViewModelProviders.of(this).get(ConfigViewModel.class);
        View root = inflater.inflate(R.layout.fragment_config, container, false);

        careNetworkPreferences = root.getContext().getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        name1 = root.findViewById(R.id.config_name1);
        name1.setText(careNetworkPreferences.getString(firstContactName, ""));

        name2 = root.findViewById(R.id.config_name2);
        name2.setText(careNetworkPreferences.getString(secondContactName, ""));

        name3 = root.findViewById(R.id.config_name3);
        name3.setText(careNetworkPreferences.getString(thirdContactName, ""));

        phone1 = root.findViewById(R.id.config_phone1);
        phone1.setText(careNetworkPreferences.getString(firstContactPhone, ""));

        phone2 = root.findViewById(R.id.config_phone2);
        phone2.setText(careNetworkPreferences.getString(secondContactPhone, ""));

        phone3 = root.findViewById(R.id.config_phone3);
        phone3.setText(careNetworkPreferences.getString(thirdContactPhone, ""));

        saveButton = root.findViewById(R.id.config_save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveChanges(careNetworkPreferences);
            }
        });

        setEditable();

        editButton = root.findViewById(R.id.config_edit_button);

        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setEditable();
            }

        });

        return root;
    }


    private void setEditable() {

        ArrayList<EditText> values = new ArrayList<EditText>() {{
            add(name1);
            add(name2);
            add(name3);
            add(phone1);
            add(phone2);
            add(phone3);
        }};

        boolean editable = !(values.get(0).isEnabled());


        for (EditText editText : values) {

            editText.setEnabled(editable);
        }

        saveButton.setEnabled(editable);

    }

    private void saveChanges(SharedPreferences careNetworkPreferences) {

        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        ArrayList<EditText> values = new ArrayList<EditText>() {{
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

        careNetworkPreferencesEditor.commit();

    }

}