package com.mytunes.dao;

import com.mytunes.model.Song;

import java.sql.SQLException;
import java.util.List;

public interface SongDao {

    public List<Song> getAllSongs() throws SQLException;

    public void deleteSong(int id) throws SQLException;

    public void updateSong(int id, String title, String artist, String path) throws SQLException;

    public void createSong(String title, String artist, String path) throws SQLException;

}
