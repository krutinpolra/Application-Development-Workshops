package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;

public class DurationController {

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    @FXML
    private Label durationWarningLabel;

    @FXML
    void handleMenu(ActionEvent event) {
        Stage stage = (Stage) checkInDatePicker.getScene().getWindow();
        if (AppState.isAdmin()) {
            SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml", "Admin Dashboard");
        } else {
            SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/LogIn.fxml", "Login Screen");
        }
    }

    @FXML
    void handlePrevious(ActionEvent event) {
        Stage stage = (Stage) checkInDatePicker.getScene().getWindow();
        SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/NumberOfGuest.fxml", "Guest Selection");
    }

    @FXML
    void handleNext(ActionEvent event) {
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();

        if (checkInDate == null || checkOutDate == null) {
            showAlert("Missing Date", "Please select both check-in and check-out dates.");
            return;
        }

        if (checkOutDate.isBefore(checkInDate)) {
            showAlert("Invalid Duration", "Check-out date must be after check-in date.");
            return;
        }

        if (checkOutDate.equals(checkInDate)) {
            showAlert("Invalid Duration", "Check-in and check-out date cannot be the same.");
            return;
        }

        AppState.setCheckInDate(checkInDate);
        AppState.setCheckOutDate(checkOutDate);

        System.out.println("✅ Duration confirmed → Check-in: " + checkInDate + " | Check-out: " + checkOutDate);

        SceneNavigator.switchTo((Stage) checkInDatePicker.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/RoomSuggestion.fxml", "Suggested Rooms");
    }




    private void showWarning(String message) {
        durationWarningLabel.setText(message);
        durationWarningLabel.setVisible(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
