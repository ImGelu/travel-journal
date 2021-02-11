package com.travel.journal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.travel.journal.room.User;

public class MainActivity extends AppCompatActivity {

    public User loggedInUser;
    public final String LOGGED_IN_USER = "LOGGED_IN_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_TravelJournal);

        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        loggedInUser = gson.fromJson(preferences.getString(LOGGED_IN_USER, ""), User.class);

        if(loggedInUser != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}