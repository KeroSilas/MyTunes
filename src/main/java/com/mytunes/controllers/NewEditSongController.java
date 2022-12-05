package com.mytunes.controllers;

import com.mytunes.dao.SongDao;
import com.mytunes.dao.SongDaoImpl;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;

public class NewEditSongController {

    private SongDao songDao;
    private int duration;

    @FXML private GridPane gridPane;

    @FXML private TextField titleTextField, artistTextField, categoryTextField, durationTextField, fileTextField;

    @FXML private Button cancelButton, saveButton;

    @FXML void handleCancel(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML void handleChooseSong(ActionEvent e) {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showOpenDialog(stage);

        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        fileTextField.setText(file.getName());

        media.getMetadata().addListener((MapChangeListener.Change<? extends String, ?> c) -> {
            if (c.wasAdded()) {
                if ("title".equals(c.getKey())) {
                    titleTextField.setText(c.getValueAdded().toString());
                } else if ("artist".equals(c.getKey())) {
                    artistTextField.setText(c.getValueAdded().toString());
                } else if ("genre".equals(c.getKey())) {
                    categoryTextField.setText(c.getValueAdded().toString());
                }
            }
        });

        mediaPlayer.setOnReady(() -> {
            duration = (int) mediaPlayer.getTotalDuration().toSeconds();
            int minutes = (duration % 3600) / 60;
            int seconds = duration % 60;
            durationTextField.setText(String.format("%02d:%02d", minutes, seconds));
        });
    }

    @FXML void handleSave(ActionEvent e) {
        try {
            songDao.createSong(titleTextField.getText(), artistTextField.getText(), categoryTextField.getText(), duration, fileTextField.getText());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public void initialize() {
        songDao = new SongDaoImpl();
    }
}