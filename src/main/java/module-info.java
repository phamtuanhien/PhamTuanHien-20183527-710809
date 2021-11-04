module com.example.tkxdpm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tkxdpm to javafx.fxml;
    exports com.example.tkxdpm;
}