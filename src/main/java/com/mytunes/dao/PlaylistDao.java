package com.mytunes.dao;

import com.mytunes.model.Playlist;
import com.mytunes.model.Song;

import java.sql.SQLException;
import java.util.List;

public interface PlaylistDao {

    List<Playlist> getAllPlaylists() throws SQLException;

    void deletePlaylist(int id) throws SQLException;

    void updatePlaylist(int id, String name) throws SQLException;

    void createPlaylist(String name) throws SQLException;

}
