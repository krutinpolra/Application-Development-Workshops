module com.seneca.apd.finalproject.hotelreservation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.seneca.apd.finalproject.hotelreservation.Controller to javafx.fxml;
    exports com.seneca.apd.finalproject.hotelreservation;
    exports com.seneca.apd.finalproject.hotelreservation.Controller;
}
