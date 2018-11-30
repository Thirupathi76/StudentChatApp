package com.demos.studentchatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by welcome on 3/2/2018.
 */

public class Utilities {

    public static final String GROUP_NAME = "room_name";
    public static final String USER_NAME = "user_name";
    public static final String MY_PREFERENCES = "MY_PREFERENCES";
    public static final String FIRST_INSTALL = "FIRST_INSTALL";
    public static final String BRANCH = "branch";
    public static final String COLLEGE = "college";

    public static void setPreference(Context context, String TAG, String value) {

        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TAG, value);
        editor.apply();

    }

    public static String getPreference(Context context, String TAG) {

        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        String value = preferences.getString(TAG, "");
        return value;

    }

    public static void clearAllPrefernces(Context context) {

        if (context != null) {
            SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            editor.commit();
        }
    }

    public static boolean checkConnection(Context context) {
      /*  ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
            }
        } else {
            // not connected to the internet
        }*/

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }



}
