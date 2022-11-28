package project.mytunes;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.*;

public class DatabaseConnector {

    private final SQLServerDataSource dataSource;

    public DatabaseConnector() {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName("kero.database.windows.net");
        dataSource.setDatabaseName("Test");
        dataSource.setUser("MyTunes");
        dataSource.setPassword("Strong.Pwd-123");
        dataSource.setPortNumber(1433);
    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) throws SQLException {
        //Test connection
        DatabaseConnector databaseConnector = new DatabaseConnector();
        try (Connection connection = databaseConnector.getConnection()) {
            System.out.println("Connection open? " + !connection.isClosed());
        }
    }
}