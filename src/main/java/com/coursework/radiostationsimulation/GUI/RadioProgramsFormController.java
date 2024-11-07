package com.coursework.radiostationsimulation.GUI;

import com.coursework.radiostationsimulation.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
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
    private TableView<RadioProgram> radioProgramsTable;
    @FXML
    private TableColumn<RadioProgram, String> radioProgramNameColumn;
    @FXML
    private TableColumn<RadioProgram, String> radioProgramTypeColumn;
    @FXML
    private TableColumn<RadioProgram, Genre> radioProgramGenreColumn;
    @FXML
    private TableColumn<RadioProgram, Integer> radioProgramDurationColumn;

    @FXML
    private ComboBox<String> searchCriteriaCombobox;
    @FXML
    private TextField searchField;

    private ObservableList<RadioProgram> radioPrograms;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        radioPrograms = FXCollections.observableArrayList();

        // Установка элементов в ComboBoxes
        radioProgramType.setItems(FXCollections.observableArrayList("По заявкам слушателей", "Хит-парад"));
        radioProgramGenre.setItems(FXCollections.observableArrayList(Genre.values()));

        // Привязка столбцов таблицы к свойствам класса MusicTrack
        radioProgramNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        radioProgramTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        radioProgramGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        radioProgramDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        // Установка значений для Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3);
        radioProgramDuration.setValueFactory(valueFactory);

        // Разрешение редактирования Spinner
        radioProgramDuration.setEditable(true);

        // Валидация ввода в Spinner
        radioProgramDuration.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            // Проверяем, пустое ли значение или не состоит ли оно только из цифр
            if (newValue.isEmpty() || !newValue.matches("\\d*")) {
                radioProgramDuration.getEditor().setText(oldValue); // Восстанавливаем старое значение
            } else {
                try {
                    int value = Integer.parseInt(newValue);
                    // Проверяем диапазон
                    if (value < 1) {
                        value = 1; // Устанавливаем минимальное значение
                    } else if (value > 3) {
                        value = 3; // Устанавливаем максимальное значение
                    }
                    radioProgramDuration.getValueFactory().setValue(value);
                    radioProgramDuration.getEditor().setText(String.valueOf(value)); // Обновляем текстовое поле
                } catch (NumberFormatException e) {
                    radioProgramDuration.getEditor().setText(oldValue); // Восстанавливаем старое значение
                }
            }
        });

        // Инициализация критериев поиска
        searchCriteriaCombobox.setItems(FXCollections.observableArrayList("Название", "Тип", "Жанр", "Длительность"));
        searchCriteriaCombobox.getSelectionModel().selectFirst();

        radioProgramsTable.setItems(radioPrograms);
    }

    public void addNewRadioProgram() {
        String name = radioProgramName.getText();
        String type = radioProgramType.getValue();
        Genre genre = radioProgramGenre.getValue();
        int duration = radioProgramDuration.getValue();

        RadioProgram newProgram;

        if (name.isEmpty() || type == null || genre == null) {
            showAlert("Ошибка Ввода", "Неполный ввод", "Заполните все обязательные поля.");
        }
        else {
            // Используем switch для создания объекта нужного подкласса
            switch (type) {
                case "По заявкам слушателей":
                    newProgram = new AccordingToListenersRequests(name, type, genre, duration);
                    break;
                case "Хит-парад":
                    newProgram = new HitParade(name, type, genre, duration);
                    break;
                default:
                    return;
            }

            // Добавляем программу в список и обновляем таблицу
            radioPrograms.add(newProgram);
            clearFields();
        }
    }

    private void clearFields() {
        radioProgramName.clear();
        radioProgramType.setValue(null);
        radioProgramGenre.setValue(null);
        radioProgramDuration.getValueFactory().setValue(1);
    }

    private  void showAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
