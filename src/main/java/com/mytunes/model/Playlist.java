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

    public int getTotalDuration() throws SQLException {
        return songsInPlaylistDao.getPlaylistDuration(getId());
    }

    public int getNumberOfSongs() throws SQLException {
        return songsInPlaylistDao.getPlaylistSize(getId());
    }
}
