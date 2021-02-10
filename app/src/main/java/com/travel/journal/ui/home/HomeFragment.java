package com.travel.journal.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.journal.R;
import com.travel.journal.Trip;
import com.travel.journal.TripAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private List<Trip> trips;
    private RecyclerView recyclerViewTrips;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trips = new ArrayList<>();
        Trip trip = null;
        for (int i = 0; i < 20; i++) {
            trip = new Trip("Name" + i, "Destination" + i, 0, 3 * i, "DateStart" + i, "DateEnd" + i, i);
            trips.add(trip);
        }

        recyclerViewTrips = getView().findViewById(R.id.recycler_view_trips);
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(getView().getContext()));

        recyclerViewTrips.setAdapter(new TripAdapter(trips));
    }
}