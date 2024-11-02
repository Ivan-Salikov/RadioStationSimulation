module com.coursework.radiostationsimulation {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.coursework.radiostationsimulation to javafx.fxml;
    exports com.coursework.radiostationsimulation;
    exports com.coursework.radiostationsimulation.GUI;
    opens com.coursework.radiostationsimulation.GUI to javafx.fxml;
}