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

    public void addPlaylist(String name) {
        int playlistId = playlistDao.createPlaylist(name);
        allPlaylists.add(new Playlist(playlistId, name));
    }

    public void updatePlaylist(Playlist playlist, String name) {
        for (Playlist p : allPlaylists) {
            if (p.getId() == playlist.getId())
                allPlaylists.set(allPlaylists.indexOf(p), new Playlist(playlist.getId(), name));
        }
        playlistDao.updatePlaylist(playlist.getId(), name);
    }

    public void removePlaylist(Playlist playlist) {
        allPlaylists.remove(playlist);
        playlistDao.deletePlaylist(playlist.getId());
    }
}
