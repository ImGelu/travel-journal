package com.travel.journal;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private final TextView textViewName, textViewDestination;
    private final MaterialCardView materialCardView;
    private long tripId;

    public TripViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewName = itemView.findViewById(R.id.trip_name);
        textViewDestination = itemView.findViewById(R.id.trip_destination);
        materialCardView = itemView.findViewById(R.id.card);

        materialCardView.setOnClickListener(this);
        materialCardView.setOnLongClickListener(this);
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public TextView getTextViewName() {
        return textViewName;
    }

    public TextView getTextViewDestination() {
        return textViewDestination;
    }

    @Override
    public void onClick(View v) {
        Snackbar.make(v, String.valueOf(tripId), BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        Snackbar.make(v, "LongClicked!", BaseTransientBottomBar.LENGTH_SHORT).show();
        return true;
    }
}