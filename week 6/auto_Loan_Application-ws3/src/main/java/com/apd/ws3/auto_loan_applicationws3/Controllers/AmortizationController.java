package com.apd.ws3.auto_loan_applicationws3.Controllers;

import com.apd.ws3.auto_loan_applicationws3.Models.LoanAmortization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class AmortizationController {
    @FXML private TableView<LoanAmortization> amortizationTable;
    @FXML private TableColumn<LoanAmortization, Integer> paymentNumberCol;
    @FXML private TableColumn<LoanAmortization, String> paymentDateCol;
    @FXML private TableColumn<LoanAmortization, String> paymentAmountCol;
    @FXML private TableColumn<LoanAmortization, String> interestAmountCol;
    @FXML private TableColumn<LoanAmortization, String> principalPaidCol;
    @FXML private TableColumn<LoanAmortization, String> endingBalanceCol;
    @FXML private Button closeButton;

    private Stage stage;

    public void setAmortizationData(List<LoanAmortization> data) {
        ObservableList<LoanAmortization> amortizationData = FXCollections.observableArrayList(data);
        amortizationTable.setItems(amortizationData);

        // Bind columns to data properties
        paymentNumberCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getPaymentNumber()).asObject());
        paymentDateCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPaymentDate()));
        paymentAmountCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getPaymentAmount())));
        interestAmountCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getInterestAmount())));
        principalPaidCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getPrincipalPaid())));
        endingBalanceCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", cellData.getValue().getEndingBalance())));
    }

    @FXML
    private void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
