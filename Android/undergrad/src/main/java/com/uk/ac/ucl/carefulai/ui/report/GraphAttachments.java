package com.uk.ac.ucl.carefulai.ui.report;

import android.graphics.Bitmap;
import android.util.Log;

import com.jjoe64.graphview.GraphView;

public class GraphAttachments {
    private Bitmap stepsGraph;
    private Bitmap callsGraph;
    private String graphJSON;


    public void setGraphJSON(String JSON) {
        graphJSON = JSON;
    }

    public String getGraphJSON(){
        return graphJSON;
    }

    public Bitmap takeImage(GraphView graph){
            Bitmap bitmap = graph.takeSnapshot();
            return bitmap;
        }

        public void setStepsGraph(GraphView graph){
            stepsGraph = takeImage(graph);
            Log.d("Works", "working");
        }

        public void setCallsGraph(GraphView graph){
            callsGraph = takeImage(graph);
            Log.d("Works", "working2");

        }

        public Bitmap getStepsGraph(){
            return stepsGraph;
        }

        public Bitmap getCallsGraph(){
            return callsGraph;
        }


//    package com.uk.ac.ucl.carefulai.ui.report;
//
//import android.graphics.Bitmap;
//
//import com.jjoe64.graphview.GraphView;
//
//    public class GraphImages {
//
//        private Bitmap stepsGraph;
//        private Bitmap callsGraph;
//        private String graphJSON;
//
//        public Bitmap takeImage(GraphView graph){
//            Bitmap bitmap = graph.takeSnapshot();
//            return bitmap;
//        }
//
//        public void setStepsGraph(GraphView graph){
//            Bitmap bitmap = takeImage(graph);
//            stepsGraph = bitmap;
//        }
//
//        public void setCallsGraph(GraphView graph){
//            callsGraph = takeImage(graph);
//        }
//
//        public Bitmap getStepsGraph(){
//            return stepsGraph;
//        }
//
//        public Bitmap getCallsGraph(){
//            return callsGraph;
//        }
//
//        public void setGraphJSON(String JSON) {
//            graphJSON = JSON;
//        }
//
//        public String getGraphJSON(){
//            return graphJSON;
//        }
//    }

}
