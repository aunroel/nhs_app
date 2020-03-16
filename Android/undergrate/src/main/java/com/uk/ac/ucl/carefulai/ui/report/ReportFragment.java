package com.uk.ac.ucl.carefulai.ui.report;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uk.ac.ucl.carefulai.DatabaseHelper;
import com.uk.ac.ucl.carefulai.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    private DatabaseHelper myDb;

    private List<String> scoreList = new ArrayList<>();


    private ReportViewModel reportViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportViewModel =
                ViewModelProviders.of(this).get(ReportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_report, container, false);

        myDb = new DatabaseHelper(root.getContext());

        return root;
    }

    private void wellbeingVContact(DatabaseHelper myDb) {
        Cursor res = myDb.getScore();
        int total =res.getCount();
        int count=0;
        while (res.moveToNext()) {

            int s = res.getInt(0);
            if (String.valueOf(s) != null) {
                scoreList.add(String.valueOf(s));
            }
            count +=1;

        }

    }

    private void wellbeingVSteps() {

    }
}
