package com.mytunes.model;

import com.mytunes.dao.SongsInPlaylistDao;
import com.mytunes.dao.SongsInPlaylistDaoImpl;

import java.sql.SQLException;
import java.util.List;

public class Playlist {

    private final SongsInPlaylistDao songsInPlaylistDao = new SongsInPlaylistDaoImpl();

    private final int id;
    private final String name;
    private List<Song> songs;

    public Playlist(int id, String name) throws SQLException {
        this.id = id;
        this.name = name;
        songs = songsInPlaylistDao.getPlaylist(id);
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

    public void addSong(Song song) throws SQLException {
        songs.add(song);
        songsInPlaylistDao.moveSongToPlaylist(getId(), song.getId());
    }

    public void removeSong(Song song) throws SQLException {
        songs.remove(song);
        songsInPlaylistDao.deleteSongFromPlaylist(getId(), song.getId());
    }

    public int getDurationInInteger() {
        int duration = 0;
        for (Song song : songs) {
            duration += song.getDurationInInteger();
        }
        return duration;
    }

    public String getDurationInString() {
        int duration = getDurationInInteger();
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public int getNumberOfSongs() {
        return songs.size();
    }
}
