package com.seneca.apd.finalproject.hotelreservation.Models;

import com.seneca.apd.finalproject.hotelreservation.InterFaces.IGuest;

import java.util.Map;

public class Guest implements IGuest {
    private int guestID;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String feedback;

    public Guest(int guestID, String name, String phoneNumber, String email, String address, String feedback) {
        this.guestID = guestID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.feedback = feedback;
    }

    @Override
    public Map<String, Object> getGuestDetails() {
        return Map.of(
                "ID", String.valueOf(guestID),
                "Name", name != null ? name : "",
                "Phone", phoneNumber != null ? phoneNumber : "",
                "Email", email != null ? email : "",
                "Address", address != null ? address : "",
                "Feedback", feedback != null ? feedback : ""
        );
    }


    @Override
    public void setGuestDetails(Map<String, String> details) {
        this.name = details.get("Name");
        this.phoneNumber = details.get("Phone");
        this.email = details.get("Email");
        this.address = details.get("Address");
        this.feedback = details.get("Feedback");
    }

    @Override
    public boolean validateGuestDetails() {
        return name != null && !name.isBlank() && email != null && email.contains("@");
    }

    public int getGuestID() {
        return this.guestID;
    }

}
