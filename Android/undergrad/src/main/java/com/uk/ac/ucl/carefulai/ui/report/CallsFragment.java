package com.uk.ac.ucl.carefulai.ui.report;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class CallsFragment extends Fragment {

    //Line 32-33 Import instances of helper classes
    private GraphHelper graphHelper = new GraphHelper();
    private DatabaseHelper myDb;

    private Button button;
    private ImageButton shareButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_calls, container, false);

        myDb = new DatabaseHelper(view.getContext()); //Initialise database

        //Line 49-60 Steps button resumes functionality and switches fragment to Steps Fragment
        button = (Button) view.findViewById(R.id.button3);
        button.setClickable(true); //Allow button functionality

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setClickable(false); //Remove button functionality when changing fragment
                StepsFragment someFragment = new StepsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag, someFragment);
                transaction.commit();

            }
        });




        GraphView graph = (GraphView) view.findViewById(R.id.graph); //Locate graph in layout
        graph.getGridLabelRenderer().setHighlightZeroLines(true); //Allow zero

        //Line 72-75 Calls Graphs - 3 PointsGraphs for different colours and a barGraph
        PointsGraphSeries<DataPoint> callsPointSeriesR = new PointsGraphSeries<>();
        PointsGraphSeries<DataPoint> callsPointSeriesG = new PointsGraphSeries<>();
        LineGraphSeries<DataPoint> callsLineSeries = new LineGraphSeries<>();
        BarGraphSeries<DataPoint> callsBarSeries =new BarGraphSeries<>();

        callsLineSeries.setColor(Color.BLACK);

        //Line 80-92 append DataPoints to the Calls Graphs, by using the graphHelper method.
        for (Integer week: graphHelper.getWellBeingScore(myDb).keySet()){
            int y = graphHelper.getWellBeingScore(myDb).get(week); //y is the wellbeing score
            if (y <= 5) {
                callsPointSeriesR.appendData(new DataPoint(week, y), true, 5000);
            }
            else{
                callsPointSeriesG.appendData(new DataPoint(week, y), true, 5000);

            }
            callsLineSeries.appendData(new DataPoint(week, y), true, 5000); //Append every value to the Line Series to make sure all points are connected
            y = graphHelper.getCalls(myDb).get(week); //y is the number of calls
            callsBarSeries.appendData(new DataPoint(week, y), true, 5000);
        }

        //Line 95-97 Customise Graphs to resemble Prototype
        graphHelper.wellbeingCustom(callsPointSeriesG, callsPointSeriesR);
        graphHelper.callsCustom(callsBarSeries);
        graphHelper.graphAxisCustom(graph);

        //Line 100-103 Add all graphs to the main graph
        graph.addSeries(callsBarSeries);
        graph.addSeries(callsPointSeriesR);
        graph.addSeries(callsPointSeriesG);
        graph.addSeries(callsLineSeries);

        //Line 106-107 Adjust axis
        graph.getViewport().setMaxY(graphHelper.maxCalls + 5);
        graph.getViewport().setMinY(0);


        //Line 111-129 Display data when clicked
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


        //Line 134-144 Share button switches fragment to Report page
        shareButton = (ImageButton) view.findViewById(R.id.imageButton);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportFragment someFragment = new ReportFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag, someFragment );
                transaction.commit();
            }
        });


        return view;
    }
}
