package com.mytunes.model;

import com.mytunes.dao.SongDao;
import com.mytunes.dao.SongDaoImpl;

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

    public void addSong(String title, String artist, String category, int duration, String path) {
        int songId = songDao.createSong(title, artist, category, duration, path);
        allSongs.add(new Song(songId, title, artist, category, duration, path));
    }

    public void updateSong(Song song, String title, String artist, String category, int duration, String path) {
        for (Song s : allSongs) {
            if (s.getId() == song.getId())
                allSongs.set(allSongs.indexOf(s), new Song(song.getId(), title, artist, category, duration, path));
        }
        songDao.updateSong(song.getId(), title, artist, category, duration, path);
    }

    public List<Song> searchSongs(String search) {
        return songDao.searchSong(search);
    }

    public void removeSong(Song song) {
        allSongs.removeIf(s -> s.getId() == song.getId());
        songDao.deleteSong(song.getId());
    }
}
