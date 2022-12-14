package com.mytunes.model;

import javafx.util.Duration;

/**
 * A class that contains a song's id, title, artist, category, duration and path.
 */

public class Song {

    private final int id;
    private String title;
    private String artist;
    private String category;
    private int duration;
    private String path;

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

    //Returns the song duration in seconds.
    public int getDurationInInteger() {
        return duration;
    }

    //Returns the song duration in a string such as this: "02:23".
    public String getDurationInString() {
        int duration = getDurationInInteger();
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String getPath() {
        return path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //A toString() method that formats a song's artist and title as such: "Adele - Hello".
    @Override
    public String toString() {
        return String.format("%s - %s", artist, title);
    }
}
