package com.coursework.radiostationsimulation.GUI;

import com.coursework.radiostationsimulation.models.Genre;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RadioProgramsFormController implements Initializable {

    @FXML
    private Button addNewRadioProgram;
    @FXML
    private Button deleteRadioProgram;
    @FXML
    private Button searchRadioProgram;

    @FXML
    private TextField radioProgramName;
    @FXML
    private ComboBox<String> radioProgramType;
    @FXML
    private ComboBox<Genre> radioProgramGenre;
    @FXML
    private Spinner<Integer> radioProgramDuration;

    @FXML
    private TableView<?> radioProgramsTable;

    @FXML
    private ComboBox<String> searchCriteriaCombobox;
    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        radioProgramType.setItems(FXCollections.observableArrayList("По заявкам слушателей", "Хит-парад"));
        radioProgramGenre.setItems(FXCollections.observableArrayList(Genre.values()));
        searchCriteriaCombobox.setItems(FXCollections.observableArrayList("Название", "Тип", "Жанр", "Длительность"));
    }
}
