package pl.coherentsolutions.service.category.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.service.category.dao.impl.CategoryDaoImpl;
import pl.coherentsolutions.service.exception.CategoryDatabaseException;
import pl.coherentsolutions.service.util.configreader.DatabaseConfigReader;
import pl.coherentsolutions.service.util.connectionprovider.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryDaoImplTest {
    private static ConnectionProvider connectionProvider;
    private final CategoryDao categoryDao = new CategoryDaoImpl();
    private Connection connection;

    @BeforeAll
    public static void setUpConnection() throws Exception {
        List<String> databaseConfig = DatabaseConfigReader.readDatabaseConfig("test");

        connectionProvider = ConnectionProvider.getInstance();
        connectionProvider.initializeConnection(databaseConfig);
    }

    @BeforeEach
    public void setUp() throws Exception {
        connection = connectionProvider.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Category (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL)");
            statement.execute("INSERT INTO Category (name) VALUES ('Category1')");
            statement.execute("INSERT INTO Category (name) VALUES ('Category2')");
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        connection = connectionProvider.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE Category");
        }
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connectionProvider.closeConnection();
    }

    @Test
    public void testReadAllShouldReturnAllCategories() throws CategoryDatabaseException {
        List<Category> categories = categoryDao.readAll();

        assertEquals(2, categories.size(), "The number of categories should be 2.");
        assertEquals("Category1", categories.get(0).getName(), "The name of the first category should be 'Category1'.");
        assertEquals("Category2", categories.get(1).getName(), "The name of the second category should be 'Category2'.");
    }

    @Test
    public void testReadShouldReturnCategoryById() throws Exception {
        Category category = categoryDao.read(1);

        assertNotNull(category, "Category should not be null.");
        assertEquals(1, category.getId(), "The ID of the category should be 1.");
        assertEquals("Category1", category.getName(), "The name of the category should be 'Category1'.");
    }

    @Test
    public void testUpdateShouldUpdateCategory() throws Exception {
        Category categoryToUpdate = new Category(1, "UpdatedCategory");
        categoryDao.update(categoryToUpdate);

        try (PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM Category WHERE id = ?")) {
            statement.setInt(1, categoryToUpdate.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next(), "Updated category should exist in the database.");
                assertEquals(categoryToUpdate.getName(), resultSet.getString("name"), "Category name should match the updated one.");
            }
        }
    }

    @Test
    public void testCreateShouldCreateNewCategory() throws Exception {
        String categoryName = "NewCategory";
        categoryDao.create(categoryName);

        try (PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM Category WHERE name = ?")) {
            statement.setString(1, categoryName);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next(), "New category should exist in the database.");
                assertEquals(categoryName, resultSet.getString("name"), "Category name should match the created one.");
            }
        }
    }

    @Test
    public void testDeleteShouldRemoveCategoryByName() throws Exception {
        String categoryName = "Category1";
        categoryDao.delete(categoryName);

        try (PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM Category WHERE name = ?")) {
            statement.setString(1, categoryName);
            try (ResultSet resultSet = statement.executeQuery()) {
                assertFalse(resultSet.next(), "Deleted category should not exist in the database.");
            }
        }
    }
}
