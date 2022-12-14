package com.mytunes.services;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MyTunesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/mytunes/views/MyTunes.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MyTunes");
        stage.getIcons().add(new Image("file:src/main/resources/com/mytunes/images/logo.png")); //Adds an icon to the title-bar.
        stage.setMinWidth(1086); //Restricts resizing to a minimum size.
        stage.setMinHeight(276);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}