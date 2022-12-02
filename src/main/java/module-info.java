module com.mytunes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;
    requires java.sql;
    requires java.naming;
    requires com.microsoft.sqlserver.jdbc;
    requires javafx.media;

    opens com.mytunes to javafx.fxml;
    exports com.mytunes;
    exports com.mytunes.controllers;
    opens com.mytunes.controllers to javafx.fxml;
    exports com.mytunes.dao;
    opens com.mytunes.dao to javafx.fxml;
    exports com.mytunes.model;
    opens com.mytunes.model to javafx.fxml;
}