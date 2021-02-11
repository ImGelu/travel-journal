package com.travel.journal.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import androidx.room.Entity;

import java.io.Serializable;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE))
public class Trip implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "user_id", index = true)
    private int userId;
    private String name;
    private String destination;
    private int type; // 0 - City Break, 1 - Sea Side, 2 - Mountains
    private double price;
    private String startDate;
    private String endDate;
    private double rating;

    public Trip(int userId, String name, String destination, int type, double price, String startDate, String endDate, double rating) {
        this.userId = userId;
        this.name = name;
        this.destination = destination;
        this.type = type;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", user_id=" + userId +
                ", name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", rating=" + rating +
                '}';
    }
}
