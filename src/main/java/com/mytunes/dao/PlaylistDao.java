package com.mytunes.dao;

import com.mytunes.model.Playlist;

import java.util.List;

public interface PlaylistDao {

    public List<Playlist> getAllPlaylists();

    public void deletePlaylist();

    public void updatePlaylist();

    public void createPlaylist();

}
