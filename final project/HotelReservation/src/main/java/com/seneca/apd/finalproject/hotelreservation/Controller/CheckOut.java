package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.DataBase.HotelDatabase;
import com.seneca.apd.finalproject.hotelreservation.Models.Guest;
import com.seneca.apd.finalproject.hotelreservation.Models.Room;
import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CheckOut {

    @FXML private TextField taxField;
    @FXML private TextField discountField;
    @FXML private TextField finalPriceField;
    @FXML private TextField summaryCapacity;
    @FXML private TextField summaryCheckIn;
    @FXML private TextField summaryCheckOut;
    @FXML private TextField summaryDuration;
    @FXML private TextField summaryEmail;
    @FXML private TextField summaryName;
    @FXML private TextField summaryPhone;
    @FXML private TextField summaryPrice;
    @FXML private TextField summaryRoomType;
    @FXML private TextField totalPriceField;
    @FXML private ComboBox<String> paymentTypeCombo;

    private int bookingID;
    private float total;

    @FXML
    public void initialize() {
        Guest guest = AppState.getSelectedGuest();
        Room room = AppState.getSelectedRoom();

        if (guest == null || room == null) {
            System.out.println("⚠️ Missing guest or room data for checkout.");
            return;
        }

        LocalDate checkIn = AppState.getCheckInDate();
        LocalDate checkOut = AppState.getCheckOutDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

        summaryName.setText(guest.getGuestDetails().get("Name").toString());
        summaryEmail.setText(guest.getGuestDetails().get("Email").toString());
        summaryPhone.setText(guest.getGuestDetails().get("Phone").toString());

        summaryRoomType.setText(room.getRoomType().name() + " Room");
        summaryCapacity.setText(String.valueOf(room.getCapacity()));
        summaryPrice.setText(String.format("$%.2f/night", room.getPrice()));

        summaryCheckIn.setText(checkIn.format(formatter));
        summaryCheckOut.setText(checkOut.format(formatter));

        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        days = Math.max(1, days);

        summaryDuration.setText(days + " Day" + (days > 1 ? "s" : ""));

        total = room.getPrice() * days;
        totalPriceField.setText(String.format("$%.2f", total));

        discountField.textProperty().addListener((obs, oldVal, newVal) -> handleCalculation(null));
        paymentTypeCombo.setOnAction(this::handleCalculation);

        fetchBookingID();
    }

    private void fetchBookingID() {
        Guest guest = AppState.getSelectedGuest();
        Room room = AppState.getSelectedRoom();

        String sql = "SELECT bookingID FROM Reservation WHERE guestID = ? AND roomID = ? AND status = 'booked'";
        try (Connection conn = HotelDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(guest.getGuestDetails().get("ID").toString()));
            stmt.setInt(2, room.getRoomID());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bookingID = rs.getInt("bookingID");
                System.out.println("✅ Booking ID found: " + bookingID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCalculation(ActionEvent event) {
        float discount = 0f;
        try {
            if (!discountField.getText().isBlank()) {
                discount = Float.parseFloat(discountField.getText());
            }
        } catch (Exception e) {
            showAlert("Invalid discount entered. Please enter a number.");
            return;
        }

        float discountedAmount = total * (discount / 100);
        float subtotal = total - discountedAmount;
        float tax = subtotal * 0.13f;
        float finalTotal = subtotal + tax;

        taxField.setText(String.format("$%.2f", tax));
        finalPriceField.setText(String.format("$%.2f", finalTotal));
    }


    @FXML
    void handleConfirm(ActionEvent event) {
        // Validate payment type
        if (paymentTypeCombo.getValue() == null) {
            showAlert("Please select a payment method.");
            return;
        }
        handleCalculation(null);
        float discount = 0f;
        try {
            if (!discountField.getText().isBlank()) {
                discount = Float.parseFloat(discountField.getText());
            }
        } catch (Exception e) {
            showAlert("Invalid discount entered. Please enter a number.");
            return;
        }

        float discountedAmount = total * (discount / 100);
        float subtotal = total - discountedAmount;
        float tax = subtotal * 0.13f;
        float finalTotal = subtotal + tax;

        taxField.setText(String.format("$%.2f", tax));
        finalPriceField.setText(String.format("$%.2f", finalTotal));

        try (Connection conn = HotelDatabase.connect()) {
            String insertBilling = "INSERT INTO Billing (bookingID, amount, tax, discount, totalAmount) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertBilling);
            ps.setInt(1, bookingID);
            ps.setFloat(2, total);
            ps.setFloat(3, tax);
            ps.setFloat(4, discount);
            ps.setFloat(5, finalTotal);
            ps.executeUpdate();

            String updateReservation = "UPDATE Reservation SET status = 'checked_out' WHERE bookingID = ?";
            PreparedStatement psReservation = conn.prepareStatement(updateReservation);
            psReservation.setInt(1, bookingID);
            psReservation.executeUpdate();

            String updateRoom = "UPDATE Room SET status = 'available' WHERE roomID = ?";
            PreparedStatement psRoom = conn.prepareStatement(updateRoom);
            psRoom.setInt(1, AppState.getSelectedRoom().getRoomID());
            psRoom.executeUpdate();

            System.out.println("✅ Guest checked out and billing recorded.");
            SceneNavigator.switchTo((Stage) summaryName.getScene().getWindow(),
                    "/com/seneca/apd/finalproject/hotelreservation/FeedBackScreen.fxml", "Feedback");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showConfirmAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML void goToGuestDetails(MouseEvent event) {}
    @FXML void goToRoomSelection(MouseEvent event) {}
    @FXML void goToStayDuration(MouseEvent event) {}

    @FXML
    void handlePrevious(ActionEvent event) {
        SceneNavigator.switchTo((Stage) summaryName.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/ListOfGuests.fxml", "Guest List");
    }

    @FXML
    void handleMenu(ActionEvent event) {
        SceneNavigator.switchTo((Stage) summaryName.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml", "Admin Dashboard");
    }
}
