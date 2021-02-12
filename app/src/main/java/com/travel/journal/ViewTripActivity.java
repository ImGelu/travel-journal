package com.travel.journal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.travel.journal.room.Trip;
import com.travel.journal.room.TripDao;
import com.travel.journal.room.TripDataBase;

import java.util.Arrays;
import java.util.List;

public class ViewTripActivity extends AppCompatActivity {

    private TripDao tripDao;
    private Trip trip;
    private ImageButton favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);

        TripDataBase tripDataBase = Room.databaseBuilder(this, TripDataBase.class, GlobalData.TRIPS_DB_NAME).allowMainThreadQueries().build();
        tripDao = tripDataBase.getTripDao();

        Bundle extras = getIntent().getExtras();
        long tripId = extras.getLong(GlobalData.VIEW_TRIP_ID);
        trip = tripDao.getTrip(tripId);

        List<String> tripTypes = Arrays.asList(getString(R.string.city_break), getString(R.string.sea_side), getString(R.string.mountains));

        TextView tripName = findViewById(R.id.trip_name);
        TextView tripDestination = findViewById(R.id.trip_destination);
        TextView tripPrice = findViewById(R.id.trip_price);
        TextView tripType = findViewById(R.id.trip_type);
        TextView tripStartDate = findViewById(R.id.trip_start_date);
        TextView tripEndDate = findViewById(R.id.trip_end_date);
        TextView tripRating = findViewById(R.id.trip_rating);
        favoriteButton = findViewById(R.id.button_favorite);

        tripName.setText(trip.getName());
        tripDestination.setText(trip.getDestination());
        tripPrice.setText(String.valueOf(trip.getPrice()));
        tripType.setText(tripTypes.get(trip.getType()));
        tripStartDate.setText(trip.getStartDate());
        tripEndDate.setText(trip.getEndDate());
        tripRating.setText(String.valueOf(trip.getRating()));

        if (trip.isFavorite()) favoriteButton.setImageResource(R.drawable.ic_baseline_star_24);
    }

    public void markAsFavorite(View view) {
        if (!trip.isFavorite()) {
            tripDao.setFavorite(trip.getId());
            trip.setFavorite(true);
            favoriteButton.setImageResource(R.drawable.ic_baseline_star_24);
            Snackbar.make(view, view.getContext().getString(R.string.favorites_added), BaseTransientBottomBar.LENGTH_SHORT).show();
        } else {
            tripDao.removeFavorite(trip.getId());
            trip.setFavorite(false);
            favoriteButton.setImageResource(R.drawable.ic_baseline_star_outline_24);
            Snackbar.make(view, view.getContext().getString(R.string.favorites_removed), BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }
}