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
import android.widget.LinearLayout;

import ca.liquidlabs.android.speedtestvisualizer.R;
import ca.liquidlabs.android.speedtestvisualizer.model.GraphType;
import ca.liquidlabs.android.speedtestvisualizer.util.SpeedTestRecordProcessorTask;
import ca.liquidlabs.android.speedtestvisualizer.util.SpeedTestRecordProcessorTask.OnDataProcessorListener;
import ca.liquidlabs.android.speedtestvisualizer.util.Tracer;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;

public class DownloadGraphFragment extends BaseGraphFragment {
    /**
     * Log tag.
     */
    private static final String LOG_TAG = DownloadGraphFragment.class.getSimpleName();

    /**
     * The fragment argument representing the section number for this fragment.
     */
    public static final String BUNDLE_ARG_HEADER = "csvHeader";
    public static final String BUNDLE_ARG_DATA = "csvData";
    public static final String BUNDLE_ARG_GRAPH_TYPE = "graphType";

    private final String TEST_DATA = "Date,ConnType,Lat,Lon,Download,Upload,Latency,ServerName,InternalIp,ExternalIp\n\"2013-04-27 10:47\",\"Wifi\",\"40.87763\",\"-73.88753\",11524,329,2248,\"Frederick, MD\",\"192.168.0.104\",\"24.246.58.77\"\n\"2013-04-27 10:46\",\"Cell\",\"39.27206\",\"-76.51323\",12848,369,39,\"Frederick, MD\",\"192.168.0.104\",\"24.246.58.77\"\n\"2013-04-27 10:21\",\"Edge\",\"42.42897\",\"-77.39760\",12924,368,18,\"Toronto, ON\",\"192.168.0.104\",\"24.246.58.77\"\n\"2013-04-27 10:18\",\"Wifi\",\"42.42897\",\"-77.39760\",13787,502,17,\"Toronto, ON\",\"192.168.0.104\",\"24.246.58.77\"\n\"2013-04-27 09:55\",\"Wifi\",\"43.74476\",\"-79.34768\",14203,460,44,\"Buffalo, NY\",\"192.168.0.104\",\"24.246.58.77\"\n\"2013-04-27 09:54\",\"Wifi\",\"43.13451\",\"-77.62008\",12407,462,45,\"Buffalo, NY\",\"192.168.0.104\",\"24.246.58.77\"\n\"2013-04-27 09:21\",\"Umts\",\"43.63553\",\"-79.80909\",14537,373,19,\"Toronto, ON\",\"192.168.0.104\",\"24.246.58.77\"\n\"2013-04-27 09:20\",\"Wifi\",\"42.93778\",\"-78.87253\",15145,383,20,\"Toronto, ON\",\"192.168.0.104\",\"24.246.58.77\"";
    private final String TEST_HEADER = "ServerName,InternalIp,ExternalIp";

    private FrameLayout mGraphViewContainer;

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
        DownloadGraphFragment fragment = new DownloadGraphFragment();
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

        new SpeedTestRecordProcessorTask(this).execute(TEST_HEADER, TEST_DATA);

        Tracer.debug(LOG_TAG, "onActivityCreated()");

    }

    /**
     * {@inheritDoc} <br/>
     * 
     * @see OnDataProcessorListener
     */
    @Override
    public void onComplete(GraphViewDataInterface[] data) {

        // Check after all processing done, if this fragment is still visible.
        if (this.isRemoving() || this.isDetached()) {
            Tracer.debug(LOG_TAG, "onComplete() >> Fragment is removing or already detached.");
            // do nothing, view is already gone.
            return;
        }

        // first - hide the progress indicator
        hideProgressIndicator();
        Tracer.debug(LOG_TAG, "onComplete: " + data);

        Tracer.debug(LOG_TAG, "onComplete() > data available? - "
                + data.length);

        // init graph data
        GraphViewSeries downloadSeries = new GraphViewSeries(data);

        GraphView graphView;
        graphView = new BarGraphView(
                getActivity().getApplicationContext() // context
                , "GraphViewDemo" // heading
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
