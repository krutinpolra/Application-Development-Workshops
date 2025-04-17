package com.seneca.apd.finalproject.hotelreservation.Models;

import com.seneca.apd.finalproject.hotelreservation.InterFaces.IReservation;

import java.util.Date;
import java.util.Map;

public class Reservation implements IReservation {
    private int reservationID;
    private int guestID;
    private int roomID;
    private Date checkInDate;
    private Date checkOutDate;
    private int numberOfGuests;
    private String status;

    public Reservation(int reservationID, int guestID, int roomID, Date checkInDate, Date checkOutDate, int numberOfGuests, String status) {
        this.reservationID = reservationID;
        this.guestID = guestID;
        this.roomID = roomID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
    }

    @Override
    public boolean createReservation() {
        return true;
    }

    @Override
    public boolean cancelReservation() {
        this.status = "cancelled";
        return true;
    }

    @Override
    public Map<String, String> getReservationDetails() {
        return Map.of(
                "ReservationID", String.valueOf(reservationID),
                "GuestID", String.valueOf(guestID),
                "RoomID", String.valueOf(roomID),
                "CheckIn", checkInDate.toString(),
                "CheckOut", checkOutDate.toString(),
                "Guests", String.valueOf(numberOfGuests),
                "Status", status
        );
    }

    @Override
    public boolean confirmReservation() {
        this.status = "confirmed";
        return true;
    }
}
