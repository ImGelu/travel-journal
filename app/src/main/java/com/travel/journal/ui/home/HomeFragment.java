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
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.travel.journal.GlobalData;
import com.travel.journal.R;
import com.travel.journal.room.Trip;
import com.travel.journal.TripAdapter;
import com.travel.journal.room.TripDao;
import com.travel.journal.room.TripDataBase;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private List<Trip> trips;
    private RecyclerView recyclerViewTrips;

    private TripDataBase tripDataBase;
    private TripDao tripDao;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tripDataBase = Room.databaseBuilder(view.getContext(), TripDataBase.class, GlobalData.TRIPS_DB_NAME).allowMainThreadQueries().build();
        tripDao = tripDataBase.getTripDao();

        trips = tripDao.getUserTrips(GlobalData.getLoggedInUser().getId());

        recyclerViewTrips = getView().findViewById(R.id.recycler_view_trips);
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(getView().getContext()));

        recyclerViewTrips.setAdapter(new TripAdapter(trips));
    }
}