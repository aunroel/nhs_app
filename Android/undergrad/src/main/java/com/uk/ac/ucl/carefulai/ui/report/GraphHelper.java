package com.uk.ac.ucl.carefulai.ui.report;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.uk.ac.ucl.carefulai.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphHelper {
    public final int Weeks = 9;
    public int maxSteps = 70;
    public int maxCalls = 15;


    GraphAttachments graphAttachments = new GraphAttachments();


    public HashMap<Integer, Integer> getWellBeingScore(DatabaseHelper myDb) {
        HashMap<Integer, Integer> wellBeingScoreList = new HashMap<>();


        Cursor c = myDb.getAllEntries();
        int totalWeeks=  c.getCount();
        if ((c != null) && (c.getCount() > 0)) {
            if  (c.moveToFirst()) {
                do {
                    int wellBeing = c.getInt(c.getColumnIndex("SCORE"));
                    int weekNum = c.getInt(c.getColumnIndex("WEEK_NUM"));
                    wellBeingScoreList.put(totalWeeks - weekNum + 1, (wellBeing));
                }while (c.moveToNext());
            }
        }
        return wellBeingScoreList;
    }


    public HashMap<Integer, Integer>  getSteps(DatabaseHelper myDb) {
        HashMap<Integer, Integer> stepsList = new HashMap<Integer, Integer> ();

        int maximumSteps = maxSteps;

        Cursor c = myDb.getAllEntries();
        int totalWeeks=  c.getCount();

        if ((c != null) && (c.getCount() > 0))  {
            if  (c.moveToFirst()) {
                do {
                    int steps = c.getInt(c.getColumnIndex("STEPS_COUNTED"));
                    if (steps > maximumSteps){
                        maximumSteps = steps;
                    }
                    int weekNum = c.getInt(c.getColumnIndex("WEEK_NUM"));
                    stepsList.put(totalWeeks-weekNum +1,steps);

                }while (c.moveToNext());
            }
        }

        maxSteps = maximumSteps;
        return stepsList;
    }


    public HashMap<Integer, Integer> getCalls(DatabaseHelper myDb) {
        HashMap<Integer, Integer>  callsList = new HashMap<Integer, Integer> ();

        int maximumCalls = maxCalls;
        Cursor c = myDb.getAllEntries();
        int totalWeeks=  c.getCount();
        if ((c != null) && (c.getCount() > 0)) {
            if  (c.moveToFirst()) {
                do {
                    int calls = c.getInt(c.getColumnIndex("CALLS_COUNT"));
                    if (calls > maximumCalls){
                        maximumCalls = calls;
                    }
                    int weekNum = c.getInt(c.getColumnIndex("WEEK_NUM"));
                    callsList.put(totalWeeks - weekNum + 1,(calls));

                }while (c.moveToNext());
            }
        }
        maxCalls = maximumCalls;
        return callsList;
    }


    public void graphAxisCustom(GraphView graph){

        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getViewport().setDrawBorder(true);


        graph.getGridLabelRenderer().setVerticalAxisTitle("Well-Being Score");
        graph.getGridLabelRenderer().setPadding(40);
        graph.getGridLabelRenderer().setNumHorizontalLabels(8);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Past Weeks");
        //graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
       // graph.getViewport().setScalableY(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);



        graph.getViewport().setMaxX(Weeks);
        graph.getViewport().setMinX(0);
    }

    public void stepsCustom(BarGraphSeries<DataPoint> stepsBarSeries){
        stepsBarSeries.setColor(Color.rgb(26, 154, 169));
        stepsBarSeries.setSpacing(30);

    }
    public void callsCustom(BarGraphSeries<DataPoint> callsBarSeries){
        callsBarSeries.setColor(Color.GRAY);
        callsBarSeries.setSpacing(30);

    }

    public void wellbeingCustom(PointsGraphSeries<DataPoint> stepsPointSeriesG, PointsGraphSeries<DataPoint> stepsPointSeriesR){
        stepsPointSeriesG.setColor(Color.rgb(114, 210, 39));
        stepsPointSeriesR.setColor(Color.rgb(235, 87, 87));
    }



    public void dataAsJSON(DatabaseHelper myDb) throws JSONException {

        JSONArray jsonListOfJson = new JSONArray();
        Cursor c = myDb.getAllEntries();
        if ((c != null) && (c.getCount() > 0)) {
            if  (c.moveToFirst()) {
                do {
                    JSONObject jsonString2 = new JSONObject() .put("week",c.getString(c.getColumnIndex("WEEK_NUM")))
                            .put("wellBeingScore", c.getString(c.getColumnIndex("SCORE")))
                            .put("weeklySteps", c.getString(c.getColumnIndex("STEPS_COUNTED")))
                            .put("weeklyCalls", c.getString(c.getColumnIndex("CALLS_COUNT")));


                    jsonListOfJson.put(jsonString2);

                }while (c.moveToNext());
            }
        }
        //Log.d("Logging",jsonListOfJson.toString());
        graphAttachments.setGraphJSON(jsonListOfJson.toString());


        //TODO
        // Add a date here somewhere?
        // Week 1 corresponds to week 1 in database but not week 1 in graph?
        // Add Reference number (Where is that)
        // Return as string for TXT sharing


    }

//    public  GraphView makeStepsGraph(GraphView graph, DatabaseHelper myDb){
//
//        PointsGraphSeries<DataPoint> stepsPointSeriesR =  new PointsGraphSeries<>();
//        PointsGraphSeries<DataPoint> stepsPointSeriesG = new PointsGraphSeries<>();
//        LineGraphSeries<DataPoint> stepsLineSeries = new LineGraphSeries<>();
//        BarGraphSeries<DataPoint> stepsBarSeries = new BarGraphSeries<>();
//
//        graph.setClickable(true);
//        graph.getGridLabelRenderer().setHighlightZeroLines(true);
//        stepsLineSeries.setColor(Color.BLACK);
//
//        for (Integer week: getWellBeingScore(myDb).keySet()){
//            int y = getWellBeingScore(myDb).get(week);
//            if (y <= 5) {
//                stepsPointSeriesR.appendData(new DataPoint(week, y), true, 5000);
//            }
//            else{
//                stepsPointSeriesG.appendData(new DataPoint(week, y), true, 5000);
//
//            }
//            stepsLineSeries.appendData(new DataPoint(week, y), true, 5000);
//            y = getSteps(myDb).get(week);
//            stepsBarSeries.appendData(new DataPoint(week, y), true, 5000);
//        }
//
//        graph.addSeries(stepsBarSeries);
//        graph.addSeries(stepsPointSeriesR);
//        graph.addSeries(stepsPointSeriesG);
//        graph.addSeries(stepsLineSeries);
//        graph.setTitle("Well-Being v steps");
//
//        graph.getViewport().setMaxX(Weeks);
//        graph.getViewport().setMinX(0);
//
//
//        graph.getViewport().setMaxY(120);
//        graph.getViewport().setMinY(0);
//
//        wellbeingCustom(stepsPointSeriesG, stepsPointSeriesR);
//        stepsCustom(stepsBarSeries);
//        graphAxisCustom(graph);
//
//        return graph;
//
//    }


}
