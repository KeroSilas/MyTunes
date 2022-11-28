module project.mytunes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;
    requires java.sql;
    requires java.naming;
    requires com.microsoft.sqlserver.jdbc;

    opens project.mytunes to javafx.fxml;
    exports project.mytunes;
}