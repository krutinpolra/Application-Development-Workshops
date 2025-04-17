module com.seneca.apd.ws45.workshop45 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // OPEN the package to JavaFX graphics (Fixes IllegalAccessException)
    opens com.seneca.apd.ws45.workshop45 to javafx.graphics, javafx.fxml;
    exports com.seneca.apd.ws45.workshop45;

    opens com.seneca.apd.ws45.workshop45.Models to javafx.base, javafx.fxml;
    exports com.seneca.apd.ws45.workshop45.Models;

    opens com.seneca.apd.ws45.workshop45.Controllers to javafx.fxml;
    exports com.seneca.apd.ws45.workshop45.Controllers;
}
