package com.seneca.apd.finalproject.hotelreservation.Models;

import com.seneca.apd.finalproject.hotelreservation.Enums.RoomType;
import com.seneca.apd.finalproject.hotelreservation.InterFaces.IRoom;

import java.util.Map;

public class Room implements IRoom {
    private int roomID;
    private RoomType roomType;
    private int numberOfBeds;
    private float price;
    private String status;
    private String imageUrl; // optional, for UI display

    public Room(int roomID, RoomType roomType, int numberOfBeds, float price, String status, String imageUrl) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    @Override
    public Map<String, String> getRoomDetails() {
        return Map.of(
                "RoomID", String.valueOf(roomID),
                "Type", roomType.name(),
                "Beds", String.valueOf(numberOfBeds),
                "Capacity", String.valueOf(getCapacity()),
                "Price", String.valueOf(price),
                "Status", status,
                "ImageUrl", imageUrl
        );
    }

    @Override
    public void setRoomDetails(Map<String, String> details) {
        if (details.containsKey("Status")) {
            this.status = details.get("Status");
        }
    }

    @Override
    public boolean checkRoomAvailability() {
        return "available".equalsIgnoreCase(status);
    }

    // Additional Getters

    public int getRoomID() {
        return roomID;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public int getCapacity() {
        return numberOfBeds * 2; // Assuming 1 bed = 2 guests
    }

    public float getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return checkRoomAvailability();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
