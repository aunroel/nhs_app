package com.uk.ac.ucl.carefulai;

public class Model {

    public int calculateScore(int stepCount, int callCount, int textCount) {

        int stepWeight = stepCount / 1000;

        int contactWeight = (callCount + textCount) / 5;

        return stepWeight + contactWeight;
    }
}
