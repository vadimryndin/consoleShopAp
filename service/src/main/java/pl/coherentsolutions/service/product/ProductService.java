package pl.coherentsolutions.service.product;

import pl.coherentsolutions.core.product.Product;

import java.util.Set;

public interface ProductService {
    Set<Product> createProducts();
    Product findProductByName(String name);
}
