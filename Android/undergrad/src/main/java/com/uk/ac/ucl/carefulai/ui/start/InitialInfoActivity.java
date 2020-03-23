package com.uk.ac.ucl.carefulai.ui.start;

import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;

import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class InitialInfoActivity extends AppCompatActivity {

    private Button noThanks1;
    private SharedPreferences dataPreferences;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 42;

    private static final int STORAGE_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page_fragment_1);
        setTitle("Welcome to Careful AI!");

        dataPreferences = this.getSharedPreferences("dataPreference", Context.MODE_PRIVATE);

        noThanks1 = findViewById(R.id.noThanks1);
        noThanks1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back(dataPreferences);
            }
        });
    }

    private void back(SharedPreferences dataPreferences)
    {
        if (dataPreferences.getBoolean("isTracking", false)) {
            startActivity(new Intent(this, AppActivity.class));
            return;
        }
        startActivity(new Intent(this, StartActivity.class));
    }
    public void statslaunch() {

        int hasSMSPermission = checkSelfPermission(Manifest.permission.READ_SMS);
        int hasCallLogPermission = checkSelfPermission(Manifest.permission.READ_CALL_LOG);
        int hasSMSReceivePermission = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
        int hasUsageStatsPermission = checkSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS);
        int hasFilesPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasInternetPermission = checkSelfPermission(Manifest.permission.INTERNET);
        int hasNetworkPermission = checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);

        if (hasSMSPermission != PackageManager.PERMISSION_GRANTED &&
                hasCallLogPermission != PackageManager.PERMISSION_GRANTED &&
                hasSMSReceivePermission != PackageManager.PERMISSION_GRANTED &&
                hasUsageStatsPermission != PackageManager.PERMISSION_GRANTED &&
                hasFilesPermission != PackageManager.PERMISSION_GRANTED &&
                hasInternetPermission != PackageManager.PERMISSION_GRANTED &&
                hasNetworkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(InitialInfoActivity.this, new String[]{
                            Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.RECEIVE_SMS, Manifest.permission.PACKAGE_USAGE_STATS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE
                    },
                    REQUEST_CODE_ASK_PERMISSIONS);

        }

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, STORAGE_CODE);


        final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar beginCal = Calendar.getInstance();
        beginCal.set((Calendar.WEEK_OF_MONTH), (Calendar.WEEK_OF_MONTH - 2));

        Calendar endCal = Calendar.getInstance();

        final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());
        Log.v("Results:", "results for " + beginCal.getTime().toString() + " - " + endCal.getTime().toString());
        if (queryUsageStats.size() == 0) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }
    public void setupscreen(View view) {
        int hasSMSPermission = checkSelfPermission(Manifest.permission.READ_SMS);
        int hasCallLogPermission = checkSelfPermission(Manifest.permission.READ_CALL_LOG);
        int hasFilesPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasContactPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);

        final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar beginCal = Calendar.getInstance();
        beginCal.add((Calendar.MINUTE), -60 * 24 * 7);
        Calendar endCal = Calendar.getInstance();
        final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());

        Log.v("Usagestat list: ", queryUsageStats.toString());
        if (queryUsageStats.size() == 0) {
            Toast.makeText(InitialInfoActivity.this, "Please enable usage stats to use this app! ", Toast.LENGTH_LONG).show();
            statslaunch();
        } else if (hasSMSPermission != PackageManager.PERMISSION_GRANTED || hasCallLogPermission != PackageManager.PERMISSION_GRANTED || hasFilesPermission != PackageManager.PERMISSION_GRANTED || hasContactPermission != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(InitialInfoActivity.this, "Please enable all permissions in order to use this app!", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(InitialInfoActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_CALL_LOG, Manifest.permission.PACKAGE_USAGE_STATS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            statslaunch();
        } else {
            startActivity(new Intent(this, PrimaryCareNetworkActivity.class));
        }
    }
}
