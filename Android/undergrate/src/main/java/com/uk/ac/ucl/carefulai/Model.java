package com.uk.ac.ucl.carefulai;

import android.content.Context;

import uclsse.comp0102.nhsxapp.api.NhsAPI;
import uclsse.comp0102.nhsxapp.api.NhsTrainingDataHolder;

public class Model {

    private NhsAPI nhsApi;

    public Model(Context context){
        nhsApi = NhsAPI.Companion.getInstance(context.getApplicationContext());
    }

    public int calculateScore(int stepCount, int callCount, int textCount) {
        NhsTrainingDataHolder data = new NhsTrainingDataHolder();
        data.setWeeklySteps(stepCount);
        data.setWeeklyCalls(callCount);
        data.setWeeklyMessages(textCount);
        int score = nhsApi.getTrainingScore(
                new Number[]{stepCount, callCount, textCount},
                NhsAPI.ModelType.Local
        );
        data.setRealWellBeingScore(score);
        nhsApi.record(data);
        return score;
    }

    public void storeBasicInformation(String supportCode, String postCode) {
        NhsTrainingDataHolder data = new NhsTrainingDataHolder();
        data.setSupportCode(supportCode);
        data.setPostCode(postCode);
        nhsApi.record(data);
    }
}