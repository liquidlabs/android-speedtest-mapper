
package ca.liquidlabs.android.speedtestmapper.util;

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
     * Used to create generic intent instead of using package name
     */
    String PLAY_STORE_BASE_URL = "http://play.google.com/store/apps/details?id=";
}
