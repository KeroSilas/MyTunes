package com.mytunes.model;

import javafx.scene.media.Media;

import java.nio.file.Path;

public class Song {

    private final int id;
    private String title;
    private String artist;
    private String category;
    private String path;

    public Song(int id, String title, String artist, String category, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.category = category;
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

    public void setPath(String path) {
        this.path = path;
    }

    //TODO: Fix this
    public int calculateDuration() {
        Path path = Path.of("src/main/resources/com/mytunes/music/" + getPath());
        Media media = new Media(path.toUri().toString());
        return (int) media.getDuration().toSeconds();
    }

    public String getDuration() {
        int duration = 242;
        int minutes = (duration / 60) % 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return artist + " - " + title;
    }
}
