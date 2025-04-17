package com.seneca.apd.finalproject.hotelreservation.InterFaces;

import java.util.Map;

public interface IBilling {
    Map<String, String> generateBill();
    boolean applyDiscount();
    float calculateTotal();
    void printBill();
}
