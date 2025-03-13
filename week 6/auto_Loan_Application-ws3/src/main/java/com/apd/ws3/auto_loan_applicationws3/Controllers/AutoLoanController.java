package com.apd.ws3.auto_loan_applicationws3.Controllers;

import com.apd.ws3.auto_loan_applicationws3.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AutoLoanController {
    @FXML private TextField nameField, phoneField, cityField, priceField, downPaymentField, interestCustom;
    @FXML private ComboBox<String> provinceComboBox;
    @FXML private RadioButton carRadio, truckRadio, vanRadio, motorcycleRadio, evRadio;
    @FXML private RadioButton newRadio, usedRadio;
    @FXML private RadioButton interest1, interest2, interest3, interest4, interestOther;
    @FXML private Slider durationSlider;
    @FXML private RadioButton weeklyRadio, biweeklyRadio, monthlyRadio;

    private static final ObservableList<Loan> savedRates = FXCollections.observableArrayList();
    private ObservableList<LoanAmortization> amortizationData = FXCollections.observableArrayList();

    private Loan lastCalculatedLoan;

    @FXML
    private void calculateLoan() {
        try {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String city = cityField.getText();
            String province = provinceComboBox.getValue();
            Customer customer = new Customer(name, phone, city, province);

            String vehicleType = carRadio.isSelected() ? "Car" : truckRadio.isSelected() ? "Truck" : vanRadio.isSelected() ? "Family Van" : motorcycleRadio.isSelected() ? "Motorcycle" : "Electric Vehicle";
            String age = newRadio.isSelected() ? "New" : "Used";
            double price = Double.parseDouble(priceField.getText());
            Vehicle vehicle = new Vehicle(vehicleType, age, price);

            double downPayment = Double.parseDouble(downPaymentField.getText());
            double interestRate = interest1.isSelected() ? 0.99 : interest2.isSelected() ? 1.99 : interest3.isSelected() ? 2.99 : interest4.isSelected() ? 3.99 : Double.parseDouble(interestCustom.getText());
            int duration = (int) durationSlider.getValue();
            String frequency = weeklyRadio.isSelected() ? "Weekly" : biweeklyRadio.isSelected() ? "Bi-Weekly" : "Monthly";

            Loan loan = new Loan(price - downPayment, interestRate, duration, frequency, customer, vehicle);
            FixedRateLoan fixedRateLoan = new FixedRateLoan(loan.getAmount(), loan.getInterestRate(), loan.getDuration());
            List<LoanAmortization> schedule = fixedRateLoan.generateAmortizationSchedule(frequency);
            amortizationData.setAll(schedule);

            lastCalculatedLoan = loan; // Save the last calculated loan

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Loan Calculation");
            alert.setHeaderText("Estimated " + frequency + " Payment");
            alert.setContentText("Your estimated payment is: $" + String.format("%.2f", schedule.get(0).getPaymentAmount()));
            alert.showAndWait();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please enter valid numbers.");
            alert.showAndWait();
        }
    }

    @FXML
    private void openAmortizationView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/apd/ws3/auto_loan_applicationws3/AmortizationView.fxml"));
            Parent root = loader.load();

            // Ensure controller receives the data
            AmortizationController controller = loader.getController();
            if (!amortizationData.isEmpty()) {
                controller.setAmortizationData(amortizationData);
            } else {
                System.out.println("🚨 No amortization data available!");
            }

            Stage stage = new Stage();
            controller.setStage(stage);
            stage.setScene(new Scene(root));
            stage.setTitle("Amortization Schedule");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void saveCurrentRate() {
        if (lastCalculatedLoan != null) {
            savedRates.add(lastCalculatedLoan);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Rate saved successfully!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please calculate the loan before saving.");
            alert.showAndWait();
        }
    }

    @FXML
    private void openSavedRatesView() {
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("Saved Rates");

        TableView<Loan> table = new TableView<>(savedRates);

        TableColumn<Loan, String> customerNameCol = new TableColumn<>("Customer Name");
        customerNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getName()));

        TableColumn<Loan, String> vehicleTypeCol = new TableColumn<>("Vehicle Type");
        vehicleTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVehicle().getType()));

        TableColumn<Loan, String> loanAmountCol = new TableColumn<>("Loan Amount");
        loanAmountCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getAmount())));

        TableColumn<Loan, String> interestRateCol = new TableColumn<>("Interest Rate");
        interestRateCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f%%", cellData.getValue().getInterestRate())));

        TableColumn<Loan, Integer> durationCol = new TableColumn<>("Loan Duration");
        durationCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());

        TableColumn<Loan, String> frequencyCol = new TableColumn<>("Payment Frequency");
        frequencyCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFrequency()));

        table.getColumns().addAll(customerNameCol, vehicleTypeCol, loanAmountCol, interestRateCol, durationCol, frequencyCol);

        VBox layout = new VBox(10, table);
        Scene scene = new Scene(layout, 800, 400);
        popUp.setScene(scene);
        popUp.showAndWait();
    }

    @FXML
    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        cityField.clear();
        priceField.clear();
        downPaymentField.clear();
        interestCustom.clear();
        lastCalculatedLoan = null;
    }
}
