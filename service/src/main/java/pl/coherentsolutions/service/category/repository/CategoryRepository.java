package pl.coherentsolutions.service.category.repository;

import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.service.exception.CategoryDatabaseException;

import java.util.List;

public interface CategoryRepository {
    Category load(int id) throws CategoryDatabaseException;
    List<Category> loadAll() throws CategoryDatabaseException;
    void add(String categoryName) throws CategoryDatabaseException;
    void update(Category category) throws CategoryDatabaseException;
    void remove(String categoryName) throws CategoryDatabaseException;
}
