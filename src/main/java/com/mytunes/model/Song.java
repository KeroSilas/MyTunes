package com.mytunes.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Path;

public class Song {

    MediaPlayer mediaPlayer;

    private final int id;
    private final String title;
    private final String artist;
    private final String category;
    private final String path;
    private final int duration;

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

    public String getDuration() {
        return String.format("%02d:%02d", duration / 60, duration % 60);
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return artist + " - " + title;
    }

    public static void main(String[] args) {
        Song song = new Song(1, "test", "test", "test", 4, "test.wav");
        System.out.println(song.getDuration());
    }
}
