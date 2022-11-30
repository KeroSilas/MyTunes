package com.mytunes.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Player {

    private Path path;
    private Media media;
    private MediaPlayer mediaPlayer;

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

    public void repeat() {
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    public void setProgress(double progress) {
        mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(progress));
    }

    public void setRate(double rate) {
        mediaPlayer.setRate(rate);
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    public int getProgress() {
        return (int) (mediaPlayer.getCurrentTime().toSeconds() / mediaPlayer.getTotalDuration().toSeconds());
    }

    public boolean isPlaying() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

}
