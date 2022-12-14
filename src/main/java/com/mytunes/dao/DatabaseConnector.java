package com.mytunes.dao;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.*;

/**
 * A class that is responsible for connecting to the SQL server.
 */

public class DatabaseConnector {

    private final SQLServerDataSource dataSource;

    //Constructor that defines the connection to the SQL server.
    public DatabaseConnector() {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName("kero.database.windows.net");
        dataSource.setDatabaseName("MyTunes");
        dataSource.setUser("MyTunes");
        dataSource.setPassword("Strong.Pwd-123");
        dataSource.setPortNumber(1433);
    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }
}