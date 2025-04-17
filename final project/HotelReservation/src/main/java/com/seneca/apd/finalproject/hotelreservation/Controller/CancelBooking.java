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
import java.sql.SQLException;
import java.time.LocalDate;

public class CancelBooking {

    @FXML private TableColumn<BookingView, String> bookingIdColumn;
    @FXML private TableColumn<BookingView, String> checkInColumn;
    @FXML private TableColumn<BookingView, String> checkOutColumn;
    @FXML private TableView<BookingView> guestTable;
    @FXML private TableColumn<BookingView, String> nameColumn;
    @FXML private TableColumn<BookingView, String> phoneColumn;
    @FXML private TableColumn<BookingView, String> roomTypeColumn;
    @FXML private TableColumn<BookingView, String> totalPriceColumn;
    @FXML private TextField searchField;

    private final ObservableList<BookingView> bookings = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadBookings();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterBookings(newVal);
        });
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
        bookingIdColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getBookingId())));
        roomTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoomType()));
        checkInColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckIn()));
        checkOutColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckOut()));
        totalPriceColumn.setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getTotalPrice())));
    }

    private void loadBookings() {
        bookings.clear();
        try (Connection conn = HotelDatabase.connect()) {
            String query = """
            SELECT r.bookingID, g.guestID, rm.roomID, g.name, g.phone, rm.room_type,
                   r.check_in_date, r.check_out_date, r.totalPrice
            FROM Reservation r
            JOIN Guest g ON r.guestID = g.guestID
            JOIN Room rm ON r.roomID = rm.roomID
            WHERE r.status != 'cancelled'
        """;

            ResultSet rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                BookingView view = new BookingView(
                        rs.getInt("bookingID"),
                        rs.getInt("guestID"),
                        rs.getInt("roomID"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("room_type"),
                        rs.getString("check_in_date"),
                        rs.getString("check_out_date"),
                        rs.getFloat("totalPrice")
                );
                bookings.add(view);
                System.out.println("📦 Booking Loaded: " + view.getName());
            }

            guestTable.setItems(bookings);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "❌ Failed to load bookings from the database").show();
        }
    }


    private void filterBookings(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            guestTable.setItems(bookings);
            return;
        }

        ObservableList<BookingView> filtered = FXCollections.observableArrayList();
        for (BookingView view : bookings) {
            if (view.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    view.getPhone().contains(keyword)) {
                filtered.add(view);
            }
        }
        guestTable.setItems(filtered);
    }

    @FXML
    void handleCancelBooking(ActionEvent event) {
        CancelBooking.BookingView selected = guestTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            AppState.setBookingId(selected.getBookingId()); // Only booking ID is needed
            SceneNavigator.switchTo((Stage) guestTable.getScene().getWindow(),
                    "/com/seneca/apd/finalproject/hotelreservation/Cancellation.fxml",
                    "Cancel Booking");
        } else {
            showWarning("Please select a reservation to cancel.");
        }
    }

    @FXML
    void handleMenu(ActionEvent event) {
        SceneNavigator.switchTo((Stage) guestTable.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml",
                "Admin Dashboard");
    }

    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class BookingView {
        private final int bookingId;
        private final int guestId;
        private final int roomId;
        private final String name, phone, roomType, checkIn, checkOut;
        private final float totalPrice;

        public BookingView(int bookingId, int guestId, int roomId, String name, String phone, String roomType, String checkIn, String checkOut, float totalPrice) {
            this.bookingId = bookingId;
            this.guestId = guestId;
            this.roomId = roomId;
            this.name = name;
            this.phone = phone;
            this.roomType = roomType;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.totalPrice = totalPrice;
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
    }

}
