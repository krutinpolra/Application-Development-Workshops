module com.example.workshop_2 {
    requires javafx.controls;
    requires javafx.fxml;

    // Allow JavaFX to access models for TableView
    opens com.example.workshop_2.models to javafx.base;
    opens com.example.workshop_2.controller to javafx.fxml;

    exports com.example.workshop_2;
    exports com.example.workshop_2.controller;
}
