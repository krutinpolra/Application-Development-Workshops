package com.seneca.apd.ws45.workshop45.Views;

import com.seneca.apd.ws45.workshop45.Controllers.InventoryManagementController;
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

    public static void loadSceneWithUser(String fxmlFile, Stage stage, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/com/seneca/apd/ws45/workshop45/" + fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof InventoryManagementController invController) {
                invController.setLoggedInUser(username);
            }

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
