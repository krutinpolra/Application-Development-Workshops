package com.apd.ws3.auto_loan_applicationws3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Auto Loan Application");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}