module Project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens Controller to javafx.fxml;
    exports Controller;
}