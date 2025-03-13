module com.example.wk_4_list_viewer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.wk_4_list_viewer to javafx.fxml;
    exports com.example.wk_4_list_viewer;
}