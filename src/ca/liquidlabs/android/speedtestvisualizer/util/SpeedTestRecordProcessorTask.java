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

package ca.liquidlabs.android.speedtestvisualizer.util;

import android.os.AsyncTask;

import ca.liquidlabs.android.speedtestvisualizer.model.SpeedTestRecord;
import ca.liquidlabs.android.speedtestvisualizer.model.StDataDownloadDate;

import com.jjoe64.graphview.GraphViewDataInterface;

import java.util.List;

/**
 * Task to calculate additional data required for mapping markers.
 * 
 * @author Hossain Khan
 */
public class SpeedTestRecordProcessorTask extends AsyncTask<String, Void, GraphViewDataInterface[]> {

    /**
     * A listener interface to notify caller when the data is processed.
     */
    public interface OnDataProcessorListener {
        /**
         * Called when the processor is about to begin long-running data
         * processing.<br/>
         * Client should give feedback to user by showing progress or similar.
         */
        void onPreProcess();

        /**
         * Called when the data is processed and returns the data.
         * 
         * @param data Processed data.
         */
        void onComplete(GraphViewDataInterface[] data);
    }

    /**
     * Instance of listener for callback.
     */
    private OnDataProcessorListener mListener;

    /**
     * Constructor for the data processor async-task
     * 
     * @param listener {@link OnDataProcessorListener} for callback
     */
    public SpeedTestRecordProcessorTask(final OnDataProcessorListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.onPreProcess();
    }

    /**
     * Parses the speedtest data, and prepares a list which can be used for
     * graphing.
     * 
     * @param params Parameters are: [0]=> CSV Header, [1]=> CSV data
     */
    @Override
    protected GraphViewDataInterface[] doInBackground(final String... params) {
        List<SpeedTestRecord> csvListData = CsvDataParser.parseCsvData(params[0], params[1]);

        final int totalCsvRecords = csvListData.size();
        GraphViewDataInterface[] graphData = new GraphViewDataInterface[totalCsvRecords];

        // convert the csv data to graph data based on type of graph.
        for (int i = 0; i < totalCsvRecords; i++) {
            graphData[i] = new StDataDownloadDate(csvListData.get(i));
        }

        try {
            // manual delay for testing
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        // return the processed data
        return graphData;
    }

    @Override
    protected void onPostExecute(final GraphViewDataInterface[] graphData) {
        mListener.onComplete(graphData);
    }

}
