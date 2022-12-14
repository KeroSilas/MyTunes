package com.mytunes.model;

import com.mytunes.dao.PlaylistDao;
import com.mytunes.dao.PlaylistDaoImpl;
import com.mytunes.dao.SongsInPlaylistDao;
import com.mytunes.dao.SongsInPlaylistDaoImpl;

import java.util.List;

/**
 * A class that is responsible for manipulating the retrieved lists from PlaylistDao.
 * Also sends queries to the database.
 */

public class PlaylistsManager {

    private final PlaylistDao playlistDao;
    private final List<Playlist> allPlaylists;

    //Constructor that retrieves all the songs in a playlist from the database and stores them in each playlist object locally.
    public PlaylistsManager() {
        playlistDao = new PlaylistDaoImpl();
        SongsInPlaylistDao songsInPlaylistDao = new SongsInPlaylistDaoImpl();
        allPlaylists = playlistDao.getAllPlaylists();
        for (Playlist p : allPlaylists) {
            p.setSongs(songsInPlaylistDao.getPlaylist(p.getId()));
        }
    }

    public List<Playlist> getAllPlaylists() {
        return allPlaylists;
    }

    //Creates a new playlist both locally and on the database.
    public void addPlaylist(String name) {
        int playlistId = playlistDao.createPlaylist(name);
        allPlaylists.add(new Playlist(playlistId, name));
    }

    //Updates a playlist both locally and on the database.
    public void updatePlaylist(Playlist playlist, String name) {
        for (Playlist p : allPlaylists) {
            if (p.getId() == playlist.getId())
                allPlaylists.get(allPlaylists.indexOf(p)).setName(name);
        }
        playlistDao.updatePlaylist(playlist.getId(), name);
    }

    //Removes a playlist both locally and on the database.
    public void removePlaylist(Playlist playlist) {
        allPlaylists.removeIf(p -> p.getId() == playlist.getId());
        playlistDao.deletePlaylist(playlist.getId());
    }
}