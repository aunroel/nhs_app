package com.uk.ac.ucl.carefulai.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.uk.ac.ucl.carefulai.LifeDataUpdate;
import com.uk.ac.ucl.carefulai.R;

//Home Fragment under the navigation, default when AppActivity is started
public class HomeFragment extends Fragment {

    private SharedPreferences dataPreferences; //used to get the recentScore

    private final String myPreferences = "dataPreference"; //used to get the right preferences list

    public static int homeSteps = 0; //updated in ThePedometerService as the sensor records more steps

    private static int previousCount = 0;

    private int newCount = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        dataPreferences = root.getContext().getSharedPreferences(myPreferences, 0);

        TextView stepsView = root.findViewById(R.id.stepsView); //TextView that displays the week's step count

        TextView callView = root.findViewById(R.id.callView); //TextView that displays the week's call count

        TextView textsView = root.findViewById(R.id.textsView); //TextView that displays the week's messages count

        TextView homeScore = root.findViewById(R.id.homeScore); //TextView that displays the most recent score (recentScore)

        LifeDataUpdate lifeDataUpdate = new LifeDataUpdate(root.getContext()); //has the latest data for calls and messages

        if(lifeDataUpdate.checkIfFirstLaunchApp()){ //if this is the first time after setup, set the call and messages data to their initial states
            lifeDataUpdate.saveDataToInitialState();
        }

        int callcount= lifeDataUpdate.getCurrentCallsCount();

        if (callcount<0){
            callcount=0;
        }

        int messagecount= lifeDataUpdate.getMessageCount(); //message count
        if (messagecount<0){
            messagecount=0;
        }

        int recentScore = dataPreferences.getInt("recentScore", 0); //recentScore (if first launch, would be the one set in the FirstEntryActivity)

        //set appropriate values to their TextViews

        stepsView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        stepsView.setText(String.valueOf(homeSteps));

        callView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        callView.setText(String.valueOf(callcount));

        textsView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        textsView.setText(String.valueOf(messagecount));

        homeScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        homeScore.setText(String.valueOf(recentScore));

        return root;
    }



}