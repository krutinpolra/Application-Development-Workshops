package com.seneca.apd.ws45.workshop45.Controllers;

import com.seneca.apd.ws45.workshop45.Models.Inventory;
import com.seneca.apd.ws45.workshop45.Models.Part;
import com.seneca.apd.ws45.workshop45.Models.Product;
import com.seneca.apd.ws45.workshop45.Views.SceneLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class InventoryManagementController {

    @FXML private TextField searchPartsField, searchProductsField;
    @FXML private TableView<Part> partsTable;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> inventoryLevelColumn;
    @FXML private TableColumn<Part, Double> priceColumn;

    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInventoryColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;
    @FXML private Button addPartButton, modifyPartButton, deletePartButton;
    @FXML private Button addProductButton, modifyProductButton, deleteProductButton;
    @FXML private Button exitButton;

    @FXML
    public void initialize() {
        // Debugging to check if productsTable is null
        if (productsTable == null) {
            System.out.println("⚠️ productsTable is NULL! Check FXML bindings.");
        } else {
            System.out.println("✅ productsTable is initialized.");
        }

        // Initialize Part Table
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Initialize Product Table
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        refreshTables();
    }


    private void refreshTables() {
        loadParts();
        loadProducts();
    }

    private void loadParts() {
        ObservableList<Part> parts = Inventory.getAllParts();
        partsTable.setItems(parts);
        partsTable.refresh();
    }

    private void loadProducts() {
        Platform.runLater(() -> {
            if (productsTable != null) {
                ObservableList<Product> products = Inventory.getAllProducts();
                System.out.println("✅ Products in Inventory: " + products);
                productsTable.setItems(products);
                productsTable.refresh();
            } else {
                System.out.println("⚠️ productsTable is still NULL when loadProducts() was called!");
            }
        });
    }


    public void refreshProductTable() {
        Platform.runLater(this::loadProducts);
    }


    @FXML
    private void handleSearchParts() {
        String query = searchPartsField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            loadParts();
        } else {
            ObservableList<Part> results = FXCollections.observableArrayList();
            try {
                int partId = Integer.parseInt(query);
                Part part = Inventory.searchPartByID(partId);
                if (part != null) {
                    results.add(part);
                }
            } catch (NumberFormatException e) {
                results = Inventory.searchPartByName(query);
            }
            partsTable.setItems(results);
            partsTable.refresh();
        }
    }

    @FXML
    private void handleSearchProducts() {
        String query = searchProductsField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            loadProducts();
        } else {
            ObservableList<Product> results = FXCollections.observableArrayList();
            try {
                int productId = Integer.parseInt(query);
                Product product = Inventory.searchProductByID(productId);
                if (product != null) {
                    results.add(product);
                }
            } catch (NumberFormatException e) {
                results = Inventory.searchProductByName(query);
            }
            productsTable.setItems(results);
            productsTable.refresh();
        }
    }

    @FXML
    private void handleAddPart() {
        SceneLoader.loadScene("AddPart.fxml", (Stage) addPartButton.getScene().getWindow());
    }

    @FXML
    private void handleModifyPart() {
        Part selected = partsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ModifyPartController.setSelectedPart(selected);
            SceneLoader.loadScene("ModifyPart.fxml", (Stage) modifyPartButton.getScene().getWindow());
        } else {
            showAlert("No Selection", "Please select a part to modify.");
        }
    }

    @FXML
    private void handleDeletePart() {
        Part selected = partsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this part?", ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait();

            if (confirmation.getResult() == ButtonType.YES) {
                Inventory.deletePart(selected);
                refreshTables();
            }
        } else {
            showAlert("No Selection", "Please select a part to delete.");
        }
    }

    @FXML
    private void handleAddProduct() {
        SceneLoader.loadScene("AddProduct.fxml", (Stage) addProductButton.getScene().getWindow());
    }

    @FXML
    private void handleModifyProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            ModifyProductController.setSelectedProduct(selectedProduct);
            SceneLoader.loadScene("ModifyProduct.fxml", (Stage) modifyProductButton.getScene().getWindow());
        } else {
            showAlert("No Selection", "Please select a product to modify.");
        }
    }


    @FXML
    private void handleDeleteProduct() {
        Product selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (!selected.getAllAssociatedParts().isEmpty()) {
                showAlert("Delete Error", "Cannot delete a product that has associated parts.");
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this product?", ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait();

            if (confirmation.getResult() == ButtonType.YES) {
                Inventory.deleteProduct(selected);
                refreshTables();
            }
        } else {
            showAlert("No Selection", "Please select a product to delete.");
        }
    }


    @FXML
    private void handleExit() {
        SceneLoader.loadScene("LogIn.fxml", (Stage) exitButton.getScene().getWindow());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}