package com.uk.ac.ucl.carefulai.ui.report;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.uk.ac.ucl.carefulai.DatabaseHelper;
import com.uk.ac.ucl.carefulai.R;

import java.util.ArrayList;

public class CallsFragment extends Fragment {


    private GraphHelper graphHelper = new GraphHelper();

    private DatabaseHelper myDb;
    private Button button;
    private ImageButton shareButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_calls, container, false);
        myDb = new DatabaseHelper(view.getContext());

        button = (Button) view.findViewById(R.id.button3);
        button.setClickable(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setClickable(false);
                StepsFragment someFragment = new StepsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag, someFragment ); // give your fragment container id in first parameter
                //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });



        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        graph.getGridLabelRenderer().setHighlightZeroLines(true);


        PointsGraphSeries<DataPoint> callsPointSeriesR = new PointsGraphSeries<>();
        PointsGraphSeries<DataPoint> callsPointSeriesG = new PointsGraphSeries<>();
        LineGraphSeries<DataPoint> callsLineSeries = new LineGraphSeries<>();
        BarGraphSeries<DataPoint> callsBarSeries =new BarGraphSeries<>();

        callsLineSeries.setColor(Color.BLACK);

        for (Integer week: graphHelper.getWellBeingScore(myDb).keySet()){
            int y = graphHelper.getWellBeingScore(myDb).get(week);
            if (y <= 5) {
                callsPointSeriesR.appendData(new DataPoint(week, y), true, 5000);
            }
            else{
                callsPointSeriesG.appendData(new DataPoint(week, y), true, 5000);

            }
            callsLineSeries.appendData(new DataPoint(week, y), true, 5000);
            y = graphHelper.getCalls(myDb).get(week);
            callsBarSeries.appendData(new DataPoint(week, y), true, 5000);
        }

        graphHelper.wellbeingCustom(callsPointSeriesG, callsPointSeriesR);
        graphHelper.callsCustom(callsBarSeries);
        graphHelper.graphAxisCustom(graph);

        graph.addSeries(callsBarSeries);
        graph.addSeries(callsPointSeriesR);
        graph.addSeries(callsPointSeriesG);
        graph.addSeries(callsLineSeries);

        graph.getViewport().setMaxY(graphHelper.maxCalls + 5);
        graph.getViewport().setMinY(0);

        //Into method?

        callsPointSeriesR.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Well-Being Score: "+Math.round(dataPoint.getY()), Toast.LENGTH_SHORT).show();
            }
        });

        callsPointSeriesG.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Well-Being Score: "+Math.round(dataPoint.getY()), Toast.LENGTH_SHORT).show();
            }
        });

        callsBarSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Weekly Calls: "+Math.round(dataPoint.getY()), Toast.LENGTH_SHORT).show();
            }
        });



        shareButton = (ImageButton) view.findViewById(R.id.imageButton);

                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReportFragment someFragment = new ReportFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frag, someFragment ); // give your fragment container id in first parameter
                        //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();
                    }
                });


       return view;
    }
}
