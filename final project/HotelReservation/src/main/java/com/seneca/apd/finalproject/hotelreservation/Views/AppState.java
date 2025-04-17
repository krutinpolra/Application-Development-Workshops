package com.seneca.apd.finalproject.hotelreservation.Views;

import com.seneca.apd.finalproject.hotelreservation.DataBase.HotelDatabase;
import com.seneca.apd.finalproject.hotelreservation.Enums.RoomType;
import com.seneca.apd.finalproject.hotelreservation.Models.Guest;
import com.seneca.apd.finalproject.hotelreservation.Models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AppState {

    private static boolean isAdmin = false;
    private static int bookingId;
    private static boolean updateMode = false;

    private static Room selectedRoom;
    private static Guest selectedGuest;

    private static LocalDate checkInDate;
    private static LocalDate checkOutDate;

    private static int adultCount = 2;
    private static int childCount = 2;

    private static int selectedBookingId = 0;

    public static boolean isUpdateMode() {
        return updateMode;
    }

    public static void setUpdateMode(boolean mode) {
        updateMode = mode;
    }

    // ✅ Admin mode flag
    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void setAdmin(boolean value) {
        isAdmin = value;
    }

    // ✅ Room
    public static Room getSelectedRoom() {
        return selectedRoom;
    }

    public static void setSelectedRoom(Room room) {
        selectedRoom = room;
    }

    // ✅ Guest
    public static int getAdultCount() {
        return adultCount;
    }

    public static void setAdultCount(int count) {
        adultCount = count;
    }

    public static int getChildCount() {
        return childCount;
    }

    public static void setChildCount(int count) {
        childCount = count;
    }

    public static Guest getSelectedGuest() {
        return selectedGuest;
    }

    public static void setSelectedGuest(Guest guest) {
        selectedGuest = guest;
    }

    // ✅ Dates
    public static LocalDate getCheckInDate() {
        return checkInDate;
    }

    public static void setCheckInDate(LocalDate date) {
        checkInDate = date;
    }

    public static LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public static void setCheckOutDate(LocalDate date) {
        checkOutDate = date;
    }

    public static int getTotalGuests() {
        return adultCount + childCount;
    }

    public static int getBookingId() {
        return bookingId;
    }

    public static void setBookingId(int id) {
        bookingId = id;
    }

    public static Room getRoomById(int roomId) {
        String query = "SELECT * FROM Room WHERE roomID = ?";
        try (Connection conn = HotelDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Room(
                        rs.getInt("roomID"),
                        RoomType.valueOf(rs.getString("room_type").toUpperCase()),
                        rs.getInt("numberOfBeds"),
                        rs.getFloat("price"),
                        rs.getString("status"),
                        rs.getString("image_url")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getSelectedBookingId() {
        return selectedBookingId;
    }

    public static void setSelectedBookingId(int id) {
        selectedBookingId = id;
    }


}
