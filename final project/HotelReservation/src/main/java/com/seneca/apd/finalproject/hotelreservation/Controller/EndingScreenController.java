package com.seneca.apd.finalproject.hotelreservation.Controller;

import com.seneca.apd.finalproject.hotelreservation.Views.AppState;
import com.seneca.apd.finalproject.hotelreservation.Views.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;

public class EndingScreenController {

    @FXML
    void handleGoHome(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (AppState.isAdmin()) {
            SceneNavigator.switchTo(stage,
                    "/com/seneca/apd/finalproject/hotelreservation/DashBoard.fxml",
                    "Admin Dashboard");
        } else {
            SceneNavigator.switchTo(stage,
                    "/com/seneca/apd/finalproject/hotelreservation/LogIn.fxml",
                    "Login");
        }
    }
}
