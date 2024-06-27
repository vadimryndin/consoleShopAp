package pl.coherentsolutions.service.util.connectionprovider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ConnectionProvider {
    private static Connection connection;
    private static ConnectionProvider instance;

    private ConnectionProvider() {}

    public synchronized static ConnectionProvider getInstance() {
        if (instance == null) {
            instance = new ConnectionProvider();
        }

        return instance;
    }

    public synchronized void initializeConnection(List<String> databaseParams) throws SQLException {
        if (connection == null || connection.isClosed()) {
            String dbUrl = databaseParams.get(0);
            String dbUser = databaseParams.get(1);
            String dbPassword = databaseParams.get(2);

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        }
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Connection is not initialized. Call initializeConnection first.");
        }

        return connection;
    }

    public synchronized void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("[LOG] Error closing database connection. Error: " + e.getMessage());
        }
    }
}
