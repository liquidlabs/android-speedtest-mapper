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

package ca.liquidlabs.android.speedtestvisualizer.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import ca.liquidlabs.android.speedtestvisualizer.R;
import ca.liquidlabs.android.speedtestvisualizer.fragments.SpeedTestInfoWindowAdapter;
import ca.liquidlabs.android.speedtestvisualizer.model.ComparableDownloadSpeed;
import ca.liquidlabs.android.speedtestvisualizer.model.SpeedTestRecord;
import ca.liquidlabs.android.speedtestvisualizer.util.AppConstants;
import ca.liquidlabs.android.speedtestvisualizer.util.CsvDataParser;
import ca.liquidlabs.android.speedtestvisualizer.util.Tracer;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

/**
 * Mapping of exported data.
 * 
 * @author Hossain Khan
 * @see https://developers.google.com/maps/documentation/android/
 * @see Maps V2 example project. Most codes are taken from sample project.
 */
public class MapperActivity extends Activity {
    private static final String LOG_TAG = MapperActivity.class.getSimpleName();

    private GoogleMap mMap;
    private static List<SpeedTestRecord> mCsvListData;
    private static int mMaxNetworkSpeed;
    private static int mMinNetworkSpeed;

    /**
     * Available filter type for connections
     */
    private static final int FILTER_TYPE_ALL = 0;
    private static final int FILTER_TYPE_WIFI = 1;
    private static final int FILTER_TYPE_CELL = 2;
    private static int FILTER_SELECTED = FILTER_TYPE_ALL;

    /**
     * The default unit used by speedtest to export the data.
     */
    private static final String SPEED_UNIT = "bps";

