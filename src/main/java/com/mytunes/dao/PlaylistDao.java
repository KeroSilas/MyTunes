package com.mytunes.dao;

import com.mytunes.model.Playlist;

import java.sql.SQLException;
import java.util.List;

public interface PlaylistDao {

    public List<Playlist> getAllPlaylists() throws SQLException;

    public void deletePlaylist(int id) throws SQLException;

    public void updatePlaylist(int id, String name) throws SQLException;

    public void createPlaylist(String name) throws SQLException;

}
