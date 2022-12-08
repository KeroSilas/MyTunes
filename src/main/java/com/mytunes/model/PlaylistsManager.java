package com.mytunes.model;

import com.mytunes.dao.PlaylistDao;
import com.mytunes.dao.PlaylistDaoImpl;

import java.util.List;

public class PlaylistsManager {

    private final PlaylistDao playlistDao;
    private final List<Playlist> allPlaylists;

    public PlaylistsManager() {
        playlistDao = new PlaylistDaoImpl();
        allPlaylists = playlistDao.getAllPlaylists();
    }

    public List<Playlist> getAllPlaylists() {
        return allPlaylists;
    }

    public void addPlaylist(Playlist playlist) {
        allPlaylists.add(playlist);
        playlistDao.createPlaylist(playlist.getName());
    }

    public void removePlaylist(Playlist playlist) {
        allPlaylists.remove(playlist);
        playlistDao.deletePlaylist(playlist.getId());
    }
}
