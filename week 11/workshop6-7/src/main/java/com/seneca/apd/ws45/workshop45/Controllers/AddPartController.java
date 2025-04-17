package com.seneca.apd.ws45.workshop45.Controllers;

import com.seneca.apd.ws45.workshop45.Models.*;
import com.seneca.apd.ws45.workshop45.Views.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddPartController {

    @FXML private TextField idField, nameField, inventoryField, priceField, maxField, minField, machineIdField, companyNameField;
    @FXML private Label machineIdLabel, companyNameLabel;
    @FXML private RadioButton inHouseRadio, outsourcedRadio;
    @FXML private ToggleGroup partTypeToggleGroup;
    @FXML private Button saveButton, cancelButton;

    private static int partIdCounter = 1; // Auto-increment part ID

    @FXML
    private void initialize() {
        int uniqueId = generateUniquePartId();
        idField.setText(String.valueOf(uniqueId));
        idField.setEditable(false);

        inHouseRadio.setOnAction(e -> switchToInHouse());
        outsourcedRadio.setOnAction(e -> switchToOutsourced());
        switchToInHouse();
    }

    private int generateUniquePartId() {
        int id = 1;
        boolean idExists;
        do {
            int finalId = id; // effectively final for lambda
            idExists = Inventory.getAllParts().stream().anyMatch(p -> p.getId() == finalId);
            if (idExists) id++;
        } while (idExists);
        return id;
    }

    private void switchToInHouse() {
        machineIdField.setVisible(true);
        machineIdLabel.setVisible(true);
        machineIdField.setEditable(true); // Allow input for Machine ID

        companyNameField.setVisible(false);
        companyNameLabel.setVisible(false);
        companyNameField.setEditable(false); // Prevent input for Company Name
    }

    private void switchToOutsourced() {
        companyNameField.setVisible(true);
        companyNameLabel.setVisible(true);
        companyNameField.setEditable(true); // Allow input for Company Name

        machineIdField.setVisible(false);
        machineIdLabel.setVisible(false);
        machineIdField.setEditable(false); // Prevent input for Machine ID
    }


    @FXML
    private void handleSave() {
        try {
            int id = generateUniquePartId(); // Auto-increment ID
            String name = nameField.getText();
            int stock = Integer.parseInt(inventoryField.getText());
            double price = Double.parseDouble(priceField.getText());
            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());

            if (min > max || stock < min || stock > max) {
                showAlert("Invalid input", "Check min/max inventory levels.");
                return;
            }

            Part newPart;
            if (inHouseRadio.isSelected()) {
                // Ensure Machine ID is valid
                int machineId = Integer.parseInt(machineIdField.getText());
                newPart = new InHouse(id, name, price, stock, min, max, machineId);
            } else {
                // Ensure Company Name is not empty
                String companyName = companyNameField.getText();
                if (companyName.trim().isEmpty()) {
                    showAlert("Invalid Input", "Company Name cannot be empty.");
                    return;
                }
                newPart = new Outsourced(id, name, price, stock, min, max, companyName);
            }

            Inventory.addPart(newPart);
            System.out.println("✅ Added Part: " + newPart);

            // Redirect to Inventory Management screen
            SceneLoader.loadScene("InventoryManagement.fxml", (Stage) saveButton.getScene().getWindow());

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid values.");
        }
    }


    @FXML
    private void handleCancel() {
        SceneLoader.loadScene("InventoryManagement.fxml", (Stage) cancelButton.getScene().getWindow());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
