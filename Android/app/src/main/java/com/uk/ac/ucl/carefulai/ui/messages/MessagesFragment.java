package com.uk.ac.ucl.carefulai.ui.messages;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.uk.ac.ucl.carefulai.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class MessagesFragment extends Fragment {


    private static final String firstContactName = "nameKey1";
    private static final String secondContactName = "nameKey2";
    private static final String thirdContactName = "nameKey3";
    private static final String myPreference = "careNetwork";
    private MessagesViewModel messagesViewModel;
    private SharedPreferences careNetworkPreferences;
    private TextView contactHistory1, contactHistory2, contactHistory3;
    private EditText name, timeField, dateField;
    private Button dateButton, timeButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String chosenActivity, chosenContact, chosenStatus, userName, phoneNumber;
    private ArrayList<String> statusList = new ArrayList<String>() {{
        add("doing great!");
        add("ok");
        add("a bit down");
    }};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        messagesViewModel =
                ViewModelProviders.of(this).get(MessagesViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_messages, container, false);

        careNetworkPreferences = root.getContext().getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        userName = careNetworkPreferences.getString("userName", "");

        name = root.findViewById(R.id.nameText);

        name.setText(userName);

        contactHistory1 = root.findViewById(R.id.contactHistory1);
        contactHistory1.setText(careNetworkPreferences.getString(firstContactName, ""));

        contactHistory2 = root.findViewById(R.id.contactHistory2);
        contactHistory2.setText(careNetworkPreferences.getString(secondContactName, ""));

        contactHistory3 = root.findViewById(R.id.contactHistory3);
        contactHistory3.setText(careNetworkPreferences.getString(thirdContactName, ""));

        timeField = root.findViewById(R.id.timeField);


        populateActivitySpinner(root, careNetworkPreferences);

        populateContactSpinner(root, careNetworkPreferences);

        populateStatusSpinner(root);

        Button sendButton = root.findViewById(R.id.sendSMS);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });


        dateButton = root.findViewById(R.id.dateButton);

        timeButton = root.findViewById(R.id.timeButton);

        dateField = root.findViewById(R.id.dateField);

        timeField = root.findViewById(R.id.timeField);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(root.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dateField.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(root.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                timeField.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        return root;
    }


    private void populateActivitySpinner(View root, SharedPreferences careNetworkPreferences) {

        ArrayList<String> activities = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            activities.add(careNetworkPreferences.getString("activityKey" + i, ""));
        }


        Spinner activitySpinner = root.findViewById(R.id.activity_spinner);

        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, activities);

        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activitySpinner.setAdapter(activityAdapter);

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                chosenActivity = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void populateStatusSpinner(View root) {


        Spinner statusSpinner = root.findViewById(R.id.status_spinner);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, statusList);

        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        statusSpinner.setAdapter(statusAdapter);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                chosenStatus = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void populateContactSpinner(final View root, final SharedPreferences careNetworkPreferences) {

        ArrayList<String> contacts = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            contacts.add(careNetworkPreferences.getString("nameKey" + i, ""));
        }

        Spinner contactSpinner = root.findViewById(R.id.contact_spinner);

        ArrayAdapter<String> contactAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, contacts);

        contactAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        contactSpinner.setAdapter(contactAdapter);

        contactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String option = adapterView.getItemAtPosition(i).toString();

                chosenContact = option;

                Map<String, ?> allEntries = careNetworkPreferences.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

                    if (entry.getValue().toString().equals(option)) {

                        String contactKey = entry.getKey().replace("nameKey", "");

                        phoneNumber = careNetworkPreferences.getString("phoneKey" + contactKey, "");

                        break;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean isNullOrEmpty(ArrayList<String> strings) {

        for (int i = 0; i < strings.size(); i++) {

            String str = strings.get(i);

            if (str == null || str.trim().isEmpty()) {

                return true;
            }

        }
        return false;
    }

    private void sendSMS() {

        final String chosenDate = dateField.getText().toString();

        final String chosenTime = timeField.getText().toString();

        final ArrayList<String> params = new ArrayList<String>() {{
            add(chosenActivity);
            add(chosenContact);
            add(chosenStatus);
            add(chosenDate);
            add(chosenTime);
        }};

        if (isNullOrEmpty(params)) {

            Toast.makeText(this.getActivity(), "Please Fill In All Fields", Toast.LENGTH_SHORT).show();

            return;

        }

        String textMessage = "Hello "
                + chosenContact
                + ", Just to let you know that I am "
                + chosenStatus
                + ". How about on "
                + chosenDate
                + ", at "
                + chosenTime
                + " we meet for a "
                + chosenActivity
                + ". All the best, "
                + userName;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, textMessage, null, null);
            Toast.makeText(this.getActivity(), "To: " + phoneNumber + ", " + textMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this.getActivity(), "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }


}
