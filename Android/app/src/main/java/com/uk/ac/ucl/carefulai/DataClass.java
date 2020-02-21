package com.uk.ac.ucl.carefulai;

import uclsse.comp0102.nhsxapp.api.StoreData;

public class DataClass implements StoreData {
    private int stepsCount;
    private int callsCount;
    private int textCount;

    DataClass(int steps, int calls, int text) {
        stepsCount = steps;
        callsCount = calls;
        textCount = text;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public int getCallsCount() {
        return callsCount;
    }

    public int getTextCount() {
        return textCount;
    }
}
