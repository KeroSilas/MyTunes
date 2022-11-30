package com.mytunes.dao;

import com.mytunes.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDaoImpl implements SongDao{

    private final DatabaseConnector databaseConnector;

    public SongDaoImpl() {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public List<Song> getAllSongs() throws SQLException {
        List<Song> songs = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM Songs;";
            Statement statement = connection.createStatement();
            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("SongsID");
                    String title = resultSet.getString("title");
                    String artist = resultSet.getString("Artist");
                    String category = resultSet.getString("Category");
                    String path = resultSet.getString("Path");

                    Song song = new Song(id, title, artist, category, path);
                    songs.add(song);
                }
            }
        }
        return songs;
    }

    @Override
    public void deleteSong(int id) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "DELETE FROM Songs WHERE SongsID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void updateSong(int id, String title, String artist, String category, String path) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "UPDATE Songs SET title = ?, Artist = ?, Category = ?, Path = ? WHERE SongsID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, artist);
            statement.setString(3, category);
            statement.setString(4, path);
            statement.setInt(5, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void createSong(String title, String artist, String category, String path) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "INSERT INTO Songs (title, Artist, Category, Path) VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, artist);
            statement.setString(3, category);
            statement.setString(4, path);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Song> searchSong(String search) throws SQLException {
        List<Song> songs = new ArrayList<>();
        try (Connection connection = databaseConnector.getConnection()) {
            String sql = "SELECT * FROM Songs WHERE title LIKE ? OR Artist LIKE ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + search + "%");
            statement.setString(2, "%" + search + "%");
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("SongsID");
                    String title = resultSet.getString("title");
                    String artist = resultSet.getString("Artist");
                    String category = resultSet.getString("Category");
                    String path = resultSet.getString("Path");

                    Song song = new Song(id, title, artist, category, path);
                    songs.add(song);
                }
            }
        }
        return songs;
    }

    //debugging
    public static void main(String[] args) throws SQLException {
        SongDaoImpl songDao = new SongDaoImpl();

        //songDao.deleteSong(3);
        //songDao.createSong("Test", "Test", "test3.wav");
        //songDao.updateSong(4, "Test", "Test", "13-unfold.mp3");

        List<Song> songs = songDao.getAllSongs();
        for (Song song : songs) {
            System.out.println(song.getId()+ " " + song.getTitle() + " " + song.getArtist() + " " + song.getPath());
        }
        System.out.println(songDao.searchSong("cb"));
    }
}
