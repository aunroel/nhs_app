package com.uk.ac.ucl.carefulai.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.TypedValue;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.uk.ac.ucl.carefulai.LifeDataUpdate;
import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.AppActivity;

public class HomeFragment extends Fragment {

    private SharedPreferences dataPreferences;

    private final String myPreferences = "dataPreference";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        dataPreferences = root.getContext().getSharedPreferences(myPreferences, 0);

        TextView stepsView = root.findViewById(R.id.stepsView);

        TextView callView = root.findViewById(R.id.callView);

        TextView textsView = root.findViewById(R.id.textsView);

        TextView homeScore = root.findViewById(R.id.homeScore);

        LifeDataUpdate lifeDataUpdate = new LifeDataUpdate(root.getContext());

        if(lifeDataUpdate.checkIfFirstLaunchApp()){
            lifeDataUpdate.saveDataToInitialState();
        }

        int callcount= lifeDataUpdate.getCurrentCallsCount();
        if (callcount<0){
            callcount=0;
        }

        int messagecount= lifeDataUpdate.getMessageCount();
        if (messagecount<0){
            messagecount=0;
        }

        int recentScore = dataPreferences.getInt("recentScore", 0);

        stepsView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        stepsView.setText(AppActivity.countedSteps);

        callView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        callView.setText(String.valueOf(callcount));

        textsView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        textsView.setText(String.valueOf(messagecount));

        homeScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        homeScore.setText(String.valueOf(recentScore));

        return root;
    }



}