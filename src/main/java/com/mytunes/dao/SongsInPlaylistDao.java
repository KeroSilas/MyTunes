package com.mytunes.dao;

import com.mytunes.model.Song;

import java.sql.SQLException;
import java.util.List;

public interface SongsInPlaylistDao {

    List<Song> getPlaylist(int playlistId) throws SQLException;

    int getPlaylistDuration(int playlistId) throws SQLException;

    int getPlaylistSize(int playlistId) throws SQLException;

    void deleteSongFromPlaylist(int playlistId, int songId) throws SQLException;

    void moveSongToPlaylist(int playlistId, int songId) throws SQLException;

}
