package com.mytunes.model;

import com.mytunes.dao.SongsInPlaylistDao;
import com.mytunes.dao.SongsInPlaylistDaoImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that contains a playlists id and name, as well as all the songs in a playlist.
 * Also sends queries to the database.
 */

public class Playlist {

    private final SongsInPlaylistDao songsInPlaylistDao;

    private final int id;
    private String name;
    private List<Song> songs;

    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
        songsInPlaylistDao = new SongsInPlaylistDaoImpl();
        songs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    //Adds a new song on the playlist both locally and on the database.
    public void addSong(Song song) {
        songs.add(song);
        songsInPlaylistDao.moveSongToPlaylist(getId(), song.getId()); //adds the song to the database as well
    }

    public void setName(String name) {
        this.name = name;
    }

    //Removes a song from the playlist both locally and on the database.
    public void removeSong(Song song) {
        songs.remove(song);
        songsInPlaylistDao.deleteSongFromPlaylist(getId(), song.getId());
    }

    //Returns the playlist duration in seconds.
    private int getDurationInInteger() {
        int duration = 0;
        for (Song song : songs) {
            duration += song.getDurationInInteger();
        }
        return duration;
    }

    //Returns the playlist duration in a string such as this: "02:32:23".
    public String getDurationInString() {
        int duration = getDurationInInteger();
        int hours = duration / 3600;
        int minutes = (duration / 60) % 60;
        int seconds = duration % 60;
        if (hours == 0)
            return String.format("%02d:%02d", minutes, seconds);
        else
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public int getNumberOfSongs() {
        return songs.size();
    }
}
