package com.mytunes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewEditPlaylistController {

    private MyTunesController myTunesController;

    @FXML private Button cancelButton, saveButton;

    @FXML private TextField nameTextField;

    @FXML void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML void handleSave() {
        if (MyTunesController.isNewPressed)
            myTunesController.getPlaylistsManager().addPlaylist(nameTextField.getText());
        else
            myTunesController.getPlaylistsManager().updatePlaylist(MyTunesController.selectedPlaylist, nameTextField.getText());

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public void initialize() {
        if (!MyTunesController.isNewPressed) {
            nameTextField.setText(MyTunesController.selectedPlaylist.getName());
        }
    }

    public void setMyTunesController(MyTunesController myTunesController) {
        this.myTunesController = myTunesController;
    }
}