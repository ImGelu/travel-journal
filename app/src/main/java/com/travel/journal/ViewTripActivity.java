package com.travel.journal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.internal.LinkedTreeMap;
import com.travel.journal.retrofit.RetrofitClientInstance;
import com.travel.journal.retrofit.Weather;
import com.travel.journal.retrofit.WeatherApi;
import com.travel.journal.room.Trip;
import com.travel.journal.room.TripDao;
import com.travel.journal.room.TripDataBase;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTripActivity extends AppCompatActivity {

    private static final String API_TOKEN = "e1980f218fc410cc362331590255d98c";

    private TripDao tripDao;
    private Trip trip;
    private ImageButton favoriteButton;
    private TextView tripName, tripDestination, tripPrice, tripType, tripStartDate, tripEndDate, tripRating, weatherNow, weatherMin, weatherMax, weatherWind, weatherCloud, weatherHumidity, weatherTitle;
    private ImageView weatherIcon, tripPicture;
    private ConstraintLayout weatherLayout;

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

        initializeComponents(tripTypes);

        switch(trip.getType()){
            case 0: tripPicture.setImageResource(R.drawable.travel_city); break;
            case 1: tripPicture.setImageResource(R.drawable.travel_sea); break;
            case 2: tripPicture.setImageResource(R.drawable.travel_mountains); break;
            default: tripPicture.setImageResource(R.drawable.travel_bg);
        }

        if (trip.isFavorite()) favoriteButton.setImageResource(R.drawable.ic_baseline_star_24);

        WeatherApi service = RetrofitClientInstance.getRetrofitInstance().create(WeatherApi.class);
        Call<Weather> call = service.getWeather(trip.getDestination(), API_TOKEN, "metric");
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if(response.code() == 200) {
                    Weather main = response.body();

                    if (main.getCurrentTemperature() > 0) {
                        weatherIcon.setImageResource(R.drawable.ic_outline_wb_sunny_24);
                        weatherIcon.setColorFilter(ActivityCompat.getColor(ViewTripActivity.this, R.color.yellow_500));
                    } else {
                        weatherIcon.setImageResource(R.drawable.ic_outline_ice_skating_24);
                        weatherIcon.setColorFilter(ActivityCompat.getColor(ViewTripActivity.this, R.color.blue_500));
                    }

                    weatherNow.setText(String.format("%s%s", main.getCurrentTemperature(), getString(R.string.degree_symbol)));
                    weatherMin.setText(String.format("%s%s", main.getMinTemperature(), getString(R.string.degree_symbol)));
                    weatherMax.setText(String.format("%s%s", main.getMaxTemperature(), getString(R.string.degree_symbol)));
                    weatherWind.setText(String.format("%s %s", main.getWind(), getString(R.string.wind_unit)));
                    weatherCloud.setText(String.format("%s%s", main.getClouds(), getString(R.string.cloud_unit)));
                    weatherHumidity.setText(String.format("%s%s", main.getHumidity(), getString(R.string.humidity_unit)));
                    weatherTitle.setText(R.string.the_weather_right_now);
                    weatherLayout.setVisibility(View.VISIBLE);
                } else {
                    weatherTitle.setText(R.string.weather_error);
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                weatherTitle.setText(R.string.weather_error);
            }

        });
    }

    private void initializeComponents(List<String> tripTypes) {
        tripName = findViewById(R.id.trip_name);
        tripDestination = findViewById(R.id.trip_destination);
        tripPrice = findViewById(R.id.trip_price);
        tripType = findViewById(R.id.trip_type);
        tripStartDate = findViewById(R.id.trip_start_date);
        tripEndDate = findViewById(R.id.trip_end_date);
        tripRating = findViewById(R.id.trip_rating);
        weatherNow = findViewById(R.id.weather_now_value);
        weatherMin = findViewById(R.id.weather_min_value);
        weatherMax = findViewById(R.id.weather_max_value);
        weatherWind = findViewById(R.id.weather_wind_value);
        weatherCloud = findViewById(R.id.weather_clouds_value);
        weatherHumidity = findViewById(R.id.weather_humidity_value);
        weatherIcon = findViewById(R.id.weather_icon);
        weatherLayout = findViewById(R.id.weather_layout);
        weatherTitle = findViewById(R.id.weather_title);
        favoriteButton = findViewById(R.id.button_favorite);
        tripPicture = findViewById(R.id.trip_picture);

        tripName.setText(trip.getName());
        tripDestination.setText(trip.getDestination());
        tripPrice.setText(String.valueOf(trip.getPrice()));
        tripType.setText(tripTypes.get(trip.getType()));
        tripStartDate.setText(trip.getStartDate());
        tripEndDate.setText(trip.getEndDate());
        tripRating.setText(String.valueOf(trip.getRating()));
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