package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class FeedbackController {

    @FXML
    private TextArea feedbackTextArea;

    @FXML
    private TextField guestEmailField;

    @FXML
    private TextField guestNameField;

    @FXML
    void handleBackHome(ActionEvent event) {
        Stage stage = (Stage) guestNameField.getScene().getWindow();
        SceneNavigator.switchTo(stage,
                "/com/seneca/apd/finalproject/hotelreservation/LogIn.fxml",
                "Login Screen");
    }

    @FXML
    void handleSubmit(ActionEvent event) {
        String name = guestNameField.getText().trim();
        String email = guestEmailField.getText().trim();
        String feedback = feedbackTextArea.getText().trim();

        if (name.isEmpty() || email.isEmpty() || feedback.isEmpty()) {
            System.out.println("⚠️ Please fill all fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            String sql = "INSERT INTO Feedback (guestID, bookingID, comments, rating) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // For now, using dummy guestID and bookingID = 1, rating = 5
            stmt.setInt(1, 1); // replace with real guestID if available
            stmt.setInt(2, 1); // replace with real bookingID if available
            stmt.setString(3, feedback);
            stmt.setInt(4, 5); // default rating

            stmt.executeUpdate();
            System.out.println("✅ Feedback submitted by: " + name);

        } catch (Exception e) {
            System.err.println("❌ Failed to save feedback: " + e.getMessage());
        }

        // Redirect to ending screen
        Stage stage = (Stage) guestNameField.getScene().getWindow();
        SceneNavigator.switchTo(stage,
                "/com/seneca/apd/finalproject/hotelreservation/EndingScreen.fxml",
                "Thanks for your feedback!");
    }
}
