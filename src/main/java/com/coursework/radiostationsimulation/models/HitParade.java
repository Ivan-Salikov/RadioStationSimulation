package com.coursework.radiostationsimulation.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class HitParade extends RadioProgram {
    // Конструктор
    public HitParade(String name, String type, Genre genre, int duration) {
        super(name, type, genre, duration);
    }


    public void GenerateHitParadePlaylist(MusicLibrary musicLibrary) {
        clearPlaylist();

        for (MusicTrack track : musicLibrary.getMusicTracks()){
            if (getTotalDuration() + track.getDuration() <= getDuration() * 60 && track.getGenre() == this.getGenre()) {
                getTracks().add(track);
            }
        }

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
