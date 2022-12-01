package com.mytunes.model;

import com.mytunes.dao.SongsInPlaylistDao;
import com.mytunes.dao.SongsInPlaylistDaoImpl;

import java.sql.SQLException;

public class Playlist {

    private final SongsInPlaylistDao songsInPlaylistDao;

    private final int id;
    private final String name;

    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
        songsInPlaylistDao = new SongsInPlaylistDaoImpl();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDurationInString() throws SQLException {
        int duration = songsInPlaylistDao.getPlaylistDuration(getId());
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public int getNumberOfSongs() throws SQLException {
        return songsInPlaylistDao.getPlaylistSize(getId());
    }
}
