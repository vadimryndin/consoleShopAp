package pl.coherentsolutions.service.category;

import org.junit.jupiter.api.Test;
import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.service.category.service.impl.CategoryServiceImpl;
import pl.coherentsolutions.service.category.repository.CategoryRepository;
import pl.coherentsolutions.service.category.service.CategoryService;
import pl.coherentsolutions.service.exception.CategoryDatabaseException;
import pl.coherentsolutions.service.exception.LoadCategoryException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CategoryServiceImplTest {
    @Test
    public void loadCategoriesShouldReturnCategoriesList() throws LoadCategoryException, CategoryDatabaseException {

        Category category1 = new Category1(1, "Category1");
        Category category2 = new Category2(2, "Category2");
        List<Category> categoriesMockList = List.of(category1, category2);

        CategoryRepository categoryRepositoryMock = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryServiceImpl(categoryRepositoryMock);
        when(categoryRepositoryMock.loadAll()).thenReturn(categoriesMockList);

        List<Category> actualCategories = categoryService.loadCategories();

        assertNotNull(actualCategories, "Method should return List of categories");
        assertEquals(categoriesMockList.size(), actualCategories.size(), "Number of categories does not match");
    }
}

class Category1 extends Category {
    public Category1(int id, String name) {
        super(id, name);
    }
}

class Category2 extends Category {
    public Category2(int id, String name) {
        super(id, name);
    }
}

