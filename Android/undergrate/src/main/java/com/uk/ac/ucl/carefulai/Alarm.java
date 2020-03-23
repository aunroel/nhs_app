package com.uk.ac.ucl.carefulai;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import uclsse.comp0102.nhsxapp.api.NhsAPI;
import uclsse.comp0102.nhsxapp.api.NhsTrainingDataHolder;

import static java.lang.StrictMath.abs;


public class Alarm extends BroadcastReceiver {

    private LifeDataUpdate lifeDataUpdate;

    private DatabaseHelper myDb;

    private SharedPreferences dataPreferences;

    private SharedPreferences careNetworkPreferences;

    private static final String carePreference = "careNetwork";

    private SharedPreferences.Editor editor;

    private Context context;

    private Date intervaldate;

    private final String myPreferences = "dataPreference";

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getBooleanExtra("mainIntent", false)) {
            mainIntent(context, intent);
        }

        else if (intent.getBooleanExtra("twoDayIntent", false)) {
            twoDayIntent(context, intent);
        }

        else if (intent.getBooleanExtra("dailyIntent", false)) {
            dailyIntent(context, intent);
        }

    }

    private void dailyIntent(final Context context, Intent intent) {
        this.context = context;

        dataPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);

        careNetworkPreferences = context.getSharedPreferences(carePreference, Context.MODE_PRIVATE);

        editor = dataPreferences.edit();

        long startedtime = dataPreferences.getLong("dailyAlarmTime", 0);

        intervaldate = new Date(startedtime);

        Log.v("onReceive called:", "Alarm is now " + intervaldate.toString()); //making sure the next scheduled score prediction is correct
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //dealing with cases where the alarm is triggered by BOOT_COMPLETED     (which is every time the phone is switched on, so not necessarily just at the end of every week...)
        String action = intent.getAction();

        if (action != null && action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Date currentdate = new Date(System.currentTimeMillis());
            ///etc.
            if ((currentdate.compareTo(intervaldate) < 0)) {  // if the phone is turned on before the next alarm, no data insertion/classification has been missed.
                Intent newintent = new Intent(context, Alarm.class);

                newintent.putExtra("dailyIntent", true);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        newintent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC, startedtime, 1000*60*60*24, pendingIntent); //just re-establish the alarm for the scheduled time as it is, because it's in the future
                Log.v("Alarm will go off at: ", String.valueOf(intervaldate));
                return;
            }
            else if ((currentdate.compareTo(intervaldate) > 0)) { //if the phone is turned on after the alarm was supposed to go off, we've missed a data insertion

                Log.d("Update missed from", intervaldate.toString());

                //Move interval forward by a week and reschedule alarm

                Calendar d = Calendar.getInstance();
                d.setTime(intervaldate);
                d.add(Calendar.MINUTE, 60*24*7);
                long newtime = d.getTimeInMillis();
                Date newdate = new Date(newtime);
                //Log.v("miss, new long is:", String.valueOf(newtime));
                Intent newintent = new Intent(context, Alarm.class);

                newintent.putExtra("dailyIntent", true);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        newintent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC, newtime, 1000*60*60*24, pendingIntent);
                Log.d("missed, alarm set for:", String.valueOf(newdate));
            }
        }


        int currentStepCount = dataPreferences.getInt("stepcount", 0);

        int currentContact = dataPreferences.getInt("callcount", 0) + dataPreferences.getInt("messagecount", 0);

        int targetSteps = careNetworkPreferences.getInt("stepsTarget", 0);

        int targetContact = careNetworkPreferences.getInt("contactTarget", 0);

        if (currentContact < targetContact || currentStepCount < targetSteps) {
            editor.putInt("tenDayNudgeCount", dataPreferences.getInt("tenDayNudgeCount", 0) + 1);
        }
        else {
            editor.putInt("tenDayNudgeCount", 0);
        }

        if (dataPreferences.getInt("tenDayNudgeCount", 0) == 10) {
            editor.putInt("tenDayNudgeCount", 0);
            editor.putBoolean("tenDayNudge", true);
        }
        editor.apply();
    }



    private void twoDayIntent(final Context context, Intent intent) {
        this.context = context;

        dataPreferences = context.getSharedPreferences(myPreferences, context.MODE_PRIVATE);

        editor = dataPreferences.edit();

        long startedtime = dataPreferences.getLong("twoDayAlarmTime", 0);

        intervaldate = new Date(startedtime);

        Log.v("onReceive called:", "Alarm is now " + intervaldate.toString()); //making sure the next scheduled score prediction is correct
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //dealing with cases where the alarm is triggered by BOOT_COMPLETED     (which is every time the phone is switched on, so not necessarily just at the end of every week...)
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Date currentdate = new Date(System.currentTimeMillis());
            ///etc.
            if ((currentdate.compareTo(intervaldate) < 0)) {  // if the phone is turned on before the next alarm, no data insertion/classification has been missed.
                Intent newintent = new Intent(context, Alarm.class);

                newintent.putExtra("twoDayIntent", true);

                newintent.putExtra("twoDayStepCount", dataPreferences.getInt("stepcount" , 0));

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        newintent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC, startedtime, 1000*60*60*24*7, pendingIntent); //just re-establish the alarm for the scheduled time as it is, because it's in the future
                Log.v("Alarm will go off at: ", String.valueOf(intervaldate));
                return;
            }
            else if ((currentdate.compareTo(intervaldate) > 0)) { //if the phone is turned on after the alarm was supposed to go off, we've missed a data insertion

                Log.d("Update missed from", intervaldate.toString());

                //Move interval forward by a week and reschedule alarm

                Calendar d = Calendar.getInstance();
                d.setTime(intervaldate);
                d.add(Calendar.MINUTE, 60*24*7);
                long newtime = d.getTimeInMillis();
                Date newdate = new Date(newtime);
                //Log.v("miss, new long is:", String.valueOf(newtime));
                Intent newintent = new Intent(context, Alarm.class);

                newintent.putExtra("twoDayIntent", true);

                newintent.putExtra("twoDayStepCount", dataPreferences.getInt("stepcount" , 0));

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        newintent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC, newtime, 1000*60*60*24*2, pendingIntent);
                Log.d("missed, alarm set for:", String.valueOf(newdate));
            }
        }


        int comparisonStepCount = intent.getIntExtra("twoDayStepCount", 0);

        int currentStepCount = dataPreferences.getInt("stepcount", 0);

        if (comparisonStepCount == currentStepCount) { //no change in two days
            SharedPreferences.Editor editor = dataPreferences.edit();
            editor.putBoolean("twoDayNudge", true);
        }
        editor.apply();
    }

    private void mainIntent(final Context context, Intent intent) {
        this.context = context;

        lifeDataUpdate=new LifeDataUpdate(this.context);

        if (lifeDataUpdate.checkIfFirstLaunchApp()) {
            lifeDataUpdate.saveDataToInitialState();
        }
        Log.v("Alarm triggered", "now");

        dataPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);

        editor = dataPreferences.edit();

        long startedtime = dataPreferences.getLong("mainAlarmTime", 0);

        intervaldate = new Date(startedtime);

        Log.v("onReceive called:", "Alarm is now " + intervaldate.toString()); //making sure the next scheduled score prediction is correct
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //dealing with cases where the alarm is triggered by BOOT_COMPLETED     (which is every time the phone is switched on, so not necessarily just at the end of every week...)
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Date currentdate = new Date(System.currentTimeMillis());
            ///etc.
            if ((currentdate.compareTo(intervaldate) < 0)) {  // if the phone is turned on before the next alarm, no data insertion/classification has been missed.
                Intent newintent = new Intent(context, Alarm.class);

                newintent.putExtra("mainIntent", true);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        newintent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC, startedtime, 1000*60*60*24*7, pendingIntent); //just re-establish the alarm for the scheduled time as it is, because it's in the future
                Log.v("Alarm will go off at: ", String.valueOf(intervaldate));
                return;
            }
            else if ((currentdate.compareTo(intervaldate) > 0)) { //if the phone is turned on after the alarm was supposed to go off, we've missed a data insertion

                Log.d("Update missed from", intervaldate.toString());

                //Move interval forward by a week and reschedule alarm

                Calendar d = Calendar.getInstance();
                d.setTime(intervaldate);
                d.add(Calendar.MINUTE, 60*24*7);
                long newtime = d.getTimeInMillis();
                Date newdate = new Date(newtime);
                //Log.v("miss, new long is:", String.valueOf(newtime));
                Intent newintent = new Intent(context, Alarm.class);
                newintent.putExtra("mainIntent", true);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        newintent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC, newtime, 1000*60*60*24*7, pendingIntent);
                Log.d("missed, alarm set for:", String.valueOf(newdate));
            }
            Intent toAlarm = new Intent("ALARM_INTENT");
            LocalBroadcastManager.getInstance(context).sendBroadcast(toAlarm);
        }

        myDb = new DatabaseHelper(context);

        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {

            Calendar c = Calendar.getInstance();
            c.setTime(intervaldate);
            c.add(Calendar.MINUTE, 60*24*7);

            long newinterval = c.getTimeInMillis();

            editor.putLong("alarmtime", newinterval);

            editor.apply();

            int steps = lifeDataUpdate.getStepsCount();
            int calls = lifeDataUpdate.getCurrentCallsCount();
            int msgs = lifeDataUpdate.getMessageCount();
            new DataStorageHelper().record(steps, calls, msgs);

            boolean isInserted = myDb.insertData(steps, calls, msgs);

            if (isInserted) {
                Toast.makeText(context, "Data Inserted - Score Prediction in Progress!", Toast.LENGTH_LONG).show();
            }
        }
        else {

            while (res.moveToNext()) {

                Calendar c = Calendar.getInstance();

                c.setTime(intervaldate);

                c.add(Calendar.MINUTE, 60*24*7);

                long newinterval = c.getTimeInMillis();

                editor.putLong("alarmtime", newinterval);

                editor.apply();

                int steps = lifeDataUpdate.getStepsCount();
                int calls = lifeDataUpdate.getCurrentCallsCount();
                int msgs = lifeDataUpdate.getMessageCount();
                new DataStorageHelper().record(steps, calls, msgs);

                boolean isInserted = myDb.insertData(steps, calls, msgs);

                if (isInserted) {
                    Toast.makeText(context, "Data Inserted - Score Prediction in Progress!", Toast.LENGTH_LONG).show();
                }

            }
        }
        Intent toAlarm = new Intent("ALARM_INTENT");
        LocalBroadcastManager.getInstance(context).sendBroadcast(toAlarm);
    }


    private class DataStorageHelper {

        private static final String SCORE_KEY = "recentScore";
        private static final String TWO_WEEK_WARN_KEY = "twoWeekWarning";
        private static final String TWO_WEEK_NUDGE_KEY = "twoWeekWarning";
        private static final String M_PREFERENCE = "dataPreference";
        private static final int CONTEXT_MODEL = Context.MODE_PRIVATE;

        private NhsAPI nhsAPI;
        private SharedPreferences dataPreferences;

        private LiveData<Integer> liveScore;
        private LiveData<Boolean> liveRecordResult;

        DataStorageHelper(){
            dataPreferences = context.getSharedPreferences(M_PREFERENCE, CONTEXT_MODEL);
            Application application = (Application) context.getApplicationContext();
            nhsAPI = new NhsAPI(application);
            liveScore = nhsAPI.getTrainingScore();
        }


        void record(int steps, int calls, int msgs){
            NhsTrainingDataHolder dataHolder = new NhsTrainingDataHolder();
            dataHolder.setWeeklySteps(steps);
            dataHolder.setWeeklyCalls(calls);
            dataHolder.setWeeklyMessages(msgs);
            liveRecordResult = nhsAPI.record(dataHolder);
            liveRecordResult.observeForever(isRecordFinishedObserver);
            liveScore = nhsAPI.getTrainingScore();
            liveScore.observeForever(scoreObserver);
        }

        private Observer<Integer> scoreObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer score) {
                liveScore.removeObserver(scoreObserver);
                SharedPreferences.Editor dataEditor = dataPreferences.edit();
                boolean isTwoWeekWarning = dataPreferences.getBoolean(TWO_WEEK_WARN_KEY, false);
                if(score>2){
                    dataEditor.putBoolean(TWO_WEEK_WARN_KEY, false);
                    dataEditor.putBoolean(TWO_WEEK_NUDGE_KEY, false);
                } else if (isTwoWeekWarning) {
                    dataEditor.putBoolean(TWO_WEEK_NUDGE_KEY, true);
                } else {
                    dataEditor.putBoolean(TWO_WEEK_WARN_KEY, true);
                }
                dataEditor.putInt(SCORE_KEY, score);
                dataEditor.apply();
            }
        };

        private Observer<Boolean> isRecordFinishedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                liveRecordResult.removeObserver(isRecordFinishedObserver);
                nhsAPI.uploadJsonNow();
                nhsAPI.updateTfModelNow();
                nhsAPI.updateTrainingScore();
            }
        };

    }

}

