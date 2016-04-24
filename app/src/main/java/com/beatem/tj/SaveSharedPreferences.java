package com.beatem.tj;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mliljenberg on 24/04/16.
 */
public class SaveSharedPreferences {
    static final String CURRENT_TRIP= "current_trip";
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setCurrentTrip(Context ctx, String tripName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(CURRENT_TRIP, tripName);
        editor.commit();
    }

    public static String getCurrentTrip(Context ctx)
    {
        return getSharedPreferences(ctx).getString(CURRENT_TRIP, "");
    }
}
