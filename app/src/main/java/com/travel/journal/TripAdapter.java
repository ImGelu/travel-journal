package com.travel.journal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.journal.room.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {
    private final List<Trip> trips;

    public TripAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        return new TripViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip currentTrip = trips.get(position);
        holder.setTripId(currentTrip.getId());

        if (currentTrip.isFavorite()) {
            holder.getButtonFavorite().setImageResource(R.drawable.ic_baseline_star_24);
        }

        switch(currentTrip.getType()){
            case 0: holder.getTripPicture().setImageResource(R.drawable.travel_city); break;
            case 1: holder.getTripPicture().setImageResource(R.drawable.travel_sea); break;
            case 2: holder.getTripPicture().setImageResource(R.drawable.travel_mountains); break;
            default: holder.getTripPicture().setImageResource(R.drawable.travel_bg);
        }

        holder.setFavorite(currentTrip.isFavorite());
        holder.getTextViewName().setText(currentTrip.getName());
        holder.getTextViewDestination().setText(currentTrip.getDestination());
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
}
