module tactical.blue {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    opens tactical.blue to javafx.fxml;
    exports tactical.blue;
}
