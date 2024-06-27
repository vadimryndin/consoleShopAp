package pl.coherentsolutions.service.catalog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coherentsolutions.core.catalog.Catalog;
import pl.coherentsolutions.core.category.Category;
import pl.coherentsolutions.core.product.Product;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatalogControllerTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void printCatalogShouldPrintPassedCatalog() {
        Catalog catalog = createCatalog();
        CatalogController.printCatalog(catalog);
        String printedOutput = outputStreamCaptor.toString().trim();

        String expectedOutput = """
                Catalog: TestCatalog
                Category: Category1
                Products:
                 - A | Price: $10.0 | Size: 40.0 | Brand: CoolBrand | Available: true
                 - B | Price: $40.0 | Size: 44.0 | Brand: NewBrand | Available: true""";

        expectedOutput = expectedOutput.replaceAll("\\R", "\n");
        printedOutput = printedOutput.replaceAll("\\R", "\n");

        assertEquals(expectedOutput, printedOutput, "The printed catalog output does not match the expected output");
    }

    @Test
    public void printProductPriceSumPerCategoryShouldPrintPriceInfo() {
        Map<String, Double> categoryNameToCalculatedPrice = new HashMap<>();
        categoryNameToCalculatedPrice.put("Category1", 200.0);
        categoryNameToCalculatedPrice.put("Category2", 100.0);
        categoryNameToCalculatedPrice.put("Category3", 500.0);

        CatalogController.printProductPriceSumPerCategory(categoryNameToCalculatedPrice);
        String printedOutput = outputStreamCaptor.toString().trim();

        String expectedOutput = "Total Price of the five most expensive products per category: \n" +
                "Category: Category3\n" +
                "Total cost: 500.0\n" +
                "\n" +
                "Category: Category2\n" +
                "Total cost: 100.0\n" +
                "\n" +
                "Category: Category1\n" +
                "Total cost: 200.0";

        expectedOutput = expectedOutput.replaceAll("\\R", "\n");
        printedOutput = printedOutput.replaceAll("\\R", "\n");

        assertEquals(expectedOutput, printedOutput, "The printed catalog output does not match the expected output");

    }

    private Catalog createCatalog() {
        List<Category> mockCategories = List.of(new Category(1, "Category1"));
        Set<Product> mockProducts = prepareMockProducts();
        for (Product product : mockProducts) {
            mockCategories.get(0).addProduct(product);
        }

        Catalog mockCatalog = new Catalog("TestCatalog");
        mockCatalog.addCategories(mockCategories);

        return mockCatalog;
    }

    private Set<Product> prepareMockProducts() {
        Product product1 = new Product("A", "CoolBrand", 10.0, 40.0, "Category1", true);
        Product product2 = new Product("B", "NewBrand", 40.0, 44.0, "Category1", true);

        return new HashSet<>(Arrays.asList(product1, product2));
    }
}
