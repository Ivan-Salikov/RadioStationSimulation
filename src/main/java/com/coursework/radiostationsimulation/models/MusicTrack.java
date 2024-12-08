package com.coursework.radiostationsimulation.models;

import java.time.LocalDate;

public class MusicTrack {
    private String title; // название трека
    private Genre genre; // жанр трека
    private String author; // автор трека
    private String artist; // испольнитель трека
    private String album; // альбом, из которого трек
    private LocalDate releaseDate; // дата выхода трека
    private int duration; // длительность трека в минутах
    private int popularity; // рейтинг популярности трека

    // Конструктор
    public MusicTrack(String title, Genre genre, String author, String artist, String album, LocalDate releaseDate, int duration) {
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.artist = artist;
        this.album = album;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.popularity = 0;
    }

    // Геттеры и сеттеры
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPopularity() {
        return popularity;
    }

    // Сброс популярности трека
    public void resetPopularity() {
        popularity = 0;
    }

    // Увеличение популярности трека
    public void incrementPopularity() {
        this.popularity++;
    }

    @Override
    public String toString() {
        return  title + " (" +
                album + ", "+
                releaseDate + ')' +
                " — " + artist;
    }
}
