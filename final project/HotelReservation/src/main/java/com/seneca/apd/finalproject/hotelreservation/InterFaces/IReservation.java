package com.seneca.apd.finalproject.hotelreservation.InterFaces;

import java.util.Map;

public interface IReservation {
    boolean createReservation();
    boolean cancelReservation();
    Map<String, String> getReservationDetails();
    boolean confirmReservation();
}
