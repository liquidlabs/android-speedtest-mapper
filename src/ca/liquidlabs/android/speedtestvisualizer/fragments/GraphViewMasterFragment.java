/*
 * Copyright 2013 Liquid Labs Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.liquidlabs.android.speedtestvisualizer.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ca.liquidlabs.android.speedtestvisualizer.R;
import ca.liquidlabs.android.speedtestvisualizer.model.GraphType;
import ca.liquidlabs.android.speedtestvisualizer.util.SpeedTestRecordProcessorTask;
import ca.liquidlabs.android.speedtestvisualizer.util.SpeedTestRecordProcessorTask.OnDataProcessorListener;
import ca.liquidlabs.android.speedtestvisualizer.util.Tracer;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

public class GraphViewMasterFragment extends BaseGraphFragment {
    /**
     * Log tag.
     */
    private static final String LOG_TAG = GraphViewMasterFragment.class.getSimpleName();

    /**
     * The fragment argument representing the section number for this fragment.
     */
    public static final String BUNDLE_ARG_HEADER = "csvHeader";
    public static final String BUNDLE_ARG_DATA = "csvData";
    public static final String BUNDLE_ARG_GRAPH_TYPE = "graphType";

    private FrameLayout mGraphViewContainer;

    //
    // Data from bundle
    //
    private String mCsvHeader;
    private String mCsvData;
    private GraphType mGraphType;

    /**
     * Creates a graph fragment to draw graphview based on data and graph type.
     * 
     * @param header CSV header for parsing.
     * @param csvData CSV Data for parsing.
     * @param graphType Type of graph to draw. See {@link GraphType}.
     * @return Fragment containing graph.
     */
    public static Fragment newInstance(final String header, final String csvData, GraphType graphType) {
        Tracer.debug(LOG_TAG, "newInstance()");
        GraphViewMasterFragment fragment = new GraphViewMasterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_ARG_HEADER, header);
        bundle.putString(BUNDLE_ARG_DATA, csvData);
        bundle.putSerializable(BUNDLE_ARG_GRAPH_TYPE, graphType);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Tracer.debug(LOG_TAG, "onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_graph_generic, container, false);
        mGraphViewContainer = (FrameLayout) rootView.findViewById(R.id.graph_view_container);

        return rootView;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the arguments and save them.
        Bundle bundleArgs = getArguments();
        mCsvHeader = bundleArgs.getString(BUNDLE_ARG_HEADER);
        mCsvData = bundleArgs.getString(BUNDLE_ARG_DATA);
        mGraphType = (GraphType) bundleArgs.getSerializable(BUNDLE_ARG_GRAPH_TYPE);

        new SpeedTestRecordProcessorTask(this).execute(mCsvHeader, mCsvData, mGraphType.name());

        Tracer.debug(LOG_TAG, "onActivityCreated()");

    }

    /**
     * {@inheritDoc} <br/>
     * 
     * @see OnDataProcessorListener
     */
    @Override
    public void onComplete(final GraphViewDataInterface[]... dataSets) {
        // Check after all processing done, if this fragment is still visible.
        if (this.isRemoving() || this.isDetached()) {
            Tracer.debug(LOG_TAG, "onComplete() >> Fragment is removing or already detached.");
            // do nothing, view is already gone.
            return;
        }

        // first - hide the progress indicator
        hideProgressIndicator();
        Tracer.debug(LOG_TAG, "onComplete (multi): " + dataSets);

        Tracer.debug(LOG_TAG, "onComplete (multi) > data available? - "
                + dataSets.length);

        GraphViewDataInterface[][] availableDataSets = dataSets;

        if (availableDataSets.length == 1) {
            addSingleSeriesGraph(availableDataSets[0]);
            return;
        }

        // graph with dynamically genereated horizontal and vertical labels
        LineGraphView graphView;
        graphView = new LineGraphView(getActivity().getApplicationContext(), "Multi");

        for (int index = 0; index < availableDataSets.length; index++) {
            // TODO Select different color
            GraphViewSeries seriesData = new GraphViewSeries("Data: " + index, new GraphViewSeriesStyle(Color.rgb(200,
                    50, 00),
                    5), availableDataSets[index]);
            // add data
            graphView.addSeries(seriesData);
        }
        // set legend
        graphView.setShowLegend(true);
        // set view port, start=2, size=40
        // graphView.setViewPort(2, 40);
        // graphView.setScrollable(true);

        // add graph to the view
        mGraphViewContainer.addView(graphView);
    }

    private void addSingleSeriesGraph(final GraphViewDataInterface[] data) {
        // init graph data
        GraphViewSeries downloadSeries = new GraphViewSeries(data);

        GraphView graphView;
        graphView = new BarGraphView(
                getActivity().getApplicationContext() // context
                , mGraphType.getGraphTitle() // heading
        );

        // override styles
        graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.GRAY);
        graphView.getGraphViewStyle().setVerticalLabelsColor(Color.GRAY);

        // add data
        graphView.addSeries(downloadSeries);

        // add graph to the view
        mGraphViewContainer.addView(graphView);
    }

}
