
package ca.liquidlabs.android.speedtestmapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import ca.liquidlabs.android.speedtestmapper.model.SpeedTestRecord;
import ca.liquidlabs.android.speedtestmapper.util.AppConstants;
import ca.liquidlabs.android.speedtestmapper.util.CsvDataParser;
import ca.liquidlabs.android.speedtestmapper.util.Tracer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
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

    private static final int FILTER_TYPE_ALL = 0;
    private static final int FILTER_TYPE_WIFI = 1;
    private static final int FILTER_TYPE_CELL = 2;
    private static int FILTER_SELECTED = FILTER_TYPE_ALL;

    private GoogleMap mMap;
    private static List<SpeedTestRecord> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get feature to show progress in actionbar when processing data
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_mapper);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_conntype_filter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.filters_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new ConnectionTypeFilterHandler());

        // Show the Up button in the action bar.
        setupActionBar();

        // Get the csv data from intent and then proceed
        Bundle bundle = getIntent().getExtras();
        String csvHeader = bundle.getString(AppConstants.KEY_SPEEDTEST_CSV_HEADER);
        String csvData = bundle.getString(AppConstants.KEY_SPEEDTEST_CSV_DATA);
        mListData = CsvDataParser.parseCsvData(csvHeader, csvData);

        // Now setup map with app the data
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // setUpMapIfNeeded();
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
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();

    }

    private void addMarkersToMap() {

        Builder mapBoundsBuilder = new LatLngBounds.Builder();
        int currentTotalRecordCount = 0;
        // Use parsed data to create map markers
        for (SpeedTestRecord speedTestRecord : mListData) {

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
                    NumberFormat.getInstance().format(speedTestRecord.getDownload()) + " (mbps)",
                    NumberFormat.getInstance().format(speedTestRecord.getUpload()) + " (mbps)"
            };

            mMap.addMarker(new MarkerOptions()
                    .position(speedTestRecord.getLatLng())
                    .title(speedTestRecord.getDate())
                    .snippet(StringUtils.join(snippetMultiInfo, '|'))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

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
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
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

    private void showProgressIndicator() {
        setProgressBarIndeterminateVisibility(true);
        setProgressBarIndeterminate(true);
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mapper, menu);
        return true;
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
