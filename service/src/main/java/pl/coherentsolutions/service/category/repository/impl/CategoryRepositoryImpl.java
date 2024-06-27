package pl.coherentsolutions.service.category.repository.impl;

import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.service.category.dao.CategoryDao;
import pl.coherentsolutions.service.category.repository.CategoryRepository;
import pl.coherentsolutions.service.exception.CategoryDatabaseException;

import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryDao categoryDao;

    public CategoryRepositoryImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public Category load(int id) throws CategoryDatabaseException {
        return categoryDao.read(id);
    }

    @Override
    public List<Category> loadAll() throws CategoryDatabaseException {
        return categoryDao.readAll();
    }

    @Override
    public void add(String categoryName) throws CategoryDatabaseException {
        categoryDao.create(categoryName);
    }

    @Override
    public void update(Category category) throws CategoryDatabaseException {
        categoryDao.update(category);
    }

    @Override
    public void remove(String categoryName) throws CategoryDatabaseException {
        categoryDao.delete(categoryName);
    }
}
