package com.example.wk_4_list_viewer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private ListView<String> leftListView, rightListView;

    @FXML
    private Button btnMoveRight, btnMoveLeft;

    @FXML
    private void moveRight() {
        String selectedItem = leftListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            leftListView.getItems().remove(selectedItem);
            rightListView.getItems().add(selectedItem);
        }
    }

    @FXML
    private void moveLeft() {
        String selectedItem = rightListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            rightListView.getItems().remove(selectedItem);
            leftListView.getItems().add(selectedItem);
        }
    }

}
