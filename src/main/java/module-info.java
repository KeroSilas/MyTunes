module project.mytunes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;
    requires java.sql;
    requires java.naming;
    requires com.microsoft.sqlserver.jdbc;
    requires javafx.media;

    opens com.mytunes to javafx.fxml;
    exports com.mytunes;
    exports com.mytunes.controller;
    opens com.mytunes.controller to javafx.fxml;
    exports com.mytunes.dao;
    opens com.mytunes.dao to javafx.fxml;
}