package pl.coherentsolutions.service.category.dao.impl;

import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.service.category.dao.CategoryDao;
import pl.coherentsolutions.service.exception.CategoryDatabaseException;
import pl.coherentsolutions.service.util.connectionprovider.ConnectionProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private final ConnectionProvider connectionProvider;

    public CategoryDaoImpl() {
        this.connectionProvider = ConnectionProvider.getInstance();
    }

    @Override
    public void create(String name) throws CategoryDatabaseException {
        String sql = "INSERT INTO Category (name) VALUES (?)";

        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("[LOG] Category " + name + " was created successfully!");
            }
        } catch (SQLException e) {
            throw new CategoryDatabaseException("Error creating category: " + name + ". Error: " + e.getMessage(), e);
        }
    }

    @Override
    public Category read(int id) throws CategoryDatabaseException {
        String sql = "SELECT Name, id FROM Category WHERE id = ?";
        Category category = null;

        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");

                    return new Category(id, name);
                } else {
                    System.out.println("[LOG] Category with ID " + id + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new CategoryDatabaseException("Error reading category with ID: " + id + ". Error: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Category> readAll() throws CategoryDatabaseException {
        String sql = "SELECT Name, id FROM Category";
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Category catalog = new Category(id, name);
                categories.add(catalog);
            }
        } catch (SQLException e) {
            throw new CategoryDatabaseException("Error reading categories from database. Error: " + e.getMessage(), e);
        }

        return categories;
    }

    @Override
    public void update(Category category) throws CategoryDatabaseException {
        String sql = "UPDATE Category SET name = ? WHERE id = ?";

        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            statement.setString(1, category.getName());
            statement.setInt(2, category.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("[LOG] Category " + category.getName() + " was updated successfully!");
            }
        } catch (SQLException e) {
            throw new CategoryDatabaseException("Error updating category: " + category.getName() + ". Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String categoryName) throws CategoryDatabaseException {
        String sql = "DELETE FROM Category WHERE name = ?";

        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            statement.setString(1, categoryName);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("[LOG] Category " + categoryName + " was deleted successfully!");
            }
        } catch (SQLException e) {
            throw new CategoryDatabaseException("Error deleting category: " + categoryName + ". Error: " + e.getMessage(), e);
        }
    }
}
