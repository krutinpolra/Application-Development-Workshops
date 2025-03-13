package com.apd.ws3.auto_loan_applicationws3.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import com.apd.ws3.auto_loan_applicationws3.Models.Loan;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Consumer;

public class SavedRateController {
    @FXML private ListView<String> savedRatesList;

    private List<Loan> loans;
    private Consumer<Loan> onLoanSelected;

    public void setSavedRates(ObservableList<String> savedRates, List<Loan> loans, Consumer<Loan> onLoanSelected) {
        this.loans = loans;
        this.onLoanSelected = onLoanSelected;
        savedRatesList.setItems(savedRates);
    }

    @FXML
    private void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            int selectedIndex = savedRatesList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && onLoanSelected != null) {
                onLoanSelected.accept(loans.get(selectedIndex));
            }
        }
    }
}
