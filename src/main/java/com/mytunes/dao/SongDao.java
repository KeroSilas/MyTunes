package com.mytunes.dao;

import com.mytunes.model.Song;

import java.util.List;

public interface SongDao {

    List<Song> getAllSongs();

    void deleteSong(int id);

    void updateSong(int id, String title, String artist, String category, int duration, String path);

    void createSong(String title, String artist, String category, int duration, String path);

    List<Song> searchSong(String search);

}
