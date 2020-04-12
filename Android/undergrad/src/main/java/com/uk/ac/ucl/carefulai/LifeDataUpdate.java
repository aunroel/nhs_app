package com.uk.ac.ucl.carefulai;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CallLog;

import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.telephony.TelephonyProvider;

//class continuously gets call and message data off the device, and is reset every week to the new call and message count after score is calculated
public class LifeDataUpdate {

    //private DatabaseHelper myDb;
    private SharedPreferences dataPreference; //used to get and put the previous call, step, and message counts
    private SharedPreferences.Editor editor; //used to edit dataPreference

    private TelephonyProvider telephonyProvider; //used to get SMS count
    //private CallsProvider callsProvider;

    private final String myPreferences = "dataPreference"; //get the dataPreference key-value list

    private Context context; //context required for Shared Preferences


    public LifeDataUpdate(Context context) {
        this.context = context;

        dataPreference = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        editor = dataPreference.edit();
        //myDb = new DatabaseHelper(context);

    }

    public boolean checkIfFirstLaunchApp() { //used to check whether to run saveDataToInitialState() and reset data to defaults
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

    public void saveDataToInitialState() { //updates the call message and step counts to the previous week's counts

        int initialCallCounts = getCallDuration().size(); //get the size of the call logs
        int initialMessageCounts = getTotalMessagesCount(); //get the size of messages sent list
        int initialStepCount = getTotalStepsCount(); //get the previous week's stepcount from shared prefs


        //update the previous call, message, and steps counts to the initial values, useful in getCurrentCallsCount(), getMessageCount(), getStepsCount()
        editor.putInt("prevTotalCallCount", initialCallCounts);

        editor.putInt("prevTotalMessageCount", initialMessageCounts);

        editor.putInt("prevTotalStepsCount", initialStepCount);

        editor.apply();
    }


    public int getCurrentCallsCount() {
        int totalCallCount = 0;
        int previousCallCount = 0;
        int currentCallCounts = 0;


        totalCallCount = getCallDuration().size(); //get the total call count right now

        previousCallCount = dataPreference.getInt("prevTotalCallCount", 0); //get the total call count last week

        currentCallCounts = totalCallCount - previousCallCount;

        return currentCallCounts; //return the difference

    }


    private List<String> getCallDuration() { //iterate through the call logs to determine the count
        List<String> calls = new ArrayList<>();
        Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null); //cursor iterates through the call logs using the CallLog.Calls data URI

        try  {
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION); //call duration column
            while (managedCursor.moveToNext()) {
                String callDuration = managedCursor.getString(duration);

                calls.add(callDuration); //add the call duration value to calls
            }
            managedCursor.close();
        } catch (Exception e) {

        }
        return calls; //return the list of call durations (really only interested in their size)
    }

    public int getMessageCount(){

        int totalMessageCount=getTotalMessagesCount(); //get the total message count right now
        int previousMessageCount= dataPreference.getInt("prevTotalMessageCount",0); //get the total message count last week
        return totalMessageCount-previousMessageCount; //return the difference


    }

    public int getStepsCount(){
        int totalStepsCount= getTotalStepsCount(); //get the total step count right now

        int previousStepsCount= dataPreference.getInt("prevTotalStepsCount",0); //get the total step count last week

        return totalStepsCount-previousStepsCount; //return the difference

    }


    private int getTotalStepsCount(){

        return dataPreference.getInt("stepcount",0); //return the total step count right now
    }


    private int getTotalMessagesCount(){
        telephonyProvider = new TelephonyProvider(context);

        try { //return the size of the total SMSs sent plus the size of the total MMSs sent
           return telephonyProvider.getSms(TelephonyProvider.Filter.SENT).getList().size()+telephonyProvider.getMms(TelephonyProvider.Filter.SENT).getList().size();

        } catch (Exception e) {

        }
        return 0;
    }

}
