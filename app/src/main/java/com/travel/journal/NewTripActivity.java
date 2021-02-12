package com.travel.journal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.travel.journal.room.Trip;
import com.travel.journal.room.TripDao;
import com.travel.journal.room.TripDataBase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewTripActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextInputLayout tripNameLayout, tripDestinationLayout, tripStartDateLayout, tripEndDateLayout, tripTypeLayout;
    private EditText tripName, tripDestination, tripStartDate, tripEndDate;
    private AutoCompleteTextView tripType;
    private Slider tripPrice;
    private RatingBar tripRating;

    private DatePickerDialog tripDatePicker;

    private List<String> tripTypes;
    private ArrayAdapter<String> spinnerAdapter;

    private TripDataBase tripDataBase;
    private TripDao tripDao;

    private Trip editableTrip;

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        extras = getIntent().getExtras();

        this.getSupportActionBar().setTitle(extras == null ? R.string.add_trip_title : R.string.edit_trip);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_new_trip);

        tripTypes = Arrays.asList(getString(R.string.city_break), getString(R.string.sea_side), getString(R.string.mountains));

        tripDataBase = Room.databaseBuilder(this, TripDataBase.class, GlobalData.TRIPS_DB_NAME).allowMainThreadQueries().build();
        tripDao = tripDataBase.getTripDao();

        initializeComponents();

        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, tripTypes);
        tripType.setAdapter(spinnerAdapter);
        tripType.setOnItemSelectedListener(this);

        if (extras != null) {
            long editableTripId = extras.getLong(GlobalData.EDIT_TRIP_ID);
            editableTrip = tripDao.getTrip(editableTripId);

            tripName.setText(editableTrip.getName());
            tripDestination.setText(editableTrip.getDestination());
            tripType.setText(tripTypes.get(editableTrip.getType()), false);
            tripStartDate.setText(editableTrip.getStartDate());
            tripEndDate.setText(editableTrip.getEndDate());
            tripPrice.setValue((float) editableTrip.getPrice());
            tripRating.setRating((float) editableTrip.getRating());
        }
    }

    private void initializeComponents() {
        tripName = findViewById(R.id.text_field_trip_name_value);
        tripDestination = findViewById(R.id.text_field_destination_value);
        tripType = findViewById(R.id.text_field_trip_type_value);
        tripStartDate = findViewById(R.id.text_field_start_date_value);
        tripEndDate = findViewById(R.id.text_field_end_date_value);
        tripPrice = findViewById(R.id.slider_price);
        tripRating = findViewById(R.id.rating_bar);

        tripNameLayout = findViewById(R.id.text_field_trip_name);
        tripDestinationLayout = findViewById(R.id.text_field_destination);
        tripStartDateLayout = findViewById(R.id.text_field_start_date);
        tripEndDateLayout = findViewById(R.id.text_field_end_date);
        tripTypeLayout = findViewById(R.id.dropdown_trip_type);

        tripName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!tripName.getText().toString().isEmpty()) tripNameLayout.setError(null);
                else tripNameLayout.setError(getString(R.string.error_required));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tripDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!tripDestination.getText().toString().isEmpty())
                    tripDestinationLayout.setError(null);
                else tripDestinationLayout.setError(getString(R.string.error_required));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setDateField(String field) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);

        tripDatePicker = new DatePickerDialog(this, R.style.my_dialog_theme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar pickedDate = Calendar.getInstance();
                pickedDate.set(year, month, dayOfMonth);

                final String formattedDate = sdf.format(pickedDate.getTime());

                if (field.equals("startDate")) {
                    tripStartDate.setText(formattedDate);
                } else if (field.equals("endDate")) {
                    tripEndDate.setText(formattedDate);
                }
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        initializeDatePickerValidation(field, sdf);
    }

    private void initializeDatePickerValidation(String field, SimpleDateFormat sdf) {
        tripDatePicker.getDatePicker().setFirstDayOfWeek(2);

        if (field.equals("startDate")) {
            if (!tripEndDate.getText().toString().isEmpty()) {
                try {
                    String tripEndDateValue = tripEndDate.getText().toString();
                    tripDatePicker.getDatePicker().setMaxDate(sdf.parse(tripEndDateValue).getTime());
                } catch (ParseException e) {
                    Toast.makeText(this, getString(R.string.generic_exception_text), Toast.LENGTH_LONG).show();
                }
            }
        } else if (field.equals("endDate")) {
            if (!tripStartDate.getText().toString().isEmpty()) {
                try {
                    String tripStartDateValue = tripStartDate.getText().toString();
                    tripDatePicker.getDatePicker().setMinDate(sdf.parse(tripStartDateValue).getTime());
                } catch (ParseException e) {
                    Toast.makeText(this, getString(R.string.generic_exception_text), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void openStartDatePicker(View view) {
        setDateField("startDate");
        tripDatePicker.show();
    }

    public void openEndDatePicker(View view) {
        setDateField("endDate");
        tripDatePicker.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedTripType = tripTypes.get(position);
        Toast.makeText(parent.getContext(), selectedTripType, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void saveTrip(View view) {
        if (!runValidations()) {
            long newTripUserId = GlobalData.getLoggedInUser().getId();
            String newTripName = tripName.getText().toString().trim();
            String newTripDestination = tripDestination.getText().toString().trim();
            int newTripType = tripTypes.indexOf(tripType.getText().toString().trim());
            double newTripPrice = tripPrice.getValue();
            String newTripStartDate = tripStartDate.getText().toString().trim();
            String newTripEndDate = tripEndDate.getText().toString().trim();
            double newTripRating = tripRating.getRating();

            Trip newTrip = new Trip(newTripUserId, newTripName, newTripDestination, newTripType, newTripPrice, newTripStartDate, newTripEndDate, newTripRating);

            if (extras != null) {
                newTrip.setId(editableTrip.getId());
                tripDao.update(newTrip);
            } else {
                tripDao.insert(newTrip);
            }
            Intent intent = new Intent(view.getContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean runValidations() {
        boolean existingErrors = false;

        if (tripName.getText().toString().isEmpty()) {
            tripNameLayout.setError(getString(R.string.error_required));
            existingErrors = true;
        }

        if (tripDestination.getText().toString().isEmpty()) {
            tripDestinationLayout.setError(getString(R.string.error_required));
            existingErrors = true;
        }

        if (tripType.getText().toString().isEmpty()) {
            tripTypeLayout.setError(getString(R.string.error_required));
            existingErrors = true;
        } else {
            tripTypeLayout.setError(null);
        }

        if (tripStartDate.getText().toString().isEmpty()) {
            tripStartDateLayout.setError(getString(R.string.error_required));
            existingErrors = true;
        } else {
            tripStartDateLayout.setError(null);
        }

        if (tripEndDate.getText().toString().isEmpty()) {
            tripEndDateLayout.setError(getString(R.string.error_required));
            existingErrors = true;
        } else {
            tripEndDateLayout.setError(null);
        }

        return existingErrors;
    }
}