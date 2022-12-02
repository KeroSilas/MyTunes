module com.mytunes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;
    requires java.sql;
    requires java.naming;
    requires com.microsoft.sqlserver.jdbc;
    requires javafx.media;

    exports com.mytunes.controllers;
    opens com.mytunes.controllers to javafx.fxml;
    exports com.mytunes.dao;
    opens com.mytunes.dao to javafx.fxml;
    exports com.mytunes.model;
    opens com.mytunes.model to javafx.fxml;
    exports com.mytunes.services;
    opens com.mytunes.services to javafx.fxml;
}