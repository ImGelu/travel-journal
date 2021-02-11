package com.travel.journal.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Trip.class, User.class}, version = 1, exportSchema = false)
public abstract class TripDataBase extends RoomDatabase {
    public abstract TripDao getTripDao();
}