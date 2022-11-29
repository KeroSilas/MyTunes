package com.mytunes.model;

public class Song {

        private String title;
        private String artist;
        private String genre;
        private String path;

        public Song(String title, String artist, String genre, String path) {
            this.title = title;
            this.artist = artist;
            this.genre = genre;
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public String getArtist() {
            return artist;
        }

        public String getGenre() {
            return genre;
        }

        public String getPath() {
            return path;
        }
}
