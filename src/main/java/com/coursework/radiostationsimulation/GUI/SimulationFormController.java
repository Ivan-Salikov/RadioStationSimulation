package com.coursework.radiostationsimulation.GUI;

import com.coursework.radiostationsimulation.models.MusicLibrary;
import com.coursework.radiostationsimulation.models.RadioProgram;
import com.coursework.radiostationsimulation.models.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SimulationFormController {
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
}
