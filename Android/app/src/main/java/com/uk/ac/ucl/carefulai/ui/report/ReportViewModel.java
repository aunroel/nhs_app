package com.uk.ac.ucl.carefulai.ui.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("VISUALISATIONS GO HERE");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
