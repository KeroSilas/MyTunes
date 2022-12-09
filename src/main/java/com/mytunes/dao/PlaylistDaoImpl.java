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
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM Playlists;";
            Statement statement = connection.createStatement();
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("playlistID");
                    String name = resultSet.getString("name");

                    Playlist playlist = new Playlist(id, name);
                    playlists.add(playlist);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return playlists;
    }

    @Override
    public void deletePlaylist(int id) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE FROM Playlists WHERE playlistID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updatePlaylist(int id, String name) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Playlists SET name = ? WHERE playlistID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int createPlaylist(String name) {
        int playlistId = 0;
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO Playlists (name) VALUES (?);";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.executeUpdate();
            ResultSet generatedKey = statement.getGeneratedKeys();
            if (generatedKey.next())
                playlistId = generatedKey.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return playlistId;
    }
}
