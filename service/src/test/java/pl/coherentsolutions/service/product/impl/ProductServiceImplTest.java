package pl.coherentsolutions.service.product.impl;

import org.junit.jupiter.api.Test;
import pl.coherentsolutions.core.product.Product;
import pl.coherentsolutions.service.product.ProductService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductServiceImplTest {

    @Test
    public void createProductsShouldReturnProductSet() {
        ProductService productService = new ProductServiceImpl();
        Set<Product> createdProducts = productService.createProducts();
        assertNotNull(createdProducts, "Method should return populated Set of products");
        assertFalse(createdProducts.isEmpty(), "Method should return populated Set of products");
    }

}
