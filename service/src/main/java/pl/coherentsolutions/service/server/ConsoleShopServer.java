package pl.coherentsolutions.service.server;

import pl.coherentsolutions.service.exception.CategoryDatabaseException;
import pl.coherentsolutions.core.exceptions.ClientServerException;
import pl.coherentsolutions.service.exception.ConfigReaderException;
import pl.coherentsolutions.service.util.configreader.DatabaseConfigReader;
import pl.coherentsolutions.service.util.connectionprovider.ConnectionProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsoleShopServer {
    private static final int PORT = 1234;
    private static ConnectionProvider connectionProvider;

    public static void main(String[] args) throws ClientServerException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER LOG] Server is listening on port " + PORT);
            runDatabaseConnection();

            ExecutorService executorService = Executors.newCachedThreadPool();

            boolean isServerRunning = true;
            while(isServerRunning) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("[SERVER LOG] New client connected " + socket);

                    executorService.submit(new ClientHandler(socket));
                } catch (IOException e) {
                    throw new ClientServerException("Error accepting client connection: " + e.getMessage(), e);
                }
            }
        } catch (IOException | ClientServerException | CategoryDatabaseException e) {
            throw new ClientServerException("Error starting server on port " + PORT + ". Error: " + e.getMessage(), e);
        } finally {
            connectionProvider.closeConnection();
        }
    }

    private static void runDatabaseConnection() throws CategoryDatabaseException {
        try {
            List<String> databaseConfig = DatabaseConfigReader.readDatabaseConfig("production");
            connectionProvider = ConnectionProvider.getInstance();
            connectionProvider.initializeConnection(databaseConfig);
        } catch (SQLException | ConfigReaderException e) {
            throw new CategoryDatabaseException("Error initializing database connection. Error: " + e.getMessage(), e);
        }
    }
}
