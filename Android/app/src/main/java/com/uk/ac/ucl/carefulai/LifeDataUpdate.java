package com.uk.ac.ucl.carefulai;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CallLog;

import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.calllog.CallsProvider;
import me.everything.providers.android.telephony.TelephonyProvider;

public class LifeDataUpdate {

    public static int stepstaken;

    private DatabaseHelper myDb;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private TelephonyProvider telephonyProvider;
    private CallsProvider callsProvider;


    private Context context;


    public LifeDataUpdate(Context context) {
        this.context = context;

        preferences = context.getSharedPreferences("TheLiveData", Context.MODE_PRIVATE);
        editor = preferences.edit();
        myDb = new DatabaseHelper(context);

    }

    public boolean checkIfFirstLaunchApp() {
        boolean firstrun = false;
        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;


        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;
        // Get saved version code
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            // This is just a normal run
        } else if (savedVersionCode == DOESNT_EXIST) {
            firstrun = true;
        } else if (currentVersionCode > savedVersionCode) {

        }
        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();

        return firstrun;
    }

    public void SaveDataToInitialState() {

        int initialCallCounts = getCallDuration().size();
        int initialMessageCounts = getTotalMessagesCount();
        float initialCallDuration = getTotalCallDuration();
        int initialStepCount = getTotalStepsCount();


        editor.putInt("prevTotalCallCount", initialCallCounts);

        editor.putInt("prevTotalMessageCount", initialMessageCounts);

        editor.putFloat("prevTotalCallsDuration", initialCallDuration);

        editor.putInt("prevTotalStepsCount", initialStepCount);



        editor.apply();
    }


    public int getCurrentCallsCount() {
        int totalCallCount = 0;
        int previousCallCount = 0;
        int currentCallCounts = 0;


        totalCallCount = getCallDuration().size();

        previousCallCount = preferences.getInt("prevTotalCallCount", 0);

        currentCallCounts = totalCallCount - previousCallCount;

        return currentCallCounts;

    }


    private List<String> getCallDuration() {
        List<String> calls = new ArrayList<>();
        Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            String callDuration = managedCursor.getString(duration);

            calls.add(callDuration);
        }
        managedCursor.close();
        return calls;

    }

    private float getTotalCallDuration() {
        float callsduaration = 0;

        List<String> calls_duration = getCallDuration();

        for (String duration : calls_duration) {
            int durarion_int = Integer.parseInt(duration);
            callsduaration += durarion_int;
        }

        return callsduaration;

    }

    public int getMessageCount() {

        int totalMessageCount = getTotalMessagesCount();
        int previousMessageCount = preferences.getInt("prevTotalMessageCount", 0);
        return totalMessageCount - previousMessageCount;


    }

    public int getStepsCount() {
        int totalStepsCount = getTotalStepsCount();

        int previousStepsCount = preferences.getInt("prevTotalStepsCount", 0);
        return totalStepsCount - previousStepsCount;

    }


    private int getTotalStepsCount() {

        SharedPreferences preferences2 = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        return preferences2.getInt("stepcount", 0);
    }


    private int getTotalMessagesCount() {
        telephonyProvider = new TelephonyProvider(context);

        return telephonyProvider.getSms(TelephonyProvider.Filter.SENT).getList().size() + telephonyProvider.getMms(TelephonyProvider.Filter.SENT).getList().size();
    }


}
