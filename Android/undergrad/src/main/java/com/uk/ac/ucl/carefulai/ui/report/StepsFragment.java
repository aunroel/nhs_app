package com.uk.ac.ucl.carefulai.ui.report;

import android.content.Intent;
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

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class StepsFragment extends Fragment {

    private GraphHelper graphHelper = new GraphHelper();
    private DatabaseHelper myDb;

    private Button button;
    private ImageButton shareButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_steps, container, false);

        myDb = new DatabaseHelper(view.getContext());

            button = (Button) view.findViewById(R.id.button);
            button.setClickable(true);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CallsFragment someFragment = new CallsFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frag, someFragment ); // give your fragment container id in first parameter
                    //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();

                }
            });



            GraphView graph = (GraphView) view.findViewById(R.id.graph);


            graph.getGridLabelRenderer().setHighlightZeroLines(true);

          PointsGraphSeries<DataPoint> stepsPointSeriesR = new PointsGraphSeries<>();
        PointsGraphSeries<DataPoint> stepsPointSeriesG = new PointsGraphSeries<>();
        LineGraphSeries<DataPoint> stepsLineSeries = new LineGraphSeries<>();
        BarGraphSeries<DataPoint> stepsBarSeries  =new BarGraphSeries<>();

            graphHelper.graphAxisCustom(graph);

            graphHelper.wellbeingCustom(stepsPointSeriesG, stepsPointSeriesR);
            stepsLineSeries.setColor(Color.BLACK);

        graphHelper.stepsCustom(stepsBarSeries);


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

//Random data
//        for ( int i =1; i < graphHelper.getWellbeingScore().length;i++){
//            int y = graphHelper.getWellbeingScore()[i];
//            if (y <= 5) {
//                stepsPointSeriesR.appendData(new DataPoint(i, y), true, 5000);
//            }
//            else{
//                stepsPointSeriesG.appendData(new DataPoint(i, y), true, 5000);
//
//            }
//            stepsLineSeries.appendData(new DataPoint(i, y), true, 5000);
//            y = graphHelper.getSteps()[i];
//            stepsBarSeries.appendData(new DataPoint(i, y), true, 5000);
//        }

//


            graph.addSeries(stepsBarSeries);
            graph.addSeries(stepsPointSeriesR);
            graph.addSeries(stepsPointSeriesG);
            graph.addSeries(stepsLineSeries);



            graph.getViewport().setMaxY(graphHelper.maxSteps + 5);
            graph.getViewport().setMinY(0);


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
