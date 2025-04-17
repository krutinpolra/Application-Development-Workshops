package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.Enums.RoomType;
import com.seneca.apd.finalproject.hotelreservation.Models.Room;
import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomSuggestion {


    @FXML
    private VBox roomContainer;
    @FXML
    private Label titleLabel;

    private List<Room> suggestedRooms;

    @FXML
    public void initialize() {
        if (AppState.isAdmin()) {
            titleLabel.setText("All Rooms Overview – Booked & Available");
        } else {
            titleLabel.setText("Based on your group size, here are our best picks for you!");
        }
        loadSuggestedRooms();
        displayRooms();
    }


    private void loadSuggestedRooms() {
        int guestCount = AppState.getTotalGuests();  // e.g., 3 (2 adults + 1 child)
        suggestedRooms = new ArrayList<>();

        String query;
        if (AppState.isAdmin()) {
            query = "SELECT * FROM Room";  // Admin sees everything
        } else {
            query = "SELECT * FROM Room WHERE numberOfBeds * 2 >= ? AND status = 'available'";
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (!AppState.isAdmin()) {
                stmt.setInt(1, guestCount);  // Only set param if not admin
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("roomID"),
                        RoomType.valueOf(rs.getString("room_type").toUpperCase()),
                        rs.getInt("numberOfBeds"),
                        rs.getFloat("price"),
                        rs.getString("status"),
                        rs.getString("image_url")
                );
                suggestedRooms.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayRooms() {
        roomContainer.getChildren().clear();

        HBox row = new HBox(30);
        row.setStyle("-fx-alignment: CENTER;");

        int count = 0;
        for (Room room : suggestedRooms) {
            VBox card = createRoomCard(room);
            row.getChildren().add(card);
            count++;

            // Add the row to the VBox after 3 cards, then reset row
            if (count % 3 == 0) {
                roomContainer.getChildren().add(row);
                row = new HBox(30);
                row.setStyle("-fx-alignment: CENTER;");
            }
        }

        // Add remaining cards if they don’t fill up a full row
        if (!row.getChildren().isEmpty()) {
            roomContainer.getChildren().add(row);
        }
    }


    private VBox createRoomCard(Room room) {
        VBox card = new VBox();
        card.getStyleClass().add("guest-card-kiosk");
        card.setSpacing(10);

        ImageView img;

        try {
            String imageName = room.getImageUrl();  // e.g., "SingleRoom1.jpg"
            String imagePath = "/com/seneca/apd/finalproject/hotelreservation/assets/images/" + imageName;
            img = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        } catch (Exception e) {
            System.out.println("⚠️ Image not found for room " + room.getRoomID() + ", using default.");
            img = new ImageView(new Image(getClass().getResource("/com/seneca/apd/finalproject/hotelreservation/assets/images/default-room.jpg").toExternalForm()));
        }

        img.setFitHeight(250);
        img.setFitWidth(350);

        Label title = new Label("Room Type: " + room.getRoomType());
        title.getStyleClass().add("card-heading-kiosk");

        Label status = new Label("Status: " + capitalize(room.getStatus()));
        Label cap = new Label("Capacity: up to " + room.getCapacity() + " Guests");
        Label price = new Label("Price: $" + room.getPrice() + "/night");

        status.getStyleClass().add("small-label-kiosk");
        cap.getStyleClass().add("small-label-kiosk");
        price.getStyleClass().add("small-label-kiosk");

        Button select = new Button();
        select.getStyleClass().add("bottom-btn-kiosk");

        if (room.getStatus().equalsIgnoreCase("available")) {
            select.setText("Select");
            select.setOnAction(e -> handleRoomSelection(room));
            card.getChildren().addAll(img, title, status, cap, price, select);
        } else {
            if (AppState.isAdmin()) {
                select.setText("Booked");
                select.setDisable(true);
                card.getChildren().addAll(img, title, status, cap, price, select);
            } else {
                // Guest shouldn't even see booked rooms, but just in case
                card.getChildren().addAll(img, title, status, cap, price);
            }
        }

        return card;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return "";
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


    private void handleRoomSelection(Room room) {
        System.out.println("Selected Room ID: " + room.getRoomID());
        AppState.setSelectedRoom(room);

        Stage stage = (Stage) roomContainer.getScene().getWindow();
        if (AppState.isUpdateMode()) {
            SceneNavigator.switchTo(stage,
                    "/com/seneca/apd/finalproject/hotelreservation/UpdateBooking.fxml",
                    "Update Booking");
        } else {
            SceneNavigator.switchTo(stage,
                    "/com/seneca/apd/finalproject/hotelreservation/GuestDetailForm.fxml",
                    "Guest Info");
        }
    }


    @FXML
    void handleMenu(ActionEvent event) {
        Stage stage = (Stage) roomContainer.getScene().getWindow();

        if (AppState.isAdmin()) {
            SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml", "Admin Dashboard");
        } else {
            SceneNavigator.switchTo(stage, "/com/seneca/apd/finalproject/hotelreservation/LogIn.fxml", "Login Screen");
        }
    }


    @FXML
    void handlePrevious(ActionEvent event) {
        SceneNavigator.switchTo((Stage) roomContainer.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/Duration.fxml", "Select Duration");
    }
}
