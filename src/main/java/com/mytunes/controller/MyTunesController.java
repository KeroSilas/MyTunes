package com.mytunes.controller;

import com.mytunes.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MyTunesController {

    private Player player;

    @FXML private Label welcomeText;

    @FXML protected void onHelloButtonClick() {
        player.play();
    }

    public void initialize() {
        player = new Player();
        player.load("test.wav");
    }
}