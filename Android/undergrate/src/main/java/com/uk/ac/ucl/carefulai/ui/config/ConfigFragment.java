package com.uk.ac.ucl.carefulai.ui.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uk.ac.ucl.carefulai.R;
import java.util.ArrayList;


public class ConfigFragment extends Fragment {

    private ConfigViewModel configViewModel;

    private EditText name1, phone1, name2, phone2, name3, phone3;

    private ImageView torfaenLink, carersTrustLink, dewisLink, universityHealthBoardLink, ageConnectsLink, friendOfMineLink;

    private Button saveButton;

    private FloatingActionButton editButton;

    private SharedPreferences careNetworkPreferences;

    private static final String myPreference = "careNetwork";

    private static final String firstContactName = "nameKey1";

    private static final String secondContactName = "nameKey2";

    private static final String thirdContactName = "nameKey3";

    private static final String firstContactPhone = "phoneKey1";

    private static final String secondContactPhone = "phoneKey2";

    private static final String thirdContactPhone = "phoneKey3";

    public View onCreateView(@NonNull final LayoutInflater inflater,
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

        phone3= root.findViewById(R.id.config_phone3);
        phone3.setText(careNetworkPreferences.getString(thirdContactPhone, ""));

        saveButton = root.findViewById(R.id.config_save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                saveChanges(careNetworkPreferences);
            }
        });

        setEditable();

        editButton = root.findViewById(R.id.config_edit_button);

        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                setEditable();
            }

        });

        torfaenLink = root.findViewById(R.id.torfaenLink);

        torfaenLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.torfaen.gov.uk/en/HealthSocialCare/Keeping-Active-and-Getting-Out/Torfaen-Community-Connectors/Torfaen-Community-Connectors.aspx")));
            }
        });

        carersTrustLink = root.findViewById(R.id.carersTrustLink);

        carersTrustLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.ctsew.org.uk/care-services")));
            }
        });

        dewisLink = root.findViewById(R.id.dewisLink);

        dewisLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.dewis.wales/")));
            }
        });

        universityHealthBoardLink = root.findViewById(R.id.universityHealthBoardLink);

        universityHealthBoardLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.wales.nhs.uk/sitesplus/866/page/81903")));
            }
        });

        ageConnectsLink = root.findViewById(R.id.ageConnectsLink);

        ageConnectsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://ageconnectstorfaen.org.uk/services/")));
            }
        });

        friendOfMineLink = root.findViewById(R.id.friendOfMineLink);

        friendOfMineLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.ffrindimi.co.uk/")));
            }
        });

        return root;
    }


    private void setEditable() {

        ArrayList<EditText> values = new ArrayList<EditText>() {{ add(name1); add(name2); add(name3); add(phone1); add(phone2); add(phone3); }};

        boolean editable = !(values.get(0).isEnabled());


        for (EditText editText : values) {

            editText.setEnabled(editable);
        }

        saveButton.setEnabled(editable);

    }
    private void saveChanges(SharedPreferences careNetworkPreferences) {

        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        ArrayList<EditText> values = new ArrayList<EditText>() {{ add(name1); add(name2); add(name3); add(phone1); add(phone2); add(phone3); }};

        int i = 1;
        int j = 1;

        for (EditText editText : values) {
            String value = editText.getText().toString();

            if (i < 4) {
                careNetworkPreferencesEditor.putString("nameKey" + i, value);
                i++;
            }
            else if (j < 4) {
                careNetworkPreferencesEditor.putString("phoneKey" + j, value);
                j++;
            }
            else {
                break;
            }
        }

        careNetworkPreferencesEditor.commit();

    }

}