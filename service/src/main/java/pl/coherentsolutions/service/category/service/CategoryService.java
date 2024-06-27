package pl.coherentsolutions.service.category.service;

import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.service.exception.LoadCategoryException;

import java.util.List;

public interface CategoryService {
    List<Category> loadCategories() throws LoadCategoryException;
}
