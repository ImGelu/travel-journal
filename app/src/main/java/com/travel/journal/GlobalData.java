package com.travel.journal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.travel.journal.room.User;

public abstract class GlobalData {
    public final static String LOGGED_IN_USER = "LOGGED_IN_USER";
    public final static String EDIT_TRIP_ID = "EDIT_TRIP_ID";
    public final static String USERS_DB_NAME = "users.db";
    public final static String TRIPS_DB_NAME = "trips.db";

    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();

        if(loggedInUser!= null) {
            String userJson = gson.toJson(loggedInUser);
            editor.putString(GlobalData.LOGGED_IN_USER, userJson);
        } else {
            editor.putString(GlobalData.LOGGED_IN_USER, null);
        }

        editor.apply();

        if(loggedInUser!= null){
            GlobalData.loggedInUser = gson.fromJson(preferences.getString(GlobalData.LOGGED_IN_USER, ""), User.class);
        } else {
            GlobalData.loggedInUser = null;
        }
    }
}
