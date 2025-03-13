package com.apd.ws3.auto_loan_applicationws3.Controllers;

import com.apd.ws3.auto_loan_applicationws3.Views.SceneLoader;
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

    private static final String VALID_EMAIL = "krutin@gmail.com";
    private static final String VALID_PASSWORD = "111";

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.equals(VALID_EMAIL) && password.equals(VALID_PASSWORD)) {
            // Proceed to Auto Loan Application UI
            SceneLoader.loadScene("AutoLoan.fxml", (Stage) loginButton.getScene().getWindow());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid email or password!");
            alert.showAndWait();
            emailField.requestFocus();
        }
    }
}
