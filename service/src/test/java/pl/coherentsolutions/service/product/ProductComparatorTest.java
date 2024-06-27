package pl.coherentsolutions.service.product;

import org.junit.jupiter.api.Test;
import pl.coherentsolutions.core.product.Product;
import pl.coherentsolutions.core.sorting.Sorting;
import pl.coherentsolutions.core.sorting.SortingCriterion;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductComparatorTest {

    @Test
    public void compareShouldReturnComparisonOfProducts() {
        SortingCriterion sortingCriterion1 = new SortingCriterion("price", Sorting.ASC);
        SortingCriterion sortingCriterion2 = new SortingCriterion("name", Sorting.DESC);
        SortingCriterion sortingCriterion3 = new SortingCriterion("size", Sorting.ASC);
        List<SortingCriterion> sortingCriteria = Arrays.asList(sortingCriterion1, sortingCriterion2, sortingCriterion3);

        Product product1 = new Product("A", "CoolBrand", 10.0, 40.0, "Cat1", true);
        Product product2 = new Product("B", "NewBrand",20.0, 44.0, "Cat2", true);
        Product product3 = new Product("B", "CoolBrand", 10.0, 40.0, "Cat1", true);
        Product product4 = new Product("A", "NewBrand",10.0, 41.0, "Cat2", true);

        ProductComparator productComparator = new ProductComparator(sortingCriteria);

        assertEquals(-1, productComparator.compare(product1, product2),
                "comparison based on price (ASC) should return -1");

        assertEquals(1, productComparator.compare(product1, product3),
                "comparison based on name (DESC) should return 1");

        assertEquals(-1, productComparator.compare(product1, product4),
                "comparison based on size (ASC) should return -1");
    }
}
