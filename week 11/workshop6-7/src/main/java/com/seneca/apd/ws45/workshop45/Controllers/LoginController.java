package com.seneca.apd.ws45.workshop45.Controllers;

import com.seneca.apd.ws45.workshop45.Views.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button cancelButton; // Ensure cancelButton is properly defined

    private static final String VALID_EMAIL = "kbpolra";
    private static final String VALID_PASSWORD = "111";

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.equals(VALID_EMAIL) && password.equals(VALID_PASSWORD)) {
            SceneLoader.loadSceneWithUser("InventoryManagement.fxml", (Stage) loginButton.getScene().getWindow(), email);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid email or password!");
            alert.showAndWait();
            emailField.requestFocus();
        }
    }

    @FXML
    private void handleCancel() {
        // Close the login window when "Cancel" is clicked
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    // Utility method to show alerts
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
