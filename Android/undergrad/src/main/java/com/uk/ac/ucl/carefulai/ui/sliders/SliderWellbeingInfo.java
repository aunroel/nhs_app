package com.uk.ac.ucl.carefulai.ui.sliders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uk.ac.ucl.carefulai.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SliderWellbeingInfo extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.welcome_page_fragment_2,null);
        return v;
    }
}
