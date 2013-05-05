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

/**
 * Application constants. Defined in interface, since they are implicitly public+final.
 */
public interface AppConstants {
    /**
     * Boolean flat to trun ON or OFF application debugging
     */
    boolean DEBUG_MODE = true;

    /**
     * Character used to join and split string data
     */
    char TEXT_SEPARATOR = '|';

    /**
     * Key name for CSV header used to send data via intent
     */
    String KEY_SPEEDTEST_CSV_HEADER = "INTENT_KEY_CSV_HEADER";

    /**
     * Key name for CSV data set used to send data via intent
     */
    String KEY_SPEEDTEST_CSV_DATA = "INTENT_KEY_CSV_DATA";

    /**
     * Package name for SpeedTest(tm) app
     */
    String PACKAGE_SPEEDTEST_APP = "org.zwanoo.android.speedtest";

    /**
     * Used to create browser based intent for App page<br/>
     * Guide: http://developer.android.com/distribute/googleplay/promote/linking.html
     */
    String PLAY_STORE_BASE_WEB_URI = "http://play.google.com/store/apps/details?id=";
    
    /**
     * Used to directly launch Google's Play Store app with query. <br/>
     * Guide: http://developer.android.com/distribute/googleplay/promote/linking.html
     */
    String PLAY_STORE_BASE_NATIVE_URI = "market://details?id=";

    /**
     * Email address for contact/issue reporting
     */
    String CONTANT_EMAIL = "contact@liquidlabs.ca";

    /**
     * Email mime type
     */
    String EMAIL_MIME_TYPE = "message/rfc822";
}
