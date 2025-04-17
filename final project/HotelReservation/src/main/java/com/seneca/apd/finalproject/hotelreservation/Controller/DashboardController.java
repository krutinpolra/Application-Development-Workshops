package com.seneca.apd.finalproject.hotelreservation.Controller;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.*;

public class DashboardController {

    @FXML
    private Button availableRoomsButton, bookRoomButton, cancelBookingButton,
            checkOutButton, searchGuestButton, updateBookingButton;

    private static final Logger logger = Logger.getLogger(DashboardController.class.getName());

    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize() {
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                rootPane.prefWidthProperty().bind(newScene.widthProperty());
                rootPane.prefHeightProperty().bind(newScene.heightProperty());
            }
        });
    }


    static {
        try {
            FileHandler fileHandler = new FileHandler("admin_logs.%g.log", 1024 * 1024, 5, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Logger setup failed", e);
        }
    }

    @FXML
    void handleAvailableRooms(ActionEvent event) {
        switchScreen("/com/seneca/apd/finalproject/hotelreservation/RoomSuggestion.fxml", "Available Rooms");
    }

    @FXML
    void handleBookRoom(ActionEvent event) {
        switchScreen("/com/seneca/apd/finalproject/hotelreservation/NumberOfGuest.fxml", "Book Room");
    }

    @FXML
    void handleCancelBooking(ActionEvent event) {
        switchScreen("/com/seneca/apd/finalproject/hotelreservation/CancelBooking.fxml", "Cancel Booking");
    }

    @FXML
    void handleCheckOut(ActionEvent event) {
        switchScreen("/com/seneca/apd/finalproject/hotelreservation/ListOfGuests.fxml", "Check Out Guest");
    }

    @FXML
    void handleSearchGuest(ActionEvent event) {
        switchScreen("/com/seneca/apd/finalproject/hotelreservation/ListOfGuests.fxml", "Search Guest");
    }

    @FXML
    void handleUpdateBooking(ActionEvent event) {
        switchScreen("/com/seneca/apd/finalproject/hotelreservation/ListOfBookings.fxml", "Update Booking");
    }

    private void switchScreen(String fxml, String title) {
        SceneNavigator.switchTo((Stage) bookRoomButton.getScene().getWindow(), fxml, title);
        logger.info("Admin navigated to: " + title);
    }

    public void handleMenu(ActionEvent actionEvent) {
        switchScreen("/com/seneca/apd/finalproject/hotelreservation/LogIn.fxml", "Login Page");
    }
}
