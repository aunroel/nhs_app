package com.uk.ac.ucl.carefulai.ui.messages;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uk.ac.ucl.carefulai.R;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MessagesFragment extends Fragment {


    private SharedPreferences careNetworkPreferences;

    private TextView contactHistory1, contactHistory2, contactHistory3;

    private static final String firstContactName = "nameKey1";

    private static final String secondContactName = "nameKey2";

    private static final String thirdContactName = "nameKey3";

    private static final String myPreference = "careNetwork";

    private int call1, call2, call3, message1, message2, message3 = 0;

    private String number1, number2, number3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_messages, container, false);

        careNetworkPreferences = root.getContext().getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        contactHistory1 = root.findViewById(R.id.contactHistory1);
        contactHistory1.setText(careNetworkPreferences.getString(firstContactName, ""));

        contactHistory2 = root.findViewById(R.id.contactHistory2);
        contactHistory2.setText(careNetworkPreferences.getString(secondContactName, ""));

        contactHistory3 = root.findViewById(R.id.contactHistory3);
        contactHistory3.setText(careNetworkPreferences.getString(thirdContactName, ""));


        number1 = careNetworkPreferences.getString("phoneKey1", "");
        number2 = careNetworkPreferences.getString("phoneKey2", "");
        number3 = careNetworkPreferences.getString("phoneKey3", "");

        getCallDetails(root);

        return root;
    }

    private void getCallDetails(View root) {
        Context context = root.getContext();
        ContentResolver cr = context.getContentResolver();
        Cursor managedCursor = cr.query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);


        try {
            int number = managedCursor.getInt(managedCursor.getColumnIndex(CallLog.Calls.NUMBER));
            int type = managedCursor.getInt(managedCursor.getColumnIndex(CallLog.Calls.TYPE));
            int date = managedCursor.getInt(managedCursor.getColumnIndex(CallLog.Calls.DATE));
            int duration = managedCursor.getInt(managedCursor.getColumnIndex(CallLog.Calls.DURATION));

            while (managedCursor.moveToNext()) {
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }

                if (phNumber.equals(number1)) call1++;
                else if (phNumber.equals(number2)) call2++;
                else if (phNumber.equals(number3)) call3++;

            }
            managedCursor.close();
        } catch (Exception e) {
            Log.d("Call log", "No call log data");
        }
        finally {

            TextView callHistory1, callHistory2, callHistory3;

            callHistory1 = root.findViewById(R.id.callHistory1);
            callHistory1.setText(String.valueOf(call1));

            callHistory2 = root.findViewById(R.id.callHistory2);
            callHistory2.setText(String.valueOf(call2));

            callHistory3 = root.findViewById(R.id.callHistory3);
            callHistory3.setText(String.valueOf(call3));
        }

    }


}
