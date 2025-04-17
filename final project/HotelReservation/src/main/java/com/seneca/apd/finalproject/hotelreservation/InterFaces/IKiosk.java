package com.seneca.apd.finalproject.hotelreservation.InterFaces;

public interface IKiosk {
    void displayWelcomeMessage();
    void guideBookingProcess();
    boolean validateInput();
    boolean confirmBooking();
}
