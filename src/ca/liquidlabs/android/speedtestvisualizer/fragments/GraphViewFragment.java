package ca.liquidlabs.android.speedtestvisualizer.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ca.liquidlabs.android.speedtestvisualizer.R;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class GraphViewFragment  extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    private LinearLayout graph1;
    private LinearLayout graph2;
    
    
    public static Fragment newInstance(String content){
        GraphViewFragment fragment = new GraphViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SECTION_NUMBER, content);
        
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graphs, container, false);
        graph1 =(LinearLayout) rootView.findViewById(R.id.graph1);
        graph2 = (LinearLayout) rootView.findViewById(R.id.graph2);
 
        return rootView;
    }


    /* (non-Javadoc)
     * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        // init example series data
        GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
                new GraphViewData(1, 2.0d)
                , new GraphViewData(2, 1.5d)
                , new GraphViewData(2.5, 3.0d) // another frequency
                , new GraphViewData(3, 2.5d)
                , new GraphViewData(4, 1.0d)
                , new GraphViewData(5, 3.0d)
        });
        String graphType = "bar";
        // graph with dynamically genereated horizontal and vertical labels
        GraphView graphView;
        if (graphType.equals("bar")) {
            graphView = new BarGraphView(
                    getActivity().getApplicationContext() // context
                    , "GraphViewDemo" // heading
            );
        } else {
            graphView = new LineGraphView(
                    getActivity().getApplicationContext() // context
                    , "GraphViewDemo" // heading
            );
        }
        graphView.addSeries(exampleSeries); // data

        graph1.addView(graphView);

        // graph with custom labels and drawBackground
        if (graphType.equals("bar")) {
            graphView = new BarGraphView(
                    getActivity().getApplicationContext()
                    , "GraphViewDemo"
            );
        } else {
            graphView = new LineGraphView(
                    getActivity().getApplicationContext()
                    , "GraphViewDemo"
            );
            ((LineGraphView) graphView).setDrawBackground(true);
            ((LineGraphView) graphView).setBackgroundColor(Color.rgb(80, 30, 30));
        }
        // custom static labels
        graphView.setHorizontalLabels(new String[] {"2 days ago", "yesterday", "today", "tomorrow"});
        graphView.setVerticalLabels(new String[] {"high", "middle", "low"});
        graphView.addSeries(exampleSeries); // data

        graph2.addView(graphView);
    }
    
}