    /**
     * {@link AsyncTask} to process all the marker data
     */
    private MarkerDataProcessorTask mDataProcessorTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get feature to show progress in actionbar when processing data
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_mapper);
        // It seems like 4.0.x enables progress by default - STOP it!
        hideProgressIndicator();

        Spinner spinner = (Spinner) findViewById(R.id.spinner_conntype_filter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.filters_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new ConnectionTypeFilterHandler());

        // Show the Up button in the action bar.
        setupActionBar();

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        // Override the activity transition animation
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Tracks activity view using analytics.
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Tracks activity view using analytics.
        EasyTracker.getInstance().activityStop(this);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // Hide the zoom controls as the button panel will cover it.
                mMap.getUiSettings().setZoomControlsEnabled(false);

                // Get the csv data from intent and then proceed
                Bundle bundle = getIntent().getExtras();
                final String csvHeader = bundle.getString(AppConstants.KEY_SPEEDTEST_CSV_HEADER);
                final String csvData = bundle.getString(AppConstants.KEY_SPEEDTEST_CSV_DATA);

                // create task if not created, otherwise re-use same task
                if (mDataProcessorTask == null) {
                    mDataProcessorTask = new MarkerDataProcessorTask();
                }
                // Dispatch an asynctask to calculate required data for markers
                mDataProcessorTask.execute(csvHeader, csvData);
            }
        }
    }

    private void setUpMap() {
        if (mCsvListData == null || mCsvListData.size() == 0) {
            // nothing to show on map - return with user msg
            Toast.makeText(this, R.string.msg_no_records_found, Toast.LENGTH_LONG).show();
            return;
        }

        // Setting an info window adapter allows us to change the both the
        // contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new SpeedTestInfoWindowAdapter(getLayoutInflater()));

        // Add lots of markers to the map.
        addMarkersToMap();

    }

    /**
     * Adds all the parsed speedtest markers to the map. This task is processor
     * intensive, stalls the main thread if there is lots of data. But
     * unfortunately, this has to be done in UI thread.
     */
    private void addMarkersToMap() {

        Builder mapBoundsBuilder = new LatLngBounds.Builder();
        int currentTotalRecordCount = 0;
        // Use parsed data to create map markers
        for (SpeedTestRecord speedTestRecord : mCsvListData) {

            if (FILTER_SELECTED == FILTER_TYPE_CELL
                    && !speedTestRecord.getConnectionType().isCell()) {
                continue; // do not add non-cell items
            } else if (FILTER_SELECTED == FILTER_TYPE_WIFI
                    && !speedTestRecord.getConnectionType().isWifi()) {
                continue; // do not add non-wifi items
            }

            /*
             * Build string array to concatenate and send info (doing it in dumb
             * old way, rather than passing serialized data). NOTE: Must be
             * retrieved in same order
             */
            String snippetMultiInfo[] = {
                    speedTestRecord.getConnectionType().toString(),
                    NumberFormat.getInstance().format(speedTestRecord.getDownload()) + " "
                            + SPEED_UNIT,
                    NumberFormat.getInstance().format(speedTestRecord.getUpload()) + " "
                            + SPEED_UNIT
            };

            mMap.addMarker(new MarkerOptions()
                    .position(speedTestRecord.getLatLng())
                    .title(speedTestRecord.getDate())
                    .snippet(StringUtils.join(snippetMultiInfo, AppConstants.TEXT_SEPARATOR))
                    .icon(BitmapDescriptorFactory.defaultMarker(speedTestRecord.getMarkerColorHue())));

            // also build the maps bounds area
            mapBoundsBuilder.include(speedTestRecord.getLatLng());

            // Update the count for selected filter
            currentTotalRecordCount++;
        }

        if (currentTotalRecordCount > 0) {
            // apply bounds if anything was added
            this.applyMapCameraBounds(mapBoundsBuilder.build());
        }
    }

    private void applyMapCameraBounds(final LatLngBounds bounds) {
        // Pan to see all markers in view.
        // Cannot zoom to bounds until the map has a size.
        final View mapView = getFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                // We use the new method when supported
                @SuppressLint("NewApi")
                // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            });
        }
    }

    private boolean checkReady() {
        if (mMap == null || mCsvListData == null) {
            Toast.makeText(this, R.string.msg_map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Clears maps
     */
    public void clearMap() {
        if (!checkReady()) {
            return;
        }
        mMap.clear();
    }

    /**
     * Resets the maps
     */
    public void updateMapMarkers() {
        if (!checkReady()) {
            return;
        }
        // Clear the map because we don't want duplicates of the markers.
        mMap.clear();
        addMarkersToMap();
    }

    /**
     * Calculates marker color warmness based on download speed. <br/>
     * Highest speed -> RED, Lowest speed -> GREEN.
     * 
     * @param speedValue Single record's speed value
     * @return hue value based on speed
     */
    private static float getWeightedMarkerValue(int speedValue) {
        int speedDifference = MapperActivity.mMaxNetworkSpeed - MapperActivity.mMinNetworkSpeed;
        if (speedDifference <= 0) {
            // this might be the case, when there is only one record and
            // MaxSpeed = MinSpeed, So, return warmest hue value
            return BitmapDescriptorFactory.HUE_RED;
        }

        // calculate hue value based on speed
        float hueVal = ((BitmapDescriptorFactory.HUE_GREEN * MapperActivity.mMaxNetworkSpeed) - (BitmapDescriptorFactory.HUE_GREEN * speedValue))
                / speedDifference;
        if (hueVal < BitmapDescriptorFactory.HUE_RED || hueVal > BitmapDescriptorFactory.HUE_ROSE) {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
        return hueVal;
    }

    /**
     * Shows progress animation in ActioBar
     */
    private void showProgressIndicator() {
        setProgressBarIndeterminateVisibility(true);
        setProgressBarIndeterminate(true);
    }

    /**
     * Hides progress animation in ActionBar
     */
    private void hideProgressIndicator() {
        setProgressBarIndeterminateVisibility(false);
        setProgressBarIndeterminate(false);
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Task to calculate additional data required for mapping markers
     */
    private class MarkerDataProcessorTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgressIndicator();
        }

        @Override
        protected Void doInBackground(String... params) {
            mCsvListData = CsvDataParser.parseCsvData(params[0], params[1]);

            // do additional operation only if there is more than 1 data
            if (mCsvListData.size() > 0) {
                Collections.sort(mCsvListData, new ComparableDownloadSpeed());
                // at this point we know there is at least one data, save min
                // and max speed data
                mMinNetworkSpeed = mCsvListData.get(0).getDownload();
                mMaxNetworkSpeed = mCsvListData.get(mCsvListData.size() - 1)
                        .getDownload();

                // For each of the marker data - update hue color value based on
                // download speed
                for (SpeedTestRecord record : mCsvListData) {
                    record.setMarkerColorHue(getWeightedMarkerValue(record
                            .getDownload()));
                }

                Tracer.debug(LOG_TAG, "Min: " + MapperActivity.mMinNetworkSpeed
                        + ", Max: " + MapperActivity.mMaxNetworkSpeed);
            }

            // Nothing to return
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            // hide progress when all processing done
            hideProgressIndicator();

            // now setup map with marker and other params
            setUpMap();
        }

    }

    /**
     * Inner listener class to listent for change in connection type filter
     * spinner.
     */
    private class ConnectionTypeFilterHandler implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (!checkReady()) {
                return;
            }

            String filterName = (String) parent.getItemAtPosition(position);
            Tracer.debug(LOG_TAG, "Selected Filter: " + filterName);
            if (filterName.equals(getString(R.string.filter_all))) {
                MapperActivity.FILTER_SELECTED = MapperActivity.FILTER_TYPE_ALL;
                MapperActivity.this.updateMapMarkers();
            } else if (filterName.equals(getString(R.string.filter_cell))) {
                MapperActivity.FILTER_SELECTED = MapperActivity.FILTER_TYPE_CELL;
                MapperActivity.this.updateMapMarkers();
            } else if (filterName.equals(getString(R.string.filter_wifi))) {
                MapperActivity.FILTER_SELECTED = MapperActivity.FILTER_TYPE_WIFI;
                MapperActivity.this.updateMapMarkers();
            } else {
                Tracer.info(LOG_TAG, "Error applying filter with name " + filterName);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // do nothing
            Tracer.debug(LOG_TAG, "OnItemSelectedListener > onNothingSelected()");
        }
    }

}
