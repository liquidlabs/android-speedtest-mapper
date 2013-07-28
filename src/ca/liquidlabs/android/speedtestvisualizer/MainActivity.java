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

package ca.liquidlabs.android.speedtestvisualizer;

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
import android.widget.ShareActionProvider;
import android.widget.TextView;

import ca.liquidlabs.android.speedtestvisualizer.activities.AboutAppActivity;
import ca.liquidlabs.android.speedtestvisualizer.activities.MapperActivity;
import ca.liquidlabs.android.speedtestvisualizer.fragments.InputDialogFragment;
import ca.liquidlabs.android.speedtestvisualizer.fragments.InputDialogFragment.InputDialogListener;
import ca.liquidlabs.android.speedtestvisualizer.util.AppConstants;
import ca.liquidlabs.android.speedtestvisualizer.util.AppPackageUtils;
import ca.liquidlabs.android.speedtestvisualizer.util.CsvDataParser;
import ca.liquidlabs.android.speedtestvisualizer.util.Tracer;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Main entry point launcher activity. Data is loaded here and verified before
 * loading maps view.
 */
public class MainActivity extends Activity implements InputDialogListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //
    // UI Views Used for this activity
    //
    private ImageView mIconFeedback;
    private TextView mMessageTextView;
    private Button mSpeedtestLinkButton;
    private Button mRelaunchMapButton;

    // Share action provider for menu item
    private ShareActionProvider mShareActionProvider;

    /**
     * Validated CSV data saved in memory
     */
    private static String mLastSessionValidData = null;
    private static boolean mIsSharedIntent = false;

    /**
     * Part of CSV header text, used for data validation
     */
    private String mCsvHeaderValidationText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tracer.debug(LOG_TAG, "onCreate");

        /*
         * Get reference to views
         */
        mIconFeedback = (ImageView) findViewById(R.id.ic_user_feedback);
        mMessageTextView = (TextView) findViewById(R.id.txt_user_feedback_guide);
        mSpeedtestLinkButton = (Button) findViewById(R.id.btn_speedtest_app_link);
        mRelaunchMapButton = (Button) findViewById(R.id.btn_relaunch_map);

        // Also load the CSV record header text, which is needed to validate
        mCsvHeaderValidationText = this.getString(R.string.speedtest_csv_header_validation);

        /*
         * Get intent, action and MIME type More info/guide:
         * http://developer.android.com/training/sharing/receive.html
         */
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            mIsSharedIntent = true;
            if ("text/plain".equals(type)) {
                // Handle text being sent
                handleIntentText(intent.getStringExtra(Intent.EXTRA_TEXT));
            } else {
                // unsupported mimetype
                this.handleInvalidText();
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Tracer.debug(LOG_TAG, "onStart");

        // Tracks activity view using analytics.
        EasyTracker.getInstance().activityStart(this);

        // Prepare session UI data - based on user input
        this.prepareSessionDataUi();

        // Prepare button to proper speedtest link
        this.prepareSpeedTestLink();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Tracks activity view using analytics.
        EasyTracker.getInstance().activityStop(this);
    }

    /**
     * Handle intent data when shared from speedtest or other app
     * 
     * @param intent Intent received by this activity
     */
    private void handleIntentText(String sharedText) {
        Tracer.debug(LOG_TAG, "handleIntentText() " + sharedText);

        if (CsvDataParser.isValidCsvData(mCsvHeaderValidationText, sharedText)) {
            // save the valid data in for current session
            mLastSessionValidData = sharedText;
            mIsSharedIntent = false;
            this.launchMapperActivity(sharedText);
        } else {
            this.handleInvalidText();
        }

    }

    /**
     * Handle text provided by user from clipboard
     * 
     * @param data User data
     */
    private void handleLocalText(String data) {
        Tracer.debug(LOG_TAG, "handleLocalText()");

        if (CsvDataParser.isValidCsvData(mCsvHeaderValidationText, data)) {
            // save the valid data in for current session
            mLastSessionValidData = data;
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

        // give ui feedback with error
        mIconFeedback.setImageResource(R.drawable.ic_disappoint);
        mMessageTextView.setText(R.string.msg_invalid_data);
        mRelaunchMapButton.setVisibility(View.GONE);
    }

    /**
     * Shows input dialog fragment to take input from user
     */
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
        intent.putExtra(AppConstants.KEY_SPEEDTEST_CSV_HEADER, mCsvHeaderValidationText);
        intent.putExtra(AppConstants.KEY_SPEEDTEST_CSV_DATA, csvData);
        startActivity(intent);
        // apply slide-in animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Prepares UI for current session - if user has already imported some data
     */
    private void prepareSessionDataUi() {
        // if shared intent, UI has been already populated
        if (mIsSharedIntent) {
            return;
        }

        if (mLastSessionValidData != null) {
            // valid data exist, user already used some data to see maps
            mIconFeedback.setImageResource(R.drawable.ic_smile_success);
            mMessageTextView.setText(R.string.msg_valid_data_session_available);
            mRelaunchMapButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchMapperActivity(mLastSessionValidData);
                }
            });
            mRelaunchMapButton.setVisibility(View.VISIBLE);
        } else {
            // Welcome user and show instructions UI
            mIconFeedback.setImageResource(R.drawable.ic_dialog_bubble);
            mMessageTextView.setText(R.string.msg_welcome_instructions);
            // TODO: Show button with YouTube demo link
            mRelaunchMapButton.setVisibility(View.GONE);
        }
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
                    AppPackageUtils.getAppIcon(getApplicationContext(),
                            GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE),
                    null, null, null);

            // Setup play store intent
            mSpeedtestLinkButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse(AppConstants.PLAY_STORE_BASE_NATIVE_URI
                                    + AppConstants.PACKAGE_SPEEDTEST_APP)));
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share_app);

        // Fetch and store ShareActionProvider. More info @
        // http://developer.android.com/training/sharing/shareaction.html
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        // Share app using share action provider
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(AppPackageUtils.getShareAppIntent(
                    getApplicationContext()));
        }

        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_paste_data:
                showInputDialog();
                return true;
            case R.id.action_report_issue:
                // Prepare email content and send intent
                startActivity(Intent.createChooser(
                        AppPackageUtils.getReportIssueIntent(getApplicationContext()),
                        getString(R.string.title_dialog_choose_email)));
                return true;
            case R.id.action_about_app:
                startActivity(new Intent(getApplicationContext(), AboutAppActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
