package com.uk.ac.ucl.carefulai;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.uk.ac.ucl.carefulai.ui.AppActivity;
import com.uk.ac.ucl.carefulai.ui.home.HomeFragment;


import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ThePedometerService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor accel;
    private Sensor peddetect;
    private Sensor pedcount;

    private final static int NOTIFICATION_ID = 95;

    public static int oldstepcount;
    public static int stepcount;
    public static int newstepcount;
    public static int curdetectedsteps;
    static double[] x;
    static int score;

    //making sure the service is running
    boolean servicestatus;
    static NotificationManager notificationManager;


    private static final String TAG = "StepService";
    public static final String BROADCAST_ACTION = "com.example.jakesetton.myfirstapp.mybroadcast";

    Intent intent = new Intent(BROADCAST_ACTION);

    private final Handler handler = new Handler();

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    //---- Setup for providers
    Context context;
    //---

    String alarmcheck;
    SharedPreferences dataPreferences;


    static double[] mean;
    static double[] stdev;
    DatabaseHelper myDb;

    private final String myPreferences = "dataPreference";

    public void onCreate() {
        super.onCreate();
        //this code draws on an earlier GitHub implementation of a pedometer service; see the references for details
        context = getApplicationContext();
        myDb = new DatabaseHelper(context);
        LocalBroadcastManager.getInstance(context).registerReceiver(stepReceiver, new IntentFilter("ALARM_INTENT"));

        dataPreferences = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);

        alarmcheck = dataPreferences.getString("alarmstatus", "");

        if (!alarmcheck.equals("on")) {
            Log.d("No alarm set", "!");
            onDestroy();
            return;
        }

        stepcount = 0;
        oldstepcount = dataPreferences.getInt("oldstep", 0);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Service", "Start");
        context = getApplicationContext();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }
        else {
            showNotification();
        }

        if(getSystemService(Context.SENSOR_SERVICE)!=null){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);}


        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            Log.d("Service", "gotaccel");
            accel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, accel, 0);
        } else {

            Toast.makeText(this, "Your device has no accelerometer!", Toast.LENGTH_LONG).show();
        }
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            peddetect = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mSensorManager.registerListener(this, peddetect, 0);
            Log.d("Service", "gotdetector");


        }
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            pedcount = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mSensorManager.registerListener(this, pedcount, 0);
            Log.d("Service", "GOTCOUNTER");

        } else {
            Toast.makeText(this, "Your device has no pedometer!", Toast.LENGTH_LONG).show();
        }

        //methods for adding up steps and getting accelerometer values

        servicestatus = false;


        handler.removeCallbacks(updateBroadcastData);
        handler.post(updateBroadcastData);

        return START_STICKY;
    }

    public void onDestroy() {
        Log.v("Service", "Stop" + stepcount + curdetectedsteps);;

        servicestatus = true;

        //dismissNotification();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(stepReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            int detectSteps = (int) event.values[0];
            curdetectedsteps += detectSteps;
        }
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int countSteps = (int) event.values[0];
            if (stepcount == 0) {
                stepcount = (int) event.values[0];
            }
            newstepcount = countSteps - stepcount;
        }
    }

    private Runnable updateBroadcastData = new Runnable() {
        public void run() {

            SharedPreferences.Editor editor = dataPreferences.edit();

            if (!servicestatus) {
                // Call the method that broadcasts data to the Activity
                broadcastSensorValue();

                editor.putInt("oldstep", dataPreferences.getInt("stepcount", 0)); //Set old step count to old value of stepcount

                editor.putInt("stepcount", newstepcount + oldstepcount);

                editor.apply();
                // Call "handler.postDelayed" again, after a specified delay.
                handler.postDelayed(this, 2000);
            }
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private void broadcastSensorValue() {
        intent.putExtra("Counted_Step_Int", newstepcount);
        intent.putExtra("Counted_Step", String.valueOf(newstepcount + oldstepcount));
        intent.putExtra("Detected_Step_Int", curdetectedsteps);
        intent.putExtra("Detected_Step", String.valueOf(curdetectedsteps));
        sendBroadcast(intent);
    }

    private void showNotification() { //update method if need be
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Pedometer");
        notificationBuilder.setContentText("Pedometer is running in the background.");
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setColor(Color.parseColor("#6600cc"));
        int colorLED = Color.argb(255, 0, 255, 0);
        notificationBuilder.setLights(colorLED, 500, 500);
        // To  make sure that the Notification LED is triggered.
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setOngoing(true);

        //Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        notificationBuilder.setContentIntent(resultPendingIntent);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //notificationManager.notify(0, notificationBuilder.build());

        startForeground(NOTIFICATION_ID, notificationBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.jakesetton.myfirstapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.GREEN);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;

        notificationManager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Pedometer session is running in the background.")
                .setColor(Color.parseColor("#6600cc"))
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(NOTIFICATION_ID, notification);

    }



    private void dismissNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private BroadcastReceiver stepReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

                final Context c = context;

                final Handler handler = new Handler();

                //final SharedPreferences.Editor editor = dataPreferences.edit();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run () {

                        score = dataPreferences.getInt("recentScore", 0);

                        Log.v("Score is: ", String.valueOf(score));
                        // to insert the new week's score after new week's data is inserted, we have to update the score value in the new line of the db
                        int week = (int) myDb.getThisWeekNumber();
                        Log.v("week is: ", String.valueOf(week));
                        myDb.insertScore(String.valueOf(week), score);

                        startDataSending(dataPreferences);

                        Intent openmainpage = new Intent(getApplicationContext(), AppActivity.class);
                        openmainpage.putExtra("origin", "alarm");
                        openmainpage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(openmainpage);

                    }
                }, 3000);
        }
    };

    private void startDataSending(SharedPreferences dataPreferences) {

        boolean remoteSharing = dataPreferences.getBoolean("remoteSharing", false);

        if (remoteSharing) {
            String url = "http://178.79.172.202:8080/androidData";

            PostRequest postRequest = new PostRequest(url, this);

            Thread thread = new Thread(postRequest);

            thread.start();
        }
    }

}