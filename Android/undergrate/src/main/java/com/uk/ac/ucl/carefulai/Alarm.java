package com.uk.ac.ucl.carefulai;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static java.lang.StrictMath.abs;

public class Alarm extends BroadcastReceiver {

    private LifeDataUpdate lifeDataUpdate;

    private DatabaseHelper myDb;

    private SharedPreferences dataPreferences;

    private SharedPreferences.Editor editor;

    private Context context;

    private Date intervaldate;

    private Model model;

    private final String myPreferences = "dataPreference";

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;

        lifeDataUpdate=new LifeDataUpdate(this.context);

        if (lifeDataUpdate.checkIfFirstLaunchApp()) {
            lifeDataUpdate.saveDataToInitialState();
        }
        Log.v("Alarm triggered", "now");

        dataPreferences = context.getSharedPreferences(myPreferences, context.MODE_PRIVATE);

        editor = dataPreferences.edit();

        long startedtime = dataPreferences.getLong("alarmtime", 0);
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
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        newintent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC, newtime, 1000*60*60*24*7, pendingIntent);
                Log.d("missed, alarm set for:", String.valueOf(newdate));
            }
        }


        model = new Model(context);
        myDb = new DatabaseHelper(context);

        int score = 0;

        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {

            Calendar c = Calendar.getInstance();
            c.setTime(intervaldate);
            c.add(Calendar.MINUTE, 60*24*7);

            long newinterval = c.getTimeInMillis();

            editor.putLong("alarmtime", newinterval);

            editor.apply();

            SharedPreferences dataPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);

            SharedPreferences.Editor dataEditor = dataPreferences.edit();

            score = model.calculateScore(abs(lifeDataUpdate.getStepsCount()),  abs(lifeDataUpdate.getCurrentCallsCount()), abs(lifeDataUpdate.getMessageCount()));

            dataEditor.putInt("recentScore", score);

            dataEditor.apply();

            boolean isInserted = myDb.insertData(lifeDataUpdate.getStepsCount(), lifeDataUpdate.getCurrentCallsCount(),lifeDataUpdate.getMessageCount());

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

                SharedPreferences dataPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);

                SharedPreferences.Editor dataEditor = dataPreferences.edit();

                score = model.calculateScore(abs(lifeDataUpdate.getStepsCount()),  abs(lifeDataUpdate.getCurrentCallsCount()), abs(lifeDataUpdate.getMessageCount()));

                dataEditor.putInt("recentScore", score);

                dataEditor.apply();
                boolean isInserted = myDb.insertData(lifeDataUpdate.getStepsCount(), lifeDataUpdate.getCurrentCallsCount(),lifeDataUpdate.getMessageCount());

                if (isInserted) {
                    Toast.makeText(context, "Data Inserted - Score Prediction in Progress!", Toast.LENGTH_LONG).show();
                }

            }
        }
        Intent toAlarm = new Intent("ALARM_INTENT");
        LocalBroadcastManager.getInstance(context).sendBroadcast(toAlarm);
    }


    public static float[] inputstandardise(float[] f) {            //this is very long-winded, but is because there is no easy-to-implement standardisation function in TF
        f[0] = ((float) (f[0] - 62979)/ (float) 24589.1687);        //mean and st.dev values calculated from original dataset
        f[1] = ((float) (f[1] - 5.9595)/ (float) 4.2198);
        f[2] = ((float) (f[2] - 1386)/ (float) 1046.6346);
        f[3] = ((float) (f[3] - 60)/ (float) 42.4264);
        f[4] = ((float) (f[4] - 4.4935)/ (float) 2.8759);
        f[5] = ((float) (f[5] - 198.612)/ (float) 103.5881);
        f[6] = ((float) (f[6] - 23307.48)/ (float) 10025.5787);
        f[7] = ((float) (f[7] - 2165.645)/ (float) 1789.5679);
        return f;
    }


}

