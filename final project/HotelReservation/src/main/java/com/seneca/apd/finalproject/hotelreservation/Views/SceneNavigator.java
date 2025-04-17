package com.seneca.apd.finalproject.hotelreservation.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {
    public static void switchTo(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
            stage.setTitle("Hotel Reservation | " + title);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Failed to load scene: " + fxmlPath);
        }
    }
}

