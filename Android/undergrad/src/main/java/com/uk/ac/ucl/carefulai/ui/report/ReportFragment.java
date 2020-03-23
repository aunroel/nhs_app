package com.uk.ac.ucl.carefulai.ui.report;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.uk.ac.ucl.carefulai.DatabaseHelper;
import com.uk.ac.ucl.carefulai.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    private DatabaseHelper myDb;
//    private List<String> scoreList = new ArrayList<>();
    private ImageButton shareButton;

    private GraphHelper graphHelper = new GraphHelper();
    private GraphAttachments graphAttachments = new GraphAttachments();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_report, container, false);
        myDb = new DatabaseHelper(root.getContext());


        //Steps Graph
        PointsGraphSeries<DataPoint> stepsPointSeriesR = new PointsGraphSeries<>();
        PointsGraphSeries<DataPoint> stepsPointSeriesG = new PointsGraphSeries<>();
        LineGraphSeries<DataPoint> stepsLineSeries = new LineGraphSeries<>();
        BarGraphSeries<DataPoint> stepsBarSeries  =new BarGraphSeries<>();



        final GraphView graph3 = (GraphView) root.findViewById(R.id.graph3);
        graph3.setClickable(true);
        graph3.getGridLabelRenderer().setHighlightZeroLines(true);
        stepsLineSeries.setColor(Color.BLACK);

        for (Integer week: graphHelper.getWellBeingScore(myDb).keySet()){
            int y = graphHelper.getWellBeingScore(myDb).get(week);
            if (y <= 5) {
                stepsPointSeriesR.appendData(new DataPoint(week, y), true, 5000);
            }
            else{
                stepsPointSeriesG.appendData(new DataPoint(week, y), true, 5000);
            }
            stepsLineSeries.appendData(new DataPoint(week, y), true, 5000);
            y = graphHelper.getSteps(myDb).get(week);
            stepsBarSeries.appendData(new DataPoint(week, y), true, 5000);
        }


        graph3.addSeries(stepsBarSeries);
        graph3.addSeries(stepsPointSeriesR);
        graph3.addSeries(stepsPointSeriesG);
        graph3.addSeries(stepsLineSeries);
        graph3.setTitle("Well-Being v steps");

        graph3.getViewport().setMaxY(graphHelper.maxSteps + 5);
        graph3.getViewport().setMinY(0);


        graphHelper.wellbeingCustom(stepsPointSeriesG, stepsPointSeriesR);
        graphHelper.stepsCustom(stepsBarSeries);
        graphHelper.graphAxisCustom(graph3);


        //Calls Graph
        PointsGraphSeries<DataPoint> callsPointSeriesR = new PointsGraphSeries<>();
        PointsGraphSeries<DataPoint> callsPointSeriesG = new PointsGraphSeries<>();
        LineGraphSeries<DataPoint> callsLineSeries = new LineGraphSeries<>();
        BarGraphSeries<DataPoint> callsBarSeries =new BarGraphSeries<>();


        final GraphView graph4 = (GraphView) root.findViewById(R.id.graph4);
        graph4.setClickable(true);
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

        graphHelper.graphAxisCustom(graph4);
        graph4.addSeries(callsBarSeries);
        graph4.addSeries(callsPointSeriesR);
        graph4.addSeries(callsPointSeriesG);
        graph4.addSeries(callsLineSeries);


        graph4.setTitle("Well-Being v Calls" );
        graph4.getViewport().setMaxY(graphHelper.maxCalls + 5);
        graph4.getViewport().setMinY(0);

        graphHelper.wellbeingCustom(callsPointSeriesG, callsPointSeriesR);
        graphHelper.callsCustom(callsBarSeries);

        graph3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph3.setClickable(false);
                graph4.setClickable(false);

                StepsFragment someFragment = new StepsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag, someFragment ); // give your fragment container id in first parameter
                //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
                //root.setEnabled(false);

            }
        });

        graph4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph3.setClickable(false);
                graph4.setClickable(false);

                CallsFragment someFragment = new CallsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag, someFragment ); // give your fragment container id in first parameter
                //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });

        shareButton = (ImageButton) root.findViewById(R.id.imageButton2);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Testing graphs work
//                try {
//                    insertIntoDb();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                //Creates and stores Bitmaps of the Calls and Steps Graph
               graphAttachments.setCallsGraph(graph4);
               graphAttachments.setStepsGraph(graph3);

            }
        });


        return root;
    }

    private void insertIntoDb( ) throws JSONException {
        myDb.insertData(70,25,2);
        myDb.insertScore(Long.toString(myDb.getThisWeekNumber()), 8);
        myDb.insertUserScore(Long.toString(myDb.getThisWeekNumber()),6);


    }


//    private void wellbeingVContact(DatabaseHelper myDb) {
//        Cursor res = myDb.getScore();
//        int total =res.getCount();
//        int count=0;
//        while (res.moveToNext()) {
//
//            int s = res.getInt(0);
//            if (String.valueOf(s) != null) {
//                scoreList.add(String.valueOf(s));
//            }
//            count +=1;
//
//        }
//
//    }

}
