package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.DataBase.HotelDatabase;
import com.seneca.apd.finalproject.hotelreservation.Enums.RoomType;
import com.seneca.apd.finalproject.hotelreservation.Models.Guest;
import com.seneca.apd.finalproject.hotelreservation.Models.Room;
import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ListOfGuests {

    @FXML private TableView<ReservationView> guestTable;
    @FXML private TableColumn<ReservationView, String> nameColumn;
    @FXML private TableColumn<ReservationView, String> phoneColumn;
    @FXML private TableColumn<ReservationView, String> bookingIdColumn;
    @FXML private TableColumn<ReservationView, String> roomTypeColumn;
    @FXML private TableColumn<ReservationView, String> checkInColumn;
    @FXML private TableColumn<ReservationView, String> checkOutColumn;
    @FXML private TableColumn<ReservationView, String> totalPriceColumn;
    @FXML private TableColumn<ReservationView, String> statusColumn;
    @FXML private TextField searchField;

    private ObservableList<ReservationView> allReservations = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadGuestData();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterGuests(newVal));
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
        bookingIdColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getBookingId())));
        roomTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoomType()));
        checkInColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckIn()));
        checkOutColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckOut()));
        totalPriceColumn.setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getTotalPrice())));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

    }

    private void loadGuestData() {
        allReservations.clear();
        try (Connection conn = HotelDatabase.connect()) {
            String query = """
                SELECT r.bookingID, g.guestID, rm.roomID, g.name, g.phone, rm.room_type,
                       r.check_in_date, r.check_out_date, r.totalPrice, r.status
                FROM Reservation r
                JOIN Guest g ON r.guestID = g.guestID
                JOIN Room rm ON r.roomID = rm.roomID
            """;

            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                ReservationView res = new ReservationView(
                        rs.getInt("bookingID"),
                        rs.getInt("guestID"),
                        rs.getInt("roomID"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("room_type"),
                        rs.getString("check_in_date"),
                        rs.getString("check_out_date"),
                        rs.getFloat("totalPrice"),
                        rs.getString("status")
                );
                allReservations.add(res);
            }

            guestTable.setItems(allReservations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterGuests(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            guestTable.setItems(allReservations);
            return;
        }

        ObservableList<ReservationView> filtered = FXCollections.observableArrayList();
        for (ReservationView res : allReservations) {
            if (res.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    res.getPhone().contains(keyword)) {
                filtered.add(res);
            }
        }
        guestTable.setItems(filtered);
    }

    @FXML
    void handleMenu(ActionEvent event) {
        SceneNavigator.switchTo((Stage) guestTable.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml",
                "Admin Dashboard");
    }

    @FXML
    void handleCheckOut(ActionEvent event) {
        ReservationView selected = guestTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Save selected guest, room, and dates to AppState for CheckOut screen
            AppState.setSelectedGuest(new Guest(
                    selected.getGuestId(), selected.getName(), selected.getPhone(), "guest@email.com", "address", null));
            AppState.setSelectedRoom(AppState.getRoomById(selected.getRoomId()));
            AppState.setCheckInDate(LocalDate.parse(selected.getCheckIn()));
            AppState.setCheckOutDate(LocalDate.parse(selected.getCheckOut()));

            SceneNavigator.switchTo((Stage) guestTable.getScene().getWindow(),
                    "/com/seneca/apd/finalproject/hotelreservation/CheckOut.fxml",
                    "Check Out");
        } else {
            showWarning("Please select a reservation to check out.");
        }
    }



    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class ReservationView {
        private final int bookingId, guestId, roomId;
        private final String name, phone, roomType, checkIn, checkOut, status;
        private final float totalPrice;

        public ReservationView(int bookingId, int guestId, int roomId, String name, String phone,
                               String roomType, String checkIn, String checkOut, float totalPrice, String status) {
            this.bookingId = bookingId;
            this.guestId = guestId;
            this.roomId = roomId;
            this.name = name;
            this.phone = phone;
            this.roomType = roomType;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.totalPrice = totalPrice;
            this.status = status;
        }

        public int getBookingId() { return bookingId; }
        public int getGuestId() { return guestId; }
        public int getRoomId() { return roomId; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getRoomType() { return roomType; }
        public String getCheckIn() { return checkIn; }
        public String getCheckOut() { return checkOut; }
        public float getTotalPrice() { return totalPrice; }
        public String getStatus() { return status; }
    }

}
