package com.seneca.apd.ws45.workshop45.Controllers;

import com.seneca.apd.ws45.workshop45.Models.Inventory;
import com.seneca.apd.ws45.workshop45.Models.Part;
import com.seneca.apd.ws45.workshop45.Models.Product;
import com.seneca.apd.ws45.workshop45.Views.SceneLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AddProductController {

    @FXML private TextField idField, nameField, inventoryField, priceField, maxField, minField, searchField;
    @FXML private TableView<Part> productTable, associatedPartsTable;

    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInventoryColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;

    // Table Columns for Available Parts
    @FXML private TableColumn<Part, Integer> partIdColumn, inventoryLevelColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Double> priceColumn;

    // Table Columns for Associated Parts
    @FXML private TableColumn<Part, Integer> associatedPartIdColumn, associatedPartInventoryColumn;
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    @FXML private TableColumn<Part, Double> associatedPartPriceColumn;

    @FXML private Button addButton, removeAssociatedPartButton, saveButton, cancelButton;

    private static int productIdCounter = 1;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // ✅ Fix: Ensure columns are correctly mapped to data model properties
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Set available parts and associated parts
        productTable.setItems(Inventory.getAllParts());
        associatedPartsTable.setItems(associatedParts);

        // Auto-generate ID for new product
        idField.setText(String.valueOf(productIdCounter));
        idField.setEditable(false);
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
            int id = productIdCounter++;
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
                showAlert("Pricing Error", "Product price must be greater than or equal to the total cost of associated parts.");
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

            // ✅ Create new product
            Product newProduct = new Product(id, name, price, stock, min, max);
            newProduct.getAllAssociatedParts().addAll(associatedParts);
            Inventory.addProduct(newProduct);

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
        confirmation.setTitle(title);
        confirmation.showAndWait();
        return confirmation.getResult() == ButtonType.YES;
    }
}
