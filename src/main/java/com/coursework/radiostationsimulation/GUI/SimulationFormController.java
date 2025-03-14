package com.coursework.radiostationsimulation.GUI;

import com.coursework.radiostationsimulation.models.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private TableColumn<RadioProgram, Integer> accordingToListenersDurationColumn;
    @FXML
    private TableColumn<RadioProgram, String> accordingToListenersTrackListColumn;

    @FXML
    private TableView<RadioProgram> hitParadeTable;
    @FXML
    private TableColumn<RadioProgram, String> hitParadeNameColumn;
    @FXML
    private TableColumn<RadioProgram, String> hitParadeGenreColumn;
    @FXML
    private TableColumn<RadioProgram, Integer> hitParadeDurationColumn;
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

    // Список треков и радиопрограмм
    public static MusicLibrary musicTracks = new MusicLibrary();
    public static ObservableList<RadioProgram> radioPrograms = FXCollections.observableArrayList();

    // Очередь запросов и радиостанция
    private RequestQueue requestQueue = new RequestQueue();
    private RadioStation radioStation;

    // Переменные для работы с симуляцией
    private Timeline simulationTimer;
    private boolean isSimulationRunning = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Инициализация радиостанции
        radioStation = new RadioStation(musicTracks, radioPrograms, requestQueue);

        // Устанавливаем диапазоны для спиннеров
        simulationDuration.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 1)); // 1-7 дней
        simulationStep.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 30, 30)); // 10-1800 сек (по умолчанию 30)

        // Обновляем данные в таблицах и статистику
        updateStatistics();
        bindRequestTable();

        // Блокируем кнопку 'Стоп' и 'Пауза'
        stopSimulationMenuItem.setDisable(true);
        pauseSimulationCheckMenuItem.setDisable(true);

        // Привязываем действие к CheckMenuItem
        pauseSimulationCheckMenuItem.setOnAction(event -> togglePauseSimulation());
    }

    // Обновление статистики
    private void updateStatistics() {
        musicTracksCountLabel.setText("Треки: " + musicTracks.getMusicTracks().size());
        radioProgramsCountLabel.setText("Радиопрограммы: " + radioPrograms.size());
        requestsCountLabel.setText("Запросы: " + requestQueue.size());
        completeRequestsLabel.setText("Выполненные запросы: " + completeRequestsTable.getItems().size());

        playedTracks.setText(radioStation.getPlayedTracks());
        mostRatedTracks.setText(radioStation.getTopRatedTracks());
        top5Genres.setText(radioStation.getGenrePopularity());
    }

    private void bindRequestTable() {
        // Установка фабрик для колонок с использованием геттеров
        requestTypeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        requestValueColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getValue()));

        completeRequestTypeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        completeRequestValueColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getValue()));

        accordingToListenersNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        accordingToListenersGenreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGenre().toString()));
        accordingToListenersDurationColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDuration()));
        accordingToListenersTrackListColumn.setCellValueFactory(data -> {
            String playlist = data.getValue().getTracks().stream()
                    .map(MusicTrack::toString)
                    .collect(Collectors.joining("\n"));
            return new SimpleStringProperty(playlist);
        });

        hitParadeNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        hitParadeGenreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGenre().toString()));
        hitParadeDurationColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDuration()));
        hitParadeTrackListColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTrackListString()));


        // Привязка данных к таблице запросов
        requestsTable.setItems(requestQueue.getRequests());
        completeRequestsTable.setItems(radioStation.getCompletedRequests());

        // Привязка данных к таблице программ по заявкам слушателей
        FilteredList<RadioProgram> accordingToListenersRequestsPrograms = new FilteredList<>(radioPrograms, program -> program instanceof AccordingToListenersRequests);
        accordingToListenersTable.setItems(accordingToListenersRequestsPrograms);

        // Привязка данных к таблице хит-парадов
        FilteredList<RadioProgram> hitParadesPrograms = new FilteredList<>(radioPrograms, program -> program instanceof HitParade);
        hitParadeTable.setItems(hitParadesPrograms);
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

            // Обновление статистики после закрытия окна
            updateStatistics();
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

            // Обновление статистики после закрытия окна
            updateStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Информация о программе
    public void showAboutDialog(){
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("О программе");
        aboutAlert.setHeaderText(null);
        aboutAlert.setContentText("Курсовая работа: Симуляция составления программ на радиостанции\n"
                + "Версия: 1.0\n"
                + "Автор: студент группы 23ПИнж(б)РПиС-2 Саликов Иван\n"
                + "Описание: Программа предназначена для симуляции работы радиостанции, "
                + "включая составление хит-парадов и программ по заявкам слушателей.");
        aboutAlert.showAndWait();
    }

    // Руководство пользователя
    public void showGuideDialog() {
        Alert guideAlert = new Alert(Alert.AlertType.INFORMATION);
        guideAlert.setTitle("Руководство пользователя");
        guideAlert.setHeaderText("Как пользоваться программой?");

        TextArea guideText = new TextArea(
                "1. Работа с фонотекой:\n"
                        + "   - Перейдите в раздел 'Фонотека'.\n"
                        + "   - Используйте кнопку 'Добавить' для добавления треков.\n"
                        + "   - Выберите трек в таблице и используйте кнопку 'Удалить' для его удаления из фонотеки.\n"
                        + "   - Введите в поле поиска нужную информацию, затем выберите тип поиска и нажмите кнопку 'Поиск' для поиска треков в фонотеке.\n\n"
                        + "2. Работа с радиопрограммами:\n"
                        + "   - Перейдите в раздел 'Радиопрограммы'.\n"
                        + "   - Используйте кнопку 'Добавить' для добавления программ.\n"
                        + "   - Выберите радиопрограмму в таблице и используйте кнопку 'Удалить' для её удаления.\n"
                        + "   - Введите в поле поиска нужную информацию, затем выберите тип поиска и нажмите кнопку 'Поиск' для поиска радиопрограмм.\n\n"
                        + "3. Симуляция:\n"
                        + "   - Перейдите в раздел 'Симуляция'.\n"
                        + "   - Предварительно добавьте от 7 до 12 радиопрограмм и минимум 2 трека.\n"
                        + "   - Введите начальные параметры симуляции и нажмите на кнопку 'Старт' для запуска симуляции.\n"
                        + "   - Для приостановки симуляции нажмите кнопку 'Пауза', для возобновления симуляции нажмите не неё ещё раз.\n"
                        + "   - Для остановки симуляции нажмите кнопку 'Стоп'."
        );
        guideText.setEditable(false);
        guideText.setWrapText(true);

        guideText.setMaxWidth(Double.MAX_VALUE);
        guideText.setMaxHeight(Double.MAX_VALUE);

        DialogPane dialogPane = guideAlert.getDialogPane();
        dialogPane.setContent(guideText);

        guideAlert.showAndWait();
    }

    // Запуск симуляции
    public void startSimulation() {
        try {
            // Проверяем достаточно ли радиопрограмм и треков для запуска симуляции
            if ((radioPrograms.size() >= 7 && radioPrograms.size() <= 12) && musicTracks.size() >= 2) {
                startSumulationMenuItem.setDisable(true);

                // Сброс таблицы запросов и очереди
                requestQueue.clear();

                // Сброс количества запросов
                requestsCountLabel.setText("Запросы: 0");

                // Установка параметров симуляции
                int duration = simulationDuration.getValue();
                int step = simulationStep.getValue();

                radioStation.configureSimulation(duration, step);
                radioStation.startSimulation();

                // Запуск таймера для выполнения simulateStep каждую секунду
                simulationTimer = new Timeline(
                        new KeyFrame(Duration.seconds(1), e -> simulateStep())
                );
                simulationTimer.setCycleCount(Timeline.INDEFINITE); // Будет работать бесконечно
                simulationTimer.play(); // Запускаем таймер

                // Обновление статуса
                updateStatistics();
                isSimulationRunning = true;

                stopSimulationMenuItem.setDisable(false);
                pauseSimulationCheckMenuItem.setDisable(false);
            }
            else {
                showAlert("Ошибка конфигурации", "Недостаточно данных для старта симуляции", "Радиопрограмм должно быть от 7 до 12, а в фонотеке должно быть не менее 2 треков");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для установки паузы/возобновления симуляции
    public void togglePauseSimulation() {
        if (pauseSimulationCheckMenuItem.isSelected()) {
            // Если пункт меню выбран, ставим симуляцию на паузу
            if (simulationTimer != null && isSimulationRunning) {
                simulationTimer.pause();
            }
        } else {
            // Если пункт меню снят, возобновляем симуляцию
            if (simulationTimer != null && isSimulationRunning) {
                simulationTimer.play();
            }
        }
    }

    // Остановка симуляции
    public void stopSimulation() {
        try {
            stopSimulationMenuItem.setDisable(true);
            pauseSimulationCheckMenuItem.setSelected(false);
            pauseSimulationCheckMenuItem.setDisable(true);

            radioStation.stopSimulation();

            if (simulationTimer != null) {
                simulationTimer.stop(); // Останавливаем таймер
            }

            // Обновление статуса
            updateStatistics();
            isSimulationRunning = false;

            startSumulationMenuItem.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Шаг симуляции (вызывается через таймер)
    public void simulateStep() {
        try {
            // Проверяем не закончилась ли симуляция
            if (radioStation.canContinueSimulation()) {
                radioStation.simulateStep(); // симуляция шага

                // Обновление статистики
                updateStatistics();
                requestsTable.refresh();
                accordingToListenersTable.refresh();
                hitParadeTable.refresh();
            }
            else {
                stopSimulation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
