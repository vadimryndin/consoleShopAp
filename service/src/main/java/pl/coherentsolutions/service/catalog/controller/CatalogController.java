package pl.coherentsolutions.service.catalog.controller;

import pl.coherentsolutions.core.catalog.Catalog;

import java.util.Map;
import java.util.stream.Collectors;

public class CatalogController {
    public static void printCatalog(Catalog catalog) {
        System.out.println("Catalog: " + catalog.getName());

        catalog.getCategories().forEach(category -> {
            System.out.println("Category: " + category.getName());
            System.out.println("Products:");
            String productsInfo = category.getProducts().stream()
                    .map(product -> " - " + product.getName() + " | Price: $" + product.getPrice() + " | Size: " + product.getSize() +
                            " | Brand: " + product.getBrand() + " | Available: " + product.getAvailable())
                    .collect(Collectors.joining("\n"));
            if (productsInfo.isEmpty()) {
                productsInfo = "No products in this category";
            }
            System.out.println(productsInfo);
            System.out.println();
        });
    }

    public static void printProductPriceSumPerCategory(Map<String, Double> categoryNameToCalculatedPrice) {
        System.out.println("Total Price of the five most expensive products per category: ");
        for (String categoryName : categoryNameToCalculatedPrice.keySet()) {
            System.out.println("Category: " + categoryName);
            System.out.println("Total cost: " + categoryNameToCalculatedPrice.get(categoryName));
            System.out.println();
        }
    }
}
