package pl.coherentsolutions.service.util.configreader;

import pl.coherentsolutions.service.exception.ConfigReaderException;
import pl.coherentsolutions.service.catalog.service.impl.CatalogServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseConfigReader {
    public static List<String> readDatabaseConfig(String environment) throws ConfigReaderException {
        String dbUrl;
        String dbUser;
        String dbPassword;

        if ("test".equals(environment)) {
            dbUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
            dbUser = "sa";
            dbPassword = "";
        } else {
            dbUrl = readConfig("db.url").get(0);
            dbUser = readConfig("db.user").get(0);
            dbPassword = readConfig("db.password").get(0);
        }

        return new ArrayList<>(List.of(dbUrl, dbUser, dbPassword));
    }

    private static List<String> readConfig(String filePath) throws ConfigReaderException {
        final String propertyFile = "config.properties";

        try (InputStream inputStream = CatalogServiceImpl.class.getClassLoader().getResourceAsStream(propertyFile)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            String property = properties.getProperty(filePath);

            if (property == null) {
                throw new IllegalArgumentException("Property not found: " + filePath);
            }

            return new ArrayList<>(List.of(property));
        } catch (IOException | IllegalArgumentException e) {
            throw new ConfigReaderException("Error fetching database configuration from property file " + propertyFile +
                    "by property " + filePath + ". Error: " + e.getMessage(), e);
        }
    }
}
