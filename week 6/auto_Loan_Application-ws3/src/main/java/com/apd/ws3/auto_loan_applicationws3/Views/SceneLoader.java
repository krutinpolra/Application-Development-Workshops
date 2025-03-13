package com.apd.ws3.auto_loan_applicationws3.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneLoader {
    public static void loadScene(String fxmlFile, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/com/apd/ws3/auto_loan_applicationws3/" + fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
