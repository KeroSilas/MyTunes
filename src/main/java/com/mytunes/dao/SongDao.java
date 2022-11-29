package com.mytunes.dao;

import com.mytunes.model.Song;

import java.util.List;

public interface SongDao {

    public List<Song> getAllSongs();

    public void deleteSong();

    public void updateSong();

    public void createSong();

}
