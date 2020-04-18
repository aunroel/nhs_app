package com.uk.ac.ucl.carefulai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.uk.ac.ucl.carefulai.ui.start.InitialInfoActivity;
import com.uk.ac.ucl.carefulai.ui.start.PermissionsActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertTrue;
@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestSuite {
    @Test
    public void usageStatsClickTester(){ //checking an intent is launched from the 'View Score History' button

        try(ActivityScenario<InitialInfoActivity> scenario = ActivityScenario.launch(InitialInfoActivity.class)) {
            scenario.onActivity(new ActivityScenario.ActivityAction<InitialInfoActivity>() {
                @Override
                public void perform(InitialInfoActivity activity) {
                    Button launcher = (Button) activity.findViewById(R.id.button4);
                    launcher.performClick();
                    Intent expectedIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    ShadowActivity shadowActivity = Shadows.shadowOf(activity);
                    Intent actualIntent = shadowActivity.getNextStartedActivity();
                    assertTrue(actualIntent.filterEquals(expectedIntent));
                }
            });
        }
    }

    @Test
    public void trackingTester() {
        try(ActivityScenario<PermissionsActivity> scenario = ActivityScenario.launch(PermissionsActivity.class)) {
            scenario.onActivity(new ActivityScenario.ActivityAction<PermissionsActivity>() {
                @Override
                public void perform(PermissionsActivity activity) {
                    SharedPreferences dataPreferences = activity.getSharedPreferences("dataPreference", 0);
                    View statsbutton = (View) activity.findViewById(R.id.view2);
                    statsbutton.performClick();
                    Button launcher = (Button) activity.findViewById(R.id.mainstarter);
                    launcher.performClick();
                    assertTrue(dataPreferences.getBoolean("isTracking", false));
                }
            });
        }
    }

    @Test
    public void dataSharingTester() {
        try(ActivityScenario<PermissionsActivity> scenario = ActivityScenario.launch(PermissionsActivity.class)) {
            scenario.onActivity(new ActivityScenario.ActivityAction<PermissionsActivity>() {
                @Override
                public void perform(PermissionsActivity activity) {
                    SharedPreferences dataPreferences = activity.getSharedPreferences("dataPreference", 0);
                    Switch switch1 = activity.findViewById(R.id.switch1);
                    switch1.setChecked(true);
                    View statsbutton = (View) activity.findViewById(R.id.view2);
                    statsbutton.performClick();
                    Button launcher = (Button) activity.findViewById(R.id.mainstarter);
                    launcher.performClick();
                    assertTrue(dataPreferences.getBoolean("remoteSharing", false));
                }
            });
        }
    }
}