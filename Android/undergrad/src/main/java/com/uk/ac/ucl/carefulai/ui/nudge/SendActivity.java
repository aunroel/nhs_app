package com.uk.ac.ucl.carefulai.ui.nudge;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;
import com.uk.ac.ucl.carefulai.ui.report.GraphAttachments;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import me.everything.providers.android.telephony.Mms;

import static me.everything.providers.android.telephony.Sms.uri;

public class SendActivity extends AppCompatActivity {

    private SharedPreferences careNetworkPreferences;

    private SharedPreferences dataPreferences;

    private TextView contactHistory1, contactHistory2, contactHistory3;

    private Switch includeDiary;


    private static final String firstContactName = "nameKey1";

    private static final String secondContactName = "nameKey2";

    private static final String thirdContactName = "nameKey3";

    private EditText name, timeField, dateField;

    private Button dateButton, timeButton;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private String chosenActivity, chosenContact, chosenStatus, userName, phoneNumber;

    private static final String myPreference = "careNetwork";

    private ArrayList<String> statusList = new ArrayList<String>() {{ add("doing great!"); add("ok"); add("a bit down"); }};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        careNetworkPreferences = this.getSharedPreferences(myPreference, Context.MODE_PRIVATE);

        dataPreferences = this.getSharedPreferences("dataPreference", Context.MODE_PRIVATE);

        userName = careNetworkPreferences.getString("userName", "");

        name = (EditText) findViewById(R.id.nameText);

        name.setText(userName);

        timeField = findViewById(R.id.timeField);


        populateActivitySpinner(careNetworkPreferences);

        populateContactSpinner(careNetworkPreferences);

        populateStatusSpinner();

        Button sendButton = (Button) findViewById(R.id.sendSMS);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendSMS(dataPreferences);
            }
        });


        dateButton = (Button) findViewById(R.id.dateButton);

        timeButton = (Button) findViewById(R.id.timeButton);

        dateField = (EditText) findViewById(R.id.dateField);

        timeField = (EditText) findViewById(R.id.timeField);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateButton();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeButton();
            }
        });

        includeDiary = findViewById(R.id.includeWellBeingDiary);

    }

    private void timeButton() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        timeField.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void dateButton() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dateField.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private void populateActivitySpinner(SharedPreferences careNetworkPreferences) {

        ArrayList<String> activities = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            activities.add(careNetworkPreferences.getString("activityKey" + i, ""));
        }


        Spinner activitySpinner = (Spinner) findViewById(R.id.activity_spinner);

        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activities);

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

    private void populateStatusSpinner() {


        Spinner statusSpinner = (Spinner) findViewById(R.id.status_spinner);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList);

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


    private void populateContactSpinner(final SharedPreferences careNetworkPreferences) {

        ArrayList<String> contacts = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            contacts.add(careNetworkPreferences.getString("nameKey" + i, ""));
        }

        Spinner contactSpinner = (Spinner) findViewById(R.id.contact_spinner);

        ArrayAdapter<String> contactAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contacts);

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

            if(str == null || str.trim().isEmpty()) {

                return true;
            }

        }
        return false;
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void sendSMS(SharedPreferences dataPreferences) {

        SharedPreferences.Editor editor = dataPreferences.edit();

        final String chosenDate = dateField.getText().toString();

        final String chosenTime = timeField.getText().toString();

        final ArrayList<String> params = new ArrayList<String>() {{ add(chosenActivity); add(chosenContact); add(chosenStatus); add(chosenDate); add(chosenTime); }};

        if (isNullOrEmpty(params)) {
            Toast.makeText(this, "Please Fill In All Fields", Toast.LENGTH_SHORT).show();
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

        int activityNumber = 0;

        for (int i = 1; i < 4; i++) {
            if (chosenActivity.equals(careNetworkPreferences.getString("activityKey" + i, ""))) activityNumber = i;
        }

        editor.putInt("activityCount" + activityNumber, dataPreferences.getInt("activityCount" + activityNumber, 0) + 1);

        if (dataPreferences.getInt("activityCount" + activityNumber, 0) > 1) {
            editor.putBoolean("activityNudge" + activityNumber, true);

            switch (activityNumber) {
                case 1:
                    editor.putBoolean("activityNudge" + 2, false);
                    editor.putBoolean("activityNudge" + 3, false);
                    editor.putInt("activityCount" + 2, 0);
                    editor.putInt("activityCount" + 3, 0);
                    break;
                case 2:
                    editor.putBoolean("activityNudge" + 1, false);
                    editor.putBoolean("activityNudge" + 3, false);
                    editor.putInt("activityCount" + 1, 0);
                    editor.putInt("activityCount" + 3, 0);
                    break;
                case 3:
                    editor.putBoolean("activityNudge" + 1, false);
                    editor.putBoolean("activityNudge" + 2, false);
                    editor.putInt("activityCount" + 1, 0);
                    editor.putInt("activityCount" + 2, 0);
                    break;
                default:
                    break;

            }

        }

        if (dataPreferences.getInt("activityCount" + activityNumber, 0) > 4) {
            editor.putBoolean("activityNudge" + activityNumber, false);
        }

        editor.apply();

        if (includeDiary.isChecked()) {
            GraphAttachments graphAttachments = new GraphAttachments();
            Bitmap steps = graphAttachments.getStepsGraph();
            Bitmap calls = graphAttachments.getCallsGraph();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra("sms_body", textMessage);
            intent.putExtra(Intent.EXTRA_STREAM, getImageUri(this, steps));
            intent.putExtra(Intent.EXTRA_STREAM, getImageUri(this, calls));
            intent.setType("image/jpeg");
            startActivity(intent);
            startActivity(new Intent(this, AppActivity.class));
        }

        else {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber,null, textMessage,null,null);
                Toast.makeText(this, "To: " + phoneNumber + ", " + textMessage, Toast.LENGTH_LONG).show();

                startActivity(new Intent(this, AppActivity.class));
            }
            catch (Exception e){
                Toast.makeText(this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

