package com.seneca.apd.finalproject.hotelreservation.Models;

import com.seneca.apd.finalproject.hotelreservation.InterFaces.IKiosk;

public class Kiosk implements IKiosk {
    private int kioskID;
    private String location;

    public Kiosk(int kioskID, String location) {
        this.kioskID = kioskID;
        this.location = location;
    }

    @Override
    public void displayWelcomeMessage() {
        System.out.println("Welcome to our hotel kiosk!");
    }

    @Override
    public void guideBookingProcess() {
        System.out.println("Please follow the steps on the screen to complete your booking.");
    }

    @Override
    public boolean validateInput() {
        return true;
    }

    @Override
    public boolean confirmBooking() {
        return true;
    }
}
