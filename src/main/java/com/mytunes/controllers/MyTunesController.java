package com.mytunes.controllers;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class MyTunesController {

    private Player player;
    private final Path playPath = Path.of("src/main/resources/com/mytunes/images/play.png");
    private final Path pausePath = Path.of("src/main/resources/com/mytunes/images/pus.png");
    private final Path mutePath = Path.of("src/main/resources/com/mytunes/images/M.png");
    private final Path unmutePath = Path.of("src/main/resources/com/mytunes/images/L.png");
    private final Image playImage = new Image(playPath.toUri().toString());
    private final Image pauseImage = new Image(pausePath.toUri().toString());
    private final Image muteImage = new Image(mutePath.toUri().toString());
    private final Image unmuteImage = new Image(unmutePath.toUri().toString());

    private SongDao songDao;
    private PlaylistDao playlistDao;

    private final ObservableList<Playlist> playlistObservableList = FXCollections.observableArrayList();
    private final ObservableList<Song> songObservableList = FXCollections.observableArrayList();
    private final ObservableList<Song> songInPlaylistObservableList = FXCollections.observableArrayList();
    private Playlist selectedPlaylist;
    private Song selectedSong, selectedSongInPlaylist;
    private boolean isSearching = false;

    @FXML private TableView<Playlist> playlistTableView;
    @FXML private TableColumn<Playlist, String> nameColumn, totalDurationColumn;
    @FXML private TableColumn<Playlist, Integer> songsColumn;

    @FXML private TableView<Song> songTableView;
    @FXML private TableColumn<Song, String> titleColumn, artistColumn, categoryColumn, durationColumn;

    @FXML private ListView<Song> songsInPlaylistListView;

    @FXML private TextField searchTextField;

    @FXML private Slider volumeSlider, progressSlider;

    @FXML private ImageView playPauseImage, muteUnmuteImage, repeatImage;

    @FXML void handlePlaylistClick(MouseEvent e) {
        selectedPlaylist = playlistTableView.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            songInPlaylistObservableList.setAll(selectedPlaylist.getSongs());
            if (selectedPlaylist.getNumberOfSongs() > 0 && e.getClickCount() == 2) {
                songTableView.getSelectionModel().clearSelection();
                player.stop();
                player.load(selectedPlaylist, selectedPlaylist.getSongs().get(0));
                player.play();
                playPauseImage.setImage(pauseImage);
            }
        }
    }

    @FXML void handleSongClick(MouseEvent e) {
        selectedSong = songTableView.getSelectionModel().getSelectedItem();
        if (selectedSong != null && e.getClickCount() == 2) {
            songsInPlaylistListView.getSelectionModel().clearSelection();
            player.stop();
            player.load(songObservableList, selectedSong);
            player.play();
            playPauseImage.setImage(pauseImage);
        }
    }

    @FXML void handleSongInPlaylistClick(MouseEvent e) {
        selectedSongInPlaylist = songsInPlaylistListView.getSelectionModel().getSelectedItem();
        if (selectedSongInPlaylist != null && e.getClickCount() == 2) {
            songTableView.getSelectionModel().clearSelection();
            player.stop();
            player.load(selectedPlaylist, selectedSongInPlaylist);
            player.play();
            playPauseImage.setImage(pauseImage);
        }
    }

    //searches for songs in the database
    @FXML void handleSearch(ActionEvent e) {
        try {
            if(!isSearching && !searchTextField.getText().isEmpty()) {
                songObservableList.setAll(songDao.searchSong(searchTextField.getText()));
                isSearching = true;
            } else {
                searchTextField.clear();
                songObservableList.setAll(songDao.getAllSongs());
                isSearching = false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //temporary implementation
    @FXML void handleAddPlaylist(ActionEvent e) {
        try {
            playlistDao.createPlaylist("New Playlist");
            updatePlaylists();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //temporary implementation
    @FXML void handleAddSong(ActionEvent e) {
        try {
            songDao.createSong("New Song", "New Artist", "New Category", 420, "New Path");
            updateSongs();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML void handleAddSongToPlaylist(ActionEvent e) {
        try {
            if (selectedPlaylist != null && selectedSong != null) {
                selectedPlaylist.addSong(selectedSong);
                updateSongsInPlaylist();
                player.updateCurrentPlaylist(selectedPlaylist);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML void handleDeleteSong(ActionEvent e) {
        try {
            songDao.deleteSong(selectedSong.getId());
            songObservableList.remove(selectedSong);
            updateSongsInPlaylist();
            player.updateCurrentPlaylist(selectedPlaylist);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML void handleDeletePlaylist(ActionEvent e) {
        try {
            playlistDao.deletePlaylist(selectedPlaylist.getId());
            playlistObservableList.remove(selectedPlaylist);
            selectedPlaylist.getSongs().clear();
            if (player.getCurrentPlaylist() == selectedPlaylist && player.getListStatus() == Player.ListStatus.PLAYLIST) {
                player.stop();
                playPauseImage.setImage(playImage);
                player.load(songObservableList, songObservableList.get(0));
            }
            updateSongsInPlaylist();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML void handleDeleteSongFromPlaylist(ActionEvent e) {
        try {
            selectedPlaylist.removeSong(selectedSongInPlaylist);
            updateSongsInPlaylist();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML void handleMoveSongUp(ActionEvent e) {
        Collections.swap(selectedPlaylist.getSongs(), selectedPlaylist.getSongs().indexOf(selectedSongInPlaylist), selectedPlaylist.getSongs().indexOf(selectedSongInPlaylist) - 1);
        updateSongsInPlaylist();
        player.updateCurrentPlaylist(selectedPlaylist);
    }

    @FXML void handleMoveSongDown(ActionEvent e) {
        Collections.swap(selectedPlaylist.getSongs(), selectedPlaylist.getSongs().indexOf(selectedSongInPlaylist), selectedPlaylist.getSongs().indexOf(selectedSongInPlaylist) + 1);
        updateSongsInPlaylist();
        player.updateCurrentPlaylist(selectedPlaylist);
    }

    //determines whether the player is playing or paused and changes the buttons action accordingly
    @FXML void handlePlayPause(ActionEvent e) {
        if (player.isPlaying()) {
            player.pause();
            playPauseImage.setImage(playImage);
        }
        else {
            player.setProgress(progressSlider.getValue() / 100);
            player.play();
            playPauseImage.setImage(pauseImage);
        }
    }

    @FXML void handleRepeat(ActionEvent e) {
        player.repeat(!player.isRepeating());
    }

    @FXML void handleNextSong(ActionEvent e) {
        player.next();
    }

    @FXML void handlePreviousSong(ActionEvent e) {
        player.previous();
    }

    @FXML void handleMuteUnmute(ActionEvent e) {
        if (player.isMuted()) {
            player.mute(false);
            volumeSlider.setDisable(false);
            muteUnmuteImage.setImage(unmuteImage);
        }
        else {
            player.mute(true);
            volumeSlider.setDisable(true);
            muteUnmuteImage.setImage(muteImage);
        }
    }

    //changes progress of player when mouse click is released
    @FXML void handleProgressSlider(MouseEvent e) {
        player.setProgress(progressSlider.getValue() / 100);
    }

    private void updateSongsInPlaylist() {
        songInPlaylistObservableList.setAll(selectedPlaylist.getSongs());
    }

    private void updateSongs() {
        try {
            songObservableList.setAll(songDao.getAllSongs());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updatePlaylists() {
        try {
            playlistObservableList.setAll(playlistDao.getAllPlaylists());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void initialize() {
        //initialize DAOs
        songDao = new SongDaoImpl();
        playlistDao = new PlaylistDaoImpl();

        try {
            //Set up the table columns and cells for the song table
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("Artist"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("DurationInString"));
            songTableView.setItems(songObservableList);
            songTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            songObservableList.addAll(songDao.getAllSongs());

            //Set up the table columns and cells for the playlist table
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            songsColumn.setCellValueFactory(new PropertyValueFactory<>("NumberOfSongs"));
            totalDurationColumn.setCellValueFactory(new PropertyValueFactory<>("DurationInString"));
            playlistTableView.setItems(playlistObservableList);
            playlistTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            playlistObservableList.addAll(playlistDao.getAllPlaylists());

            songsInPlaylistListView.setItems(songInPlaylistObservableList);
            songsInPlaylistListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            //initialize player with first song on Songs list
            player = new Player(songObservableList, songObservableList.get(0));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //set default volume to 50%
        player.setVolume(0.5);
        volumeSlider.setValue(player.getVolume() * 100);

        //add listener to volumeSlider
        volumeSlider.valueProperty().addListener((ov, oldValue, newValue) ->
                player.setVolume(newValue.doubleValue() / 100)
        );

        /*
        //add listener to currentTime in player, this is what makes the progressBar move by itself
        player.currentTimeProperty().addListener((ov, oldValue, newValue) -> {
            if(!progressSlider.isPressed())
                progressSlider.setValue(newValue.toSeconds() / player.getTotalDuration().toMillis() * 100);
        });

        //runnable that runs when a song has reached the end
        player.setOnEndOfMedia(() -> {
            if(!player.isRepeating()) {
                progressSlider.setValue(0);
                player.next();
            }
        });
        */

        //automatically update progressSlider, unless progressSlider is being dragged
        Timer progressTimer = new Timer();
        progressTimer.schedule(new TimerTask() {
            @Override public void run() {
                if (!progressSlider.isPressed()) {
                    progressSlider.setValue(player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100);
                }
                if (player.isEndOfMedia() && !player.isRepeating()) {
                    player.next();
                }
            }
        }, 0L, 20L);
    }
}