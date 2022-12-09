package com.mytunes.dao;

import com.mytunes.model.Playlist;

import java.util.List;

public interface PlaylistDao {

    List<Playlist> getAllPlaylists();

    void deletePlaylist(int id);

    void updatePlaylist(int id, String name);

    int createPlaylist(String name);

}
