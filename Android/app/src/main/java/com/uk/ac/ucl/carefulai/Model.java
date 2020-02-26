package com.uk.ac.ucl.carefulai;

public class Model {

    public int calculateScore(int stepCount, int callCount, int textCount) {

        return stepCount / 1000 + (callCount + textCount) / 5;
    }
}
