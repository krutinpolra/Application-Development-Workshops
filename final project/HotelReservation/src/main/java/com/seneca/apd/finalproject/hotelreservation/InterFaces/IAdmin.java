package com.seneca.apd.finalproject.hotelreservation.InterFaces;

import java.util.Map;

public interface IAdmin {
    boolean login();
    Map<String, String> searchGuest();
    boolean checkOutGuest();
    Map<String, String> generateReport();
}
