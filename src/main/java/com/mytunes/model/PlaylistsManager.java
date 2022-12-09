package com.mytunes.model;

import com.mytunes.dao.PlaylistDao;
import com.mytunes.dao.PlaylistDaoImpl;
import com.mytunes.dao.SongsInPlaylistDao;
import com.mytunes.dao.SongsInPlaylistDaoImpl;

import java.util.List;

public class PlaylistsManager {

    private final PlaylistDao playlistDao;
    private final List<Playlist> allPlaylists;

    public PlaylistsManager() {
        playlistDao = new PlaylistDaoImpl();
        SongsInPlaylistDao songsInPlaylistDao = new SongsInPlaylistDaoImpl();
        allPlaylists = playlistDao.getAllPlaylists();
        for (Playlist p : allPlaylists) {
            p.setSongs(songsInPlaylistDao.getPlaylist(p.getId())); //retrieves the songs on the playlist from the database
        }
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
                allPlaylists.get(allPlaylists.indexOf(p)).setName(name);
        }
        playlistDao.updatePlaylist(playlist.getId(), name);
    }

    public void removePlaylist(Playlist playlist) {
        allPlaylists.removeIf(p -> p.getId() == playlist.getId());
        playlistDao.deletePlaylist(playlist.getId());
    }
}
