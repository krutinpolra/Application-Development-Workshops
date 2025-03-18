package com.seneca.apd.ws45.workshop45.Controllers;

import com.seneca.apd.ws45.workshop45.Models.*;
import com.seneca.apd.ws45.workshop45.Views.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModifyPartController {

    @FXML private TextField idField, nameField, inventoryField, priceField, maxField, minField, machineIdField, companyNameField;
    ;
    @FXML private RadioButton inHouseRadio, outsourcedRadio;
    @FXML private Button saveButton, cancelButton;
    @FXML private ToggleGroup partTypeToggleGroup;

    private static Part selectedPart;

    public static void setSelectedPart(Part part) {
        selectedPart = part;
    }

    @FXML
    private void initialize() {
        if (selectedPart != null) {
            idField.setText(String.valueOf(selectedPart.getId()));
            nameField.setText(selectedPart.getName());
            inventoryField.setText(String.valueOf(selectedPart.getStock()));
            priceField.setText(String.valueOf(selectedPart.getPrice()));
            maxField.setText(String.valueOf(selectedPart.getMax()));
            minField.setText(String.valueOf(selectedPart.getMin()));

            if (selectedPart instanceof InHouse) {
                inHouseRadio.setSelected(true);
                machineIdField.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
                switchToInHouse();
            } else if (selectedPart instanceof Outsourced) {
                outsourcedRadio.setSelected(true);
                companyNameField.setText(((Outsourced) selectedPart).getCompanyName());
                switchToOutsourced();
            }
        }

        // Add event listeners to radio buttons to switch fields dynamically
        inHouseRadio.setOnAction(e -> switchToInHouse());
        outsourcedRadio.setOnAction(e -> switchToOutsourced());
    }


    private void switchToInHouse() {
        machineIdField.setVisible(true);
        companyNameField.setVisible(false);
    }

    private void switchToOutsourced() {
        machineIdField.setVisible(false);
        companyNameField.setVisible(true);
    }


    @FXML
    private void handleSave() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int stock = Integer.parseInt(inventoryField.getText());
            double price = Double.parseDouble(priceField.getText());
            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());

            if (min > max || stock < min || stock > max) {
                showAlert("Invalid Input", "Check min/max inventory levels.");
                return;
            }

            Part updatedPart;
            if (inHouseRadio.isSelected()) {
                int machineId = Integer.parseInt(machineIdField.getText());
                updatedPart = new InHouse(id, name, price, stock, min, max, machineId);
            } else {
                // ✅ Fix: Handle Outsourced Part properly
                String companyName = companyNameField.getText();
                updatedPart = new Outsourced(id, name, price, stock, min, max, companyName);
            }

            // ✅ Find and replace the old part in the Inventory
            int index = -1;
            for (int i = 0; i < Inventory.getAllParts().size(); i++) {
                if (Inventory.getAllParts().get(i).getId() == id) {
                    index = i;
                    break;
                }
            }

            if (index >= 0) {
                Inventory.updatePart(index, updatedPart);
            }

            // ✅ Close Modify Part window and reload Inventory
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
            SceneLoader.loadScene("InventoryManagement.fxml", stage);

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
