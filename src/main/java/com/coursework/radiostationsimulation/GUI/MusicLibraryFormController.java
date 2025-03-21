package com.coursework.radiostationsimulation.GUI;

import com.coursework.radiostationsimulation.models.Genre;
import com.coursework.radiostationsimulation.models.MusicTrack;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.DateTimeException;
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
    private TableView<MusicTrack> musicLibraryTable;
    @FXML
    private TableColumn<MusicTrack, String> musicTrackNameColumn;
    @FXML
    private TableColumn<MusicTrack, Genre> musicTrackGenreColumn;
    @FXML
    private TableColumn<MusicTrack, String> musicTrackAuthorColumn;
    @FXML
    private TableColumn<MusicTrack, String> musicTrackArtistColumn;
    @FXML
    private TableColumn<MusicTrack, String> musicTrackAlbumColumn;
    @FXML
    private TableColumn<MusicTrack, LocalDate> musicTrackReleaseDateColumn;
    @FXML
    private TableColumn<MusicTrack, Integer> musicTrackDurationColumn;

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

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> searchCriteriaComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Привязка столбцов таблицы к свойствам класса MusicTrack
        musicTrackNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        musicTrackGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        musicTrackAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        musicTrackArtistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        musicTrackAlbumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        musicTrackReleaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        musicTrackDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

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

        // Инициализация критериев поиска
        searchCriteriaComboBox.setItems(FXCollections.observableArrayList(
                "Название", "Жанр", "Автор", "Исполнитель", "Альбом", "Год выпуска", "Длительность"
        ));
        searchCriteriaComboBox.getSelectionModel().selectFirst();

        musicLibraryTable.setItems(SimulationFormController.musicTracks.getMusicTracks());
    }

    // Добавление нового трека в фонотеку
    public void addNewTrack() {
        String title = musicTrackName.getText();
        Genre genre = musicTrackGenre.getValue();
        String author = musicTrackAuthor.getText();
        String artist = musicTrackArtist.getText();
        String album = musicTrackAlbum.getText();
        LocalDate releaseDate = musicTrackReleaseDate.getValue();
        int duration = musicTrackDuration.getValue();

        if (title.isEmpty() || genre == null || author.isEmpty() || artist.isEmpty() || album.isEmpty()) {
            showAlert("Ошибка Ввода", "Неполный ввод", "Заполните все обязательные поля.");
        }
        else {
            MusicTrack newTrack = new MusicTrack(title, genre, author, artist, album, releaseDate, duration);
            SimulationFormController.musicTracks.addMusicTrack(newTrack);
            clearFields();
        }
    }

    // Удаление выбранного трека
    public void deleteSelectedTrack() {
        MusicTrack selectedTrack = musicLibraryTable.getSelectionModel().getSelectedItem();
        if (selectedTrack != null) {
            SimulationFormController.musicTracks.removeMusicTrack(selectedTrack);
        }
        else {
            showAlert("Ошибка удаления", "Нет выбора", "Выберите трек для удаления.");
        }
    }

    // Поиск трека
    public void searchTrack() {
        String searchTerm = searchField.getText().trim();
        String selectedCriteria = searchCriteriaComboBox.getValue();

        // Фильтруем фонотеку в соответствие с выбранным критерием поиска
        FilteredList<MusicTrack> filteredList = new FilteredList<>(SimulationFormController.musicTracks.getMusicTracks(), track -> {
            if (searchTerm.isEmpty()) {
                return true;
            }
            switch (selectedCriteria) {
                case "Название":
                    return track.getTitle().equalsIgnoreCase(searchTerm);
                case "Жанр":
                    return track.getGenre().toString().equalsIgnoreCase(searchTerm);
                case "Автор":
                    return track.getAuthor().equalsIgnoreCase(searchTerm);
                case "Исполнитель":
                    return track.getArtist().equalsIgnoreCase(searchTerm);
                case "Альбом":
                    return track.getAlbum().equalsIgnoreCase(searchTerm);
                case "Год выпуска":
                    try {
                        LocalDate date = LocalDate.parse(searchTerm);
                        return track.getReleaseDate().equals(date);
                    } catch (DateTimeException e) {
                        return false;
                    }
                case "Длительность":
                    try {
                        int duration = Integer.parseInt(searchTerm);
                        return track.getDuration() == duration;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                default:
                    return false;
            }
        });

        musicLibraryTable.setItems(filteredList);
    }

    // Очищаем форму ввода информации о треке
    private void clearFields() {
        musicTrackName.clear();
        musicTrackGenre.setValue(null);
        musicTrackAuthor.clear();
        musicTrackArtist.clear();
        musicTrackAlbum.clear();
        musicTrackReleaseDate.setValue(LocalDate.now());
        musicTrackDuration.getValueFactory().setValue(1);
    }

    // Окно с предупреждением об ошибке
    private  void showAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
