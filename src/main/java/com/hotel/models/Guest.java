package com.hotel.models;

public class Guest {
    private int id;
    private String name;
    private String roomNumber;
    private String checkInDate;

    public Guest(String name, String roomNumber) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.checkInDate = "Today";  // For simplicity
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getCheckInDate() {
        return checkInDate;
    }
}
