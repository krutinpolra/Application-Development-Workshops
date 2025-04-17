package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.util.logging.*;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton, cancelButton, guestButton;

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("system_logs.%g.log", 1024 * 1024, 10, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Logger setup failed", e);
        }
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = emailField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Admin WHERE userName=? AND password=?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                logger.info("Admin login success: " + username);
                AppState.setAdmin(true); // ✅ mark as admin
                SceneNavigator.switchTo((Stage) loginButton.getScene().getWindow(),
                        "/com/seneca/apd/finalproject/hotelreservation/Dashboard.fxml", "Admin Dashboard");
            } else {
                logger.warning("Failed admin login attempt for username: " + username);
                showAlert("Login Failed", "Invalid credentials.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during login", e);
            showAlert("Error", "Database error.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading dashboard", e);
            showAlert("Error", "Unable to load admin panel.");
        }
    }

    @FXML
    void handleGuestBooking(ActionEvent event) {
        AppState.setAdmin(false); // ✅ mark as guest
        SceneNavigator.switchTo((Stage) guestButton.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/NumberOfGuest.fxml", "Book as Guest");
    }

    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
