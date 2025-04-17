package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.DataBase.HotelDatabase;
import com.seneca.apd.finalproject.hotelreservation.Models.Guest;
import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuestDetailController {

    @FXML
    private TextField addressField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    void handleMenu(ActionEvent event) {
        Stage stage = (Stage) nameField.getScene().getWindow();
        if (AppState.isAdmin()) {
            SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml", "Admin Dashboard");
        } else {
            SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/LogIn.fxml", "Login Screen");
        }
    }

    @FXML
    void handlePrevious(ActionEvent event) {
        Stage stage = (Stage) nameField.getScene().getWindow();
        SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/RoomSuggestion.fxml", "Room Suggestion");
    }

    @FXML
    void handleNext(ActionEvent event) {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        // ✅ Field validations
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            showAlert("Missing Information", "All fields are required.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            showAlert("Invalid Phone", "Phone number should contain only digits (10-15 digits).");
            return;
        }

        Guest tempGuest = new Guest(0, name, phone, email, address, null);

        try (Connection conn = HotelDatabase.connect()) {
            if (AppState.isUpdateMode() && AppState.getSelectedGuest() != null) {
                // Update guest
                Guest existing = AppState.getSelectedGuest();
                String updateSql = "UPDATE Guest SET name = ?, phone = ?, email = ?, address = ? WHERE guestID = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, name);
                updateStmt.setString(2, phone);
                updateStmt.setString(3, email);
                updateStmt.setString(4, address);
                updateStmt.setInt(5, existing.getGuestID());
                updateStmt.executeUpdate();

                Guest updatedGuest = new Guest(existing.getGuestID(), name, phone, email, address, null);
                AppState.setSelectedGuest(updatedGuest);

                System.out.println("✅ Guest updated: " + name);

            } else {
                // Insert new guest
                String sql = "INSERT INTO Guest (name, phone, email, address) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setString(3, email);
                stmt.setString(4, address);
                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int guestId = keys.getInt(1);
                    Guest newGuest = new Guest(guestId, name, phone, email, address, null);
                    AppState.setSelectedGuest(newGuest);

                    System.out.println("✅ Guest saved: " + name + " (ID: " + guestId + ")");
                }
            }

            Stage stage = (Stage) nameField.getScene().getWindow();
            SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/BookingSummary.fxml", "Confirm Booking");

        } catch (SQLException e) {
            showAlert("Database Error", "Unable to save guest information.\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
