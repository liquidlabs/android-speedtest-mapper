
package ca.liquidlabs.android.speedtestmapper;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ca.liquidlabs.android.speedtestmapper.InputDialogFragment.InputDialogListener;
import ca.liquidlabs.android.speedtestmapper.model.SpeedTestRecord;
import ca.liquidlabs.android.speedtestmapper.util.AppConstants;
import ca.liquidlabs.android.speedtestmapper.util.AppPackageUtils;
import ca.liquidlabs.android.speedtestmapper.util.CsvDataParser;
import ca.liquidlabs.android.speedtestmapper.util.Tracer;

import java.util.List;

/**
 * Main entry point launcher activity. Data is loaded here and verified before
 * loading maps view.
 */
public class MainActivity extends Activity implements InputDialogListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ImageView mIconFeedback;
    private TextView mMessageTextView;
    private Button mSpeedtestLinkButton;
    private static String mLastSessionValidData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tracer.println("onCreate");

        /*
         * Get reference to views
         */
        mIconFeedback = (ImageView) findViewById(R.id.ic_user_feedback);
        mMessageTextView = (TextView) findViewById(R.id.txt_user_feedback_guide);
        mSpeedtestLinkButton = (Button) findViewById(R.id.btn_speedtest_app_link);

        /*
         * Get intent, action and MIME type More info/guide:
         * http://developer.android.com/training/sharing/receive.html
         */
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleIntentText(intent); // Handle text being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen

            // Prepare button to proper speedtest link
            this.prepareSpeedTestLink();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Tracer.println("onStart");

    }

    private void handleIntentText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

        if (CsvDataParser.isValidCsvData(sharedText)) {
            Tracer.Toast(this, "Got data, length : " + sharedText.length());
            mLastSessionValidData = sharedText; // save the valid data in for current session
            this.launchMapperActivity(sharedText);
        } else {
            this.handleInvalidText();
        }

    }

    private void handleLocalText(String data) {
        if (CsvDataParser.isValidCsvData(data)) {
            mLastSessionValidData = data; // save the valid data in for current session
            this.launchMapperActivity(data);
        } else {
            this.handleInvalidText();
        }
    }

    /**
     * Unexpected text is shared/input. Show user feedback.
     */
    private void handleInvalidText() {
        Tracer.debug(LOG_TAG, "handleInvalidText()");
        Tracer.Toast(this, "Unable to parse CSV. Make sure you use proper export feature.");
    }

    private void showInputDialog() {
        FragmentManager fm = getFragmentManager();
        InputDialogFragment editNameDialog = InputDialogFragment.newInstance();
        editNameDialog.show(fm, "fragment_input_data");
    }

    /**
     * Launches mapping activity when valid CSV data is found.
     * 
     * @param csvData Valid speedtest data
     */
    private void launchMapperActivity(String csvData) {
        // Test data ready - go to maps view
        Intent intent = new Intent(this, MapperActivity.class);
        intent.putExtra(AppConstants.KEY_SPEEDTEST_CSV_DATA, csvData);
        startActivity(intent);
    }

    /**
     * Prepares speedtest app link button to help user to easily install or
     * launch app.
     */
    private void prepareSpeedTestLink() {
        Tracer.debug(LOG_TAG, "prepareSpeedTestLink()");
        if (AppPackageUtils.isSpeedTestAppInstalled(getApplicationContext())) {
            // Prepare link to SpeedTest app
            mSpeedtestLinkButton.setText(R.string.lbl_launch_speedtest_app);
            mSpeedtestLinkButton.setCompoundDrawablesWithIntrinsicBounds(AppPackageUtils
                    .getAppIcon(getApplicationContext(), AppConstants.PACKAGE_SPEEDTEST_APP), null,
                    null, null);

            // Also setup click listener
            mSpeedtestLinkButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(AppPackageUtils.getSpeedTestAppIntent(getApplicationContext()));
                }
            });
        } else {
            // Prepare link to SpeedTest app in Google Play
            mSpeedtestLinkButton.setText(R.string.lbl_get_app_googleplay);
            mSpeedtestLinkButton.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_google_play_store, 0, 0, 0);

            // Setup play store intent
            mSpeedtestLinkButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse(AppConstants.PLAY_STORE_BASE_URL
                                    + AppConstants.PACKAGE_SPEEDTEST_APP)));
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_paste_data:
                showInputDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //
    // InputDialogListener implementation
    //

    /**
     * Callback from input dialog fragment with data
     */
    @Override
    public void onFinishEditDialog(String inputText) {
        Tracer.debug(LOG_TAG, "onFinishEditDialog: " + inputText);
        this.handleLocalText(inputText);
    }

}
