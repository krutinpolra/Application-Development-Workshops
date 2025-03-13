/*
Workshop: 2
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/

package com.example.workshop_2.controller;

import com.example.workshop_2.models.VehicleData;
import com.example.workshop_2.models.MaintenanceRecord;
import com.example.workshop_2.models.UsageLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private Button nextButton, previousButton;

    @FXML
    private TextField costField, descriptionField, kmField, makeField, modelField, yearField;

    @FXML
    private DatePicker endDate, maintenanceDate, startDate;

    @FXML
    private AnchorPane formContainer;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label titleLabel;

    @FXML
    private ComboBox<String> typeOfCars;

    @FXML
    private ComboBox<String> showSummary;

    private final ObservableList<VehicleData> vehicleData = FXCollections.observableArrayList();
    private final ObservableList<MaintenanceRecord> maintenanceData = FXCollections.observableArrayList();
    private final ObservableList<UsageLog> usageData = FXCollections.observableArrayList();

    @FXML
    private void handleNext() {
        int selectedIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (selectedIndex < tabPane.getTabs().size() - 1) {
            tabPane.getSelectionModel().select(selectedIndex + 1);
        }
    }

    @FXML
    private void handlePrevious() {
        int selectedIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            tabPane.getSelectionModel().select(selectedIndex - 1);
        }
    }

    @FXML
    private void initialize() {
        // Populate the summary selection ComboBox
        showSummary.setItems(FXCollections.observableArrayList("Vehicles", "Maintenance Records", "Usage Logs"));
        showSummary.setOnAction(event -> handleViewSummary());
    }

    @FXML
    private void handleSave() {
        String model = modelField.getText();
        String make = makeField.getText();
        String year = yearField.getText();
        String type = typeOfCars.getValue();


        String date = String.valueOf(maintenanceDate.getValue());
        String description = descriptionField.getText();
        String cost = costField.getText();

        String sDate = String.valueOf(startDate.getValue());
        String eDate = String.valueOf(endDate.getValue());
        String kmDriven = kmField.getText();

        if (model.isEmpty() || make.isEmpty() || year.isEmpty() || type == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All vehicle fields must be filled!");
            return;
        }
        if (date.isEmpty() || description.isEmpty() || cost.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All vehicle maintenance fields must be filled!");
            return;
        }
        if (sDate.isEmpty() || eDate.isEmpty() || kmDriven.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All vehicle usage fields must be filled!");
            return;
        }

        vehicleData.add(new VehicleData(model, make, year, type));
        maintenanceData.add(new MaintenanceRecord(model, make, date, description, cost));
        usageData.add(new UsageLog(model, make, sDate, eDate, kmDriven));

        showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle data have been Saved!");

        clearFields();

    }

    @FXML
    private void handleClear() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (selectedTab.getText().equals("Add Vehicle")) {
            modelField.clear();
            makeField.clear();
            yearField.clear();
            typeOfCars.getSelectionModel().clearSelection();
        }
        else if (selectedTab.getText().equals("Add Vehicle Maintenance")) {
            maintenanceDate.setValue(null);
            descriptionField.clear();
            costField.clear();
        }
        else if (selectedTab.getText().equals("Add Vehicle Usage")) {
            startDate.setValue(null);
            endDate.setValue(null);
            kmField.clear();
        }
    }


    @FXML
    private void handleViewSummary() {
        Stage summaryStage = new Stage();

        TableView<?> tableView;

        String selectedType = showSummary.getValue();
        if ("Vehicles".equals(selectedType)) {
            summaryStage.setTitle("Vehicle Summary");
            TableView<VehicleData> vehicleTableView = new TableView<>(vehicleData);

            TableColumn<VehicleData, String> modelCol = new TableColumn<>("Model");
            modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

            TableColumn<VehicleData, String> makeCol = new TableColumn<>("Make");
            makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));

            TableColumn<VehicleData, String> yearCol = new TableColumn<>("Year");
            yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

            TableColumn<VehicleData, String> typeCol = new TableColumn<>("Type");
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

            vehicleTableView.getColumns().addAll(modelCol, makeCol, yearCol, typeCol);
            tableView = vehicleTableView;
        } else if ("Maintenance Records".equals(selectedType)) {
            summaryStage.setTitle("Vehicle Maintenance Summary");
            TableView<MaintenanceRecord> maintenanceTableView = new TableView<>(maintenanceData);

            TableColumn<MaintenanceRecord, String> modelCol = new TableColumn<>("Model");
            modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

            TableColumn<MaintenanceRecord, String> makeCol = new TableColumn<>("Make");
            makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));

            TableColumn<MaintenanceRecord, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

            TableColumn<MaintenanceRecord, String> descriptionCol = new TableColumn<>("Description");
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

            TableColumn<MaintenanceRecord, String> costCol = new TableColumn<>("Cost");
            costCol.setCellValueFactory(new PropertyValueFactory<>("cost"));

            maintenanceTableView.getColumns().addAll(modelCol, makeCol, dateCol, descriptionCol, costCol);
            tableView = maintenanceTableView;
        } else { // "Usage Logs"
            summaryStage.setTitle("vehicle Usage Summary");
            TableView<UsageLog> usageTableView = new TableView<>(usageData);

            TableColumn<UsageLog, String> modelCol = new TableColumn<>("Model");
            modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

            TableColumn<UsageLog, String> makeCol = new TableColumn<>("Make");
            makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));

            TableColumn<UsageLog, String> startDateCol = new TableColumn<>("Start Date");
            startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));

            TableColumn<UsageLog, String> endDateCol = new TableColumn<>("End Date");
            endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

            TableColumn<UsageLog, String> kilometersCol = new TableColumn<>("Kilometers Driven");
            kilometersCol.setCellValueFactory(new PropertyValueFactory<>("kilometers"));

            usageTableView.getColumns().addAll(modelCol, makeCol, startDateCol, endDateCol, kilometersCol);
            tableView = usageTableView;
        }

        VBox vbox = new VBox(tableView);
        Scene scene = new Scene(vbox, 500, 300);
        summaryStage.setScene(scene);
        summaryStage.show();
    }



    private void clearFields() {
        modelField.clear();
        makeField.clear();
        yearField.clear();
        typeOfCars.getSelectionModel().clearSelection();
        costField.clear();
        descriptionField.clear();
        kmField.clear();
        maintenanceDate.setValue(null);
        startDate.setValue(null);
        endDate.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}