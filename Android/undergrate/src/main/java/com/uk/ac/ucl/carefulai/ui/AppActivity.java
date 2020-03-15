package com.uk.ac.ucl.carefulai.ui;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uk.ac.ucl.carefulai.Alarm;
import com.uk.ac.ucl.carefulai.DatabaseHelper;
import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ThePedometerService;
import com.uk.ac.ucl.carefulai.ui.start.StartActivity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;
import me.everything.providers.android.telephony.Mms;
import me.everything.providers.android.telephony.Sms;
import me.everything.providers.android.telephony.TelephonyProvider;
import me.everything.providers.core.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AppActivity extends AppCompatActivity {

    private TelephonyProvider telephonyProvider;

    private CallsProvider callsProvider;

    private String countedSteps = "";

    private SharedPreferences dataPreferences;

    private static final String dataPreference = "dataPreference";


    private Dialog seekBarDialog;

    private SeekBar simpleSeekBar;

    private Button saveUserScore;

    private DatabaseHelper myDb;

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

        seekBarDialog = new Dialog(this);

        myDb = new DatabaseHelper(this);

        if (!isMyServiceRunning(ThePedometerService.class)) { startStepCount(); insertData(); }

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) { //checks whether the pedometer is working
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startStepCount(){
        Intent intent = new Intent(this, ThePedometerService.class);
        ContextCompat.startForegroundService(this, intent);
        registerReceiver(broadcastReceiver, new IntentFilter(ThePedometerService.BROADCAST_ACTION));
    }



    private void showFeedback() {

        int score = dataPreferences.getInt("recentScore", 0);

        seekBarDialog.setContentView(R.layout.epic_yesnofeedback);

        ImageView closePopup = (ImageView) seekBarDialog.findViewById(R.id.closePopup);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarDialog.dismiss();
            }
        });

        simpleSeekBar =findViewById(R.id.simpleSeekBar);

        simpleSeekBar.setProgress(score);

        simpleSeekBar.setMax(10);

        saveUserScore = (Button) seekBarDialog.findViewById(R.id.saveSeekbar);

        saveUserScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserScore();
            }
        });

    }

    private void saveUserScore() {

        int userScore = simpleSeekBar.getProgress();

        int week = (int) myDb.getThisWeekNumber();

        myDb.insertUserScore(String.valueOf(week),userScore);

        seekBarDialog.dismiss();
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
        long time= System.currentTimeMillis();
        Date d = new Date(time);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MINUTE, 60*24*7);
        time = c.getTimeInMillis();
        editor.putLong("alarmtime", time);
        editor.apply();
        Log.v("alarmtime is now: ",String.valueOf(new Date(dataPreferences.getLong("alarmtime", 0))));
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC, time,1000*60*60*24*7, pendingIntent);
        Toast.makeText(AppActivity.this, "Tracking Started!", Toast.LENGTH_LONG).show();

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // call updateUI passing in our intent which is holding the data to display.
            updateViews(intent);
        }
    };

    private void updateViews(Intent intent) { //the pedometer service updates the static variable countedsteps
        SharedPreferences.Editor dataPreferencesEditor = dataPreferences.edit();

        if (intent.getStringExtra("Counted_Step") != null) {
            countedSteps = intent.getStringExtra("Counted_Step");

            dataPreferencesEditor.putInt("freshsteps",Integer.parseInt(countedSteps));
            dataPreferencesEditor.apply();
        }
        else {
            Log.v("counted int value", "null");
        }
    }

    protected void onResume() {
        super.onResume();
        Intent fromalarm = getIntent();
        if (fromalarm.getStringExtra("origin") != null) {
            if (fromalarm.getStringExtra("origin").equals("alarm")) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("On feedback called:", "now");
                        showFeedback();
                    }
                }, 900);
            }
            fromalarm.removeExtra("origin");
        }
        Log.v("onResume:", "executed");

        //Send people to the first page of the app if they haven't granted permissions yet
        Calendar beginCal = Calendar.getInstance(); //is there a better way of doing this than checking for usage stats?
        beginCal.add((Calendar.MINUTE), -60*24*7);
        Calendar endCal = Calendar.getInstance();
        final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());
        Intent intent;
        if (queryUsageStats.size() == 0) {
            intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }

        int latestscore = dataPreferences.getInt("recentScore", 0);
        Log.v("Latest score in S.Pref:", String.valueOf(latestscore));

    }
}
