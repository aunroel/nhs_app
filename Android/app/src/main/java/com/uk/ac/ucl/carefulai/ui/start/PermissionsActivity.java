package com.uk.ac.ucl.carefulai.ui.start;

import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.uk.ac.ucl.carefulai.R;

import java.util.Calendar;
import java.util.List;

public class PermissionsActivity extends AppCompatActivity {
    private static final int STORAGE_CODE = 999;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 42;
    Button statsbutton;
    Button mainlauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        statsbutton = findViewById(R.id.statsenable);
        mainlauncher = findViewById(R.id.mainstarter);

    }

    public void statslaunch(View view) {

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
            ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{
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

    public void infoscreen(View view) {

        int hasSMSPermission = checkSelfPermission(Manifest.permission.READ_SMS);
        int hasCallLogPermission = checkSelfPermission(Manifest.permission.READ_CALL_LOG);
        int hasFilesPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

        final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar beginCal = Calendar.getInstance();
        beginCal.add((Calendar.MINUTE), -60 * 24 * 7);
        Calendar endCal = Calendar.getInstance();
        final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());

        Log.v("Usagestat list: ", queryUsageStats.toString());
        if (queryUsageStats.size() == 0) {
            Toast.makeText(PermissionsActivity.this, "Please enable usage stats to use this app!", Toast.LENGTH_LONG).show();
        } else if (hasSMSPermission != PackageManager.PERMISSION_GRANTED || hasCallLogPermission != PackageManager.PERMISSION_GRANTED || hasFilesPermission != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(PermissionsActivity.this, "Please enable all permissions in order to use this app!", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_CALL_LOG, Manifest.permission.PACKAGE_USAGE_STATS, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);

        } else {
            startActivity(new Intent(this, InitialInfoActivity.class));
        }
    }
}
