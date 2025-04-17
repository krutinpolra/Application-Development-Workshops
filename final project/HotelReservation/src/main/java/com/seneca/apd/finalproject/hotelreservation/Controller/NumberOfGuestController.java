package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NumberOfGuestController {

    @FXML private Label adultCountLabel;
    @FXML private Label childCountLabel;
    @FXML private Label adultWarningLabel;
    @FXML private Label childrenWarningLabel;

    private int adultCount = 2;
    private int childCount = 2;

    private final int MAX_ADULTS = 2;
    private final int MIN_ADULTS = 1;
    private final int MAX_CHILDREN = 4;
    private final int MIN_CHILDREN = 0;

    @FXML
    public void initialize() {
        adultCount = AppState.getAdultCount();
        childCount = AppState.getChildCount();
        updateLabels();
        hideWarnings();
    }

    @FXML
    void handleAdultMinus(ActionEvent event) {
        if (adultCount > MIN_ADULTS) {
            adultCount--;
        } else {
            showWarning(adultWarningLabel, "At least 1 adult is required");
        }
        updateLabels();
    }

    @FXML
    void handleAdultPlus(ActionEvent event) {
        if (adultCount < MAX_ADULTS) {
            adultCount++;
        } else {
            showWarning(adultWarningLabel, "Max 2 adults per room");
        }
        updateLabels();
    }

    @FXML
    void handleChildMinus(ActionEvent event) {
        if (childCount > MIN_CHILDREN) {
            childCount--;
        } else {
            showWarning(childrenWarningLabel, "Minimum 0 children allowed");
        }
        updateLabels();
    }

    @FXML
    void handleChildPlus(ActionEvent event) {
        if (childCount < MAX_CHILDREN) {
            childCount++;
        } else {
            showWarning(childrenWarningLabel, "Max 4 children per room");
        }
        updateLabels();
    }

    @FXML
    void handleNext(ActionEvent event) {
        if (adultCount < 1) {
            showAlert("Invalid Input", "At least one adult is required to proceed.");
            return;
        }

        AppState.setAdultCount(adultCount);
        AppState.setChildCount(childCount);

        System.out.println("✅ Proceeding → Adults: " + adultCount + ", Children: " + childCount);

        Stage stage = (Stage) adultCountLabel.getScene().getWindow();
        SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/Duration.fxml", "Select Duration");
    }

    @FXML
    void handleMenu(ActionEvent event) {
        Stage stage = (Stage) adultCountLabel.getScene().getWindow();
        if (AppState.isAdmin()) {
            SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml", "Admin Dashboard");
        } else {
            SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/LogIn.fxml", "Login Screen");
        }
    }

    @FXML
    void handleRules(ActionEvent event) {
        showAlert("Hotel Rules", """
                🛏 Max 2 adults per room
                👶 Max 4 children per room
                👨‍👩‍👧 At least 1 adult required
                🚫 No pets allowed
                🕒 Check-in after 3:00 PM
                """);
    }

    private void updateLabels() {
        adultCountLabel.setText(String.valueOf(adultCount));
        childCountLabel.setText(String.valueOf(childCount));
    }

    private void hideWarnings() {
        adultWarningLabel.setVisible(false);
        childrenWarningLabel.setVisible(false);
    }

    private void showWarning(Label label, String message) {
        label.setText(message);
        label.setVisible(true);

        FadeTransition fade = new FadeTransition(Duration.seconds(2), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> {
            label.setVisible(false);
            label.setOpacity(1.0);
        });
        fade.play();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
