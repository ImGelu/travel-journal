package com.travel.journal.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE email = :mail AND password = :password")
    User getUser(String mail, String password);

    @Query("SELECT * FROM User WHERE email = :mail")
    User getUserByEmail(String mail);

    @Insert
    long insert(User user);
}