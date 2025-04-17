package com.seneca.apd.finalproject.hotelreservation.InterFaces;

import java.util.Map;

public interface IRoom {
    Map<String, String> getRoomDetails();
    void setRoomDetails(Map<String, String> details);
    boolean checkRoomAvailability();
}