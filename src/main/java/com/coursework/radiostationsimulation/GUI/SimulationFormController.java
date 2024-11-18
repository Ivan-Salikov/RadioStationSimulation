package com.coursework.radiostationsimulation.GUI;

import com.coursework.radiostationsimulation.models.MusicLibrary;
import com.coursework.radiostationsimulation.models.RadioProgram;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

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
    private TableView<?> requestsTable;
    @FXML
    private TableView<?> completeRequestsTable;
    @FXML
    private TableView<?> accordingToListenersTable;
    @FXML
    private TableView<?> hitParadeTable;

    @FXML
    private Spinner<?> simulationDuration;
    @FXML
    private Spinner<?> simulationStep;
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
