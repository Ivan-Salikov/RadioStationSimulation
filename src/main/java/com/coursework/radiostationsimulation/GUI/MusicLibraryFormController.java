package com.coursework.radiostationsimulation.GUI;

import com.coursework.radiostationsimulation.models.Genre;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MusicLibraryFormController implements Initializable {
    @FXML
    private Button addNewMusicTrack;
    @FXML
    private Button deleteMusicTrack;
    @FXML
    private Button searchMusicTrack;

    @FXML
    private TableView<?> musicLibraryTable;
    @FXML
    private TableColumn<?, ?> musicTrackNameColumn;
    @FXML
    private TableColumn<?, ?> musicTrackGenreColumn;
    @FXML
    private TableColumn<?, ?> musicTrackAuthorColumn;
    @FXML
    private TableColumn<?, ?> musicTrackArtistColumn;
    @FXML
    private TableColumn<?, ?> musicTrackAlbumColumn;
    @FXML
    private TableColumn<?, ?> musicTrackReleaseDateColumn;
    @FXML
    private TableColumn<?, ?> musicTrackDurationColumn;

    @FXML
    private TextField musicTrackName;
    @FXML
    private ComboBox<Genre> musicTrackGenre;
    @FXML
    private TextField musicTrackAuthor;
    @FXML
    private TextField musicTrackArtist;
    @FXML
    private TextField musicTrackAlbum;
    @FXML
    private DatePicker musicTrackReleaseDate;
    @FXML
    private Spinner<Integer> musicTrackDuration;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Установка элементов в ComboBox жанров
        musicTrackGenre.setItems(FXCollections.observableArrayList(Genre.values()));

        // Установка текущей даты в DatePicker
        musicTrackReleaseDate.setValue(LocalDate.now());

        // Установка фабрики для ячеек даты, чтобы отключить выбор будущих дат
        musicTrackReleaseDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                // Запрещаем выбор будущих дат
                if (item != null && item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // Устанавливаем цвет фона для недоступных ячеек
                }
            }
        });

        // Установка значений для Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 600);
        musicTrackDuration.setValueFactory(valueFactory);

        // Разрешение редактирования Spinner
        musicTrackDuration.setEditable(true);

        // Валидация ввода в Spinner
        // Валидация ввода в Spinner
        musicTrackDuration.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            // Проверяем, пустое ли значение или не состоит ли оно только из цифр
            if (newValue.isEmpty() || !newValue.matches("\\d*")) {
                musicTrackDuration.getEditor().setText(oldValue); // Восстанавливаем старое значение
            } else {
                try {
                    int value = Integer.parseInt(newValue);
                    // Проверяем диапазон
                    if (value < 1) {
                        value = 1; // Устанавливаем минимальное значение
                    } else if (value > 600) {
                        value = 600; // Устанавливаем максимальное значение
                    }
                    musicTrackDuration.getValueFactory().setValue(value);
                    musicTrackDuration.getEditor().setText(String.valueOf(value)); // Обновляем текстовое поле
                } catch (NumberFormatException e) {
                    musicTrackDuration.getEditor().setText(oldValue); // Восстанавливаем старое значение
                }
            }
        });
    }
}
