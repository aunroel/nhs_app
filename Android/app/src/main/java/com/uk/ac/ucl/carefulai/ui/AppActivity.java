package com.uk.ac.ucl.carefulai.ui;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uk.ac.ucl.carefulai.Alarm;
import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ThePedometerService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;
import me.everything.providers.android.telephony.Mms;
import me.everything.providers.android.telephony.Sms;
import me.everything.providers.android.telephony.TelephonyProvider;
import me.everything.providers.core.Data;

public class AppActivity extends AppCompatActivity {

    private TelephonyProvider telephonyProvider;

    private CallsProvider callsProvider;

    private String countedSteps = "";

    private SharedPreferences dataPreferences;

    private static final String dataPreference = "dataPreference";
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // call updateUI passing in our intent which is holding the data to display.
            updateViews(intent);
        }
    };


    private boolean isMyServiceRunning(Class<?> serviceClass) { //checks whether the pedometer is working
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_config, R.id.navigation_report, R.id.navigation_home, R.id.navigation_messages, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        dataPreferences = getApplicationContext().getSharedPreferences(dataPreference, Context.MODE_PRIVATE);


        if (!isMyServiceRunning(ThePedometerService.class)) {
            startStepCount();
            insertData();
        }

    }

    private void startStepCount() {
        Intent intent = new Intent(this, ThePedometerService.class);
        ContextCompat.startForegroundService(this, intent);
        registerReceiver(broadcastReceiver, new IntentFilter(ThePedometerService.BROADCAST_ACTION));
    }

    private void insertData() {

        SharedPreferences.Editor editor = dataPreferences.edit();

        telephonyProvider = new TelephonyProvider(this); //when the alarm is turned on, store the current total number of outgoing calls and texts
        callsProvider = new CallsProvider(this);
        List<Sms> smses = telephonyProvider.getSms(TelephonyProvider.Filter.SENT).getList();
        List<Mms> mmses = telephonyProvider.getMms(TelephonyProvider.Filter.SENT).getList();

        int countedMessages = smses.size() + mmses.size();
        Data<Call> calls = callsProvider.getCalls();
        int countedCalls = calls.getList().size();

        editor.putInt("callcount", countedCalls);
        editor.putInt("messagecount", countedMessages);
        editor.apply();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long time = System.currentTimeMillis();
        Date d = new Date(time);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MINUTE, 60 * 24 * 7);
        time = c.getTimeInMillis();
        editor.putLong("alarmtime", time);
        editor.apply();
        Log.v("alarmtime is now: ", String.valueOf(new Date(dataPreferences.getLong("alarmtime", 0))));
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC, time, 1000 * 60 * 60 * 24 * 7, pendingIntent);
        Toast.makeText(AppActivity.this, "Tracking Started!", Toast.LENGTH_LONG).show();

    }

    private void updateViews(Intent intent) { //the pedometer service updates the static variable countedsteps
        SharedPreferences.Editor dataPreferencesEditor = dataPreferences.edit();

        if (intent.getStringExtra("Counted_Step") != null) {
            countedSteps = intent.getStringExtra("Counted_Step");

            dataPreferencesEditor.putInt("freshsteps", Integer.parseInt(countedSteps));
            dataPreferencesEditor.apply();
        } else {
            Log.v("counted int value", "null");
        }
    }
}
