package com.seneca.apd.finalproject.hotelreservation.InterFaces;

import java.util.Map;

public interface IGuest {
    Map<String, Object> getGuestDetails();
    void setGuestDetails(Map<String, String> details);
    boolean validateGuestDetails();
}