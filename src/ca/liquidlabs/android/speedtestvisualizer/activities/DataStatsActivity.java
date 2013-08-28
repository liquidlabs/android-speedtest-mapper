
package ca.liquidlabs.android.speedtestvisualizer.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import ca.liquidlabs.android.speedtestvisualizer.R;
import ca.liquidlabs.android.speedtestvisualizer.fragments.GraphViewMasterFragment;
import ca.liquidlabs.android.speedtestvisualizer.model.GraphType;
import ca.liquidlabs.android.speedtestvisualizer.util.AppConstants;

import java.util.Locale;

public class DataStatsActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    // Instance Properties
    private String mCsvHeader;
    private String mCsvData;

    /**
     * List of graph types supported by this activity.
     */
    private final GraphType availableGraphTypes[] = {
            GraphType.DATE_VS_DOWNLOAD, GraphType.DATE_VS_UPLOAD, GraphType.DATE_VS_LATENCY,
            GraphType.DATE_VS_DOWNLOAD_UPLOAD
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get feature to show progress in actionbar when processing data
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_data_stats);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Get the csv data from intent and then proceed
        Bundle bundle = getIntent().getExtras();
        mCsvHeader = bundle.getString(AppConstants.KEY_SPEEDTEST_CSV_HEADER);
        mCsvData = bundle.getString(AppConstants.KEY_SPEEDTEST_CSV_DATA);

    }

    @Override
    protected void onPause() {
        // Override the activity transition animation
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.data_stats, menu);
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            GraphType selectedGraphType = availableGraphTypes[position];

            if (selectedGraphType == GraphType.DATE_VS_DOWNLOAD) {
                return GraphViewMasterFragment.newInstance(mCsvHeader, mCsvData, selectedGraphType);
            }
            else if (selectedGraphType == GraphType.DATE_VS_UPLOAD) {
                return GraphViewMasterFragment.newInstance(mCsvHeader, mCsvData, selectedGraphType);
            }
            else if (selectedGraphType == GraphType.DATE_VS_LATENCY) {
                return GraphViewMasterFragment.newInstance(mCsvHeader, mCsvData, selectedGraphType);
            }
            else if (selectedGraphType == GraphType.DATE_VS_DOWNLOAD_UPLOAD) {
                return GraphViewMasterFragment.newInstance(mCsvHeader, mCsvData, selectedGraphType);
            }
            else {
                // FIXME - fix this.
                return null;
            }
        }

        @Override
        public int getCount() {
            // return number of available graph types
            return availableGraphTypes.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return availableGraphTypes[position].getShortTitle().toUpperCase(l);
        }
    }

}
