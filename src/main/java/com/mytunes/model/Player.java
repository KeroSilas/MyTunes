package com.mytunes.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Path;
import java.sql.SQLException;

public class Player {

    private Path path;
    private Media media;
    private MediaPlayer mediaPlayer;

    private Song currentSong;
    private Playlist currentPlaylist;
    private ObservableList<Song> allSongs;

    private double volumeBeforeMediaChange;
    private boolean isPlayingBeforeMediaChange;
    private boolean isMutedBeforeMediaChange;
    private boolean isRepeatingBeforeMediaChange;

    private boolean isShuffling;

    private ListStatus listStatus;

    public enum ListStatus {
        ALL_SONGS,
        PLAYLIST,
        DEFAULT,
    }

    public Player() {
        setListStatus(ListStatus.DEFAULT);

        path = Path.of("src/main/resources/com/mytunes/music/default.mp3");
        media = new Media(path.toUri().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    public Player(ObservableList<Song> songs, Song song) throws SQLException {
        setListStatus(ListStatus.ALL_SONGS);

        path = Path.of("src/main/resources/com/mytunes/music/" + song.getPath());
        media = new Media(path.toUri().toString());
        mediaPlayer = new MediaPlayer(media);

        currentSong = song;
        allSongs = songs;
    }

    //only used in methods next() and previous()
    private void load(Song song) {
        path = Path.of("src/main/resources/com/mytunes/music/" + song.getPath());
        media = new Media(path.toUri().toString());

        volumeBeforeMediaChange = mediaPlayer.getVolume();
        isPlayingBeforeMediaChange = mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
        isMutedBeforeMediaChange = isMuted();
        isRepeatingBeforeMediaChange = isRepeating();

        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setVolume(volumeBeforeMediaChange);
        if (isPlayingBeforeMediaChange)
            mediaPlayer.play();
        if (isMutedBeforeMediaChange)
            mediaPlayer.setMute(true);
        if (isRepeatingBeforeMediaChange)
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        currentSong = song;
    }

    public void load(ObservableList<Song> songs, Song song) {
        setListStatus(ListStatus.ALL_SONGS);

        path = Path.of("src/main/resources/com/mytunes/music/" + song.getPath());
        media = new Media(path.toUri().toString());

        volumeBeforeMediaChange = mediaPlayer.getVolume();
        isMutedBeforeMediaChange = isMuted();
        isRepeatingBeforeMediaChange = isRepeating();

        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setVolume(volumeBeforeMediaChange);
        if (isMutedBeforeMediaChange)
            mediaPlayer.setMute(true);
        if (isRepeatingBeforeMediaChange)
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        currentSong = song;
        allSongs = songs;
    }

    public void load(Playlist playlist, Song song) {
        setListStatus(ListStatus.PLAYLIST);

        path = Path.of("src/main/resources/com/mytunes/music/" + song.getPath());
        media = new Media(path.toUri().toString());

        volumeBeforeMediaChange = mediaPlayer.getVolume();
        isMutedBeforeMediaChange = isMuted();
        isRepeatingBeforeMediaChange = isRepeating();

        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setVolume(volumeBeforeMediaChange);
        if (isMutedBeforeMediaChange)
            mediaPlayer.setMute(true);
        if (isRepeatingBeforeMediaChange)
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        currentSong = song;
        currentPlaylist = playlist;
    }

    public void play() {
        mediaPlayer.play();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void next() {
        if (getListStatus() == Player.ListStatus.ALL_SONGS) {
            if (isShuffling()) {
                load(allSongs.get((int)(allSongs.size() * Math.random())));
            }
            else if (allSongs.indexOf(getCurrentSong()) == allSongs.size() - 1) {
                load(allSongs.get(0));
            }
            else {
                Song nextSong = allSongs.get(allSongs.indexOf(getCurrentSong()) + 1);
                load(nextSong);
            }
        }
        else if (getListStatus() == Player.ListStatus.PLAYLIST) {
            if (isShuffling()) {
                load(currentPlaylist.getSongs().get((int)(currentPlaylist.getSongs().size() * Math.random())));
            }
            else if (getCurrentPlaylist().getSongs().indexOf(getCurrentSong()) == getCurrentPlaylist().getSongs().size() - 1) {
                load(getCurrentPlaylist().getSongs().get(0));
            }
            else {
                Song nextSong = getCurrentPlaylist().getSongs().get(getCurrentPlaylist().getSongs().indexOf(getCurrentSong()) + 1);
                load(nextSong);
            }
        }
    }

    public void previous() {
        if (getCurrentTime().toSeconds() > 3) {
            reset();
        }
        else if (getListStatus() == Player.ListStatus.ALL_SONGS) {
            if (allSongs.indexOf(getCurrentSong()) == 0) {
                reset();
            }
            else {
                Song previousSong = allSongs.get(allSongs.indexOf(getCurrentSong()) - 1);
                load(previousSong);
            }
        }
        else if (getListStatus() == Player.ListStatus.PLAYLIST) {
            if (getCurrentPlaylist().getSongs().indexOf(getCurrentSong()) == 0) {
                reset();
            }
            else {
                Song previousSong = getCurrentPlaylist().getSongs().get(getCurrentPlaylist().getSongs().indexOf(getCurrentSong()) - 1);
                load(previousSong);
            }
        }
    }

    public void reset() {
        mediaPlayer.seek(mediaPlayer.getStartTime());
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void mute(boolean mute) {
        mediaPlayer.setMute(mute);
    }

    public void repeat(boolean repeat) {
        if (repeat) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        else {
            mediaPlayer.setCycleCount(1);
        }
    }

    public void shuffle(boolean shuffle) {
        isShuffling = shuffle;
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    //returns current time of song in seconds
    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }

    public Duration getCycleDuration() {
        return mediaPlayer.getCycleDuration();

    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public ListStatus getListStatus() {
        return listStatus;
    }

    public void setListStatus(ListStatus listStatus) {
        this.listStatus = listStatus;
    }

    //volume value must be between 0 and 1
    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    //progress value must be between 0 and 1
    public void setProgress(double progress) {
        mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(progress));
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

    public Runnable getOnReady() {
        return mediaPlayer.getOnReady();
    }
}
