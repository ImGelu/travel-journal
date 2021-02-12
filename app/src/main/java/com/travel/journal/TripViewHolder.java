package com.travel.journal;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.travel.journal.room.TripDao;
import com.travel.journal.room.TripDataBase;

public class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private final TextView textViewName, textViewDestination;
    private final ImageButton buttonFavorite;

    private final TripDao tripDao;

    private long tripId;
    private boolean isFavorite;

    public TripViewHolder(@NonNull View itemView) {
        super(itemView);

        TripDataBase tripDataBase = Room.databaseBuilder(itemView.getContext(), TripDataBase.class, GlobalData.TRIPS_DB_NAME).allowMainThreadQueries().build(); //! Not sure if starting a Database Builder is ok...
        tripDao = tripDataBase.getTripDao();

        textViewName = itemView.findViewById(R.id.trip_name);
        textViewDestination = itemView.findViewById(R.id.trip_destination);
        MaterialCardView materialCardView = itemView.findViewById(R.id.card);
        buttonFavorite = itemView.findViewById(R.id.button_favorite);

        materialCardView.setOnClickListener(this);
        materialCardView.setOnLongClickListener(this);
        buttonFavorite.setOnClickListener(v -> {
            if (!isFavorite) {
                tripDao.setFavorite(tripId);
                setFavorite(true);
                buttonFavorite.setImageResource(R.drawable.ic_baseline_star_24);
                Snackbar.make(v, v.getContext().getString(R.string.favorites_added), BaseTransientBottomBar.LENGTH_SHORT).show();
            } else {
                tripDao.removeFavorite(tripId);
                setFavorite(false);
                buttonFavorite.setImageResource(R.drawable.ic_baseline_star_outline_24);
                Snackbar.make(v, v.getContext().getString(R.string.favorites_removed), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public TextView getTextViewName() {
        return textViewName;
    }

    public TextView getTextViewDestination() {
        return textViewDestination;
    }

    public ImageButton getButtonFavorite() {
        return buttonFavorite;
    }

    @Override
    public void onClick(View v) {
        Snackbar.make(v, String.valueOf(getAdapterPosition()), BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        Intent intent = new Intent(v.getContext(), NewTripActivity.class);
        intent.putExtra(GlobalData.EDIT_TRIP_ID, this.getTripId());
        v.getContext().startActivity(intent);

        return true;
    }
}