package com.uk.ac.ucl.carefulai;

import android.content.Context;

import uclsse.comp0102.nhsxapp.api.NhsAPI;

public class Model {

    private NhsAPI nhsApi;

    public Model(Context context){
        nhsApi = NhsAPI.Companion.getInstance(context.getApplicationContext());
    }

    public int calculateScore(int stepCount, int callCount, int textCount) {
        return nhsApi.getTrainingScore(
                new Number[]{stepCount, callCount, textCount},
                NhsAPI.ModelType.Local
        );
    }
}