package com.coursework.radiostationsimulation.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

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
        musicTracks.remove(track);
    }

    public MusicTrack getRandomTrack() {
        if (musicTracks.isEmpty()) return null;
        return musicTracks.get((int) (Math.random() * musicTracks.size()));
    }

    // Поиск трека по названию
    public MusicTrack findTrackByName(String name) {
        return musicTracks.stream()
                .filter(track -> track.getTitle().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // Поиск трека по автору
    public MusicTrack findTrackByAuthor(String author) {
        return musicTracks.stream()
                .filter(track -> track.getAuthor().equalsIgnoreCase(author))
                .findFirst()
                .orElse(null);
    }

    // Поиск трека по альбому
    public MusicTrack findTrackByAlbum(String album) {
        return musicTracks.stream()
                .filter(track -> track.getAlbum().equalsIgnoreCase(album))
                .findFirst()
                .orElse(null);
    }

    // Поиск трека по исполнителю
    public MusicTrack findTrackByPerformer(String performer) {
        return musicTracks.stream()
                .filter(track -> track.getArtist().equalsIgnoreCase(performer))
                .findFirst()
                .orElse(null);
    }

    // Получение треков определенного жанра
    public ObservableList<MusicTrack> getTracksByGenre(Genre genre) {
        return FXCollections.observableArrayList(
                musicTracks.stream()
                        .filter(track -> track.getGenre() == genre)
                        .collect(Collectors.toList())
        );
    }

    // Получение всех треков
    public ObservableList<MusicTrack> getMusicTracks(){
        return musicTracks;
    }

    public int size() {
        return musicTracks.size();
    }
}
