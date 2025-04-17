package com.seneca.apd.finalproject.hotelreservation.Models;

import com.seneca.apd.finalproject.hotelreservation.InterFaces.IBilling;

import java.util.Map;

public class Billing implements IBilling {
    private int billID;
    private int reservationID;
    private float amount;
    private float tax;
    private float totalAmount;
    private float discount;

    public Billing(int billID, int reservationID, float amount, float tax, float discount) {
        this.billID = billID;
        this.reservationID = reservationID;
        this.amount = amount;
        this.tax = tax;
        this.discount = discount;
        this.totalAmount = calculateTotal();
    }

    @Override
    public Map<String, String> generateBill() {
        return Map.of(
                "BillID", String.valueOf(billID),
                "ReservationID", String.valueOf(reservationID),
                "Amount", String.valueOf(amount),
                "Tax", String.valueOf(tax),
                "Discount", String.valueOf(discount),
                "Total", String.valueOf(totalAmount)
        );
    }

    @Override
    public boolean applyDiscount() {
        this.totalAmount -= discount;
        return true;
    }

    @Override
    public float calculateTotal() {
        return amount + tax - discount;
    }

    @Override
    public void printBill() {
        System.out.println(generateBill());
    }
}
