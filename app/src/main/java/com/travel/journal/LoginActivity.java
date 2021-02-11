package com.travel.journal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.travel.journal.room.User;
import com.travel.journal.room.UserDao;
import com.travel.journal.room.UserDataBase;

public class LoginActivity extends AppCompatActivity {

    public final String LOGGED_IN_USER = "LOGGED_IN_USER";
    public final String DB_NAME = "users.db";

    private EditText userEmail, userPassword;
    private TextInputLayout userEmailLayout, userPasswordLayout;

    private UserDataBase userDataBase;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDataBase = Room.databaseBuilder(this, UserDataBase.class, DB_NAME).allowMainThreadQueries().build();
        userDao = userDataBase.getUserDao();

        userEmail = findViewById(R.id.text_field_email_value);
        userPassword = findViewById(R.id.text_field_password_value);
        userEmailLayout = findViewById(R.id.text_field_email);
        userPasswordLayout = findViewById(R.id.text_field_password);

        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!userEmail.getText().toString().isEmpty()) userEmailLayout.setError(null);
                else userEmailLayout.setError(getString(R.string.error_required));

                if (!TextUtils.isEmpty(s) && !Patterns.EMAIL_ADDRESS.matcher(s).matches()) userEmailLayout.setError(getString(R.string.error_email_pattern));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!userPassword.getText().toString().isEmpty()) userPasswordLayout.setError(null);
                else userPasswordLayout.setError(getString(R.string.error_required));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void openRegisterActivity(View view) {
        Intent intent = new Intent(view.getContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void beginLogin(View view) {
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            User user = userDao.getUser(email, password);

            if (user != null) {
                Gson gson = new Gson();
                String userJson = gson.toJson(user);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(LOGGED_IN_USER, userJson);
                editor.apply();

                Intent intent = new Intent(view.getContext(), HomeActivity.class);
                startActivity(intent);
            } else {
                userEmailLayout.setError(getString(R.string.error_wrong_credentials));
            }
        } else {
            if (email.isEmpty()) {
                userEmailLayout.setError(getString(R.string.error_required));
            } else {
                userEmailLayout.setError(null);
            }

            if (password.isEmpty()) {
                userPasswordLayout.setError(getString(R.string.error_required));
            } else {
                userPasswordLayout.setError(null);
            }
        }
    }
}