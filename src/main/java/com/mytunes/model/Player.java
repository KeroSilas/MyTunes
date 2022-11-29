package com.mytunes.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Path;

public class Player {

    private MediaPlayer mediaPlayer;

    public Player() {
    }

    //loads audio file
    public void load(String file) {
        Path path = Path.of("src/main/resources/com/mytunes/music/" + file);
        Media media = new Media(path.toUri().toString());
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

    public void shuffle() {
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    public void setRate(double rate) {
        mediaPlayer.setRate(rate);
    }

    public void getVolume() {
        mediaPlayer.getVolume();
    }

}
