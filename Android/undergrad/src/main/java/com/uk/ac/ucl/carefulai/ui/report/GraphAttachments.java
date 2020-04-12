package com.uk.ac.ucl.carefulai.ui.report;

import android.graphics.Bitmap;

import com.jjoe64.graphview.GraphView;

public class GraphAttachments {
    public static Bitmap stepsGraph;
    public static Bitmap callsGraph;
    public static String graphJSON;


    public void setGraphJSON(String JSON) {
        graphJSON = JSON;
    }

    public String getGraphJSON(){
        return graphJSON;
    }

    //Method returns a bitmap of a graph
    public Bitmap takeImage(GraphView graph){
        Bitmap bitmap = graph.takeSnapshot();

        return bitmap;
    }


    public void setStepsGraph(GraphView graph){
        stepsGraph = takeImage(graph);

    }

    public void setCallsGraph(GraphView graph){
        callsGraph = takeImage(graph);
    }

    public Bitmap getStepsGraph() {

        return stepsGraph;
    }

    public Bitmap getCallsGraph(){
        return callsGraph;
    }



}
