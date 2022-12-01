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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.nio.file.Path;
import java.sql.SQLException;
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
    private SongsInPlaylistDao songsInPlaylistDao;

    private final ObservableList<Playlist> playlistObservableList = FXCollections.observableArrayList();
    private final ObservableList<Song> songObservableList = FXCollections.observableArrayList();
    private final ObservableList<Song> songInPlaylistObservableList = FXCollections.observableArrayList();
    private Playlist selectedPlaylist;
    private Song selectedSong;
    private Song selectedSongInPlaylist;

    @FXML private TableView<Playlist> playlistTableView;
    @FXML private TableColumn<Playlist, String> nameColumn;
    @FXML private TableColumn<Playlist, Integer> songsColumn;
    @FXML private TableColumn<Playlist, String> totalDurationColumn;

    @FXML private TableView<Song> songTableView;
    @FXML private TableColumn<Song, String> titleColumn;
    @FXML private TableColumn<Song, String> artistColumn;
    @FXML private TableColumn<Song, String> categoryColumn;
    @FXML private TableColumn<Song, String> durationColumn;

    @FXML private ListView<Song> songsInPlaylistListView;

    @FXML private TextField searchTextField;

    @FXML private Slider volumeSlider;
    @FXML private Slider progressSlider;

    @FXML private ImageView playPauseImage;
    @FXML private ImageView muteUnmuteImage;
    @FXML private ImageView repeatImage;

    @FXML void handlePlaylistClick(MouseEvent e) {
        selectedPlaylist = playlistTableView.getSelectionModel().getSelectedItem();
        updateSongsInPlaylists();
    }

    @FXML void handleSongClick(MouseEvent e) {
        selectedSong = songTableView.getSelectionModel().getSelectedItem();
        if (selectedSong != null && e.getClickCount() == 2) {
            songsInPlaylistListView.getSelectionModel().clearSelection();
            player.setPlaylistStatus(Player.PlaylistStatus.ALL_SONGS);
            player.load(selectedSong);
            player.play();
        }
    }

    @FXML void handleSongInPlaylistClick(MouseEvent e) {
        selectedSongInPlaylist = songsInPlaylistListView.getSelectionModel().getSelectedItem();
        if (selectedSongInPlaylist != null && e.getClickCount() == 2) {
            songTableView.getSelectionModel().clearSelection();
            player.setPlaylistStatus(Player.PlaylistStatus.PLAYLIST);
            player.load(selectedSongInPlaylist);
            player.play();
        }
    }

    //searches for songs in the database
    @FXML void handleSearch(ActionEvent e) {
        try {
            songObservableList.setAll(songDao.searchSong(searchTextField.getText()));
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

    //temporary implementation
    @FXML void handleAddSongToPlaylist(ActionEvent e) {
        try {
            if (selectedPlaylist != null && selectedSong != null) {
                songsInPlaylistDao.moveSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());
            }
            updateSongsInPlaylists();
            updatePlaylists();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML void handleDeleteSong(ActionEvent e) {
        try {
            songDao.deleteSong(selectedSong.getId());
            updateSongs();
            updateSongsInPlaylists();
            updatePlaylists();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML void handleDeletePlaylist(ActionEvent e) {
        try {
            playlistDao.deletePlaylist(selectedPlaylist.getId());
            updatePlaylists();
            updateSongsInPlaylists();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //determines whether the player is playing or paused and changes the buttons action accordingly
    @FXML void handlePlayPause(ActionEvent e) {
        if (player.isPlaying()) {
            player.pause();
            playPauseImage.setImage(playImage);
        }
        else {
            player.play();
            playPauseImage.setImage(pauseImage);
        }
    }

    @FXML void handleRepeat(ActionEvent e) {
        /*if (player.isRepeating()) {
            player.setRepeat(false);
        }
        else {
            player.setRepeat(true);
        }*/

        player.setRepeat(!player.isRepeating());
    }

    @FXML void handleShuffle(ActionEvent e) {
        //TODO: implement shuffle
    }

    @FXML void handleNextSong(ActionEvent e) {
        if(player.getPlaylistStatus() == Player.PlaylistStatus.DEFAULT) {
            player.stop();
        }
        else if (player.getPlaylistStatus() == Player.PlaylistStatus.ALL_SONGS) {
            if (songObservableList.indexOf(player.getCurrentSong()) == songObservableList.size() - 1) {
                player.load(songObservableList.get(0));
            }
            else {
                Song previousSong = songObservableList.get(songObservableList.indexOf(player.getCurrentSong()) + 1);
                player.load(previousSong);
            }
        }
        else if (player.getPlaylistStatus() == Player.PlaylistStatus.PLAYLIST) {
            if (songInPlaylistObservableList.indexOf(player.getCurrentSong()) == songInPlaylistObservableList.size() - 1) {
                player.load(songInPlaylistObservableList.get(0));
            }
            else {
                Song previousSong = songInPlaylistObservableList.get(songInPlaylistObservableList.indexOf(player.getCurrentSong()) - 1);
                player.load(previousSong);
            }
        }
    }

    @FXML void handlePreviousSong(ActionEvent e) {
        if (player.getCurrentTime() > 3) {
            player.reset();
        }
        else if(player.getPlaylistStatus() == Player.PlaylistStatus.DEFAULT) {
            player.reset();
        }
        else if (player.getPlaylistStatus() == Player.PlaylistStatus.ALL_SONGS) {
            if (songObservableList.indexOf(player.getCurrentSong()) == 0) {
                player.reset();
            }
            else {
                Song previousSong = songObservableList.get(songObservableList.indexOf(player.getCurrentSong()) - 1);
                player.load(previousSong);
            }
        }
        else if (player.getPlaylistStatus() == Player.PlaylistStatus.PLAYLIST) {
            if (songInPlaylistObservableList.indexOf(player.getCurrentSong()) == 0) {
                player.reset();
            }
            else {
                Song previousSong = songInPlaylistObservableList.get(songInPlaylistObservableList.indexOf(player.getCurrentSong()) - 1);
                player.load(previousSong);
            }
        }
    }

    @FXML void handleMuteUnmute(ActionEvent e) {
        if (player.isMuted()) {
            player.setVolume(volumeSlider.getValue() / 100);
            muteUnmuteImage.setImage(unmuteImage);
        }
        else {
            player.setVolume(0);
            muteUnmuteImage.setImage(muteImage);
        }
    }

    //changes progress of player when mouse click is released
    @FXML void handleProgressSlider(MouseEvent e) {
        player.setProgress(progressSlider.getValue() / 100);
    }

    private void updateSongsInPlaylists() {
        try {
            if (selectedPlaylist != null) {
                songInPlaylistObservableList.setAll(songsInPlaylistDao.getPlaylist(selectedPlaylist.getId()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
        player = new Player(); //initialize player
        player.setVolume(0.5); //set default volume to 0.5 (50%)
        volumeSlider.setValue(player.getVolume() * 100);
        volumeSlider.valueProperty().addListener((ov, oldValue, newValue) ->
                player.setVolume(newValue.doubleValue() / 100)
        ); //add listener to volumeSlider

        //automatically update progressSlider, unless progressSlider is being dragged
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
            totalDurationColumn.setCellValueFactory(new PropertyValueFactory<>("DurationInString"));
            playlistTableView.setItems(playlistObservableList);
            playlistTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            playlistObservableList.addAll(playlistDao.getAllPlaylists());

            //Set up the table columns and cells for the song table
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("Artist"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("DurationInString"));
            songTableView.setItems(songObservableList);
            songTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            songObservableList.addAll(songDao.getAllSongs());

            songsInPlaylistListView.setItems(songInPlaylistObservableList);
            songsInPlaylistListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}