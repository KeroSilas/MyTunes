package com.mytunes.dao;

import com.mytunes.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongsInPlaylistDaoImpl implements SongsInPlaylistDao {

    private final DatabaseConnector databaseConnector;

    public SongsInPlaylistDaoImpl() {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public List<Song> getPlaylist(int playlistId) throws SQLException {
        List<Song> playlist = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT SongsInPlaylist.PlaylistID, Songs.SongsID, Songs.title, Songs.Artist, Songs.Category, Songs.Path " +
                         "FROM SongsInPlaylist " +
                         "INNER JOIN Songs " +
                         "ON SongsInPlaylist.SongsID = Songs.SongsID " +
                         "WHERE SongsInPlaylist.PlaylistID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistId);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("SongsID");
                    String title = resultSet.getString("title");
                    String artist = resultSet.getString("Artist");
                    String category = resultSet.getString("Category");
                    String path = resultSet.getString("Path");

                    Song song = new Song(id, title, artist, category, path);
                    playlist.add(song);
                }
            }
        }
        return playlist;
    }


    @Override
    public void deleteSongFromPlaylist(int playlistId, int songId) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE FROM SongsInPlaylist WHERE PlaylistID = ? AND SongsID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistId);
            statement.setInt(2, songId);
            statement.executeUpdate();
        }
    }

    @Override
    public void moveSongToPlaylist(int playlistId, int songId) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO SongsInPlaylist (PlaylistID, SongsID) VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistId);
            statement.setInt(2, songId);
            statement.executeUpdate();
        }
    }
}
