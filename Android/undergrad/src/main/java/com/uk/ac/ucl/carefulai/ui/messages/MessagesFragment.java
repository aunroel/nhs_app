package com.uk.ac.ucl.carefulai.ui.messages;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.uk.ac.ucl.carefulai.R;

public class MessagesFragment extends Fragment {


    private SharedPreferences careNetworkPreferences; //used to get the Care Network names and numbers to determine the call and message count

    private TextView contactHistory1, contactHistory2, contactHistory3; //TextViews for Care Network names

    //keys for the Care Network names in careNetworkPreferences
    private static final String firstContactName = "nameKey1";

    private static final String secondContactName = "nameKey2";

    private static final String thirdContactName = "nameKey3";

    private static final String myPreference = "careNetwork"; //get the appropriate key-value list

    private int call1, call2, call3, message1, message2, message3 = 0; //default the call and message counts to 0

    private String number1, number2, number3; //phone numbers for each contact used to map to the native Call Logs and Inbox

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_messages, container, false);

        careNetworkPreferences = root.getContext().getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        //set name TextViews to the values in careNetworkPreferences
        contactHistory1 = root.findViewById(R.id.contactHistory1);
        contactHistory1.setText(careNetworkPreferences.getString(firstContactName, "Contact 1"));

        contactHistory2 = root.findViewById(R.id.contactHistory2);
        contactHistory2.setText(careNetworkPreferences.getString(secondContactName, "Contact 2"));

        contactHistory3 = root.findViewById(R.id.contactHistory3);
        contactHistory3.setText(careNetworkPreferences.getString(thirdContactName, "Contact 3"));


        //set phone numbers to the values in careNetworkPreferences
        number1 = careNetworkPreferences.getString("phoneKey1", "");
        number2 = careNetworkPreferences.getString("phoneKey2", "");
        number3 = careNetworkPreferences.getString("phoneKey3", "");

        getCallDetails(root); //get the call histories for each contact

        getMessagesDetails(root); //get the message histories for each contact

        return root;
    }

    //get the call count for each contact
    private void getCallDetails(View root) {
        Context context = root.getContext();
        ContentResolver cr = context.getContentResolver();


        //check if call logs are empty
        try (Cursor managedCursor = cr.query(CallLog.Calls.CONTENT_URI, null,
                null, null, null)) { //iterate through the call logs using a Cursor using the CallLog.Calls URI
            int number = managedCursor.getInt(managedCursor.getColumnIndex(CallLog.Calls.NUMBER)); //the number column in each call log entry

            while (managedCursor.moveToNext()) { //for each entry in call logs
                String phNumber = managedCursor.getString(number); //get the phone number associated with this entry from the 'number' column
                //increment the appropriate call count if the number matches any of the Care Network contacts
                if (phNumber.equals(number1)) call1++;
                else if (phNumber.equals(number2)) call2++;
                else if (phNumber.equals(number3)) call3++;

            }
        } catch (Exception e) { //no call logs, log
            Log.i("Call log", "No call log data");
        } finally { //set the call history for each contact to the appropriate count

            TextView callHistory1, callHistory2, callHistory3; //the TextViews for the call histories

            callHistory1 = root.findViewById(R.id.callHistory1);
            callHistory1.setText(String.valueOf(call1));

            callHistory2 = root.findViewById(R.id.callHistory2);
            callHistory2.setText(String.valueOf(call2));

            callHistory3 = root.findViewById(R.id.callHistory3);
            callHistory3.setText(String.valueOf(call3));
        }

    }

    //get the message count for each contact
    private void getMessagesDetails(View root) {

        Uri uri = Telephony.Sms.Inbox.CONTENT_URI; //URI for the native messages inbox on the device

        //Cursor to iterate through the inbox
        try (Cursor cursor = root.getContext().getContentResolver().query(uri, null, null, null, null)) { // must check the result to prevent exception
            int number = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.Inbox.ADDRESS)); //defined number column in URI for each entry in inbox

            while (cursor.moveToNext()) { //for each entry in the inbox
                String phNumber = cursor.getString(number);
                //increment the appropriate message count if the number matches any of the Care Network contacts
                if (phNumber.equals(number1)) message1++;
                else if (phNumber.equals(number2)) message2++;
                else if (phNumber.equals(number3)) message3++;
            }
        } catch (Exception e) {
            Log.i("SMS Inbox", "No SMS Inbox data");
        } finally { //set the message history for each contact to the appropriate count
            TextView messageHistory1, messageHistory2, messageHistory3; //the TextViews for the message histories

            messageHistory1 = root.findViewById(R.id.messageHistory1);
            messageHistory1.setText(String.valueOf(message1));

            messageHistory2 = root.findViewById(R.id.messageHistory2);
            messageHistory2.setText(String.valueOf(message2));

            messageHistory3 = root.findViewById(R.id.messageHistory3);
            messageHistory3.setText(String.valueOf(message3));

        }
    }

}
