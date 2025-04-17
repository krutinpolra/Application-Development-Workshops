package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.DataBase.HotelDatabase;
import com.seneca.apd.finalproject.hotelreservation.Models.Guest;
import com.seneca.apd.finalproject.hotelreservation.Models.Room;
import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BookingSummary {

    @FXML private TextField summaryCapacity;
    @FXML private TextField summaryCheckIn;
    @FXML private TextField summaryCheckOut;
    @FXML private TextField summaryDuration;
    @FXML private TextField summaryEmail;
    @FXML private TextField summaryName;
    @FXML private TextField summaryPhone;
    @FXML private TextField summaryPrice;
    @FXML private TextField summaryRoomType;
    @FXML private TextField summaryTotal;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    @FXML
    public void initialize() {
        Guest guest = AppState.getSelectedGuest();
        Room room = AppState.getSelectedRoom();
        LocalDate checkIn = AppState.getCheckInDate();
        LocalDate checkOut = AppState.getCheckOutDate();

        if (guest != null) {
            summaryName.setText(safe(guest.getGuestDetails().get("Name")));
            summaryEmail.setText(safe(guest.getGuestDetails().get("Email")));
            summaryPhone.setText(safe(guest.getGuestDetails().get("Phone")));
        }

        if (room != null) {
            summaryRoomType.setText(room.getRoomType().name() + " Room");
            summaryCapacity.setText(String.valueOf(room.getCapacity()));
            summaryPrice.setText(String.format("$%.2f/night", room.getPrice()));
        }

        if (checkIn != null && checkOut != null) {
            summaryCheckIn.setText(checkIn.format(formatter));
            summaryCheckOut.setText(checkOut.format(formatter));

            long days = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (days <= 0) days = 1; // Minimum stay 1 day
            summaryDuration.setText(days + " Day" + (days > 1 ? "s" : ""));

            if (room != null) {
                float total = room.getPrice() * days;
                summaryTotal.setText(String.format("$%.2f", total));
            }
        } else {
            summaryCheckIn.setText("N/A");
            summaryCheckOut.setText("N/A");
            summaryDuration.setText("N/A");
            summaryTotal.setText("N/A");
        }
    }

    private String safe(Object obj) {
        return obj != null ? obj.toString() : "N/A";
    }

    @FXML
    void handleConfirm(ActionEvent event) {
        Guest guest = AppState.getSelectedGuest();
        Room room = AppState.getSelectedRoom();
        LocalDate checkIn = AppState.getCheckInDate();
        LocalDate checkOut = AppState.getCheckOutDate();

        if (guest == null || room == null || checkIn == null || checkOut == null) {
            System.out.println("❌ Missing data. Cannot confirm booking.");
            return;
        }

        long duration = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (duration <= 0) duration = 1;
        float totalPrice = room.getPrice() * duration;

        try (Connection conn = HotelDatabase.connect()) {
            if (AppState.isUpdateMode() && AppState.getSelectedBookingId() != 0) {
                // ✅ UPDATE logic
                String updateSql = """
                UPDATE Reservation SET
                    guestID = ?,
                    roomID = ?,
                    check_in_date = ?,
                    check_out_date = ?,
                    totalPrice = ?,
                    status = 'booked'
                WHERE bookingID = ?
            """;
                PreparedStatement stmt = conn.prepareStatement(updateSql);
                stmt.setInt(1, guest.getGuestID());
                stmt.setInt(2, room.getRoomID());
                stmt.setString(3, checkIn.toString());
                stmt.setString(4, checkOut.toString());
                stmt.setFloat(5, totalPrice);
                stmt.setInt(6, AppState.getSelectedBookingId());
                stmt.executeUpdate();

                System.out.println("✅ Booking updated successfully.");
                AppState.setUpdateMode(false); // Reset update mode
                AppState.setSelectedBookingId(0); // Reset booking ID

            } else {
                // 🆕 INSERT new booking logic
                String insertSql = """
                INSERT INTO Reservation (guestID, roomID, check_in_date, check_out_date, totalPrice, status)
                VALUES (?, ?, ?, ?, ?, 'booked')
            """;
                PreparedStatement ps = conn.prepareStatement(insertSql);
                ps.setInt(1, guest.getGuestID());
                ps.setInt(2, room.getRoomID());
                ps.setString(3, checkIn.toString());
                ps.setString(4, checkOut.toString());
                ps.setFloat(5, totalPrice);
                ps.executeUpdate();

                System.out.println("✅ New booking saved to database.");
            }

            // ✅ Room status update
            String updateRoom = "UPDATE Room SET status = 'booked' WHERE roomID = ?";
            PreparedStatement ps2 = conn.prepareStatement(updateRoom);
            ps2.setInt(1, room.getRoomID());
            ps2.executeUpdate();

            // 🎉 Go to final screen
            Stage stage = (Stage) summaryName.getScene().getWindow();
            SceneNavigator.switchTo(stage,
                    "/com/seneca/apd/finalproject/hotelreservation/EndingScreen.fxml",
                    "Booking Complete");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handlePrevious(ActionEvent event) {
        SceneNavigator.switchTo((Stage) summaryName.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/GuestDetailForm.fxml",
                "Guest Info");
    }

    @FXML
    void handleMenu(ActionEvent event) {
        Stage stage = (Stage) summaryName.getScene().getWindow();
        if (AppState.isAdmin()) {
            SceneNavigator.switchTo(stage,
                    "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml",
                    "Admin Dashboard");
        } else {
            SceneNavigator.switchTo(stage,
                    "/com/seneca/apd/finalproject/hotelreservation/LogIn.fxml",
                    "Login Screen");
        }
    }


    public void handleUpdate(MouseEvent mouseEvent) {
        Stage stage = (Stage) summaryName.getScene().getWindow();
        SceneNavigator.switchTo(stage,
                "/com/seneca/apd/finalproject/hotelreservation/UpdateBooking.fxml",
                "Update Booking");
    }
}
