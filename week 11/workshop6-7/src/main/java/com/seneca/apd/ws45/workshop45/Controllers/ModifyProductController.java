package com.seneca.apd.ws45.workshop45.Controllers;

import com.seneca.apd.ws45.workshop45.Models.Inventory;
import com.seneca.apd.ws45.workshop45.Models.Part;
import com.seneca.apd.ws45.workshop45.Models.Product;
import com.seneca.apd.ws45.workshop45.Views.SceneLoader;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModifyProductController {

    @FXML private TextField idField, nameField, inventoryField, priceField, maxField, minField, searchField;
    @FXML private TableView<Part> productTable, associatedPartsTable;
    @FXML private TableColumn<Part, Integer> partIdColumn, inventoryLevelColumn, associatedPartIdColumn, associatedPartInventoryColumn;
    @FXML private TableColumn<Part, String> partNameColumn, associatedPartNameColumn;
    @FXML private TableColumn<Part, Double> priceColumn, associatedPartPriceColumn;
    @FXML private Button addButton, removeAssociatedPartButton, saveButton, cancelButton;

    private static Product selectedProduct;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public static void setSelectedProduct(Product product) {
        selectedProduct = product;
    }

    @FXML
    private void initialize() {
        if (selectedProduct != null) {
            idField.setText(String.valueOf(selectedProduct.getId()));
            nameField.setText(selectedProduct.getName());
            inventoryField.setText(String.valueOf(selectedProduct.getStock()));
            priceField.setText(String.valueOf(selectedProduct.getPrice()));
            maxField.setText(String.valueOf(selectedProduct.getMax()));
            minField.setText(String.valueOf(selectedProduct.getMin()));

            associatedParts.setAll(selectedProduct.getAllAssociatedParts());
            associatedPartsTable.setItems(associatedParts);
        }

        setupPartTables();
        idField.setEditable(false);
    }

    private void setupPartTables() {
        // Available Parts Table
        partIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        partNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        inventoryLevelColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        // Associated Parts Table
        associatedPartIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        associatedPartNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        associatedPartInventoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        associatedPartPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        productTable.setItems(Inventory.getAllParts());
        associatedPartsTable.setItems(associatedParts);
    }


    @FXML
    private void handleAddPart() {
        Part selectedPart = productTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null && !associatedParts.contains(selectedPart)) {
            associatedParts.add(selectedPart);
        } else {
            showAlert("Selection Error", "Please select a valid part.");
        }
    }

    @FXML
    private void handleRemoveAssociatedPart() {
        Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            if (showConfirmation("Remove Part", "Are you sure you want to remove this associated part?")) {
                associatedParts.remove(selectedPart);
            }
        } else {
            showAlert("Selection Error", "Please select a part to remove.");
        }
    }

    @FXML
    private void handleSave() {
        try {
            int id = selectedProduct.getId();
            String name = nameField.getText().trim();
            int stock = Integer.parseInt(inventoryField.getText());
            double price = Double.parseDouble(priceField.getText());
            int min = Integer.parseInt(minField.getText());
            int max = Integer.parseInt(maxField.getText());

            // 🔎 Validation checks
            if (name.isEmpty()) {
                showAlert("Input Error", "Product name cannot be empty.");
                return;
            }
            if (price < getTotalPartsCost()) {
                showAlert("Pricing Error", "Product price must be greater than the total cost of associated parts.");
                return;
            }
            if (min > max) {
                showAlert("Input Error", "Minimum stock cannot be greater than Maximum.");
                return;
            }
            if (stock < min || stock > max) {
                showAlert("Inventory Error", "Stock must be between Min and Max.");
                return;
            }
            if (associatedParts.isEmpty()) {
                showAlert("Association Error", "A product must have at least one associated part.");
                return;
            }

            // ✅ Find index in inventory
            int index = -1;
            ObservableList<Product> allProducts = Inventory.getAllProducts();
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getId() == id) {
                    index = i;
                    break;
                }
            }

            if (index == -1) {
                showAlert("Update Error", "Product not found in inventory.");
                return;
            }

            // ✅ Update Product
            selectedProduct.setName(name);
            selectedProduct.setStock(stock);
            selectedProduct.setPrice(price);
            selectedProduct.setMin(min);
            selectedProduct.setMax(max);
            selectedProduct.getAllAssociatedParts().clear();
            selectedProduct.getAllAssociatedParts().addAll(associatedParts);

            Inventory.updateProduct(index, selectedProduct);

            SceneLoader.loadScene("InventoryManagement.fxml", (Stage) saveButton.getScene().getWindow());

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numerical values.");
        }
    }

    @FXML
    private void handleCancel() {
        if (showConfirmation("Cancel", "Are you sure you want to cancel? Unsaved data will be lost.")) {
            SceneLoader.loadScene("InventoryManagement.fxml", (Stage) cancelButton.getScene().getWindow());
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        ObservableList<Part> searchResults = FXCollections.observableArrayList();

        // Try searching by ID first
        try {
            int id = Integer.parseInt(query);
            Part part = Inventory.searchPartByID(id);
            if (part != null) {
                searchResults.add(part);
            }
        } catch (NumberFormatException e) {
            // Search by name if input is not an integer
            searchResults = Inventory.searchPartByName(query);
        }

        if (searchResults.isEmpty()) {
            showAlert("Not Found", "No parts match your search.");
        } else {
            productTable.setItems(searchResults);
        }
    }

    private double getTotalPartsCost() {
        return associatedParts.stream().mapToDouble(Part::getPrice).sum();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();
        return confirmation.getResult() == ButtonType.YES;
    }
}
