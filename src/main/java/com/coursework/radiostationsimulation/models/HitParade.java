package com.coursework.radiostationsimulation.models;

import javafx.collections.FXCollections;

import java.util.Comparator;

public class HitParade extends RadioProgram {
    // Конструктор
    public HitParade(String name, String type, Genre genre, int duration) {
        super(name, type, genre, duration);
    }

    // Составление плейлиста хит-парада
    public void GenerateHitParadePlaylist(MusicLibrary musicLibrary) {
        clearPlaylist();

        // Проверка: хватает ли длительности программы для добавления трека и соответствует ли он жанру программы
        for (MusicTrack track : musicLibrary.getMusicTracks()){
            if (getTotalDuration() + track.getDuration() <= getDuration() * 60 && track.getGenre() == this.getGenre()) {
                getTracks().add(track);
            }
        }

        // Сортировка треков по рейтингу
        FXCollections.sort(getTracks(), Comparator.comparingInt(MusicTrack::getPopularity).reversed());
    }

    @Override
    public String getTrackListString() {
        StringBuilder trackListBuilder = new StringBuilder();
        int index = 1;
        for (MusicTrack track : getTracks()) {
            trackListBuilder.append(index++)
                    .append(". ")
                    .append(track.toString())
                    .append("\n");
        }
        return trackListBuilder.toString().trim();
    }

}
