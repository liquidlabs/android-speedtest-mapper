
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

import ca.liquidlabs.android.speedtestmapper.model.SpeedTestRecord;
import ca.liquidlabs.android.speedtestmapper.util.AppConstants;
import ca.liquidlabs.android.speedtestmapper.util.CsvDataParser;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Mapping of exported data.
 * 
 * @author Hossain Khan
 * @see https://developers.google.com/maps/documentation/android/
 * @see https://developers.google.com/maps/documentation/android/reference/com/google/android/gms/maps/package-summary
 * @see Maps V2 example project. Most codes are taken from sample project.
 */
public class MapperActivity extends Activity {

    private GoogleMap mMap;
    private Builder mBoundsBuilder;
    private static List<SpeedTestRecord> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get feature to show progress in actionbar when processing data
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_mapper);
        
        // Show the Up button in the action bar.
        setupActionBar();
        
        // Get the csv data from intent and then proceed
        Bundle bundle = getIntent().getExtras();
        String csvData = bundle.getString(AppConstants.KEY_SPEEDTEST_CSV_DATA);
        mListData = CsvDataParser.parseCsvData(csvData);
        
        // Now setup map with app the data
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mBoundsBuilder = new LatLngBounds.Builder();
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
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mBoundsBuilder.build(), 50));
                }
            });
        }

    }

    private void addMarkersToMap() {

        // Use parsed data to create map markers
        for (SpeedTestRecord speedTestRecord : mListData) {
            mMap.addMarker(new MarkerOptions()
                    .position(speedTestRecord.getLatLng())
                    .title("Download (mbps): " + speedTestRecord.getDownload()
                            +"\n Upload (mbps): " + speedTestRecord.getUpload())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            // also build the maps bounds area
            mBoundsBuilder.include(speedTestRecord.getLatLng());
        }
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

}
