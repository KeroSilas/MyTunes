package com.mytunes.controller;

import com.mytunes.dao.*;
import com.mytunes.model.Player;
import com.mytunes.model.Playlist;
import com.mytunes.model.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class MyTunesController {

    private Player player;

    private SongDao songDao;
    private PlaylistDao playlistDao;
    private SongsInPlaylistDao songsInPlaylistDao;

    private ObservableList<Playlist> playlistObservableList = FXCollections.observableArrayList();
    private ObservableList<Song> songObservableList = FXCollections.observableArrayList();
    private Playlist selectedPlaylist;
    private Song selectedSong;

    @FXML private TableView<Playlist> playlistTableView;
    @FXML private TableColumn<Playlist, String> nameColumn;
    @FXML private TableColumn<Playlist, Integer> songsColumn;
    @FXML private TableColumn<Playlist, String> durationColumn;

    @FXML private TableView<Song> songTableView;
    @FXML private TableColumn<Song, String> titleColumn;
    @FXML private TableColumn<Song, String> artistColumn;
    @FXML private TableColumn<Song, String> categoryColumn;
    @FXML private TableColumn<Song, String> timeColumn;

    @FXML private ListView<Song> selectedListView;

    @FXML private TextField testTextField;

    @FXML private Slider volumeSlider;

    @FXML private Slider progressSlider;

    @FXML void handlePlaylistClick(MouseEvent e) {
        try {
            selectedPlaylist = playlistTableView.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null) {
                selectedListView.getItems().setAll(songsInPlaylistDao.getPlaylist(selectedPlaylist.getId()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML void handleSongClick(MouseEvent e) {
        selectedSong = songTableView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            player.load(selectedSong.getPath());
            player.play();
        }
    }

    //searches for songs in the database
    @FXML void handleSearch(ActionEvent e) {
        try {
            songObservableList.setAll(songDao.searchSong(testTextField.getText()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //temporary implementation
    @FXML void handleAddPlaylist(ActionEvent e) {
        try {
            playlistDao.createPlaylist("New Playlist");
            playlistObservableList.setAll(playlistDao.getAllPlaylists());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //temporary implementation
    @FXML void handleAddSong(ActionEvent e) {
        try {
            songDao.createSong("New Song", "New Artist", "New Category", "New Path");
            songObservableList.setAll(songDao.getAllSongs());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //temporary implementation
    @FXML void handleAddSongToPlaylist(ActionEvent e) {
        try {
            songsInPlaylistDao.moveSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());
            selectedListView.getItems().setAll(songsInPlaylistDao.getPlaylist(selectedPlaylist.getId()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //determines whether the player is playing or paused and changes the buttons action accordingly
    @FXML void handlePlayPause(ActionEvent e) {
        if(player.isPlaying()) {
            player.pause();
        }
        else {
            player.play();
        }
    }

    @FXML void handleStop(ActionEvent e) {
        player.stop();
    }

    @FXML void handleRepeat(ActionEvent e) {
        player.repeat();
    }

    //changes progress of player when mouse click is released
    @FXML void handleProgressSlider(MouseEvent e) {
        player.setProgress(progressSlider.getValue() / 100);
    }

    public void initialize() {
        player = new Player(); //initialize player
        player.setVolume(0.5); //set default volume to 0.5 (50%)
        volumeSlider.setValue(player.getVolume() * 100);
        volumeSlider.valueProperty().addListener((ov, oldValue, newValue) ->
                player.setVolume(newValue.doubleValue() / 100)
        ); //add listener to volumeSlider

        //automatically update progressSlider every 0.1s, unless progressSlider is being dragged
        Timer progressTimer = new Timer();
        progressTimer.schedule(new TimerTask() {
            @Override public void run() {
                if(!progressSlider.isPressed()) {
                    progressSlider.setValue(player.getCurrentProgress() * 100);
                }
            }
        }, 0L, 100L);

        //initialize DAOs
        songDao = new SongDaoImpl();
        playlistDao = new PlaylistDaoImpl();
        songsInPlaylistDao = new SongsInPlaylistDaoImpl();

        try {
            //Set up the table columns and cells for the playlist table
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            songsColumn.setCellValueFactory(new PropertyValueFactory<>("NumberOfSongs"));
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("Duration"));
            playlistTableView.setItems(playlistObservableList);
            playlistTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            playlistObservableList.addAll(playlistDao.getAllPlaylists());

            //Set up the table columns and cells for the song table
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("Artist"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("Duration"));
            songTableView.setItems(songObservableList);
            songTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            songObservableList.addAll(songDao.getAllSongs());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}