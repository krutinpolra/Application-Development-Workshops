package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.DataBase.HotelDatabase;
import com.seneca.apd.finalproject.hotelreservation.Models.Guest;
import com.seneca.apd.finalproject.hotelreservation.Models.Room;
import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class UpdateBooking {

    @FXML private TextField summaryCapacity;
    @FXML private TextField summaryEmail;
    @FXML private TextField summaryName;
    @FXML private TextField summaryPhone;
    @FXML private TextField summaryPrice;
    @FXML private TextField summaryRoomType;
    @FXML private TextField summaryTotal;

    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    @FXML
    public void initialize() {
        Guest guest = AppState.getSelectedGuest();
        Room room = AppState.getSelectedRoom();
        LocalDate checkIn = AppState.getCheckInDate();
        LocalDate checkOut = AppState.getCheckOutDate();

        if (guest != null) {
            summaryName.setText(guest.getGuestDetails().get("Name").toString());
            summaryEmail.setText(guest.getGuestDetails().get("Email").toString());
            summaryPhone.setText(guest.getGuestDetails().get("Phone").toString());
        }

        if (room != null) {
            summaryRoomType.setText(room.getRoomType().name() + " Room");
            summaryCapacity.setText(String.valueOf(room.getCapacity()));
            summaryPrice.setText(String.format("$%.2f/night", room.getPrice()));
        }

        if (checkIn != null && checkOut != null) {
            checkInPicker.setValue(checkIn);
            checkOutPicker.setValue(checkOut);
            long days = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (days <= 0) days = 1;
            float total = room.getPrice() * days;
            summaryTotal.setText(String.format("$%.2f", total));
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        try (Connection conn = HotelDatabase.connect()) {
            int bookingId = AppState.getSelectedBookingId();
            Guest guest = AppState.getSelectedGuest();
            Room room = AppState.getSelectedRoom();
            LocalDate checkIn = checkInPicker.getValue();
            LocalDate checkOut = checkOutPicker.getValue();

            // Also update AppState values
            AppState.setCheckInDate(checkIn);
            AppState.setCheckOutDate(checkOut);

            if (bookingId <= 0 || guest == null || room == null || checkIn == null || checkOut == null) {
                System.out.println("❌ Missing data. Please make sure all selections are made.");
                return;
            }

            // Update Guest
            String updateGuest = "UPDATE Guest SET name = ?, email = ?, phone = ? WHERE guestID = ?";
            PreparedStatement ps = conn.prepareStatement(updateGuest);
            ps.setString(1, summaryName.getText());
            ps.setString(2, summaryEmail.getText());
            ps.setString(3, summaryPhone.getText());
            ps.setInt(4, AppState.getSelectedGuest().getGuestID());
            ps.executeUpdate();

            long duration = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (duration <= 0) duration = 1;
            float totalPrice = AppState.getSelectedRoom().getPrice() * duration;

            // Update Reservation
            String updateReservation = """
                UPDATE Reservation SET
                    check_in_date = ?,
                    check_out_date = ?,
                    totalPrice = ?,
                    guestID = ?,
                    roomID = ?
                WHERE bookingID = ?
            """;
            PreparedStatement ps2 = conn.prepareStatement(updateReservation);
            ps2.setString(1, checkIn.toString());
            ps2.setString(2, checkOut.toString());
            ps2.setFloat(3, totalPrice);
            ps2.setInt(4, AppState.getSelectedGuest().getGuestID());
            ps2.setInt(5, AppState.getSelectedRoom().getRoomID());
            ps2.setInt(6, bookingId);
            ps2.executeUpdate();

            System.out.println("✅ Booking updated successfully");

            AppState.setUpdateMode(false);

            Stage stage = (Stage) summaryName.getScene().getWindow();
            SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/EndingScreen.fxml", "Booking Updated");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Failed to update booking.");
        }
    }

    @FXML
    void handleMenu(ActionEvent event) {
        SceneNavigator.switchTo(
                (Stage) summaryName.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml",
                "Dashboard");
    }

    @FXML
    void handlePrevious(ActionEvent event) {
        SceneNavigator.switchTo(
                (Stage) summaryName.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/ListOfGuests.fxml",
                "Guest List");
    }

    @FXML
    void goToRoomSelection(MouseEvent event) {
        AppState.setUpdateMode(true);
        SceneNavigator.switchTo(
                (Stage) summaryName.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/RoomSuggestion.fxml",
                "Select New Room"
        );
    }
}
