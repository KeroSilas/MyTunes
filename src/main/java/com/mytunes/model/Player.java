package com.mytunes.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Path;

public class Player {

    private Path path;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Song currentSong;

    private PlaylistStatus playlistStatus;

    public enum PlaylistStatus {
        ALL_SONGS,
        PLAYLIST,
        DEFAULT
    }

    public Player() {
        path = Path.of("src/main/resources/com/mytunes/music/test.wav");
        media = new Media(path.toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        setPlaylistStatus(PlaylistStatus.DEFAULT);
    }

    //loads audio file
    public void load(Song song) {
        path = Path.of("src/main/resources/com/mytunes/music/" + song.getPath());
        media = new Media(path.toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        currentSong = song;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public PlaylistStatus getPlaylistStatus() {
        return playlistStatus;
    }

    public void setPlaylistStatus(PlaylistStatus playlistStatus) {
        this.playlistStatus = playlistStatus;
    }

    public void play() {
        mediaPlayer.play();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void reset() {
        mediaPlayer.seek(mediaPlayer.getStartTime());
    }

    public void setRepeat(boolean repeat) {
        if(repeat) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } else {
            mediaPlayer.setCycleCount(1);
        }
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    //returns current progress of song
    public double getCurrentProgress() {
        return mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis();
    }

    public int getCurrentTime() {
        return (int) mediaPlayer.getCurrentTime().toSeconds();
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    public void setProgress(double progress) {
        mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(progress));
    }

    public boolean isPlaying() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isEndOfMedia() {
        return mediaPlayer.getCurrentTime().equals(mediaPlayer.getTotalDuration());
    }

    public boolean isRepeating() {
        return mediaPlayer.getCycleCount() == MediaPlayer.INDEFINITE;
    }

    public boolean isMuted() {
        return getVolume() == 0;
    }

}
