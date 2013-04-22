
package ca.liquidlabs.android.speedtestmapper.util;

public interface AppConstants {
    /**
     * Boolean flat to trun ON or OFF application debugging
     */
    boolean DEBUG_MODE = true;

    String TAG_TRACE = "[STM]";
    
    /**
     * Key name used to send data via intent
     */
    String KEY_SPEEDTEST_CSV_DATA = "INTENT_KEY_CSV_DATA";

    /**
     * Package name for SpeedTest(tm) app
     */
    String PACKAGE_SPEEDTEST_APP = "org.zwanoo.android.speedtest";

    /**
     * Since this app is targeted for newer API, we do not need to worry about
     * old market package name 'com.google.market'. <br/>
     * 
     * UPDATE: Unable to use this package name, since for some reason Play Store
     * was not found by this package name.
     */
    String PACKAGE_GOOGLE_PLAY_STORE = "com.google.vending";
    
    /**
     * Used to create generic intent instead of using package name
     */
    String PLAY_STORE_BASE_URL = "http://play.google.com/store/apps/details?id=";
}
