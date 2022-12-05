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
        stage.getIcons().add(new Image("file:src/main/resources/com/mytunes/images/logo.png"));
        stage.widthProperty().addListener((o, oldValue, newValue)->{
            if(newValue.intValue() < 1044.0) {
                stage.setResizable(false);
                stage.setWidth(1044);
                stage.setResizable(true);
            }
        });
        stage.heightProperty().addListener((o, oldValue, newValue)->{
            if(newValue.intValue() < 600) {
                stage.setResizable(false);
                stage.setHeight(600);
                stage.setResizable(true);
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}