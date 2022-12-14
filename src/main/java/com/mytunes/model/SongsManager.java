package com.mytunes.model;

import com.mytunes.dao.SongDao;
import com.mytunes.dao.SongDaoImpl;

import java.util.List;

/**
 * A class that is responsible for manipulating the retrieved lists from SongDao.
 * Also sends queries to the database.
 */

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

    //Creates a new song both locally and on the database.
    public void addSong(String title, String artist, String category, int duration, String path) {
        int songId = songDao.createSong(title, artist, category, duration, path);
        allSongs.add(new Song(songId, title, artist, category, duration, path));
    }

    //Updates a song both locally and on the database.
    public void updateSong(Song song, String title, String artist, String category, int duration, String path) {
        for (Song s : allSongs) {
            if (s.getId() == song.getId()) {
                allSongs.get(allSongs.indexOf(s)).setTitle(title);
                allSongs.get(allSongs.indexOf(s)).setArtist(artist);
                allSongs.get(allSongs.indexOf(s)).setCategory(category);
                allSongs.get(allSongs.indexOf(s)).setDuration(duration);
                allSongs.get(allSongs.indexOf(s)).setPath(path);
            }
        }
        songDao.updateSong(song.getId(), title, artist, category, duration, path);
    }

    //Returns a list of the songs that the was searched for on the database.
    public List<Song> searchSongs(String search) {
        return songDao.searchSong(search);
    }

    //Removes a song both locally and on the database.
    public void removeSong(Song song) {
        allSongs.removeIf(s -> s.getId() == song.getId());
        songDao.deleteSong(song.getId());
    }
}
