package pl.coherentsolutions.service.catalog.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.coherentsolutions.core.catalog.Catalog;
import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.core.product.Product;
import pl.coherentsolutions.core.sorting.Sorting;
import pl.coherentsolutions.core.sorting.SortingCriterion;
import pl.coherentsolutions.service.catalog.service.CatalogService;
import pl.coherentsolutions.service.catalog.service.impl.CatalogServiceImpl;
import pl.coherentsolutions.service.category.service.CategoryService;
import pl.coherentsolutions.service.category.dao.impl.CategoryDaoImpl;
import pl.coherentsolutions.service.category.service.impl.CategoryServiceImpl;
import pl.coherentsolutions.service.category.repository.impl.CategoryRepositoryImpl;
import pl.coherentsolutions.service.exception.LoadCategoryException;
import pl.coherentsolutions.service.product.ProductService;
import pl.coherentsolutions.service.product.impl.ProductServiceImpl;
import pl.coherentsolutions.service.util.configreader.DatabaseConfigReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CatalogServiceImplTest {
    private static List<String> databaseConfig;

    @BeforeAll
    public static void setUpConnection() throws Exception {
        databaseConfig = DatabaseConfigReader.readDatabaseConfig("test");
    }

    @Test
    public void testCreateCatalog() throws LoadCategoryException {
        CategoryService categoryServiceMock = mock(CategoryService.class);
        List<Category> mockCategories = prepareMockCategories();
        when(categoryServiceMock.loadCategories()).thenReturn(mockCategories);

        ProductService productServiceMock = mock(ProductService.class);
        Set<Product> mockProducts = prepareMockProducts();
        when(productServiceMock.createProducts()).thenReturn(mockProducts);

        CatalogService catalogService = new CatalogServiceImpl(categoryServiceMock, productServiceMock);
        Catalog catalog = catalogService.createCatalog();

        assertNotNull(catalog.getName(), "Catalog should has name");
        assertNotNull(catalog.getCategories(), "Catalog should has categories");
        assertEquals(mockCategories.size(), catalog.getCategories().size(), "Number of categories does not match");
        for (Category category : catalog.getCategories()) {
            assertNotNull(category.getProducts(), "Categories should has products");
            assertEquals(calculateProductSumByCategoryName(category), category.getProducts().size(), "Number of products does not match");
        }
    }

    @Test
    public void sortCatalogShouldReturnSortedCatalog() {
        Catalog unsortedCatalog = createCatalog();
        CatalogService catalogService =
                new CatalogServiceImpl(new CategoryServiceImpl(new CategoryRepositoryImpl(new CategoryDaoImpl())), new ProductServiceImpl());
        Catalog sortedCatalog = catalogService.sortCatalog(unsortedCatalog, createSortingCriteria());
        List<Product> sortedProducts = sortedCatalog.getCategories().get(0).getProducts();

        for (int i = 1; i < sortedProducts.size(); i++) {
            assertTrue(sortedProducts.get(i-1).getPrice() <= sortedProducts.get(i).getPrice(),
                    "Products should be sorted by price ASC");
            if (sortedProducts.get(i - 1).getPrice().equals(sortedProducts.get(i).getPrice())) {
                assertTrue(sortedProducts.get(i-1).getPrice() >= sortedProducts.get(i).getPrice(),
                        "Products should be sorted by size DESC");
            }
        }
    }

    @Test
    public void findMostExpensiveProductsShouldReturnPriceSum() {
        Catalog catalogMock = createCatalog();
        CatalogService catalogService =
                new CatalogServiceImpl(new CategoryServiceImpl(new CategoryRepositoryImpl(new CategoryDaoImpl())), new ProductServiceImpl());

        Map<String, Double> categoryNameToCalculatedPrice = catalogService.findMostExpensiveProducts(catalogMock, 2);
        for (Double sumPrice : categoryNameToCalculatedPrice.values()) {
            assertEquals(40, sumPrice, "Sum of top 2 prices for each category is 40");
        }
    }

    private Catalog createCatalog() {
        List<Category> mockCategories = prepareMockCategories();
        Set<Product> mockProducts = prepareMockProducts();
        for (Product product : mockProducts) {
            for (Category category : mockCategories) {
                if (product.getCategory().equalsIgnoreCase(category.getName())) {
                    category.addProduct(product);
                    break;
                }
            }
        }

        Catalog mockCatalog = new Catalog("TestCatalog");
        mockCatalog.addCategories(mockCategories);

        return mockCatalog;
    }
    private List<Category> prepareMockCategories() {
        return Arrays.asList(new Category(1, "Category1"), new Category(2, "Category2"));
    }

    private Set<Product> prepareMockProducts() {
        Product product1 = new Product("A", "CoolBrand", 10.0, 40.0, "Category1", true);
        Product product2 = new Product("B", "NewBrand",40.0, 44.0, "Category2", true);
        Product product3 = new Product("B", "CoolBrand", 15.0, 40.0, "Category1", true);
        Product product4 = new Product("C", "NewBrand",10.0, 41.0, "Category1", true);
        Product product5 = new Product("A", "NewBrand",25.0, 41.0, "Category1", true);

        return new HashSet<>(Arrays.asList(product1, product2, product3, product4, product5));
    }

    private int calculateProductSumByCategoryName(Category category) {
        Map<String, List<Product>> categoryToProductMap = new HashMap<>();

        for (Product product : prepareMockProducts()) {
            List<Product> products = categoryToProductMap.get(category.getName());
            if (products != null) {
                List<Product> copy = new ArrayList<>(products);
                copy.add(product);
                categoryToProductMap.put(product.getCategory(), copy);
            } else {
                List<Product> productList = List.of(product);
                categoryToProductMap.put(product.getCategory(), productList);
            }
        }

        return categoryToProductMap.get(category.getName()).size();
    }

    private List<SortingCriterion> createSortingCriteria() {
        SortingCriterion sortingCriterion1 = mock(SortingCriterion.class);
        when(sortingCriterion1.getSortingCriterion()).thenReturn("price");
        when(sortingCriterion1.getSorting()).thenReturn(Sorting.ASC);

        SortingCriterion sortingCriterion2 = mock(SortingCriterion.class);
        when(sortingCriterion2.getSortingCriterion()).thenReturn("size");
        when(sortingCriterion2.getSorting()).thenReturn(Sorting.DESC);

        List<SortingCriterion> sortingCriteria = new ArrayList<>();
        sortingCriteria.add(sortingCriterion1);
        sortingCriteria.add(sortingCriterion2);

        return sortingCriteria;
    }
}
