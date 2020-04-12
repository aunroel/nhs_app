package com.uk.ac.ucl.carefulai.ui.report;


import android.database.Cursor;
import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.uk.ac.ucl.carefulai.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GraphHelper {

    public final int Weeks = 9; //Number of weeks to display on graph
    public int maxSteps = 70; //default number of steps on y axis
    public int maxCalls = 15; //default number of calls on y axis

    //Import instances of helper classes
    GraphAttachments graphAttachments = new GraphAttachments();


    //Method returns a HashMap of week number and their corresponding wellbeing score
    public HashMap<Integer, Integer> getWellBeingScore(DatabaseHelper myDb) {
        HashMap<Integer, Integer> wellBeingScoreList = new HashMap<>(); //Create Hashmap

        Cursor c = myDb.getAllEntries(); //c checks database
        int totalWeeks =  c.getCount();


        if ((c != null) && (c.getCount() > 0)) { //Checks if database is empty
            if  (c.moveToFirst()) {
                do {
                    int wellBeing = c.getInt(c.getColumnIndex("SCORE"));
                    int weekNum = c.getInt(c.getColumnIndex("WEEK_NUM"));
                    wellBeingScoreList.put(totalWeeks - weekNum + 1, (wellBeing)); //Change week order so week 1 is the most recent.
                }while (c.moveToNext());
            }
        }
        return wellBeingScoreList;
    }


    //Method returns a HashMap of week number and their corresponding Steps
    public HashMap<Integer, Integer> getSteps(DatabaseHelper myDb) {
        HashMap<Integer, Integer> stepsList = new HashMap<Integer, Integer> ();//Create Hashmap

        int maximumSteps = maxSteps;

        Cursor c = myDb.getAllEntries(); //c checks database
        int totalWeeks=  c.getCount();


        if ((c != null) && (c.getCount() > 0))  { //Checks if database is empty
            if  (c.moveToFirst()) {
                do {
                    int steps = c.getInt(c.getColumnIndex("STEPS_COUNTED"));

                    //Change max steps which are displayed if no of steps increases.
                    if (steps > maximumSteps){
                        maximumSteps = steps;
                    }

                    int weekNum = c.getInt(c.getColumnIndex("WEEK_NUM"));
                    stepsList.put(totalWeeks-weekNum +1,steps); //Change week order so week 1 is the most recent.

                }while (c.moveToNext());
            }
        }
        maxSteps = maximumSteps; //Return maxSteps to be used
        return stepsList;
    }


    //Method returns a HashMap of week number and their corresponding number of calls
    public HashMap<Integer, Integer> getCalls(DatabaseHelper myDb) {
        HashMap<Integer, Integer>  callsList = new HashMap<Integer, Integer> (); //Create Hashmap

        int maximumCalls = maxCalls;

        Cursor c = myDb.getAllEntries(); //c checks database
        int totalWeeks= c.getCount();


        if ((c != null) && (c.getCount() > 0)) { //Checks if database is empty
            if  (c.moveToFirst()) {
                do {
                    int calls = c.getInt(c.getColumnIndex("CALLS_COUNT"));

                    //Change max calls which are displayed if no of steps increases.
                    if (calls > maximumCalls){
                        maximumCalls = calls;
                    }

                    int weekNum = c.getInt(c.getColumnIndex("WEEK_NUM"));
                    callsList.put(totalWeeks - weekNum + 1,(calls)); //Change week order so week 1 is the most recent.

                }while (c.moveToNext());
            }
        }
        maxCalls = maximumCalls; //Return maxCalls to be used
        return callsList;
    }


    public void graphAxisCustom(GraphView graph){
        graph.getGridLabelRenderer().setHighlightZeroLines(true); //Allow zero line
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE); // Remove grid
        graph.getViewport().setDrawBorder(true); //Draw Borders


        graph.getGridLabelRenderer().setVerticalAxisTitle("Well-Being Score");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Past Weeks");

        graph.getGridLabelRenderer().setPadding(40); //Set padding
        graph.getGridLabelRenderer().setNumHorizontalLabels(8); //Number of x axis labels
        graph.getGridLabelRenderer().setNumVerticalLabels(6); //Number of y axis labels

        //Line 132-138 Allow scrolling and custom axis
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        //Set X axis bounds
        graph.getViewport().setMaxX(Weeks);
        graph.getViewport().setMinX(0);

    }

    //Method customises the steps graphs
    public void stepsCustom(BarGraphSeries<DataPoint> stepsBarSeries){
        stepsBarSeries.setColor(Color.rgb(26, 154, 169)); //Blue
        stepsBarSeries.setSpacing(30);

    }

    //Method customises the calls graphs
    public void callsCustom(BarGraphSeries<DataPoint> callsBarSeries){
        callsBarSeries.setColor(Color.GRAY);
        callsBarSeries.setSpacing(30);

    }

    //Method customises the wellbeing graph points
    public void wellbeingCustom(PointsGraphSeries<DataPoint> stepsPointSeriesG, PointsGraphSeries<DataPoint> stepsPointSeriesR){
        stepsPointSeriesG.setColor(Color.rgb(114, 210, 39)); //Green
        stepsPointSeriesR.setColor(Color.rgb(235, 87, 87)); //Red
    }


    public void dataAsJSON(DatabaseHelper myDb) throws JSONException {

        JSONArray jsonListOfJson = new JSONArray(); //Create JSON Array
        Cursor c = myDb.getAllEntries(); //c checks database

        if ((c != null) && (c.getCount() > 0)) { //Checks if database is empty
            if  (c.moveToFirst()) {
                do {
                    JSONObject jsonString2 = new JSONObject()  //Create a Json Object for each week
                            .put("week",c.getString(c.getColumnIndex("WEEK_NUM"))) //add week number
                            .put("wellBeingScore", c.getString(c.getColumnIndex("SCORE"))) //add score
                            .put("weeklySteps", c.getString(c.getColumnIndex("STEPS_COUNTED"))) //add steps
                            .put("weeklyCalls", c.getString(c.getColumnIndex("CALLS_COUNT"))); //add counts
                    jsonListOfJson.put(jsonString2); //add to array

                }while (c.moveToNext());
            }
        }
        graphAttachments.setGraphJSON(jsonListOfJson.toString()); //Set JSON value to be used.

    }




}
