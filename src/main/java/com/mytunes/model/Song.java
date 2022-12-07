package com.mytunes.model;


import javafx.util.Duration;

public class Song {

    private final int id;
    private final String title;
    private final String artist;
    private final String category;
    private final int duration;
    private final String path;

    public Song(int id, String title, String artist, String category, int duration, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.duration = duration;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getCategory() {
        return category;
    }

    public Duration getDuration() {
        return new Duration(duration);
    }

    public int getDurationInInteger() {
        return duration;
    }

    public String getDurationInString() {
        int duration = getDurationInInteger();
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", artist, title);
    }
}
