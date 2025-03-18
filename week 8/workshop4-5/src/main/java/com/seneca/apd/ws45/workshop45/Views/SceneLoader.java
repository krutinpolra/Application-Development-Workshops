package com.seneca.apd.ws45.workshop45.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneLoader {
    public static void loadScene(String fxmlFile, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/com/seneca/apd/ws45/workshop45/" + fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
