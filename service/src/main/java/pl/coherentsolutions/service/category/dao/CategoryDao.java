package pl.coherentsolutions.service.category.dao;

import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.service.exception.CategoryDatabaseException;

import java.util.List;

public interface CategoryDao {
    void create(String categoryName) throws CategoryDatabaseException;
    Category read(int id) throws CategoryDatabaseException;
    List<Category> readAll() throws CategoryDatabaseException;
    void update(Category category) throws CategoryDatabaseException;
    void delete(String categoryName) throws CategoryDatabaseException;
}
