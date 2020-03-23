package com.uk.ac.ucl.carefulai.ui.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uk.ac.ucl.carefulai.MainActivity;
import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;
import com.uk.ac.ucl.carefulai.ui.SearchContactActivity;
import com.uk.ac.ucl.carefulai.ui.home.HomeFragment;

import java.util.ArrayList;


public class ConfigFragment extends Fragment {

    private TextView name1, phone1, name2, phone2, name3, phone3;

    private ImageView torfaenLink;
    private ImageView carersTrustLink;
    private ImageView dewisLink;
    private ImageView universityHealthBoardLink;
    private ImageView ageConnectsLink;
    private ImageView friendOfMineLink;
    private ImageView whatsappLink;
    private ImageView alzheimersLink;
    private ImageView headspaceLink;
    private ImageView callContact1, callContact2, callContact3, textContact1, textContact2, textContact3;

    private Button saveButton;

    //private FloatingActionButton editButton;

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
        final View root = inflater.inflate(R.layout.fragment_config, container, false);

        careNetworkPreferences = root.getContext().getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        name1 = root.findViewById(R.id.config_name1);
        name1.setText(careNetworkPreferences.getString(firstContactName, ""));
        name1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 1));
            }
        });

        name2 = root.findViewById(R.id.config_name2);
        name2.setText(careNetworkPreferences.getString(secondContactName, ""));
        name2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 2));
            }
        });

        name3 = root.findViewById(R.id.config_name3);
        name3.setText(careNetworkPreferences.getString(thirdContactName, ""));
        name3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 3));
            }
        });

        phone1 = root.findViewById(R.id.config_phone1);
        phone1.setText(careNetworkPreferences.getString(firstContactPhone, ""));
        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 1));
            }
        });

        phone2 = root.findViewById(R.id.config_phone2);
        phone2.setText(careNetworkPreferences.getString(secondContactPhone, ""));
        phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 2));
            }
        });

        phone3= root.findViewById(R.id.config_phone3);
        phone3.setText(careNetworkPreferences.getString(thirdContactPhone, ""));
        phone3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 3));
            }
        });

        saveButton = root.findViewById(R.id.config_save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                saveChanges(careNetworkPreferences);
            }
        });

        /*
        setEditable();

        editButton = root.findViewById(R.id.config_edit_button);

        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                setEditable();
            }

        });
         */

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

        alzheimersLink = root.findViewById(R.id.alzheimersApp);

        alzheimersLink.setOnClickListener(new View.OnClickListener() {
            Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "There is no package available in android", Toast.LENGTH_LONG).show();

            }
        });

        whatsappLink = root.findViewById(R.id.whatsappApp);

        whatsappLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
                else {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
                }
            }
        });

        headspaceLink = root.findViewById(R.id.headspaceApp);

        headspaceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.getsomeheadspace.android");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
                else {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.getsomeheadspace.android")));
                }
            }
        });


        final Activity activity = this.getActivity();

        callContact1 = root.findViewById(R.id.callContact1);
        callContact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialContactPhone(phone1.getText().toString());
            }
        });

        callContact2 = root.findViewById(R.id.callContact2);
        callContact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialContactPhone(phone2.getText().toString());
            }
        });

        callContact3 = root.findViewById(R.id.callContact3);
        callContact3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialContactPhone(phone3.getText().toString());
            }
        });

        textContact1 = root.findViewById(R.id.textContact1);
        textContact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textContact(phone1.getText().toString(), root.getContext(), activity);
            }
        });

        textContact2 = root.findViewById(R.id.textContact2);
        textContact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textContact(phone2.getText().toString(), root.getContext(), activity);
            }
        });

        textContact3 = root.findViewById(R.id.textContact3);
        textContact3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textContact(phone3.getText().toString(), root.getContext(), activity);
            }
        });


        return root;
    }

    private void dialContactPhone(final String phoneNumber) {
        if (phoneNumber.equals("") || !phoneNumber.isEmpty()) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        }
        else {
            Toast.makeText(this.getActivity(), "Failed to make call, invalid phone number", Toast.LENGTH_LONG).show();
        }
    }

    private void textContact(final String phoneNumber, final Context context, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Send Text Message");

// Set up the input
        final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = input.getText().toString();
                if (!phoneNumber.equals("") || !message.equals("")) {
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber,null, message,null,null);
                        Toast.makeText(activity, "To: " + phoneNumber + ", " + message, Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e){
                        Toast.makeText(activity, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(activity, "Failed to send text message, check details / message and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    /*
    private void setEditable() {

        ArrayList<TextView> values = new ArrayList<TextView>() {{ add(name1); add(name2); add(name3); add(phone1); add(phone2); add(phone3); }};

        boolean editable = !(values.get(0).isEnabled());


        for (TextView editText : values) {

            editText.setEnabled(editable);
        }
        saveButton.setEnabled(editable);

    }

     */

    private void saveChanges(SharedPreferences careNetworkPreferences) {

        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        ArrayList<TextView> values = new ArrayList<TextView>() {{ add(name1); add(name2); add(name3); add(phone1); add(phone2); add(phone3); }};

        int i = 1;
        int j = 1;

        for (TextView editText : values) {
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

        startActivity(new Intent(this.getActivity(), AppActivity.class));
    }

}