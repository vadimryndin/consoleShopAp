package pl.coherentsolutions.service.product.impl;

import pl.coherentsolutions.core.product.Product;
import pl.coherentsolutions.service.product.ProductService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ProductServiceImpl implements ProductService {

    @Override
    public Set<Product> createProducts() {
        Set<Product> products = new HashSet<>();
        products.add(new Product("Training Ball Euro 24 Fussballliebe", "Adidas", 19.99, 5.0, "football", true));
        products.add(new Product("Training Ball Euro 24 Fussballliebe", "Adidas", 19.99, 4.0, "football", true));
        products.add(new Product("Predator123 League MG - Black", "Adidas", 164.99, 44.0, "football", false));
        products.add(new Product("Predator987 League MG - Black", "Adidas", 264.99, 42.0, "football", false));
        products.add(new Product("Predator League MG - Black", "Adidas", 64.99, 43.0, "football", false));
        products.add(new Product("JORDAN MAX", "Nike", 350.00, 44.0, "basketball", true));
        products.add(new Product("JORDAN MAX", "Nike", 350.00, 42.0, "basketball", true));
        products.add(new Product("JORDAN MAX", "Nike", 450.00, 43.0, "basketball", true));
        products.add(new Product("JORDAN MAX", "Nike", 340.00, 41.0, "basketball", false));
        products.add(new Product("JORDAN MAX", "Nike", 300.00, 40.0, "basketball", true));
        products.add(new Product("JORDAN MAX", "Nike", 500.00, 43.0, "basketball", true));
        products.add(new Product("Electric Mountain Bike E-ST 500", "ROCKRIDER", 1399.00, 27.5, "cycling", true));
        products.add(new Product("Mountain Bike E-ST 500", "ROCKRIDER", 999.00, 24.5, "cycling", true));
        products.add(new Product("ASICS GEL WINDHAWK MEN'S RUNNING SHOES", "ASICS", 79.99, 44.0, "running", true));
        products.add(new Product("ACICS GEL WINDHAWK MEN'S RUNNING SHOES", "ASICS", 179.99, 44.0, "running", true));
        products.add(new Product("ABICS GEL WINDHAWK MEN'S RUNNING SHOES", "ASICS", 279.99, 41.0, "running", true));
        products.add(new Product("ASICS GEL WINDHAWK MEN'S RUNNING SHOES", "ASICS", 49.99, 43.0, "running", true));
        products.add(new Product("ASICS GEL WINDHAWK MEN'S RUNNING SHOES", "BACICS", 49.99, 43.0, "running", true));
        products.add(new Product("ABICS GEL WINDHAWK MEN'S RUNNING SHOES", "ASICS", 379.99, 41.0, "running", true));
        products.add(new Product("ASICS GEL WINDHAWK MEN'S RUNNING SHOES", "ASICS", 499.99, 43.0, "running", true));
        products.add(new Product("ASICS GEL WINDHAWK MEN'S RUNNING SHOES", "BACICS", 149.99, 43.0, "running", true));

        return products;
    }

    @Override
    public Product findProductByName(String name) {
        List<Product> products = new ArrayList<>(createProducts());
        return products.stream()
                .filter(product -> Objects.equals(product.getName(), name))
                .findFirst()
                .orElse(null);
    }
}
