package com.coursework.radiostationsimulation.GUI;

import com.coursework.radiostationsimulation.models.MusicLibrary;
import com.coursework.radiostationsimulation.models.RadioProgram;
import com.coursework.radiostationsimulation.models.Request;
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
    private Label requestsCountLable;
    @FXML
    private Label completeRequestsLable;

    public static MusicLibrary musicTracks = new MusicLibrary();
    public static ObservableList<RadioProgram> radioPrograms = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTracksAndProgramsCount();
    }

    private void updateTracksAndProgramsCount() {
        musicTracksCountLabel.setText("Треки: " + musicTracks.getMusicTracks().size());
        radioProgramsCountLabel.setText("Радиопрограммы: " + radioPrograms.size());
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
}
