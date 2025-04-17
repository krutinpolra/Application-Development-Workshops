package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.seneca.apd.finalproject.hotelreservation.DataBase.HotelDatabase.connect;

public class Cancellation {

    @FXML private TextField guestName;
    @FXML private TextField guestEmail;
    @FXML private TextField guestPhone;
    @FXML private TextField guestBookingId;
    @FXML private TextField guestRoomType;
    @FXML private TextField guestCheckIn;
    @FXML private TextField guestCheckOut;

    @FXML private CheckBox guestRequest;
    @FXML private CheckBox paymentIssue;
    @FXML private CheckBox systemError;
    @FXML private CheckBox otherReason;
    @FXML private TextField otherReasonField;

    @FXML
    public void initialize() {
        int bookingId = AppState.getBookingId(); // Get the selected booking ID
        String sql = """
            SELECT g.name, g.phone, g.email, rm.room_type,
                   r.check_in_date, r.check_out_date, r.bookingID
            FROM Reservation r
            JOIN Guest g ON r.guestID = g.guestID
            JOIN Room rm ON r.roomID = rm.roomID
            WHERE r.bookingID = ?
        """;

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                guestName.setText(rs.getString("name"));
                guestPhone.setText(rs.getString("phone"));
                guestEmail.setText(rs.getString("email"));
                guestRoomType.setText(rs.getString("room_type") + " Room");
                guestBookingId.setText(String.valueOf(bookingId));

                // Format the date
                DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

                LocalDate checkIn = LocalDate.parse(rs.getString("check_in_date"), inputFormat);
                LocalDate checkOut = LocalDate.parse(rs.getString("check_out_date"), inputFormat);

                guestCheckIn.setText(checkIn.format(outputFormat));
                guestCheckOut.setText(checkOut.format(outputFormat));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "❌ Failed to fetch guest details from DB").show();
        }
    }

    @FXML
    void handleCancelBooking(ActionEvent event) {
        int bookingID = Integer.parseInt(guestBookingId.getText());
        String reason = "";

        if (guestRequest.isSelected()) reason += "Guest Request; ";
        if (paymentIssue.isSelected()) reason += "Payment Issue; ";
        if (systemError.isSelected()) reason += "System Error; ";
        if (otherReason.isSelected()) reason += "Other: " + otherReasonField.getText();

        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // 🔒 Start transaction

            // 1️⃣ Insert into Cancellation table
            String insert = """
            INSERT INTO Cancellation (bookingID, reason, cancelled_on)
            VALUES (?, ?, ?)
        """;
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setInt(1, bookingID);
            ps.setString(2, reason.trim());
            ps.setString(3, LocalDate.now().format(DateTimeFormatter.ISO_DATE));
            ps.executeUpdate();

            // 2️⃣ Update Reservation table
            String updateReservation = "UPDATE Reservation SET status = 'cancelled' WHERE bookingID = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateReservation);
            updateStmt.setInt(1, bookingID);
            updateStmt.executeUpdate();

            // 3️⃣ Get roomID from Reservation
            String fetchRoom = "SELECT roomID FROM Reservation WHERE bookingID = ?";
            PreparedStatement fetchRoomStmt = conn.prepareStatement(fetchRoom);
            fetchRoomStmt.setInt(1, bookingID);
            ResultSet rs = fetchRoomStmt.executeQuery();

            if (rs.next()) {
                int roomId = rs.getInt("roomID");

                // 4️⃣ Update Room table
                String updateRoom = "UPDATE Room SET status = 'available' WHERE roomID = ?";
                PreparedStatement roomStmt = conn.prepareStatement(updateRoom);
                roomStmt.setInt(1, roomId);
                roomStmt.executeUpdate();

                System.out.println("✅ Room marked available again (ID: " + roomId + ")");
            }

            conn.commit(); // ✅ End transaction
            System.out.println("✅ Booking cancelled successfully!");

            SceneNavigator.switchTo((Stage) guestName.getScene().getWindow(),
                    "/com/seneca/apd/finalproject/hotelreservation/EndingScreen.fxml", "Thank you!");

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "❌ Failed to cancel booking").show();
        }
    }


    @FXML
    void handleBack(ActionEvent event) {
        SceneNavigator.switchTo((Stage) guestName.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/ListOfGuests.fxml", "Guest List");
    }

    @FXML
    void handleMenu(ActionEvent event) {
        SceneNavigator.switchTo((Stage) guestName.getScene().getWindow(),
                "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml", "Dashboard");
    }
}
