package me.habism.flightyoke.core;

import android.util.Log;

public class AppLogger {
    private static final String TAG = "FlightYoke";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg, Throwable t) {
        Log.e(TAG, msg, t);
    }
}