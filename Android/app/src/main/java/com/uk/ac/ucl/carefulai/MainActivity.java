package com.uk.ac.ucl.carefulai;

import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.uk.ac.ucl.carefulai.ui.AppActivity;
import com.uk.ac.ucl.carefulai.ui.start.StartActivity;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_CODE = 999;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //a check to see if the app has already been set up (i.e. if Usage Stats have been enabled) - if not, launch the welcome page
        final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar beginCal = Calendar.getInstance();
        beginCal.add((Calendar.MINUTE), -60 * 24 * 7);
        Calendar endCal = Calendar.getInstance();
        final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());

        int hasSMSPermission = checkSelfPermission(Manifest.permission.READ_SMS);
        int hasCallLogPermission = checkSelfPermission(Manifest.permission.READ_CALL_LOG);
        int hasCallLogPermission2 = checkSelfPermission(Manifest.permission.WRITE_CALL_LOG);
        int hasSMSReceivePermission = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
        int hasUsageStatsPermission = checkSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS);
        int hasFilesPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasInternetPermission = checkSelfPermission(Manifest.permission.INTERNET);
        int hasNetworkPermission = checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);


        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //requestPermissions(permissions, STORAGE_CODE);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, STORAGE_CODE);
        }


        final String[] NECESSARY_PERMISSIONS = new String[]{Manifest.permission.GET_ACCOUNTS};

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    NECESSARY_PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }

        if (hasSMSPermission != PackageManager.PERMISSION_GRANTED &&
                hasCallLogPermission2 != PackageManager.PERMISSION_GRANTED &&
                hasCallLogPermission != PackageManager.PERMISSION_GRANTED &&
                hasSMSReceivePermission != PackageManager.PERMISSION_GRANTED &&
                hasUsageStatsPermission != PackageManager.PERMISSION_GRANTED &&
                hasFilesPermission != PackageManager.PERMISSION_GRANTED &&
                hasInternetPermission != PackageManager.PERMISSION_GRANTED &&
                hasNetworkPermission != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }
        if (queryUsageStats.size() == 0) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(this, AppActivity.class));
        }
    }

}
