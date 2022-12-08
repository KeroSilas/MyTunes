package com.mytunes.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Path;
import java.util.Random;

public class Player {

    private Path path;
    private Media media;
    private MediaPlayer mediaPlayer;

    private Song currentSong;
    private Playlist currentPlaylist;
    private ObservableList<Song> allSongs;

    private boolean isShuffling;

    private ListStatus listStatus;

    public enum ListStatus {
        ALL_SONGS,
        PLAYLIST,
    }

    //default constructor for when there is no songs
    public Player() {
        path = Path.of("src/main/resources/com/mytunes/music/default.mp3");
        load(path);
    }

    //constructor for when songs list is not empty
    public Player(ObservableList<Song> songs, Song song) {
        setListStatus(ListStatus.ALL_SONGS);

        path = Path.of("src/main/resources/com/mytunes/music/" + song.getPath());
        load(path);

        currentSong = song;
        allSongs = songs;
    }

    //base loader for passing in a file
    private void load(Path path) {
        media = new Media(path.toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        //mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    //used on end of media or when pressing prev/next
    private void load(Song song) {
        //stores player values in temporary variables so that they can be reapplied on the new media-player object
        double volumeBeforeMediaChange = mediaPlayer.getVolume();
        boolean isMutedBeforeMediaChange = isMuted();
        boolean isRepeatingBeforeMediaChange = isRepeating();
        mediaPlayer.dispose();

        path = Path.of("src/main/resources/com/mytunes/music/" + song.getPath());
        load(path);

        //reapplies player values from before
        mediaPlayer.setVolume(volumeBeforeMediaChange);
        if (isMutedBeforeMediaChange)
            mute(true);
        if (isRepeatingBeforeMediaChange)
            repeat(true);
        play();

        currentSong = song;
    }

    //used when clicking a song on the all songs list
    public void load(ObservableList<Song> songs, Song song) {
        setListStatus(ListStatus.ALL_SONGS);
        load(song);
        allSongs = songs;
    }

    //used when clicking a song on a playlist
    public void load(Playlist playlist, Song song) {
        setListStatus(ListStatus.PLAYLIST);
        load(song);
        currentPlaylist = playlist;
    }

    public void play() {
        mediaPlayer.play();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    //loads next song on either the all songs list or the currently playing playlist
    public void next() {
        if (getListStatus() == Player.ListStatus.ALL_SONGS) {
            if (isShuffling()) {
                Random random = new Random();
                load(allSongs.get(random.nextInt(allSongs.size())));
            } else if (allSongs.indexOf(getCurrentSong()) == allSongs.size() - 1) { //checks if current song is at the end of the list
                load(allSongs.get(0)); //returns to first song on the list
            } else {
                Song nextSong = allSongs.get(allSongs.indexOf(getCurrentSong()) + 1); //gets next song on the list
                load(nextSong);
            }
        } else if (getListStatus() == Player.ListStatus.PLAYLIST) {
            if (isShuffling()) {
                Random random = new Random();
                random.nextInt(currentPlaylist.getSongs().size());
                load(currentPlaylist.getSongs().get(random.nextInt(currentPlaylist.getSongs().size() - 1)));
            } else if (currentPlaylist.getSongs().indexOf(getCurrentSong()) == currentPlaylist.getSongs().size() - 1) {
                load(currentPlaylist.getSongs().get(0));
            } else {
                Song nextSong = currentPlaylist.getSongs().get(currentPlaylist.getSongs().indexOf(getCurrentSong()) + 1);
                load(nextSong);
            }
        }
    }

    //loads previous song on either the all songs list or the currently playing playlist
    public void previous() {
        if (getCurrentTime().toSeconds() > 3) { //checks if less than 3 seconds have passed
            reset(); //resets currently playing song
        } else if (getListStatus() == Player.ListStatus.ALL_SONGS) {
            if (allSongs.indexOf(getCurrentSong()) == 0) { //checks if current song is at the start of the list
                reset();
            } else {
                Song previousSong = allSongs.get(allSongs.indexOf(getCurrentSong()) - 1); //gets previous song on the list
                load(previousSong);
            }
        } else if (getListStatus() == Player.ListStatus.PLAYLIST) {
            if (currentPlaylist.getSongs().indexOf(getCurrentSong()) == 0) {
                reset();
            } else {
                Song previousSong = currentPlaylist.getSongs().get(currentPlaylist.getSongs().indexOf(getCurrentSong()) - 1);
                load(previousSong);
            }
        }
    }

    public void reset() {
        mediaPlayer.seek(mediaPlayer.getStartTime()); //seeks the media-player back to its start time
    }

    public void mute(boolean mute) {
        mediaPlayer.setMute(mute);
    }

    public void repeat(boolean repeat) {
        if (repeat) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } else {
            mediaPlayer.setCycleCount(1);
        }
    }

    public void shuffle(boolean shuffle) {
        isShuffling = shuffle;
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public Media getMedia() {
        return mediaPlayer.getMedia();
    }

    public ListStatus getListStatus() {
        return listStatus;
    }

    //list status must be either ALL_SONGS or PLAYLIST
    public void setListStatus(ListStatus listStatus) {
        this.listStatus = listStatus;
    }

    //volume value must be between 0 and 1
    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    //progress value must be between 0 and 1
    public void setProgress(double progress) {
        mediaPlayer.seek(media.getDuration().multiply(progress));
    }

    public boolean isPlaying() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isRepeating() {
        return mediaPlayer.getCycleCount() == MediaPlayer.INDEFINITE;
    }

    public boolean isShuffling() {
        return isShuffling;
    }

    public boolean isMuted() {
        return mediaPlayer.isMute();
    }

    public void updateCurrentPlaylist(Playlist playlist) {
        currentPlaylist = playlist;
    }

    public void updateCurrentAllSongs(ObservableList<Song> allSongs) {
        this.allSongs = allSongs;
    }

    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return mediaPlayer.currentTimeProperty();
    }

    public void setOnEndOfMedia(Runnable runnable) {
        mediaPlayer.setOnEndOfMedia(runnable);
    }
}