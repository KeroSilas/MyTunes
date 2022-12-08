package com.mytunes.controllers;

import com.mytunes.dao.*;
import com.mytunes.model.Player;
import com.mytunes.model.Playlist;
import com.mytunes.model.Song;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class MyTunesController {

    private Player player;
    private final Image playImage = new Image("file:src/main/resources/com/mytunes/images/play.png");
    private final Image pauseImage = new Image("file:src/main/resources/com/mytunes/images/pause.png");
    private final Image muteImage = new Image("file:src/main/resources/com/mytunes/images/muted.png");
    private final Image unmuteImage = new Image("file:src/main/resources/com/mytunes/images/unmuted.png");
    private final Image repeatImage = new Image("file:src/main/resources/com/mytunes/images/repeat.png");
    private final Image unrepeatImage = new Image("file:src/main/resources/com/mytunes/images/repeating.png");
    private final Image shuffleImage = new Image("file:src/main/resources/com/mytunes/images/shuffle.png");
    private final Image unshuffleImage = new Image("file:src/main/resources/com/mytunes/images/shuffling.png");
    private final Image searchImage = new Image("file:src/main/resources/com/mytunes/images/search.png");
    private final Image unsearchImage = new Image("file:src/main/resources/com/mytunes/images/cancel.png");
    private final Image defaultAlbumImage = new Image("file:src/main/resources/com/mytunes/images/default-album-art.png");

    private SongDao songDao;
    private PlaylistDao playlistDao;
    private boolean isSearching = false;
    protected static boolean isNewPressed;

    private final ObservableList<Playlist> playlistObservableList = FXCollections.observableArrayList();
    private final ObservableList<Song> songObservableList = FXCollections.observableArrayList();
    private final ObservableList<Song> songInPlaylistObservableList = FXCollections.observableArrayList();
    protected static Playlist selectedPlaylist;
    protected static Song selectedSong;
    private Song selectedSongInPlaylist;

    @FXML private TableView<Playlist> playlistTableView;
    @FXML private TableColumn<Playlist, String> nameColumn, totalDurationColumn;
    @FXML private TableColumn<Playlist, Integer> songsColumn;
    @FXML private TableView<Song> songTableView;
    @FXML private TableColumn<Song, String> titleColumn, artistColumn, categoryColumn, durationColumn;
    @FXML private ListView<Song> songsInPlaylistListView;

    @FXML private TextField searchTextField;

    @FXML private Slider volumeSlider, progressSlider;

    @FXML private Button editPlaylistButton, deletePlaylistButton, editSongButton, deleteSongButton, moveSongUpButton, moveSongDownButton, deleteSongFromPlaylistButton, addSongToPlaylistButton;

    @FXML private Label currentSongTitleLabel, currentSongArtistLabel, currentTimeLabel, totalDurationLabel, volumeLabel;

    @FXML private ImageView playPauseImage, muteUnmuteImage, repeatUnrepeatImage, shuffleUnshuffleImage, searchUnsearchImage, albumCoverImage;

    ///// --- LIST AND SELECTION METHODS --- /////

    @FXML void handlePlaylistClick(MouseEvent e) {
        selectedPlaylist = playlistTableView.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            songInPlaylistObservableList.setAll(selectedPlaylist.getSongs());
            if (selectedPlaylist.getNumberOfSongs() > 0 && e.getClickCount() == 2) {
                songTableView.getSelectionModel().clearSelection();
                player.load(selectedPlaylist, selectedPlaylist.getSongs().get(0));
                update();
                songsInPlaylistListView.getSelectionModel().select(player.getCurrentSong());
            }
        }
    }

    @FXML void handleSongClick(MouseEvent e) {
        selectedSong = songTableView.getSelectionModel().getSelectedItem();
        if (selectedSong != null && e.getClickCount() == 2) {
            songsInPlaylistListView.getSelectionModel().clearSelection();
            player.load(songObservableList, selectedSong);
            update();
        }
    }

    @FXML void handleSongInPlaylistClick(MouseEvent e) {
        selectedSongInPlaylist = songsInPlaylistListView.getSelectionModel().getSelectedItem();
        if (selectedSongInPlaylist != null && e.getClickCount() == 2) {
            songTableView.getSelectionModel().clearSelection();
            player.load(selectedPlaylist, selectedSongInPlaylist);
            update();
        }
    }

    @FXML void handleMoveSongUp() {
        Collections.swap(selectedPlaylist.getSongs(), selectedPlaylist.getSongs().indexOf(selectedSongInPlaylist), selectedPlaylist.getSongs().indexOf(selectedSongInPlaylist) - 1);
        songInPlaylistObservableList.setAll(selectedPlaylist.getSongs());
        songsInPlaylistListView.getSelectionModel().select(selectedSongInPlaylist);
        player.updateCurrentPlaylist(selectedPlaylist);
    }

    @FXML void handleMoveSongDown() {
        Collections.swap(selectedPlaylist.getSongs(), selectedPlaylist.getSongs().indexOf(selectedSongInPlaylist), selectedPlaylist.getSongs().indexOf(selectedSongInPlaylist) + 1);
        songInPlaylistObservableList.setAll(selectedPlaylist.getSongs());
        songsInPlaylistListView.getSelectionModel().select(selectedSongInPlaylist);
        player.updateCurrentPlaylist(selectedPlaylist);
    }

    ///// --- DAO CONTROLS --- /////

    //searches for songs in the database
    @FXML void handleSearch() {
        if (!isSearching && !searchTextField.getText().isEmpty()) {
            songObservableList.setAll(songDao.searchSong(searchTextField.getText()));
            searchUnsearchImage.setImage(unsearchImage);
            selectedSong = null;
            isSearching = true;
        } else {
            searchTextField.clear();
            songObservableList.setAll(songDao.getAllSongs());
            searchUnsearchImage.setImage(searchImage);
            selectedSong = null;
            isSearching = false;
        }
    }

    @FXML void handleAddPlaylist() {
        isNewPressed = true;
        openNewEditPlaylistWindow();
        playlistObservableList.setAll(playlistDao.getAllPlaylists());
    }

    @FXML void handleAddSong() {
        isNewPressed = true;
        openNewEditSongWindow();
        songObservableList.setAll(songDao.getAllSongs());
        player.updateCurrentAllSongs(songObservableList);
    }

    @FXML void handleEditPlaylist() {
        isNewPressed = false;
        openNewEditPlaylistWindow();
        playlistObservableList.setAll(playlistDao.getAllPlaylists());
    }

    @FXML void handleEditSong() {
        isNewPressed = false;
        openNewEditSongWindow();
        songObservableList.setAll(songDao.getAllSongs());
    }

    @FXML void handleAddSongToPlaylist() {
        if (selectedPlaylist != null && selectedSong != null) {
            selectedPlaylist.addSong(selectedSong);
            songInPlaylistObservableList.setAll(selectedPlaylist.getSongs());
            playlistObservableList.setAll(playlistDao.getAllPlaylists());
            player.updateCurrentPlaylist(selectedPlaylist);
        }
    }

    @FXML void handleDeleteSong() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete this song?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            songDao.deleteSong(selectedSong.getId());
            songObservableList.remove(selectedSong);
            selectedPlaylist.getSongs().remove(selectedSong); //TO FIX: only deletes the song from the selected playlist, won't be removed from other playlists until all playlists are refreshed from the server
            songInPlaylistObservableList.setAll(selectedPlaylist.getSongs());
            player.updateCurrentPlaylist(selectedPlaylist);
            player.updateCurrentAllSongs(songObservableList);
            selectedSong = null;
        }
    }

    @FXML void handleDeletePlaylist() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete this playlist?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            playlistDao.deletePlaylist(selectedPlaylist.getId());
            playlistObservableList.remove(selectedPlaylist);
            selectedPlaylist.getSongs().clear();
            songInPlaylistObservableList.setAll(selectedPlaylist.getSongs());
            //if the playlist that is getting deleted is currently loaded, then switch the player to load the first song on the all songs list
            if (player.getCurrentPlaylist() == selectedPlaylist && player.getListStatus() == Player.ListStatus.PLAYLIST) {
                player.load(songObservableList, songObservableList.get(0));
                update();
            }
            selectedPlaylist = null;
        }
    }

    @FXML void handleDeleteSongFromPlaylist() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to delete this song from the playlist?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            selectedPlaylist.removeSong(selectedSongInPlaylist);
            songInPlaylistObservableList.setAll(selectedPlaylist.getSongs());
            playlistObservableList.setAll(playlistDao.getAllPlaylists());
            player.updateCurrentPlaylist(selectedPlaylist);
            selectedSongInPlaylist = null;
        }
    }

    ///// --- PLAYER CONTROLS --- /////

    //determines whether the player is playing or paused and changes the buttons action accordingly
    @FXML void handlePlayPause() {
        if (player.isPlaying()) {
            player.pause();
            playPauseImage.setImage(playImage);
        } else {
            player.setProgress(progressSlider.getValue() / 100);
            player.play();
        }
    }

    @FXML void handleRepeat() {
        if (player.isRepeating()) {
            player.repeat(false);
            repeatUnrepeatImage.setImage(repeatImage);
        } else {
            player.repeat(true);
            repeatUnrepeatImage.setImage(unrepeatImage);
        }
    }

    @FXML void handleShuffle() {
        if (player.isShuffling()) {
            player.shuffle(false);
            shuffleUnshuffleImage.setImage(shuffleImage);
        } else {
            player.shuffle(true);
            shuffleUnshuffleImage.setImage(unshuffleImage);
        }
    }

    @FXML void handleNextSong() {
        player.next();
        update();
    }

    @FXML void handlePreviousSong() {
        player.previous();
        update();
    }

    @FXML void handleMuteUnmute() {
        if (player.isMuted()) {
            player.mute(false);
            muteUnmuteImage.setImage(unmuteImage);
        } else {
            player.mute(true);
            muteUnmuteImage.setImage(muteImage);
        }
        if (player.getVolume() == 0) {
            player.mute(false);
            volumeSlider.setValue(20);
            muteUnmuteImage.setImage(unmuteImage);
        }
    }

    //changes progress of player when mouse click is released
    @FXML void handleProgressSlider() {
        player.setProgress(progressSlider.getValue() / 100);
    }

    public void initialize() {
        //initialize DAOs
        songDao = new SongDaoImpl();
        playlistDao = new PlaylistDaoImpl();

        //Set up the table columns and cells for the song table
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("DurationInString"));
        songObservableList.addAll(songDao.getAllSongs());
        songTableView.setItems(songObservableList);
        songTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Set up the table columns and cells for the playlist table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        songsColumn.setCellValueFactory(new PropertyValueFactory<>("NumberOfSongs"));
        totalDurationColumn.setCellValueFactory(new PropertyValueFactory<>("DurationInString"));
        playlistObservableList.addAll(playlistDao.getAllPlaylists());
        playlistTableView.setItems(playlistObservableList);
        playlistTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        songsInPlaylistListView.setItems(songInPlaylistObservableList);
        songsInPlaylistListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //initialize player with first song on Songs list if there is any
        if (songObservableList.isEmpty()) {
            player = new Player();
        } else {
            player = new Player(songObservableList, songObservableList.get(0));
            songTableView.getSelectionModel().select(player.getCurrentSong());
            Platform.runLater(() -> songTableView.requestFocus());
        }

        //add listener to volumeSlider
        volumeSlider.valueProperty().addListener((ov, oldValue, newValue) -> {
            double percentage = newValue.doubleValue();
            player.setVolume(percentage / 100);
            volumeSlider.setStyle(sliderProgressStyle(percentage));
            volumeLabel.setText(String.format("%s%%", newValue.intValue()));
            if (!Objects.equals(oldValue, newValue) && player.getVolume() == 0) {
                muteUnmuteImage.setImage(muteImage);
            } else if (!Objects.equals(oldValue, newValue) && player.getVolume() > 0) {
                player.mute(false);
                muteUnmuteImage.setImage(unmuteImage);
            }
        });

        playlistTableView.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue) && playlistTableView.getSelectionModel().getSelectedItem() == null) {
                editPlaylistButton.setDisable(true);
                deletePlaylistButton.setDisable(true);
            } else if (!Objects.equals(oldValue, newValue)) {
                editPlaylistButton.setDisable(false);
                deletePlaylistButton.setDisable(false);
            }
        });

        songTableView.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue) && songTableView.getSelectionModel().getSelectedItem() == null) {
                editSongButton.setDisable(true);
                deleteSongButton.setDisable(true);
                addSongToPlaylistButton.setDisable(true);
            } else if (!Objects.equals(oldValue, newValue)) {
                editSongButton.setDisable(false);
                deleteSongButton.setDisable(false);
                addSongToPlaylistButton.setDisable(false);
            }
        });

        songsInPlaylistListView.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue) && songsInPlaylistListView.getSelectionModel().getSelectedItem() == null) {
                moveSongUpButton.setDisable(true);
                moveSongDownButton.setDisable(true);
                deleteSongFromPlaylistButton.setDisable(true);
            } else if (!Objects.equals(oldValue, newValue)) {
                moveSongUpButton.setDisable(false);
                moveSongDownButton.setDisable(false);
                deleteSongFromPlaylistButton.setDisable(false);
            }
        });

        //set default volume to 50%
        volumeSlider.setValue(50);

        update();
    }

    //refreshes listeners; this is necessary because when a new media file is loaded, the listeners don't get updated
    private void update() {
        //automatically moves progress slider with current time on song
        player.currentTimeProperty().addListener((ov, oldValue, newValue) -> {
            double percentage = newValue.toSeconds() / player.getCurrentSong().getDurationInInteger() * 100;
            if (!progressSlider.isPressed())
                progressSlider.setValue(percentage);
            progressSlider.setStyle(sliderProgressStyle(percentage));

            //keeps current time label updated
            int currentTime = (int) newValue.toSeconds();
            int minutes = (currentTime % 3600) / 60;
            int seconds = currentTime % 60;
            currentTimeLabel.setText(String.format("%02d:%02d", minutes, seconds));
        });

        //if repeat button isn't toggled on, automatically loads next song on end of media
        player.setOnEndOfMedia(() -> {
            if (!player.isRepeating()) {
                player.next();
                update(); //recursively calls the method when at end of media
            }
        });

        player.setOnPlaying(() -> playPauseImage.setImage(pauseImage));

        //updates selection and focus toward currently playing song
        if (player.getListStatus() == Player.ListStatus.ALL_SONGS) {
            songTableView.getSelectionModel().select(player.getCurrentSong());
            songTableView.requestFocus();
            songTableView.scrollTo(player.getCurrentSong());
            selectedSong = songTableView.getSelectionModel().getSelectedItem();
        } else if (player.getListStatus() == Player.ListStatus.PLAYLIST) {
            songsInPlaylistListView.getSelectionModel().select(player.getCurrentSong());
            songsInPlaylistListView.requestFocus();
            songsInPlaylistListView.scrollTo(player.getCurrentSong());
            selectedSongInPlaylist = songsInPlaylistListView.getSelectionModel().getSelectedItem();
        }

        //updates all relevant labels for currently playing song
        totalDurationLabel.setText(player.getCurrentSong().getDurationInString());
        currentSongTitleLabel.setText(player.getCurrentSong().getTitle());
        currentSongArtistLabel.setText(player.getCurrentSong().getArtist());

        //retrieves album cover on currently playing song
        albumCoverImage.setImage(defaultAlbumImage);
        player.getMedia().getMetadata().addListener((MapChangeListener.Change<? extends String, ?> c) -> {
            if (c.wasAdded()) {
                if ("image".equals(c.getKey())) {
                    albumCoverImage.setImage((Image) c.getValueAdded());
                }
            }
        });
    }

    private void openNewEditSongWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/mytunes/views/NewEditSong.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            if (isNewPressed) {
                stage.getIcons().add(new Image("file:src/main/resources/com/mytunes/images/add.png"));
                stage.setTitle("New Song");
            } else {
                stage.getIcons().add(new Image("file:src/main/resources/com/mytunes/images/edit.png"));
                stage.setTitle("Edit Song");
            }
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openNewEditPlaylistWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/mytunes/views/NewEditPlaylist.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            if (isNewPressed) {
                stage.getIcons().add(new Image("file:src/main/resources/com/mytunes/images/add.png"));
                stage.setTitle("New Playlist");
            } else {
                stage.getIcons().add(new Image("file:src/main/resources/com/mytunes/images/edit.png"));
                stage.setTitle("Edit Playlist");
            }
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String sliderProgressStyle(double percentage) {
        return String.format(
                "-track-color: linear-gradient(to right, " +
                        "-fx-accent 0%%, " +
                        "-fx-accent %1$.1f%%, " +
                        "-default-track-color %1$.1f%%, " +
                        "-default-track-color 100%%);",
                percentage);
    }
}