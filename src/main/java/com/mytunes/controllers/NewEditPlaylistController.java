package com.mytunes.controllers;

import com.mytunes.dao.PlaylistDao;
import com.mytunes.dao.PlaylistDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class NewEditPlaylistController {

    private PlaylistDao playlistDao;

    @FXML private Button cancelButton, saveButton;

    @FXML private TextField nameTextField;

    @FXML void handleCancel(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML void handleSave(ActionEvent e) {
        try {
            playlistDao.createPlaylist(nameTextField.getText());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public void initialize() {
        playlistDao = new PlaylistDaoImpl();
    }
}