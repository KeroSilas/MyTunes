package com.mytunes.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Path;

public class Player {

    private Path path;
    private Media media;
    private MediaPlayer mediaPlayer;
    private String currentFile = "empty";

    public Player() {
        path = Path.of("src/main/resources/com/mytunes/music/test.wav");
        media = new Media(path.toUri().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    //loads audio file
    public void load(String file) {
        path = Path.of("src/main/resources/com/mytunes/music/" + file);
        media = new Media(path.toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        currentFile = file;
    }

    public String getCurrentFile() {
        return currentFile;
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
