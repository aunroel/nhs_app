package com.uk.ac.ucl.carefulai;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import uclsse.comp0102.nhsxapp.api.NhsAPI;
import uclsse.comp0102.nhsxapp.api.NhsTrainingDataHolder;

import static java.lang.StrictMath.abs;

//controlled by AI team, but used to generate wellbeing score in PedometerService
public class Model {

    private static final String UNKNOWN_STRING = "UNKNOWN";

    private static final int USER_SCORE_IN_DATABASE = 4;

    private static final String CARE_PREF = "careNetwork";
    private static final String SUPPORT_CODE_IN_PREF = "carer_support_ref";
    private static final String POSTCODE_IN_PREF = "postcode";

    private NhsAPI nhsAPI;
    private DatabaseHelper databaseHelper;
    private LifeDataUpdate lifeDataUpdate;
    private SharedPreferences sharedPreferences;

    private NhsTrainingDataHolder nhsDataCache;

    Model(Context context){
        Context appContext = context.getApplicationContext();
        nhsAPI = new NhsAPI((Application) appContext);
        databaseHelper = new DatabaseHelper(appContext);
        lifeDataUpdate = new LifeDataUpdate(appContext);
        sharedPreferences = appContext.getSharedPreferences(CARE_PREF, Context.MODE_PRIVATE);
    }

    LiveData<Boolean> recordThisWeekData(){
        return nhsAPI.record(getNhsDataCacheCloneThisWeek());
    }

    LiveData<Integer> calculateScore(int stepCount, int callCount, int msgsCount) {
        NhsTrainingDataHolder data = getNhsDataCacheCloneThisWeek();
        data.setWeeklySteps(stepCount);
        data.setWeeklyCalls(callCount);
        data.setWeeklyMessages(msgsCount);
        return nhsAPI.calculateTrainingDirectlyResultFrom(data);
    }

    private NhsTrainingDataHolder getNhsDataCacheCloneThisWeek(){
        if(nhsDataCache == null) {
            NhsTrainingDataHolder newData = new NhsTrainingDataHolder();
            String postCode = sharedPreferences.getString(POSTCODE_IN_PREF, UNKNOWN_STRING);
            newData.setPostCode(postCode);
            String supportCode = sharedPreferences.getString(SUPPORT_CODE_IN_PREF, UNKNOWN_STRING);
            newData.setSupportCode(supportCode);
            int realWellBeingScore = databaseHelper.getLastLine().getInt(USER_SCORE_IN_DATABASE);
            newData.setRealWellBeingScore(realWellBeingScore);
            int stepsCount = abs(lifeDataUpdate.getStepsCount());
            newData.setWeeklySteps(stepsCount);
            int callsCount = abs(lifeDataUpdate.getCurrentCallsCount());
            newData.setWeeklyCalls(callsCount);
            int msgsCount = abs(lifeDataUpdate.getMessageCount());
            newData.setWeeklyMessages(msgsCount);
            nhsDataCache = newData;
        }
        return nhsDataCache.deeplyClone();
    }

    public static abstract class OneTimeObserver<T> implements Observer<T>, LifecycleOwner {
        private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return lifecycleRegistry;
        }

        @Override
        public void onChanged(T t) {
            onChangedCore(t);
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        }

        abstract void onChangedCore(T t);
    }
}
