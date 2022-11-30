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

    @FXML void handlePlaylistClick(MouseEvent e) throws SQLException {
        selectedPlaylist = playlistTableView.getSelectionModel().getSelectedItem();
        if(selectedPlaylist != null) {
            selectedListView.getItems().setAll(songsInPlaylistDao.getPlaylist(selectedPlaylist.getId()));
        }
    }

    @FXML void handleSongClick(MouseEvent e) throws SQLException {
        selectedSong = songTableView.getSelectionModel().getSelectedItem();
        if(selectedSong != null) {
            player.load(selectedSong.getPath());
            player.play();
        }
    }

    @FXML void handleSearch(ActionEvent e) throws SQLException {
        songObservableList.setAll(songDao.searchSong(testTextField.getText()));
    }

    //temporary implementation
    @FXML void handleAddPlaylist() {
        try {
            playlistDao.createPlaylist("New Playlist");
            playlistObservableList.setAll(playlistDao.getAllPlaylists());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //temporary implementation
    @FXML void handleAddSong() {
        try {
            songDao.createSong("New Song", "New Artist", "New Category", "New Path");
            songObservableList.setAll(songDao.getAllSongs());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //temporary implementation
    @FXML void handleAddSongToPlaylist() {
        try {
            songsInPlaylistDao.moveSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());
            selectedListView.getItems().setAll(songsInPlaylistDao.getPlaylist(selectedPlaylist.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    }
}