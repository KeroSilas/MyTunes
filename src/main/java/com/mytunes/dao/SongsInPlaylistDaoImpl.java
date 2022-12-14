package com.mytunes.dao;

import com.mytunes.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses the DatabaseConnector class and sends queries to the SongsInPlaylist table on the database.
 */

public class SongsInPlaylistDaoImpl implements SongsInPlaylistDao {

    private final DatabaseConnector databaseConnector;

    public SongsInPlaylistDaoImpl() {
        databaseConnector = new DatabaseConnector();
    }

    //Retrieves a list of the songs on a playlist and returns an ArrayList with them.
    @Override
    public List<Song> getPlaylist(int playlistId) {
        List<Song> playlist = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT SongsInPlaylist.playlistID, Songs.songID, Songs.title, Songs.artist, Songs.category, Songs.duration, Songs.path " +
                         "FROM SongsInPlaylist " +
                         "INNER JOIN Songs " +
                         "ON SongsInPlaylist.songID = Songs.songID " +
                         "WHERE SongsInPlaylist.playlistID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistId);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("songID");
                    String title = resultSet.getString("title");
                    String artist = resultSet.getString("artist");
                    String category = resultSet.getString("category");
                    int duration = resultSet.getInt("duration");
                    String path = resultSet.getString("path");

                    Song song = new Song(id, title, artist, category, duration, path);
                    playlist.add(song);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return playlist;
    }

    //Deletes a song from a playlist.
    @Override
    public void deleteSongFromPlaylist(int playlistId, int songId) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE TOP (1) FROM SongsInPlaylist WHERE playlistID = ? AND songID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistId);
            statement.setInt(2, songId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Moves a song into a playlist.
    @Override
    public void moveSongToPlaylist(int playlistId, int songId) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO SongsInPlaylist (playlistID, songID) VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistId);
            statement.setInt(2, songId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
