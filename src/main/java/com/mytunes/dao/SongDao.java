package com.mytunes.dao;

import com.mytunes.model.Song;

import java.sql.SQLException;
import java.util.List;

public interface SongDao {

    List<Song> getAllSongs() throws SQLException;

    void deleteSong(int id) throws SQLException;

    void updateSong(int id, String title, String artist, String path) throws SQLException;

    void createSong(String title, String artist, String path) throws SQLException;

}
