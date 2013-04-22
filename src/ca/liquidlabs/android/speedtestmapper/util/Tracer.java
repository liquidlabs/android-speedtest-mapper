
package ca.liquidlabs.android.speedtestmapper.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Helper methods to debug/log data and disable for product app
 */
public class Tracer {

    /**
     * It is a utility class. Private constructors prevent a class from being
     * explicitly instantiated by callers.
     */
    private Tracer() {
    }

    /**
     * Display a Toast message for a short time
     * 
     * @param c Application Context
     * @param message The Message to be displayed
     */
    public static void Toast(Context c, String message)
    {
        if (!AppConstants.DEBUG_MODE)
            return;
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display a Toast message for a short time. Calls the other method by
     * getting the context from Activity
     * 
     * @param a Activity from which it was invoked
     * @param message The Message to be displayed
     */
    public static void Toast(Activity a, String message)
    {
        Tracer.Toast(a.getApplicationContext(), message);
    }

    /**
     * Display a Toast message for a short time. Calls the other method by
     * getting the context from Activity and toString from the object
     * 
     * @param a Activity from which it was invoked
     * @param Object Invokes the toString method of object
     */
    public static void Toast(Activity a, Object obj)
    {
        Tracer.Toast(a.getApplicationContext(), obj == null ? " NULL " : obj.toString());
    }

    /**
     * Logs an error in the App
     * 
     * @param tag The Tag [from a list of values specified in Constants]
     * @param message the error message
     */
    public static void error(String tag, String message)
    {
        if (!AppConstants.DEBUG_MODE)
            return;
        Log.e(tag, message);
    }

    /**
     * Logs an error in the App
     * 
     * @param tag The Tag [from a list of values specified in Constants]
     * @param message the error message
     */
    public static void error(String tag, Exception e)
    {
        if (!AppConstants.DEBUG_MODE)
            return;
        Tracer.error(tag, e.getMessage());
    }

    /**
     * Logs an info message of the app
     * 
     * @param tag The Tag [from a list of values specified in Constants]
     * @param message the error message
     */
    public static void info(String tag, String message)
    {
        if (!AppConstants.DEBUG_MODE)
            return;
        Log.i(tag, message);
    }

    /**
     * Logs an info message of the app
     * 
     * @param tag The Tag [from a list of values specified in Constants]
     * @param message the error message
     */
    public static void info(String tag, Exception e)
    {
        if (!AppConstants.DEBUG_MODE)
            return;
        Tracer.info(tag, e.getMessage());
    }

    public static void println(String m)
    {
        if (!AppConstants.DEBUG_MODE)
            return;
        Tracer.debug(AppConstants.TAG_TRACE + "", m);
    }

    /**
     * Logs a debug message with a tag
     * 
     * @param tag
     * @param m
     */
    public static void debug(String tag, String m)
    {
        if (!AppConstants.DEBUG_MODE)
            return;
        Log.d(tag, m == null ? "[null]" : m);
    }

    /**
     * Logs a debug message with a tag
     * 
     * @param tag
     * @param e
     */
    public static void debug(String tag, Exception e)
    {
        if (!AppConstants.DEBUG_MODE)
            return;
        Log.d(tag, e.getMessage());
    }

    public static void showActionRuntime(String actionName, long timeStarted) {
        Tracer.debug("RUNTIME-BENCHMARK", actionName + " - took ***"
                + (System.currentTimeMillis() - timeStarted) + "*** milli-seconds to complete");
    }
}
