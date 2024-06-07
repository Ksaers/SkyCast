module dev.skycast {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.desktop;
    requires javafx.media;

    exports dev.skycast;
    opens dev.skycast to javafx.fxml;
}