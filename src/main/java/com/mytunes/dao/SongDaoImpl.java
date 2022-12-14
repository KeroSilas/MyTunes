package com.mytunes.dao;

import com.mytunes.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses the DatabaseConnector class and sends queries to the Songs table on the database.
 */

public class SongDaoImpl implements SongDao{

    private final DatabaseConnector databaseConnector;

    public SongDaoImpl() {
        databaseConnector = new DatabaseConnector();
    }

    //Retrieves all the songs on the Songs table and returns an ArrayList with them.
    @Override
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM Songs;";
            Statement statement = connection.createStatement();
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("songID");
                    String title = resultSet.getString("title");
                    String artist = resultSet.getString("artist");
                    String category = resultSet.getString("category");
                    int duration = resultSet.getInt("duration");
                    String path = resultSet.getString("path");

                    Song song = new Song(id, title, artist, category, duration, path);
                    songs.add(song);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return songs;
    }

    //Deletes a song.
    @Override
    public void deleteSong(int id) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE FROM Songs WHERE songID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Updates a song's values.
    @Override
    public void updateSong(int id, String title, String artist, String category, int duration, String path) {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Songs SET title = ?, artist = ?, category = ?, duration = ?, path = ? WHERE songID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, artist);
            statement.setString(3, category);
            statement.setInt(4, duration);
            statement.setString(5, path);
            statement.setInt(6, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Creates a new song.
    //Also returns an int value for the ID of the song that was just created.
    @Override
    public int createSong(String title, String artist, String category, int duration, String path) {
        int songId = 0;
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO Songs (title, artist, category, duration, path) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, title);
            statement.setString(2, artist);
            statement.setString(3, category);
            statement.setInt(4, duration);
            statement.setString(5, path);
            statement.executeUpdate();
            ResultSet generatedKey = statement.getGeneratedKeys();
            if (generatedKey.next())
                songId = generatedKey.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return songId;
    }

    //Returns a list of songs where the search input matches a title or an artist.
    @Override
    public List<Song> searchSong(String search) {
        List<Song> songs = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM Songs WHERE title LIKE ? OR artist LIKE ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
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
                    songs.add(song);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return songs;
    }
}
