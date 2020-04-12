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

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class StepsFragment extends Fragment {

    //Import instances of helper classes
    private GraphHelper graphHelper = new GraphHelper();
    private DatabaseHelper myDb;


    private Button button;
    private ImageButton shareButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_steps, container, false);

        myDb = new DatabaseHelper(view.getContext()); //Initialise databaseHelper

        //Line 54-66 Steps button resumes functionality and switches fragment to Calls Fragment
        button = (Button) view.findViewById(R.id.button);
        button.setClickable(true); //Allow button functionality

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CallsFragment someFragment = new CallsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag, someFragment );
                transaction.commit();

            }
        });



        GraphView graph = (GraphView) view.findViewById(R.id.graph);  //Locate graph in layout
        graph.getGridLabelRenderer().setHighlightZeroLines(true); //Allow zero

        //Line 75-78 Steps Graphs - 3 PointsGraphs for different colours and a barGraph
        PointsGraphSeries<DataPoint> stepsPointSeriesR = new PointsGraphSeries<>();
        PointsGraphSeries<DataPoint> stepsPointSeriesG = new PointsGraphSeries<>();
        LineGraphSeries<DataPoint> stepsLineSeries = new LineGraphSeries<>();
        BarGraphSeries<DataPoint> stepsBarSeries  = new BarGraphSeries<>();

        //Line 81-84 Customise Graphs to resemble Prototype
        graphHelper.graphAxisCustom(graph);
        graphHelper.wellbeingCustom(stepsPointSeriesG, stepsPointSeriesR);
        stepsLineSeries.setColor(Color.BLACK);
        graphHelper.stepsCustom(stepsBarSeries);


        //Line 79-89 append DataPoints to the Steps Graphs, by using the graphHelper method.
        for (Integer week: graphHelper.getWellBeingScore(myDb).keySet()){
            int y = graphHelper.getWellBeingScore(myDb).get(week);

            // 83-88 If wellbeing score is <= 5, add the datapoint to the Red Steps Graph, else add to Green Steps Graph
            if (y <= 5) {
                stepsPointSeriesR.appendData(new DataPoint(week, y), true, 5000);
            }
            else{
                stepsPointSeriesG.appendData(new DataPoint(week, y), true, 5000);
            }

            // Append every value to the Line Series to make sure all points are connected
            stepsLineSeries.appendData(new DataPoint(week, y), true, 5000);
            //Bar chart y is now the number of steps
            y = graphHelper.getSteps(myDb).get(week);
            stepsBarSeries.appendData(new DataPoint(week, y), true, 5000);
        }



        //Line 109-112 Add all graphs to the main graph
        graph.addSeries(stepsBarSeries);
        graph.addSeries(stepsPointSeriesR);
        graph.addSeries(stepsPointSeriesG);
        graph.addSeries(stepsLineSeries);

        //Line 115-116 Adjust axis
        graph.getViewport().setMaxY(graphHelper.maxSteps + 5);
        graph.getViewport().setMinY(0);


        //Line 120-138 Display data when clicked
        stepsPointSeriesR.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Well-Being Score: "+Math.round(dataPoint.getY()), Toast.LENGTH_SHORT).show();
            }
        });

        stepsPointSeriesG.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Well-Being Score: "+Math.round(dataPoint.getY()), Toast.LENGTH_SHORT).show();
            }
        });

        stepsBarSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Weekly Steps: "+Math.round(dataPoint.getY()), Toast.LENGTH_SHORT).show();
            }
        });


        //Line 143-152 Share button switches fragment to Report page
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
