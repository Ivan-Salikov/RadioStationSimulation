package com.coursework.radiostationsimulation.GUI;

import com.coursework.radiostationsimulation.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SimulationFormController implements Initializable {
    @FXML
    private MenuItem openMusicLibraryMenuItem;
    @FXML
    private MenuItem openRadioProgramsMenuItem;

    @FXML
    private MenuItem startSumulationMenuItem;
    @FXML
    private MenuItem stopSimulationMenuItem;
    @FXML
    private CheckMenuItem pauseSimulationCheckMenuItem;

    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private MenuItem instructionMenuItem;

    @FXML
    private TableView<Request> requestsTable;
    @FXML
    private TableColumn<Request, String> requestTypeColumn;
    @FXML
    private TableColumn<Request, String> requestValueColumn;

    @FXML
    private TableView<Request> completeRequestsTable;
    @FXML
    private TableColumn<Request, String> completeRequestTypeColumn;
    @FXML
    private TableColumn<Request, String> completeRequestValueColumn;


    @FXML
    private TableView<RadioProgram> accordingToListenersTable;
    @FXML
    private TableColumn<RadioProgram, String> accordingToListenersNameColumn;
    @FXML
    private TableColumn<RadioProgram, String> accordingToListenersGenreColumn;
    @FXML
    private TableColumn<RadioProgram, String> accordingToListenersDurationColumn;
    @FXML
    private TableColumn<RadioProgram, String> accordingToListenersTrackListColumn;

    @FXML
    private TableView<RadioProgram> hitParadeTable;
    @FXML
    private TableColumn<RadioProgram, String> hitParadeNameColumn;
    @FXML
    private TableColumn<RadioProgram, String> hitParadeGenreColumn;
    @FXML
    private TableColumn<RadioProgram, String> hitParadeDurationColumn;
    @FXML
    private TableColumn<RadioProgram, String> hitParadeTrackListColumn;


    @FXML
    private Spinner<Integer> simulationDuration;
    @FXML
    private Spinner<Integer> simulationStep;
    @FXML
    private Label radioProgramsCountLabel;
    @FXML
    private Label musicTracksCountLabel;

    @FXML
    private TextArea playedTracks;
    @FXML
    private TextArea mostRatedTracks;
    @FXML
    private TextArea top5Genres;
    @FXML
    private Label requestsCountLabel;
    @FXML
    private Label completeRequestsLabel;

    public static MusicLibrary musicTracks = new MusicLibrary();
    public static ObservableList<RadioProgram> radioPrograms = FXCollections.observableArrayList();

    private RequestQueue requestQueue = new RequestQueue();
    private RadioStation radioStation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Инициализация радиостанции
        radioStation = new RadioStation(musicTracks, radioPrograms, requestQueue);

        // Устанавливаем диапазоны для спиннеров
        simulationDuration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 1)); // 1-7 дней
        simulationStep.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 30, 30)); // 10-1800 сек (по умолчанию 30)

        updateTracksAndProgramsCount();
        bindRequestTable();
    }

    private void updateTracksAndProgramsCount() {
        musicTracksCountLabel.setText("Треки: " + musicTracks.getMusicTracks().size());
        radioProgramsCountLabel.setText("Радиопрограммы: " + radioPrograms.size());
        requestsCountLabel.setText("Запросы: " + requestQueue.size());
    }

    private void bindRequestTable() {
        // Привязка данных к таблице запросов
        requestsTable.setItems(requestQueue.getRequests());

        // Установка фабрик для колонок с использованием геттеров
        requestTypeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        requestValueColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getValue()));
    }

    // Открывает окно для работы с фонотекой.
    public void openMusicLibraryWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coursework/radiostationsimulation/MusicLibraryForm.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Фонотека");
            stage.initModality(Modality.WINDOW_MODAL); // Модальное окно
            stage.initOwner(musicTracksCountLabel.getScene().getWindow()); // Привязываем к текущему окну
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Обновление меток после закрытия окна
            updateTracksAndProgramsCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Открывает окно для работы с радиопрограммами.
    public void openRadioProgramsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/coursework/radiostationsimulation/RadioProgramsForm.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Радиопрограммы");
            stage.initModality(Modality.WINDOW_MODAL); // Модальное окно
            stage.initOwner(radioProgramsCountLabel.getScene().getWindow()); // Привязываем к текущему окну
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Обновление меток после закрытия окна
            updateTracksAndProgramsCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Запуск симуляции
    public void startSimulation() {
        try {
            int duration = simulationDuration.getValue();
            int step = simulationStep.getValue();

            radioStation.configureSimulation(duration, step);
            radioStation.startSimulation();

            // Обновление статуса
            updateTracksAndProgramsCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Остановка симуляции
    public void stopSimulation() {
        try {
            radioStation.stopSimulation();

            // Обновление статуса
            updateTracksAndProgramsCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Шаг симуляции (вызывается вручную или через таймер)
    public void simulateStep() {
        try {
            if (radioStation.canContinueSimulation()) {
                radioStation.simulateStep();

                // Обновление таблицы запросов
                updateTracksAndProgramsCount();
                requestsTable.refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
