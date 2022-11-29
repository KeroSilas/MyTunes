package com.mytunes.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private final int id;
    private String name;
    private final List<Song> songs;

    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Song> getAllSongs() {
        return songs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public int calculateDuration() {
        int duration = 0;
        for (Song song : songs) {
            duration += song.calculateDuration();
        }
        return duration;
    }

    public String getDuration() {
        int duration = calculateDuration();
        int hours = duration / 3600;
        int minutes = (duration / 60) % 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
