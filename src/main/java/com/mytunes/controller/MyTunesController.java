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

public class MyTunesController {

    private Player player;
    private SongDao songDao;
    private PlaylistDao playlistDao;
    private SongsInPlaylistDao songsInPlaylistDao;

    private ObservableList<Playlist> playlistObservableList = FXCollections.observableArrayList();
    private ObservableList<Song> songObservableList = FXCollections.observableArrayList();

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

    @FXML void handlePlaylistClick(MouseEvent e) throws SQLException {
        //returns the selected playlist's songs
        Playlist selectedPlaylist = playlistTableView.getSelectionModel().getSelectedItem();
        if(selectedPlaylist != null) {
            selectedListView.getItems().setAll(songsInPlaylistDao.getPlaylist(selectedPlaylist.getId()));
        }
    }

    @FXML void handleSongClick() {
    }

    @FXML void handleAddPlaylist() {
    }

    @FXML void handleAddSong() {
    }

    @FXML void handleAddSongToPlaylist() {
    }

    @FXML void handlePlayPause() {
        if(player.isPlaying()) {
            player.pause();
        }
        else {
            player.play();
        }
    }

    @FXML void handleStop() {
        player.stop();
    }

    @FXML void handleRepeat() {
        player.repeat();
    }

    @FXML void handleTest(ActionEvent e) {
        System.out.println("Test");
        player.load("test.wav");
        player.play();
    }

    public void initialize() throws SQLException {
        player = new Player();
        songDao = new SongDaoImpl();
        playlistDao = new PlaylistDaoImpl();
        songsInPlaylistDao = new SongsInPlaylistDaoImpl();

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

        //for testing purposes
        for (Playlist playlist : playlistObservableList) {
            System.out.println(playlist.getId() + " " + playlist.getName());
        }
    }
}