package com.uk.ac.ucl.carefulai;

//TODO: comment this

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;
import java.util.Date;

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

    private Model model;

    private final String myPreferences = "dataPreference";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Alarm", "!");

        if ("MAIN_ALARM".equals(intent.getAction())) {
            mainIntent(context, intent);
        }

        else if ("DAILY_ALARM".equals(intent.getAction())) {
            twoDayIntent(context, intent);
        }

        else if ("TWO_DAY_ALARM".equals(intent.getAction())) {
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
                String action2 = "DAILY_ALARM";

                Intent dailyIntent = new Intent(action2,null,  context, Alarm.class);

                dailyIntent.putExtra("dailyIntent", true);

                PendingIntent dailyPendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        dailyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC, startedtime,1000*60*60*24, dailyPendingIntent); //just re-establish the alarm for the scheduled time as it is, because it's in the future
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
                String action2 = "DAILY_ALARM";

                Intent dailyIntent = new Intent(action2,null,  context, Alarm.class);

                dailyIntent.putExtra("dailyIntent", true);

                PendingIntent dailyPendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        dailyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC, newtime,1000*60*60*24, dailyPendingIntent);
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
                String action3 = "TWO_DAY_ALARM";

                Intent twoDayIntent = new Intent(action3, null, context, Alarm.class);

                twoDayIntent.putExtra("twoDayIntent", true);

                twoDayIntent.putExtra("twoDayStepCount", dataPreferences.getInt("stepcount" , 0));

                PendingIntent twoDayPendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        twoDayIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC, startedtime, 1000*60*60*24*2, twoDayPendingIntent);//just re-establish the alarm for the scheduled time as it is, because it's in the future
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
                String action3 = "TWO_DAY_ALARM";

                Intent twoDayIntent = new Intent(action3, null, context, Alarm.class);

                twoDayIntent.putExtra("twoDayIntent", true);

                twoDayIntent.putExtra("twoDayStepCount", dataPreferences.getInt("stepcount" , 0));

                PendingIntent twoDayPendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        twoDayIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC, newtime, 1000*60*60*24*2, twoDayPendingIntent);
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
                String action1 = "MAIN_ALARM";

                Intent mainIntent = new Intent(action1, null, context, Alarm.class);

                mainIntent.putExtra("mainIntent", true);

                PendingIntent mainPendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC, startedtime,1000*60, mainPendingIntent); //just re-establish the alarm for the scheduled time as it is, because it's in the future
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
                String action1 = "MAIN_ALARM";

                Intent mainIntent = new Intent(action1, null, context, Alarm.class);

                mainIntent.putExtra("mainIntent", true);

                PendingIntent mainPendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC, newtime,1000*60, mainPendingIntent);
                Log.d("missed, alarm set for:", String.valueOf(newdate));
            }
            Intent toAlarm = new Intent("ALARM_INTENT");
            LocalBroadcastManager.getInstance(context).sendBroadcast(toAlarm);
        }


        myDb = new DatabaseHelper(context);

        /* START CODE FOR MODEL */
        model = new Model(context);
        model.recordThisWeekData();
        /* END CODE FOR MODEL */

        LiveData<Integer> liveScore;

        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {

            Calendar c = Calendar.getInstance();
            c.setTime(intervaldate);
            c.add(Calendar.MINUTE, 60*24*7);

            long newinterval = c.getTimeInMillis();

            editor.putLong("alarmtime", newinterval);

            editor.apply();

            /* START OF MODIFICATION ABOUT MODEL */
            liveScore = model.calculateScore(
                    abs(lifeDataUpdate.getStepsCount()),
                    abs(lifeDataUpdate.getCurrentCallsCount()),
                    abs(lifeDataUpdate.getMessageCount())
            );
            Model.OneTimeObserver<Integer> scoreUpdateObserver = new Model.OneTimeObserver<Integer>() {
                @Override void onChangedCore(Integer score) {

                    SharedPreferences dataPreferences =
                            context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
                    SharedPreferences.Editor dataEditor = dataPreferences.edit();

                    boolean isTwoWeekWarning =
                            dataPreferences.getBoolean("twoWeekWarning", false);
                    if (isTwoWeekWarning && score <= 2) {
                        dataEditor.putBoolean("twoWeekNudge", true);
                    } else if (score <= 2) {
                        dataEditor.putBoolean("twoWeekWarning", true);
                    }
                    dataEditor.putInt("recentScore", score);
                    dataEditor.apply();

                    boolean isInserted = myDb.insertData(
                            lifeDataUpdate.getStepsCount(),
                            lifeDataUpdate.getCurrentCallsCount(),
                            lifeDataUpdate.getMessageCount()
                    );
                    if (isInserted) {
                        String predictingNotice = "Data Inserted - Score Prediction in Progress!";
                        Toast.makeText(context, predictingNotice, Toast.LENGTH_LONG).show();
                    }
                }
            };
            liveScore.observe(scoreUpdateObserver, scoreUpdateObserver);
            /*END OF MODIFY ABOUT MODEL*/
        }
        else {

            while (res.moveToNext()) {

                Calendar c = Calendar.getInstance();

                c.setTime(intervaldate);

                c.add(Calendar.MINUTE, 60*24*7);

                long newinterval = c.getTimeInMillis();

                editor.putLong("alarmtime", newinterval);

                editor.apply();

                /* START OF MODIFICATION ABOUT MODEL */
                liveScore = model.calculateScore(
                        abs(lifeDataUpdate.getStepsCount()),
                        abs(lifeDataUpdate.getCurrentCallsCount()),
                        abs(lifeDataUpdate.getMessageCount())
                );
                Model.OneTimeObserver<Integer> scoreUpdateObserver = new Model.OneTimeObserver<Integer>() {
                    @Override
                    void onChangedCore(Integer score) {

                        SharedPreferences dataPreferences =
                                context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
                        SharedPreferences.Editor dataEditor = dataPreferences.edit();
                        boolean isTwoWeekWarning =
                                dataPreferences.getBoolean("twoWeekWarning", false);
                        if (isTwoWeekWarning && score <= 2) {
                            dataEditor.putBoolean("twoWeekNudge", true);
                        } else if (score <= 2) {
                            dataEditor.putBoolean("twoWeekWarning", true);
                        } else {
                            dataEditor.putBoolean("twoWeekWarning", false);
                            dataEditor.putBoolean("twoWeekNudge", false);
                        }
                        dataEditor.putInt("recentScore", score);
                        dataEditor.apply();

                        boolean isInserted = myDb.insertData(
                                lifeDataUpdate.getStepsCount(),
                                lifeDataUpdate.getCurrentCallsCount(),
                                lifeDataUpdate.getMessageCount()
                        );
                        if (isInserted) {
                            String predictingNotice = "Data Inserted - Score Prediction in Progress!";
                            Toast.makeText(context, predictingNotice, Toast.LENGTH_LONG).show();
                        }
                    }
                };
                liveScore.observe(scoreUpdateObserver, scoreUpdateObserver);
                /*END OF MODIFY ABOUT MODEL*/
            }
        }
        Intent toAlarm = new Intent("ALARM_INTENT");
        LocalBroadcastManager.getInstance(context).sendBroadcast(toAlarm);
    }


}

