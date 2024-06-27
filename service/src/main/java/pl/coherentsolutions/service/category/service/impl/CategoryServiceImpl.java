package pl.coherentsolutions.service.category.service.impl;

import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.service.category.service.CategoryService;
import pl.coherentsolutions.service.category.repository.CategoryRepository;
import pl.coherentsolutions.service.exception.CategoryDatabaseException;
import pl.coherentsolutions.service.exception.LoadCategoryException;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> loadCategories() throws LoadCategoryException {
        try {
            return this.categoryRepository.loadAll();
        } catch (CategoryDatabaseException e) {
            throw new LoadCategoryException("Error loading categories: " + e.getMessage(), e);
        }
    }
}
