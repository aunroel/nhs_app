package com.uk.ac.ucl.carefulai.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.start.InitialInfoActivity;

//starts the setup flow again
public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        //go to the InitialInfoActivity to start the setup flow again
        startActivity(new Intent(root.getContext(), InitialInfoActivity.class));

        return root;
    }
}