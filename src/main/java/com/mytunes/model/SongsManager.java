package com.mytunes.model;

import com.mytunes.dao.SongDao;
import com.mytunes.dao.SongDaoImpl;

import java.util.ArrayList;
import java.util.List;

public class SongsManager {

    private final SongDao songDao;
    private final List<Song> allSongs;

    public SongsManager() {
        songDao = new SongDaoImpl();
        allSongs = songDao.getAllSongs();
    }

    public List<Song> getAllSongs() {
        return allSongs;
    }

    public void addSong(Song song) {
        allSongs.add(song);
        songDao.createSong(song.getTitle(), song.getArtist(), song.getCategory(), song.getDurationInInteger(), song.getPath());
    }

    public void removeSong(Song song) {
        allSongs.remove(song);
        songDao.deleteSong(song.getId());
    }
}
