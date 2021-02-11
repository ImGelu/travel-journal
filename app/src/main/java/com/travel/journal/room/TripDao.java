package com.travel.journal.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDao {
    @Query("SELECT * FROM Trip")
    List<Trip> getTrips();

    @Query("SELECT * FROM Trip WHERE user_id = :user_id")
    List<Trip> getUserTrips(int user_id);

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}