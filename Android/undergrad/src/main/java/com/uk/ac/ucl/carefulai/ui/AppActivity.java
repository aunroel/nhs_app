package com.uk.ac.ucl.carefulai.ui;


import android.app.ActivityManager;
import android.app.AlarmManager;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uk.ac.ucl.carefulai.Alarm;
import com.uk.ac.ucl.carefulai.DatabaseHelper;
import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ThePedometerService;
import com.uk.ac.ucl.carefulai.ui.nudge.ActivitySupportActivity;
import com.uk.ac.ucl.carefulai.ui.nudge.PersonalNudgeActivity;
import com.uk.ac.ucl.carefulai.ui.nudge.WellbeingActivity;
import com.uk.ac.ucl.carefulai.ui.start.StartActivity;

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

    private TelephonyProvider telephonyProvider; //provider for call count

    private CallsProvider callsProvider; //provider for messages count

    public static String countedSteps = "0";

    private SharedPreferences dataPreferences; //used below for steps, callscount, messagecount, isTracking, and nudges

    private SharedPreferences careNetworkPreferences; //used below to clear care network

    private static final String dataPreference = "dataPreference";

    private NavController navController; //controls the bottom navigation view

    private DatabaseHelper myDb; //used below to clear database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view); //get the bottom nav view
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_config, R.id.navigation_report, R.id.navigation_home, R.id.navigation_messages, R.id.navigation_settings)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment); //the controller for the navigation host fragment that switches between the other fragments
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration); //set up the navigation bar below the fragment
        NavigationUI.setupWithNavController(navView, navController);

        dataPreferences = this.getSharedPreferences(dataPreference, Context.MODE_PRIVATE);

        careNetworkPreferences = getApplicationContext().getSharedPreferences("careNetwork", Context.MODE_PRIVATE);

        myDb = new DatabaseHelper(this);

        if (dataPreferences.getBoolean("isTracking", false)) { //if isTracking is enabled
            if (!isMyServiceRunning(ThePedometerService.class)) { //if the pedometer service isnt running
                startStepCount(); //start the pedometer service
                insertData(); //insert the callscount and messagecount and start the nudge alarms
            }
        }
        else { //isTracking is disabled, clear all user data
            noTrackingPermission(dataPreferences, careNetworkPreferences, myDb);
        }

    }
    
    @Override
    public void onBackPressed() {
        //back to setup disabled, so do nothing
    }

    private void noTrackingPermission(SharedPreferences dataPreferences, SharedPreferences careNetworkPreferences, DatabaseHelper myDb) {
        myDb.clearDatabase(); //clear SQLite database

        SharedPreferences.Editor dataEditor = dataPreferences.edit(); //dataPreferences editor

        SharedPreferences.Editor careEditor = careNetworkPreferences.edit(); //careNetwork editor

        dataEditor.clear(); //clear data

        dataEditor.apply(); //save data clear

        careEditor.clear(); //clear care network

        careEditor.apply(); //save care network clear

        Toast.makeText(this, "Tracking stopped, clearing data", Toast.LENGTH_LONG).show(); //inform user tracking has stopped and data cleared

        startActivity(new Intent(this, StartActivity.class)); //go back to start activity
    }

    protected void onResume() { //run after onCreate when AppActivity is started again
        super.onResume();
        //if isTracking is disabled, clear data
        if (!dataPreferences.getBoolean("isTracking", false))  noTrackingPermission(dataPreferences, careNetworkPreferences, myDb);

        startActivity(new Intent(this, PersonalNudgeActivity.class));

        //get the intent that started the activity
        Intent fromalarm = getIntent();
        if (fromalarm.getBooleanExtra("contactEdit", false)) { //set in ContactsAdapter, if true navigate to Care Network tab
            fromalarm.removeExtra("contactEdit"); //remove the extra
            navController.navigate(R.id.navigation_config); //navigate to Care Network tab to see the added contact
            return;
        }
        else if (fromalarm.getStringExtra("origin") != null) { //origin extra set in Alarm class
            if (fromalarm.getStringExtra("origin").equals("alarm")) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() { //start the WellbeingActivity on new thread after 900ms
                    @Override
                    public void run() {
                        Log.v("On feedback called:", "now");
                        showFeedback(); // start WellbeingActivity
                    }
                }, 900);
            }
            fromalarm.removeExtra("origin"); //remove extra
        }

        Log.v("onResume:", "executed");

        //Send people to the first page of the app if they haven't granted permissions yet
        Calendar beginCal = Calendar.getInstance();
        beginCal.add((Calendar.MINUTE), -60*24*7);
        Calendar endCal = Calendar.getInstance();
        final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());
        Intent intent;
        if (queryUsageStats.size() == 0) {
            intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }
        //usage stats enabled, so check for wellbeing nudges
        else if (dataPreferences.getBoolean("twoDayNudge", false)
                || dataPreferences.getBoolean("twoWeekNudge", false)
                || dataPreferences.getBoolean("tenDayNudge", false) //all set in Alarm class if nudge conditions are met
        ) {

            //edit dataPreferences to remove the nudge booleans (so they reset)
            SharedPreferences.Editor editor = dataPreferences.edit();
            editor.putBoolean("twoDayNudge", false);
            editor.putBoolean("twoWeekNudge", false);
            editor.putBoolean("tenDayNudge", false);
            editor.apply();
            startActivity(new Intent(this, PersonalNudgeActivity.class)); //start the "Let your network know you're okay" activity
        }
        //no wellbeing nudges, so check for activity aid nudge (if an activity has been suggested more than twice, set in SendMessageActivity)
        else if (dataPreferences.getBoolean("activityNudge1", false)
                || dataPreferences.getBoolean("activityNudge2", false)
                || dataPreferences.getBoolean("activityNudge3", false)
        ) {
            startActivity(new Intent(this, ActivitySupportActivity.class)); //go to ActivitySupportActivity
        }

        int latestscore = dataPreferences.getInt("recentScore", 0);

        Log.v("Latest score in S.Pref:", String.valueOf(latestscore)); //log the most recent score

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

    private void showFeedback() { //starts WellbeingActivity
        startActivity(new Intent(this, WellbeingActivity.class));
    }

    private void startStepCount(){ //Starts ThePedometerService and registers the receiver that updates the front end data
        Intent intent = new Intent(this, ThePedometerService.class);
        ContextCompat.startForegroundService(this, intent);
        registerReceiver(broadcastReceiver, new IntentFilter(ThePedometerService.BROADCAST_ACTION));
    }


    private void insertData() {

        SharedPreferences.Editor editor = dataPreferences.edit();

        telephonyProvider = new TelephonyProvider(this); //store the current total number of outgoing calls and texts
        callsProvider = new CallsProvider(this);
        List<Sms> smses = telephonyProvider.getSms(TelephonyProvider.Filter.SENT).getList(); //list of all text messages sent
        List<Mms> mmses = telephonyProvider.getMms(TelephonyProvider.Filter.SENT).getList(); //list of all multimedia messages sent

        int countedMessages = smses.size() + mmses.size(); //get the counted messages
        Data<Call> calls = callsProvider.getCalls(); //get the list of calls made
        int countedCalls = calls.getList().size(); //get the calls count

        editor.putInt("callcount", countedCalls); //store callcount
        editor.putInt("messagecount", countedMessages); //store messagecount
        editor.apply();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); //manager used to set the nudge alarms

        //timestamps
        long time= System.currentTimeMillis();
        Date d = new Date(time);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MINUTE, 60*24*7);
        time = c.getTimeInMillis();


        //store timestamps
        editor.putLong("mainAlarmTime", time);
        editor.putLong("dailyAlarmTime", time);
        editor.putLong("twoDayAlarmTime", time);
        editor.apply();

        //weekly alarm for wellbeing score
        String action1 = "MAIN_ALARM";

        //intent action has to differ in order for the Alarm class to determine that they're different intents
        Intent mainIntent = new Intent(action1, null, this, Alarm.class);

        //check used in Alarm class to add data into database every week
        mainIntent.putExtra("mainIntent", true);

        //pending intent uses a time ticker to trigger the intent
        PendingIntent mainPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC, time,1000*60*60*24*7, mainPendingIntent); //set every 7 days

        //alarm to check for missed target for ten consecutive days
        String action2 = "DAILY_ALARM";

        Intent dailyIntent = new Intent(action2,null,  this, Alarm.class);

        dailyIntent.putExtra("dailyIntent", true);

        PendingIntent dailyPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                dailyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC, time,1000*60, dailyPendingIntent); //checked everyday


        //alarm to check for no movement for two consecutive days
        String action3 = "TWO_DAY_ALARM";

        Intent twoDayIntent = new Intent(action3, null, this, Alarm.class);

        twoDayIntent.putExtra("twoDayIntent", true);

        twoDayIntent.putExtra("twoDayStepCount", dataPreferences.getInt("stepcount" , 0));

        PendingIntent twoDayPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                twoDayIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC, time, 1000*60*60*24*2, twoDayPendingIntent);

        editor.putString("alarmstatus", "on");
        editor.apply();

        Log.d("insertData", "done");
    }

    //used in starting ThePedometerService above
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // call updateUI passing in our intent which is holding the data to display.
            updateViews(intent);
        }
    };

    //Intent comes from ThePedometerService with the "Counted_Step" extra to store into preferences
    private void updateViews(Intent intent) { //the pedometer service updates the static variable countedsteps
        SharedPreferences.Editor dataPreferencesEditor = dataPreferences.edit();

        if (intent.getStringExtra("Counted_Step") != null) {
            countedSteps = intent.getStringExtra("Counted_Step");
//            dataPreferencesEditor.putInt("freshsteps",Integer.parseInt(countedSteps));
//            dataPreferencesEditor.apply();
        }
        else {
            Log.v("counted int value", "null");
        }
    }


}
