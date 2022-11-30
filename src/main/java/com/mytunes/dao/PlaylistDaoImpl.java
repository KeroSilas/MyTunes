package com.mytunes.dao;

import com.mytunes.model.Playlist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDaoImpl implements PlaylistDao {

    private final DatabaseConnector databaseConnector;

    public PlaylistDaoImpl() {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public List<Playlist> getAllPlaylists() throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM Playlists;";
            Statement statement = connection.createStatement();
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("PlaylistsID");
                    String name = resultSet.getString("Name");

                    Playlist playlist = new Playlist(id, name);
                    playlists.add(playlist);
                }
            }
        }
        return playlists;
    }

    @Override
    public void deletePlaylist(int id) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE FROM Playlists WHERE PlaylistsID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void updatePlaylist(int id, String name) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Playlists SET Name = ? WHERE PlaylistsID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void createPlaylist(String name) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO Playlists (Name) VALUES (?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.executeUpdate();
        }
    }
}
