package com.uk.ac.ucl.carefulai.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.TypedValue;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.uk.ac.ucl.carefulai.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private TextView stepsView;

    private TextView contactView;

    private SharedPreferences dataPreferences;

    private final String myPreferences = "dataPreference";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        dataPreferences = root.getContext().getSharedPreferences(myPreferences, 0);

        stepsView = root.findViewById(R.id.stepsView);

        contactView = root.findViewById(R.id.contactView);

        int stepCount = dataPreferences.getInt("stepcount", 0);

        if (stepCount == 0) {
            stepsView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            stepsView.setText("No step data collected yet");
        }

        else  {
            stepsView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
            stepsView.setText(String.valueOf(stepCount));
        }

        int contactCount = dataPreferences.getInt("callcount", 0) + dataPreferences.getInt("messagecount", 0);

        if (contactCount == 0) {
            contactView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            contactView.setText("No contact data collected yet");
        }

        else  {
            contactView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
            contactView.setText(String.valueOf(contactCount));
        }


        return root;
    }
}