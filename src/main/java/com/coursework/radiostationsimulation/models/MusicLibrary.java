package com.coursework.radiostationsimulation.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MusicLibrary {
    private ObservableList<MusicTrack> musicTracks; // фонотека

    // Конструктор
    public MusicLibrary() {
        this.musicTracks = FXCollections.observableArrayList();
    }

    // Добавление нового трека в список
    public void addMusicTrack(MusicTrack track) {
        musicTracks.add(track);
    }

    // Удаление трека из списка
    public void removeMusicTrack(MusicTrack track) {
    }

    // Получение всех треков
    public ObservableList<MusicTrack> getMusicTracks(){
        return musicTracks;
    }
}
