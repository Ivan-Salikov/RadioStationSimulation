package com.coursework.radiostationsimulation.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class RadioProgram {
    private  String name; // название программы
    private  String type; // тип программы
    private Genre genre; // жанр программы
    private  int duration; // длительность программы
    private ObservableList<MusicTrack> tracks; // список треков

    private String lastPlayedArtist = ""; // Последний исполнитель для программы

    // Конструктор
    public RadioProgram(String name, String type, Genre genre, int duration) {
        this.name = name;
        this.type = type;
        this.genre = genre;
        this.duration = duration;
        this.tracks = FXCollections.observableArrayList();
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLastPlayedArtist() {
        return lastPlayedArtist;
    }

    public void setLastPlayedArtist(String lastPlayedArtist) {
        this.lastPlayedArtist = lastPlayedArtist;
    }

    public int getTotalDuration() {
        return tracks.stream().mapToInt(MusicTrack::getDuration).sum();
    }

    public ObservableList<MusicTrack> getTracks() {
        return tracks;
    }

    public String getTrackListString() {
        return tracks.stream()
                .map(MusicTrack::toString)
                .collect(Collectors.joining("; "));
    }

    public void addTrack(MusicTrack track) {
        if (tracks.contains(track)) {
            return;
        }

        if (getTotalDuration() + track.getDuration() <= duration*60) {
            tracks.add(track);
        }
    }

    public boolean isComplete() {
        return getTotalDuration() >= duration*60;
    }

    public void clearPlaylist(){
        tracks.clear();
    }

}
