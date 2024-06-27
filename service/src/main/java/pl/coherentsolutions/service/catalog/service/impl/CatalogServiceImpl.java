package pl.coherentsolutions.service.catalog.service.impl;

import pl.coherentsolutions.core.product.Product;
import pl.coherentsolutions.core.catalog.Catalog;
import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.core.sorting.SortingCriterion;
import pl.coherentsolutions.service.catalog.service.CatalogService;
import pl.coherentsolutions.service.category.service.CategoryService;
import pl.coherentsolutions.service.exception.LoadCategoryException;
import pl.coherentsolutions.service.product.ProductComparator;
import pl.coherentsolutions.service.product.ProductService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CatalogServiceImpl implements CatalogService {
    private CategoryService categoryService;
    private ProductService productService;

    public CatalogServiceImpl(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @Override
    public Catalog createCatalog() throws LoadCategoryException {
        Catalog sportShopCatalog = new Catalog("Decathlon");

        List<Category> categories = this.categoryService.loadCategories();

        Set<Product> products = this.productService.createProducts();
        for (Product product : products) {
            for (Category category : categories) {
                if (product.getCategory().equalsIgnoreCase(category.getName())) {
                    category.addProduct(product);
                    break;
                }
            }
        }

        sportShopCatalog.addCategories(categories);

        return sportShopCatalog;
    }

    @Override
    public Catalog sortCatalog(Catalog catalog, List<SortingCriterion> criteria) {
        List<Category> sortedCategories = new ArrayList<>();
        List<Category> categories = catalog.getCategories();

        for (Category category : categories) {
            List<Product> sortedProducts =  category.getProducts().stream().sorted(new ProductComparator(criteria)).toList();
            Category sortedProductsCategory = new Category(category.getId(), category.getName());

            for (Product product : sortedProducts) {
                sortedProductsCategory.addProduct(product);
            }

            sortedCategories.add(sortedProductsCategory);
        }

        Catalog sortedCatalog = new Catalog(catalog.getName());
        sortedCatalog.addCategories(sortedCategories);

        return sortedCatalog;
    }

    @Override
    public Map<String, Double> findMostExpensiveProducts(Catalog catalog, int range) {
        Map<String, Double> categoryNameToCalculatedPrice = new HashMap<>();
        List<Category> categories = catalog.getCategories();

        for (Category category : categories) {
            List<Double> sortedPrices = new ArrayList<>();
            category.getProducts().forEach(product -> sortedPrices.add(product.getPrice()));
            sortedPrices.sort(Comparator.reverseOrder());

            Double totalTop5Prices = sortedPrices.stream().limit(range).mapToDouble(Double::doubleValue).sum();

            categoryNameToCalculatedPrice.put(category.getName(), totalTop5Prices);
        }

        return categoryNameToCalculatedPrice;
    }
}
