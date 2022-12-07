package com.mytunes.dao;

import com.mytunes.model.Song;

import java.util.List;

public interface SongsInPlaylistDao {

    List<Song> getPlaylist(int playlistId);

    void deleteSongFromPlaylist(int playlistId, int songId);

    void moveSongToPlaylist(int playlistId, int songId);

}
