package com.mytunes.controller;

import com.mytunes.dao.PlaylistDao;
import com.mytunes.dao.PlaylistDaoImpl;
import com.mytunes.dao.SongDao;
import com.mytunes.dao.SongDaoImpl;
import com.mytunes.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MyTunesController {

    private Player player;
    private SongDao songDao;
    private PlaylistDao playlistDao;

    @FXML private Label welcomeText;

    @FXML protected void onHelloButtonClick() {
        player.play();
    }

    public void initialize() {
        player = new Player();
        songDao = new SongDaoImpl();
        playlistDao = new PlaylistDaoImpl();

        player.load("test.wav");
    }
}