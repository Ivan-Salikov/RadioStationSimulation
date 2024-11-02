package com.coursework.radiostationsimulation.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SimulationFormController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}