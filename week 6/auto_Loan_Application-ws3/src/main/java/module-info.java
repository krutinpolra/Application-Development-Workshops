module com.apd.ws3.auto_loan_applicationws3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.apd.ws3.auto_loan_applicationws3 to javafx.fxml;
    exports com.apd.ws3.auto_loan_applicationws3;
    exports com.apd.ws3.auto_loan_applicationws3.Controllers;
    opens com.apd.ws3.auto_loan_applicationws3.Controllers to javafx.fxml;
}