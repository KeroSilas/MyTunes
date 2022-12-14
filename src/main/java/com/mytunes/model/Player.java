package com.mytunes.model;

import javafx.beans.property.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Path;
import java.util.*;

/**
 * A class that is responsible for manipulating a MediaPlayer object and load Song objects from playlists/all-songs.
 * Also provides methods such as next(), previous() and shuffle().
 */

public class Player {

    private Path path;
    private Media media;
    private MediaPlayer mediaPlayer;

    private final SimpleBooleanProperty hasLoadedProperty = new SimpleBooleanProperty();
    private Song currentSong;
    private Playlist currentPlaylist;
    private List<Song> allSongs;

    private boolean isShuffling;
    private int shuffleCounter = 0;
    private final List<Integer> shuffleNumbers = new ArrayList<>();

    private ListStatus listStatus;

    //Enum list that are used to specify whether the player is playing from a playlist or the all-songs list.
    public enum ListStatus {
        ALL_SONGS,
        PLAYLIST,
    }

    //Default constructor for when there are no songs.
    public Player() {
        path = Path.of("src/main/resources/com/mytunes/music/default.mp3");
        load(path);
    }

    //Constructor for when songs list is not empty.
    public Player(List<Song> songs, Song song) {
        this(); //Initialize the base MediaPlayer object, so that the next line can utilize its functions.
        load(songs, song);
        pause();
    }

    //Base loader for passing in a file or initializing the MediaPlayer object.
    private void load(Path path) {
        media = new Media(path.toUri().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    //Used in prev/next methods.
    private void load(Song song) {
        //Stores player values in temporary variables so that they can be reapplied on the new media-player object.
        double volumeBeforeMediaChange = mediaPlayer.getVolume();
        boolean isMutedBeforeMediaChange = isMuted();
        boolean isRepeatingBeforeMediaChange = isRepeating();

        mediaPlayer.dispose(); //Removes the previous MediaPlayer object, as a new one will be created from the next load.

        path = Path.of("src/main/resources/com/mytunes/music/", song.getPath());
        load(path);

        //Reapplies player values from before.
        setVolume(volumeBeforeMediaChange);
        mute(isMutedBeforeMediaChange);
        repeat(isRepeatingBeforeMediaChange);
        play();

        currentSong = song;

        //Toggles to either true or false when a song has been loaded.
        //It is used to detect a change to run the update() method in MyTunesController.
        hasLoadedProperty.set(!hasLoadedProperty.get());
    }

    //Used when clicking a song on the all songs list.
    public void load(List<Song> songs, Song song) {
        setListStatus(ListStatus.ALL_SONGS);
        load(song);

        //Resets shuffle algorithm.
        shuffleCounter = 0;
        shuffleNumbers.clear();
        while (shuffleCounter < songs.size()) {
            shuffleNumbers.add(shuffleCounter++);
        }
        Collections.shuffle(shuffleNumbers);
        shuffleNumbers.remove((Integer) songs.indexOf(song));
        shuffleNumbers.add(songs.indexOf(song));

        allSongs = songs;
    }

    //Used when clicking a song on a playlist.
    public void load(Playlist playlist, Song song) {
        setListStatus(ListStatus.PLAYLIST);
        load(song);

        //Resets shuffle algorithm.
        shuffleCounter = 0;
        shuffleNumbers.clear();
        while (shuffleCounter < playlist.getSongs().size()) {
            shuffleNumbers.add(shuffleCounter++);
        }
        Collections.shuffle(shuffleNumbers);
        shuffleNumbers.remove((Integer) playlist.getSongs().indexOf(song));
        shuffleNumbers.add(playlist.getSongs().indexOf(song));

        currentPlaylist = playlist;
    }

    public void play() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime()); //Without this line, a small stutter would occur when resuming after pause.
        mediaPlayer.play();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    //Loads next song on either the all songs list or the currently playing playlist.
    public void next() {
        if (getListStatus() == Player.ListStatus.ALL_SONGS) {
            if (isShuffling()) {
                load(allSongs.get(shuffleNumbers.get(0)));
                shuffleNumbers.add(shuffleNumbers.get(0));
                shuffleNumbers.remove(0);
            } else if (allSongs.indexOf(getCurrentSong()) == allSongs.size() - 1) { //Checks if current song is at the end of the list.
                load(allSongs.get(0)); //Returns to first song on the list.
            } else {
                Song nextSong = allSongs.get(allSongs.indexOf(getCurrentSong()) + 1); //Gets next song on the list.
                load(nextSong);
            }
        } else if (getListStatus() == Player.ListStatus.PLAYLIST) {
            if (isShuffling()) {
                load(currentPlaylist.getSongs().get(shuffleNumbers.get(0)));
                shuffleNumbers.add(shuffleNumbers.get(0));
                shuffleNumbers.remove(0);
            } else if (currentPlaylist.getSongs().indexOf(getCurrentSong()) == currentPlaylist.getSongs().size() - 1) {
                load(currentPlaylist.getSongs().get(0));
            } else {
                Song nextSong = currentPlaylist.getSongs().get(currentPlaylist.getSongs().indexOf(getCurrentSong()) + 1);
                load(nextSong);
            }
        }
    }

    //Loads previous song on either the all songs list or the currently playing playlist.
    public void previous() {
        if (getCurrentTime().toSeconds() > 3) { //Checks if less than 3 seconds have passed.
            reset(); //Resets currently playing song.
        } else if (getListStatus() == Player.ListStatus.ALL_SONGS) {
            if (allSongs.indexOf(getCurrentSong()) == 0) { //Checks if current song is at the start of the list.
                reset();
            } else {
                Song previousSong = allSongs.get(allSongs.indexOf(getCurrentSong()) - 1); //Gets previous song on the list.
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
        mediaPlayer.seek(mediaPlayer.getStartTime()); //Seeks the media-player back to its start time.
    }

    public void mute(boolean mute) {
        mediaPlayer.setMute(mute);
    }

    //Repeats the currently playing song when it reaches end of media.
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

    //Used to retrieve metadata in MyTunesController.
    public Media getMedia() {
        return mediaPlayer.getMedia();
    }

    public ListStatus getListStatus() {
        return listStatus;
    }

    //List status must be either ALL_SONGS or PLAYLIST.
    public void setListStatus(ListStatus listStatus) {
        this.listStatus = listStatus;
    }

    //Volume value must be between 0 and 1.
    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    //Progress value must be between 0 and 1.
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

    public void updateCurrentAllSongs(List<Song> allSongs) {
        this.allSongs = allSongs;
    }

    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return mediaPlayer.currentTimeProperty();
    }

    public ReadOnlyBooleanProperty hasLoadedProperty() {
        return hasLoadedProperty;
    }

    public void setOnEndOfMedia(Runnable runnable) {
        mediaPlayer.setOnEndOfMedia(runnable);
    }

    public void setOnPlaying(Runnable runnable) {
        mediaPlayer.setOnPlaying(runnable);
    }
}