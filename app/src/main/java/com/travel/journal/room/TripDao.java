package com.travel.journal.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDao {
    @Query("SELECT * FROM Trip WHERE id = :tripId")
    Trip getTrip(long tripId);

    @Query("SELECT * FROM Trip WHERE user_id = :userId")
    List<Trip> getUserTrips(long userId);

    @Insert
    long insert(Trip trip);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Trip trip);

    @Query("UPDATE Trip SET is_favorite = 1 WHERE id = :tripId")
    void setFavorite(long tripId);

    @Query("UPDATE Trip SET is_favorite = 0 WHERE id = :tripId")
    void removeFavorite(long tripId);

    @Delete
    void delete(Trip trip);
}