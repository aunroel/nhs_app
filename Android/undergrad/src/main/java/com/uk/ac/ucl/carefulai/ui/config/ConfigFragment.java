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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;
import com.uk.ac.ucl.carefulai.ui.SearchContactActivity;

import java.util.ArrayList;

//Fragment for Care Network tab on main flow
public class ConfigFragment extends Fragment {

    private TextView name1, phone1, name2, phone2, name3, phone3; //TextViews for names and phone numbers of care network

    //image views for each of the apps and website links
    private ImageView torfaenLink;
    private ImageView carersTrustLink;
    private ImageView dewisLink;
    private ImageView universityHealthBoardLink;
    private ImageView ageConnectsLink;
    private ImageView friendOfMineLink;
    private ImageView whatsappLink;
    private ImageView alzheimersLink;
    private ImageView headspaceLink;

    //image views for the call and message buttons for each care network contact
    private ImageView callContact1, callContact2, callContact3, textContact1, textContact2, textContact3;

    private Button saveButton; //save care network changes button

    //private FloatingActionButton editButton;

    private SharedPreferences careNetworkPreferences; //used to get the currently saved care network details

    private static final String myPreference = "careNetwork"; //key for the care network preferences list

    //keys for the care network details in careNetworkPreferences
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

        // get each TextView from the layout
        // similar onClickListeners for each TextView, opens the SearchContactActivity to allow the user to select a contact
        // intent extra contactID marks which TextView is being edited
        name1 = root.findViewById(R.id.config_name1);
        name1.setText(careNetworkPreferences.getString(firstContactName, "Contact 1"));
        name1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 1));
            }
        });

        name2 = root.findViewById(R.id.config_name2);
        name2.setText(careNetworkPreferences.getString(secondContactName, "Contact 2"));
        name2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 2));
            }
        });

        name3 = root.findViewById(R.id.config_name3);
        name3.setText(careNetworkPreferences.getString(thirdContactName, "Contact 3"));
        name3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 3));
            }
        });

        phone1 = root.findViewById(R.id.config_phone1);
        phone1.setText(careNetworkPreferences.getString(firstContactPhone, "Number 1"));
        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 1));
            }
        });

        phone2 = root.findViewById(R.id.config_phone2);
        phone2.setText(careNetworkPreferences.getString(secondContactPhone, "Number 2"));
        phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), SearchContactActivity.class).putExtra("contactId", 2));
            }
        });

        phone3= root.findViewById(R.id.config_phone3);
        phone3.setText(careNetworkPreferences.getString(thirdContactPhone, "Number 3"));
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

        //get each web info image link from the layout
        //set the onClickListener to the appropriate web url to open in the native browser
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

        //get each app image link from the layout
        //set the onClickListener to open the app if installed, or redirect to the Play Store page for that app

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

        //get each call and message icon from layout
        //set the appropriate onClickListener with the care network detail from the displayed TextView

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

    //used to dial a phone number, used as the onClickListener for each call icon
    private void dialContactPhone(final String phoneNumber) {
        if (phoneNumber.equals("") || !phoneNumber.isEmpty()) { //ensure the phone number isnt an empty string
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        }
        else { //notify the user that the phone number is invalid
            Toast.makeText(this.getActivity(), "Failed to make call, invalid phone number", Toast.LENGTH_LONG).show();
        }
    }

    //used to text a phone number, used as the onClickListener for each message icon
    private void textContact(final String phoneNumber, final Context context, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); //pop up dialog for the user to input the text message
        builder.setTitle("Send Text Message"); //dialog title

// Set up the text input
        final EditText input = new EditText(context);
// Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the send button in the dialog
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = input.getText().toString(); //get the user's text message
                if (!phoneNumber.equals("") || !message.equals("")) { //ensure the phone number and message arent empty strings
                    try {
                        SmsManager smsManager = SmsManager.getDefault(); //get the default device messaging service
                        smsManager.sendTextMessage(phoneNumber,null, message,null,null); //send the message
                        Toast.makeText(activity, "To: " + phoneNumber + ", " + message, Toast.LENGTH_LONG).show(); //display the message sent as a toast to the user
                    }
                    catch (Exception e){
                        Toast.makeText(activity, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show(); //if fail, notify user
                    }
                }
                else {
                    Toast.makeText(activity, "Failed to send text message, check details / message and try again", Toast.LENGTH_LONG).show(); //invalid phone number or message, notify user
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel(); //cancel button
            }
        });

        builder.show(); //show the dialog
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

    //saves the changes to the care network to sharedpreferences and then opens AppActivity
    private void saveChanges(SharedPreferences careNetworkPreferences) {

        //editor for careNetworkPreferences
        SharedPreferences.Editor careNetworkPreferencesEditor = careNetworkPreferences.edit();

        //list of TextViews with new details
        ArrayList<TextView> values = new ArrayList<TextView>() {{ add(name1); add(name2); add(name3); add(phone1); add(phone2); add(phone3); }};

        int i = 1;
        int j = 1;

        //iterate through the TextViews and add the values to careNetworkPreferences
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

        //commit changes
        careNetworkPreferencesEditor.commit();

        //start AppActivity
        startActivity(new Intent(this.getActivity(), AppActivity.class));
    }

}