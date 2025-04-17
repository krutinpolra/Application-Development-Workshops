package com.seneca.apd.finalproject.hotelreservation;

import com.seneca.apd.finalproject.hotelreservation.DataBase.HotelDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Ensure database and tables are initialized
        try (Connection conn = HotelDatabase.connect()) {
            System.out.println("✅ Database connection successful.");
        } catch (Exception e) {
            System.err.println("❌ Failed to connect to database: " + e.getMessage());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), stage.getWidth(), stage.getHeight());
        stage.setTitle("Hotel Reservation!");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
