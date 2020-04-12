package com.uk.ac.ucl.carefulai.ui.report;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.uk.ac.ucl.carefulai.DatabaseHelper;
import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.nudge.PersonalNudgeActivity;

public class ReportFragment extends Fragment {

    //Line 47-49 Import instances of helper classes
    private DatabaseHelper myDb;
    private GraphHelper graphHelper = new GraphHelper();
    private GraphAttachments graphAttachments = new GraphAttachments();


    private ImageButton shareButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_report, container, false); //Load view


        myDb = new DatabaseHelper(root.getContext()); //Initialise Databasehelper


        //Line 65-68 Steps Graphs - 3 PointsGraphs for different colours and a barGraph
        PointsGraphSeries<DataPoint> stepsPointSeriesR = new PointsGraphSeries<>();
        PointsGraphSeries<DataPoint> stepsPointSeriesG = new PointsGraphSeries<>();
        LineGraphSeries<DataPoint> stepsLineSeries = new LineGraphSeries<>();
        BarGraphSeries<DataPoint> stepsBarSeries  =new BarGraphSeries<>();




        final GraphView graph3 = (GraphView) root.findViewById(R.id.graph3); //Locate graph in layout
        graph3.setClickable(true); //Allow functionality
        graph3.getGridLabelRenderer().setHighlightZeroLines(true); //Allow zero
        stepsLineSeries.setColor(Color.BLACK); //Line graph colour black

        //Line 79-95 append DataPoints to the Steps Graphs, by using the graphHelper method.
        for (Integer week: graphHelper.getWellBeingScore(myDb).keySet()){
            int y = graphHelper.getWellBeingScore(myDb).get(week); //y is the Wellbeing score

            // 83-88 If wellbeing score is <= 5, add the datapoint to the Red Steps Graph, else add to Green Steps Graph
            if (y <= 5) {
                stepsPointSeriesR.appendData(new DataPoint(week, y), true, 5000);
            }
            else{
                stepsPointSeriesG.appendData(new DataPoint(week, y), true, 5000);
            }

            //Append every value to the Line Series to make sure all points are connected
            stepsLineSeries.appendData(new DataPoint(week, y), true, 5000);
            //Bar chart y is now the number of steps
            y = graphHelper.getSteps(myDb).get(week);
            stepsBarSeries.appendData(new DataPoint(week, y), true, 5000);
        }


        //Line 99-103 Add all graphs to the main graph and set Title
        graph3.addSeries(stepsBarSeries);
        graph3.addSeries(stepsPointSeriesR);
        graph3.addSeries(stepsPointSeriesG);
        graph3.addSeries(stepsLineSeries);
        graph3.setTitle("Well-Being v steps");

        //Line 106-107 Adjust axis
        graph3.getViewport().setMaxY(graphHelper.maxSteps + 5);
        graph3.getViewport().setMinY(0);


        //Line 111-113 Customise Graphs to resemble Prototype
        graphHelper.wellbeingCustom(stepsPointSeriesG, stepsPointSeriesR);
        graphHelper.stepsCustom(stepsBarSeries);
        graphHelper.graphAxisCustom(graph3);


        //Line 117-120 Calls Graph - 3 PointsGraphs for different colours and a barGraph
        PointsGraphSeries<DataPoint> callsPointSeriesR = new PointsGraphSeries<>();
        PointsGraphSeries<DataPoint> callsPointSeriesG = new PointsGraphSeries<>();
        LineGraphSeries<DataPoint> callsLineSeries = new LineGraphSeries<>();
        BarGraphSeries<DataPoint> callsBarSeries =new BarGraphSeries<>();


        final GraphView graph4 = (GraphView) root.findViewById(R.id.graph4);//Locate graph in layout
        graph4.setClickable(true); // allow functionality
        graph4.getGridLabelRenderer().setHighlightZeroLines(true); //Allow zero

        callsLineSeries.setColor(Color.BLACK); //Line graph colour black

        //Line 130-144 append DataPoints to the Calls Graphs, by using the graphHelper method.
        for (Integer week: graphHelper.getWellBeingScore(myDb).keySet()){
            int y = graphHelper.getWellBeingScore(myDb).get(week);
            // 133-138 If wellbeing score is <= 5, add the datapoint to the Red Calls Graph, else add to Green Calls Graph
            if (y <= 5) {
                callsPointSeriesR.appendData(new DataPoint(week, y), true, 5000);
            }
            else{
                callsPointSeriesG.appendData(new DataPoint(week, y), true, 5000);
            }
            // Append every value to the Line Series to make sure all points are connected
            callsLineSeries.appendData(new DataPoint(week, y), true, 5000);
            //Bar chart y is now the number of steps
            y = graphHelper.getCalls(myDb).get(week);
            callsBarSeries.appendData(new DataPoint(week, y), true, 5000);
        }

        //Line 147-151 Add all graphs to the main graph and set Title
        graph4.addSeries(callsBarSeries);
        graph4.addSeries(callsPointSeriesR);
        graph4.addSeries(callsPointSeriesG);
        graph4.addSeries(callsLineSeries);
        graph4.setTitle("Well-Being v Calls" );

        //Line 154-155 Adjust axis
        graph4.getViewport().setMaxY(graphHelper.maxCalls + 5);
        graph4.getViewport().setMinY(0);

        //Line 158-160 Customise Graphs to resemble Prototype
        graphHelper.wellbeingCustom(callsPointSeriesG, callsPointSeriesR);
        graphHelper.callsCustom(callsBarSeries);
        graphHelper.graphAxisCustom(graph4);

        //Line 163-178 On Click method to switch Fragment to enlarged Steps Graph
        graph3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Disable activity on fragment
                graph3.setClickable(false);
                graph4.setClickable(false);


                //Line 173-176 switches fragments
                StepsFragment someFragment = new StepsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag, someFragment );
                transaction.commit();

            }
        });

        //Line 163-176 On Click method to switch Fragment to enlarged Calls Graph
        graph4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Disable activity on fragment
                graph3.setClickable(false);
                graph4.setClickable(false);

                //Line 191-194 switches fragments
                CallsFragment someFragment = new CallsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag, someFragment );
                transaction.commit();

            }
        });

        //Line 200-211 Share button saves as an image and Json to be included in wellbeing diary
        shareButton = (ImageButton) root.findViewById(R.id.imageButton2); //Initialise share button
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity(), "WellBeing Diary ready to share in message", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getActivity(), PersonalNudgeActivity.class));

            }
        });

        //Creates and stores Bitmaps of the Calls and Steps Graph
        graphAttachments.setCallsGraph(graph4);
        graphAttachments.setStepsGraph(graph3);

        return root; //Return view
    }


}
